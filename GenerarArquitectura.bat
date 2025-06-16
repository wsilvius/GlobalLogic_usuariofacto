@echo off
setlocal

REM ===== 1. Crear estructura de arquitectura limpia =====

call :create_dir applications\app-service\src\main\java
call :create_dir applications\app-service\src\main\resources
call :create_dir domain\model
call :create_dir domain\usecase
call :create_dir infrastructure\driven-adapters
call :create_dir infrastructure\entry-points
call :create_dir infrastructure\helpers

REM ===== 2. Mover carpeta 'src' si existe =====
if exist src (
    echo Moviendo carpeta 'src' a 'applications\app-service\src'...
    move /Y src applications\app-service\ > nul
    echo Carpeta movida correctamente.
) else (
    echo No se encontró la carpeta 'src' para mover.
)

echo.
echo Estructura de carpetas y configuración Gradle generadas correctamente.
goto :eof

:create_dir
if not exist "%~1" (
    echo Creando carpeta: %~1
    mkdir "%~1"
) else (
    echo Ya existe: %~1
)
goto :eof
