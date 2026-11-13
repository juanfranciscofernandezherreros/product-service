@echo off
setlocal enabledelayedexpansion

:: ======================================================================
::  AI CRUD GENERATOR - MULTI-MICROSERVICIO (un servicio = un repositorio)
::  Java 21 + Hexagonal + BDD + Docker PostgreSQL
::  Lee .ai\services.txt y genera CADA servicio en SU carpeta y SU remoto.
::
::  Variables opcionales:
::    MODEL        modelo de Claude (si se omite, el que use la CLI por defecto)
::    CLAUDE_ARGS  argumentos extra para la CLI (p. ej. --permission-mode acceptEdits)
:: ======================================================================

set "MODEL_ARG="
if not "%MODEL%"=="" set "MODEL_ARG=--model %MODEL%"

set "ROOT=%~dp0"
set "MANIFEST=%ROOT%.ai\services.txt"

echo ========================================================
echo   AI CRUD GENERATOR - MULTI-MICROSERVICIO
if "%MODEL%"=="" ( echo   Modelo activo: por defecto de la CLI ) else ( echo   Modelo activo: %MODEL% )
echo   Manifiesto: %MANIFEST%
echo ========================================================
echo.

where claude >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] No se encuentra 'claude' CLI en el PATH.
    pause
    exit /b 1
)
where git >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] No se encuentra 'git' en el PATH.
    pause
    exit /b 1
)
if not exist "%MANIFEST%" (
    echo [ERROR] No existe el manifiesto .ai\services.txt.
    echo Crea una linea por microservicio: SERVICE_NAME;TARGET_DIR;REMOTE_URL;SPEC_FILE;DB_SPEC
    pause
    exit /b 1
)

:: Las rutas relativas del manifiesto se resuelven desde la raiz del generador.
pushd "%ROOT%"

set /a COUNT=0
set /a FAIL=0

:: Recorre el manifiesto: ignora comentarios (#) y lineas vacias.
:: OJO: "for /f" colapsa los ';' consecutivos, asi que un SPEC_FILE vacio (Database-First)
:: desplaza DB_SPEC al cuarto campo; :process lo detecta por la extension .sql.
for /f "usebackq eol=# tokens=1-5 delims=;" %%A in ("%MANIFEST%") do (
    if not "%%A"=="" (
        set /a COUNT+=1
        call :process "%%A" "%%B" "%%C" "%%D" "%%E"
        if errorlevel 1 set /a FAIL+=1
    )
)

popd
echo.
echo ========================================================
echo   PROCESO COMPLETADO. Servicios procesados: %COUNT%  Fallidos: %FAIL%
echo ========================================================
if %COUNT% equ 0 echo [AVISO] El manifiesto no tiene ninguna linea activa.
if %FAIL% neq 0 ( pause & exit /b 1 )
pause
exit /b 0

:: ----------------------------------------------------------------------
:: :process  SERVICE_NAME  TARGET_DIR  REMOTE_URL  SPEC_FILE  DB_SPEC
:: ----------------------------------------------------------------------
:process
set "SERVICE_NAME=%~1"
set "TARGET_DIR=%~2"
set "REMOTE_URL=%~3"
set "SPEC_FILE=%~4"
set "DB_SPEC=%~5"

:: SPEC_FILE vacio en el manifiesto: el cuarto campo leido es en realidad el DB_SPEC (.sql).
if /i "%SPEC_FILE:~-4%"==".sql" if "%DB_SPEC%"=="" (
    set "DB_SPEC=%SPEC_FILE%"
    set "SPEC_FILE="
)

:: Modo: si no se da OpenAPI (SPEC_FILE vacio) -> DATABASE-FIRST (se derivara del DDL).
if "%SPEC_FILE%"=="" ( set "GENERATION_MODE=db-first" ) else ( set "GENERATION_MODE=api-first" )

echo.
echo ########################################################
echo #  Microservicio: %SERVICE_NAME%  [%GENERATION_MODE%]
echo #  Carpeta : %TARGET_DIR%
echo #  Remoto  : %REMOTE_URL%
echo #  OpenAPI : %SPEC_FILE%
echo #  Esquema : %DB_SPEC%
echo ########################################################

if "%TARGET_DIR%"=="" ( echo [ERROR] TARGET_DIR vacio para %SERVICE_NAME%. & exit /b 1 )
if "%REMOTE_URL%"=="" ( echo [ERROR] REMOTE_URL vacio para %SERVICE_NAME%. & exit /b 1 )
if "%DB_SPEC%"=="" ( echo [ERROR] DB_SPEC vacio para %SERVICE_NAME%: indica el esquema SQL del servicio. & exit /b 1 )
if not exist "%ROOT%%DB_SPEC%" ( echo [ERROR] No existe el esquema de BD %DB_SPEC%. & exit /b 1 )
if "%GENERATION_MODE%"=="api-first" if not exist "%ROOT%%SPEC_FILE%" ( echo [ERROR] No existe el contrato %SPEC_FILE%. & exit /b 1 )

:: Falla pronto si el remoto no existe o no es accesible (antes de gastar las fases).
git ls-remote "%REMOTE_URL%" >nul 2>nul
if errorlevel 1 ( echo [ERROR] El remoto %REMOTE_URL% no existe o no es accesible. Crea el repositorio antes de generar. & exit /b 1 )

:: Normaliza TARGET_DIR a ruta absoluta (admite rutas relativas a la raiz del generador).
for %%I in ("%TARGET_DIR%") do set "TARGET_DIR=%%~fI"
if not exist "%TARGET_DIR%" mkdir "%TARGET_DIR%"

echo Copiando reglas, skills, plantillas y especificaciones a "%TARGET_DIR%"...
copy /Y "%ROOT%CLAUDE.md" "%TARGET_DIR%\" >nul
xcopy /E /I /Y "%ROOT%.claude" "%TARGET_DIR%\.claude" >nul
xcopy /E /I /Y "%ROOT%.ai\prompts" "%TARGET_DIR%\.ai\prompts" >nul
copy /Y "%ROOT%templates\checkstyle.xml" "%TARGET_DIR%\" >nul
copy /Y "%ROOT%templates\.editorconfig" "%TARGET_DIR%\" >nul
xcopy /E /I /Y "%ROOT%templates\plans" "%TARGET_DIR%\plans" >nul
if not exist "%TARGET_DIR%\specs" mkdir "%TARGET_DIR%\specs"
copy /Y "%ROOT%%DB_SPEC%" "%TARGET_DIR%\specs\database-spec.sql" >nul
if "%GENERATION_MODE%"=="api-first" copy /Y "%ROOT%%SPEC_FILE%" "%TARGET_DIR%\specs\crud-spec.yaml" >nul
if exist "%ROOT%specs\acceptance\%SERVICE_NAME%.md" copy /Y "%ROOT%specs\acceptance\%SERVICE_NAME%.md" "%TARGET_DIR%\specs\acceptance-criteria.md" >nul

(
    echo SERVICE_NAME=%SERVICE_NAME%
    echo REMOTE_URL=%REMOTE_URL%
    echo TARGET_DIR=%TARGET_DIR%
    echo GENERATION_MODE=%GENERATION_MODE%
) > "%TARGET_DIR%\.ai\project-context.txt"

pushd "%TARGET_DIR%"
echo Directorio de trabajo: %CD%

:: El prompt de cada fase se pasa a la CLI por la entrada estandar.
if "%GENERATION_MODE%"=="db-first" (
    echo [FASE 0/5] Database-First: derivando OpenAPI desde el esquema...
    type ".ai\prompts\00-db-to-openapi.md" | claude %MODEL_ARG% %CLAUDE_ARGS% -p
    if errorlevel 1 ( echo [ERROR] Fase 0 fallo en %SERVICE_NAME%. & popd & exit /b 1 )
)

echo [FASE 1/5] Plan y tareas...
type ".ai\prompts\01-plan-and-tasks.md" | claude %MODEL_ARG% %CLAUDE_ARGS% -p
if errorlevel 1 ( echo [ERROR] Fase 1 fallo en %SERVICE_NAME%. & popd & exit /b 1 )

echo [FASE 2/5] Dominio...
type ".ai\prompts\02-domain.md" | claude %MODEL_ARG% %CLAUDE_ARGS% -p
if errorlevel 1 ( echo [ERROR] Fase 2 fallo en %SERVICE_NAME%. & popd & exit /b 1 )

echo [FASE 3/5] Aplicacion + tests...
type ".ai\prompts\03-application.md" | claude %MODEL_ARG% %CLAUDE_ARGS% -p
if errorlevel 1 ( echo [ERROR] Fase 3 fallo en %SERVICE_NAME%. & popd & exit /b 1 )

echo [FASE 4/5] Adaptadores, Docker, Cucumber...
type ".ai\prompts\04-adapters-and-tests.md" | claude %MODEL_ARG% %CLAUDE_ARGS% -p
if errorlevel 1 ( echo [ERROR] Fase 4 fallo en %SERVICE_NAME%. & popd & exit /b 1 )

echo [FASE 5/5] Verificacion, doc HTML y push a %REMOTE_URL%...
type ".ai\prompts\05-e2e-verification.md" | claude %MODEL_ARG% %CLAUDE_ARGS% -p
if errorlevel 1 ( echo [ERROR] Fase 5 fallo en %SERVICE_NAME%. & popd & exit /b 1 )

popd
echo [OK] %SERVICE_NAME% generado en %TARGET_DIR% y sincronizado con %REMOTE_URL%.
exit /b 0
