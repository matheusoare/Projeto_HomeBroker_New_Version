#!/bin/bash
mkdir -p bin
javac -encoding UTF-8 -cp "lib/*" -d bin $(find src -name "*.java")
java -cp "bin:lib/*" broker.visao.Main
