:: Compile class
javac CommandRunner.java

:: Create out directory
@RD /S /Q build
mkdir build

:: Build JAR
:: jar cvfe <JAR_output_name> <main_class> <.class_file>
jar cvfe build/commandRunner.jar CommandRunner CommandRunner.class