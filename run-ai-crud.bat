@echo off
setlocal enabledelayedexpansion

:: Modelo por defecto para analisis, generacion y validacion
set MODEL=claude-3-5-sonnet-20241022

echo ========================================================
echo   AI CRUD GENERATOR - JAVA 21 + HEXAGONAL + BDD + DOCKER PGSQL
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

where git >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] No se encuentra 'git' en el PATH. Es necesario para el repositorio remoto.
    pause
    exit /b 1
)

:: Solicitar nombre del microservicio, carpeta destino y repositorio remoto
set "SERVICE_NAME="
set "TARGET_DIR="
set "REMOTE_URL="

set /p SERVICE_NAME="1. Introduce el nombre del microservicio (ej. product-service): "
if "%SERVICE_NAME%"=="" (
    echo [ERROR] Debes indicar un nombre de microservicio valido.
    pause
    exit /b 1
)

set /p TARGET_DIR="2. Introduce la carpeta destino para guardar el proyecto (ej. C:\proyectos\product-service): "
if "%TARGET_DIR%"=="" (
    echo [ERROR] Debes indicar una carpeta destino valida.
    pause
    exit /b 1
)

set /p REMOTE_URL="3. Introduce la URL del repositorio Git remoto para subir todo (ej. https://github.com/tu-usuario/product-service.git): "
if "%REMOTE_URL%"=="" (
    echo [ERROR] Debes indicar la URL del repositorio remoto.
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

:: Guardar contexto del microservicio y repositorio para todas las fases
(
echo SERVICE_NAME=%SERVICE_NAME%
echo REMOTE_URL=%REMOTE_URL%
echo TARGET_DIR=%TARGET_DIR%
) > "%TARGET_DIR%\.ai\project-context.txt"

pushd "%TARGET_DIR%"
echo Directorio de trabajo actual: %CD%
echo Microservicio: %SERVICE_NAME%
echo Repositorio remoto: %REMOTE_URL%
echo.

echo [FASE 1/5] Generando Plan de Implementacion y Tareas (Java 21)...
claude --model %MODEL% -p "$(type .ai\prompts\01-plan-and-tasks.md)"
if %errorlevel% neq 0 goto :error

echo.
echo [FASE 2/5] Generando Capa de Dominio Puro (Java 21)...
claude --model %MODEL% -p "$(type .ai\prompts\02-domain.md)"
if %errorlevel% neq 0 goto :error

echo.
echo [FASE 3/5] Generando Puertos, Casos de Uso y Tests JUnit 5...
claude --model %MODEL% -p "$(type .ai\prompts\03-application.md)"
if %errorlevel% neq 0 goto :error

echo.
echo [FASE 4/5] Generando Microservicio %SERVICE_NAME% (Java 21, Docker Compose PGSQL, JUnit, Cucumber HTML, README y CLAUDE.md)...
claude --model %MODEL% -p "$(type .ai\prompts\04-adapters-and-tests.md)"
if %errorlevel% neq 0 goto :error

echo.
echo [FASE 5/5] Verificacion Final, Doc Funcional HTML y Git Push a: %REMOTE_URL%...
claude --model %MODEL% -p "$(type .ai\prompts\05-e2e-verification.md)"
if %errorlevel% neq 0 goto :error

popd
echo.
echo ========================================================
echo   PROCESO COMPLETADO CON EXITO: %SERVICE_NAME%
echo   Ruta: %TARGET_DIR%\backend
echo   Doc Funcional HTML: %TARGET_DIR%\backend\docs\index.html
echo   Reporte Cucumber HTML: %TARGET_DIR%\backend\target\cucumber-reports\cucumber.html
echo   Repositorio Git sincronizado con: %REMOTE_URL%
echo ========================================================
pause
exit /b 0

:error
popd
echo.
echo [ERROR] Hubo un fallo en la ejecucion de la fase actual. Revisa los logs.
pause
exit /b 1
