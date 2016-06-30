@REM How JvZ runs the ITs from a clean slate if it would be on Windows

mvn clean install --show-version -Prun-its,embedded -Dmaven.repo.local=%cd%/repo 

@REM If behind a proxy try this..

@REM mvn clean install --show-version -Prun-its,embedded -Dmaven.repo.local=%cd%/repo -Dproxy.active=true -Dproxy.type=http -Dproxy.host=<host> -Dproxy.port=<port> -Dproxy.user= -Dproxy.pass=
