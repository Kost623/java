#!/usr/bin/env bash
set -e
JUNIT=lib/junit-platform-console-standalone-1.10.2.jar
rm -rf out && mkdir -p out/main out/test
javac -encoding UTF-8 -d out/main Class/*.java Exceptions/*.java Service/*.java
javac -encoding UTF-8 -cp out/main:$JUNIT -d out/test test/*.java
java -jar $JUNIT execute --class-path out/main:out/test --scan-class-path out/test --details=tree --disable-banner
