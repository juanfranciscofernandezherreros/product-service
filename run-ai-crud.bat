@echo off
setlocal enabledelayedexpansion

:: ======================================================================
::  AI CRUD GENERATOR - MULTI-MICROSERVICIO (un servicio = un repositorio)
::  Java 21 + Hexagonal + BDD + Docker PostgreSQL
::  Lee .ai\services.txt y genera CADA servicio en SU carpeta y SU remoto.
:: ======================================================================

if "%MODEL%"=="" set MODEL=claude-3-5-sonnet-20241022

set "ROOT=%~dp0"
set "MANIFEST=%ROOT%.ai\services.txt"

echo ========================================================
echo   AI CRUD GENERATOR - MULTI-MICROSERVICIO
echo   Modelo activo: %MODEL%
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
    echo Crea una linea por microservicio: SERVICE_NAME;TARGET_DIR;REMOTE_URL;SPEC_FILE
    pause
    exit /b 1
)

set /a COUNT=0
set /a FAIL=0

:: Recorre el manifiesto: ignora comentarios (#) y lineas vacias.
:: Cada servicio se procesa en la subrutina :process (exit /b solo retorna, no rompe el bucle).
for /f "usebackq eol=# tokens=1-5 delims=;" %%A in ("%MANIFEST%") do (
    if not "%%A"=="" (
        set /a COUNT+=1
        call :process "%%A" "%%B" "%%C" "%%D" "%%E"
        if errorlevel 1 set /a FAIL+=1
    )
)

echo.
echo ========================================================
echo   PROCESO COMPLETADO. Servicios procesados: %COUNT%  Fallidos: %FAIL%
echo ========================================================
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
if "%DB_SPEC%"=="" set "DB_SPEC=specs\database-spec.sql"

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
if not exist "%ROOT%%DB_SPEC%" ( echo [ERROR] No existe el esquema de BD %DB_SPEC%. & exit /b 1 )
if "%GENERATION_MODE%"=="api-first" if not exist "%ROOT%%SPEC_FILE%" ( echo [ERROR] No existe el contrato %SPEC_FILE%. & exit /b 1 )

if not exist "%TARGET_DIR%" mkdir "%TARGET_DIR%"

echo Copiando reglas y especificaciones a "%TARGET_DIR%"...
copy /Y "%ROOT%CLAUDE.md" "%TARGET_DIR%\" >nul
copy /Y "%ROOT%checkstyle.xml" "%TARGET_DIR%\" >nul
copy /Y "%ROOT%.editorconfig" "%TARGET_DIR%\" >nul
xcopy /E /I /Y "%ROOT%plans" "%TARGET_DIR%\plans" >nul
xcopy /E /I /Y "%ROOT%.ai" "%TARGET_DIR%\.ai" >nul
if not exist "%TARGET_DIR%\specs" mkdir "%TARGET_DIR%\specs"
copy /Y "%ROOT%%DB_SPEC%" "%TARGET_DIR%\specs\database-spec.sql" >nul
if "%GENERATION_MODE%"=="api-first" copy /Y "%ROOT%%SPEC_FILE%" "%TARGET_DIR%\specs\crud-spec.yaml" >nul
if exist "%ROOT%specs\acceptance-criteria.md" copy /Y "%ROOT%specs\acceptance-criteria.md" "%TARGET_DIR%\specs\" >nul

(
    echo SERVICE_NAME=%SERVICE_NAME%
    echo REMOTE_URL=%REMOTE_URL%
    echo TARGET_DIR=%TARGET_DIR%
    echo GENERATION_MODE=%GENERATION_MODE%
) > "%TARGET_DIR%\.ai\project-context.txt"

pushd "%TARGET_DIR%"
echo Directorio de trabajo: %CD%

if "%GENERATION_MODE%"=="db-first" (
    echo [FASE 0/5] Database-First: derivando OpenAPI desde el esquema...
    claude --model %MODEL% -p "$(type .ai\prompts\00-db-to-openapi.md)"
    if errorlevel 1 ( echo [ERROR] Fase 0 fallo en %SERVICE_NAME%. & popd & exit /b 1 )
)

echo [FASE 1/5] Plan y tareas...
claude --model %MODEL% -p "$(type .ai\prompts\01-plan-and-tasks.md)"
if errorlevel 1 ( echo [ERROR] Fase 1 fallo en %SERVICE_NAME%. & popd & exit /b 1 )

echo [FASE 2/5] Dominio...
claude --model %MODEL% -p "$(type .ai\prompts\02-domain.md)"
if errorlevel 1 ( echo [ERROR] Fase 2 fallo en %SERVICE_NAME%. & popd & exit /b 1 )

echo [FASE 3/5] Aplicacion + tests...
claude --model %MODEL% -p "$(type .ai\prompts\03-application.md)"
if errorlevel 1 ( echo [ERROR] Fase 3 fallo en %SERVICE_NAME%. & popd & exit /b 1 )

echo [FASE 4/5] Adaptadores, Docker, Cucumber...
claude --model %MODEL% -p "$(type .ai\prompts\04-adapters-and-tests.md)"
if errorlevel 1 ( echo [ERROR] Fase 4 fallo en %SERVICE_NAME%. & popd & exit /b 1 )

echo [FASE 5/5] Verificacion, doc HTML y push a %REMOTE_URL%...
claude --model %MODEL% -p "$(type .ai\prompts\05-e2e-verification.md)"
if errorlevel 1 ( echo [ERROR] Fase 5 fallo en %SERVICE_NAME%. & popd & exit /b 1 )

popd
echo [OK] %SERVICE_NAME% generado en %TARGET_DIR% y sincronizado con %REMOTE_URL%.
exit /b 0
