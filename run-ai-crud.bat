@echo off
rem Genera los servicios del manifiesto, compila, deja que la IA corrija un build roto y sube
rem cada uno a su remoto. Toda la logica esta en scripts\generate.py.
rem   run-ai-crud.bat                      todos los servicios
rem   run-ai-crud.bat -s access-service    solo uno
rem   run-ai-crud.bat --mode ai            pipeline antiguo (todo el codigo lo escribe la IA)
cd /d "%~dp0"
where python >nul 2>nul
if errorlevel 1 (
    echo [ERROR] Hace falta Python 3 en el PATH.
    exit /b 1
)
python scripts\generate.py --build --ai-fix 2 --ai-rules --push %*
exit /b %errorlevel%
