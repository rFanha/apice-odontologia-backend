# Comandos usados

Registro dos comandos usados para acompanhar o desenvolvimento item a item.

## Itens 14 a 22

```powershell
rg --files src
Get-ChildItem -Force .\docs
Get-Content -Raw -Path .\docs\documentacao.md
Get-Content -Raw -Path .\pom.xml
Get-Content -Raw -Path .\src\main\resources\application.properties
Get-ChildItem -Recurse -File .\src\main\java | Select-Object -ExpandProperty FullName
javac -version
Get-Command mvn -ErrorAction SilentlyContinue | Select-Object -ExpandProperty Source
rg --files -g 'mvnw*' -g '.mvn/**'
mvn -version
mvn -DskipTests compile
```

## Itens 23 a 28

```powershell
Get-Content -Raw -Path .\docs\documentacao.md
rg --files src\main\java docs
Get-Content -Raw -Path .\src\main\java\com\ruifanha\clinicawisestart\domain\usuario\Usuario.java
Get-Content -Raw -Path .\docs\comandos-usados.md
mvn -DskipTests compile
```
