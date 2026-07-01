New-Item -ItemType Directory -Force -Path bin | Out-Null
javac -encoding UTF-8 -cp "lib/*" -d bin $(Get-ChildItem -Recurse -Filter *.java -Path src | ForEach-Object { $_.FullName })
java -cp "bin;lib/*" broker.visao.Main
