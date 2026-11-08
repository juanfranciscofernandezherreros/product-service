@echo off
setlocal enabledelayedexpansion

:: Modelo por defecto para analisis, generacion y validacion
set MODEL=claude-3-5-sonnet-20241022

echo ========================================================
echo   AI CRUD GENERATOR - JAVA HEXAGONAL + BDD + SPEC
echo   Modelo activo: %MODEL%
echo ========================================================
echo.

where claude >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] No se encuentra 'claude' CLI en el PATH.
    echo Instala Claude Code CLI o adapta este script a tu agente.
    pause
    exit /b 1
)

:: Solicitar carpeta destino para el proyecto generado
set "TARGET_DIR="
set /p TARGET_DIR="Introduce la carpeta destino para guardar el proyecto (ej. C:\proyectos\mi-crud): "

if "%TARGET_DIR%"=="" (
    echo [ERROR] Debes indicar una carpeta destino valida.
    pause
    exit /b 1
)

if not exist "%TARGET_DIR%" (
    echo Creando carpeta destino: "%TARGET_DIR%"
    mkdir "%TARGET_DIR%"
    if %errorlevel% neq 0 (
        echo [ERROR] No se pudo crear la carpeta destino.
        pause
        exit /b 1
    )
)

echo Copiando estructura base, reglas y especificaciones a "%TARGET_DIR%"...
copy /Y "CLAUDE.md" "%TARGET_DIR%\" >nul
copy /Y "checkstyle.xml" "%TARGET_DIR%\" >nul
copy /Y ".editorconfig" "%TARGET_DIR%\" >nul
xcopy /E /I /Y "specs" "%TARGET_DIR%\specs" >nul
xcopy /E /I /Y "plans" "%TARGET_DIR%\plans" >nul
xcopy /E /I /Y ".ai" "%TARGET_DIR%\.ai" >nul

pushd "%TARGET_DIR%"
echo Directorio de trabajo actual: %CD%
echo.

echo [FASE 1/5] Generando Plan de Implementacion y Tareas...
claude --model %MODEL% -p "$(type .ai\prompts\01-plan-and-tasks.md)"
if %errorlevel% neq 0 goto :error

echo.
echo [FASE 2/5] Generando Capa de Dominio Puro...
claude --model %MODEL% -p "$(type .ai\prompts\02-domain.md)"
if %errorlevel% neq 0 goto :error

echo.
echo [FASE 3/5] Generando Puertos, Casos de Uso y Tests JUnit 5...
claude --model %MODEL% -p "$(type .ai\prompts\03-application.md)"
if %errorlevel% neq 0 goto :error

echo.
echo [FASE 4/5] Generando Adaptadores, Endpoints, JUnit y Cucumber BDD...
claude --model %MODEL% -p "$(type .ai\prompts\04-adapters-and-tests.md)"
if %errorlevel% neq 0 goto :error

echo.
echo [FASE 5/5] Verificacion Final End-to-End (Checkstyle + ArchUnit + JUnit + Cucumber)...
claude --model %MODEL% -p "$(type .ai\prompts\05-e2e-verification.md)"
if %errorlevel% neq 0 goto :error

popd
echo.
echo ========================================================
echo   PROCESO COMPLETADO CON EXITO EN: %TARGET_DIR%
echo ========================================================
pause
exit /b 0

:error
popd
echo.
echo [ERROR] Hubo un fallo en la ejecucion de la fase actual. Revisa los logs.
pause
exit /b 1
