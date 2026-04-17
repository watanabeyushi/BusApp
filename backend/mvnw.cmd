@REM Maven Wrapper (Windows) — runs Maven without a global mvn install
@echo off
setlocal
set "MAVEN_PROJECTBASEDIR=%~dp0"
cd /d "%MAVEN_PROJECTBASEDIR%"
java -classpath ".mvn\wrapper\maven-wrapper.jar" "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%." org.apache.maven.wrapper.MavenWrapperMain %*
exit /b %ERRORLEVEL%
