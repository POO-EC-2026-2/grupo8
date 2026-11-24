
# CI Maven

Configuração de integração contínua utilizando GitHub Actions e Java 17.

O workflow `.github/workflows/maven.yml` realiza:

- Compilação dos módulos Modelo, Backend e Desktop.
- Execução do TesteDominio.
- Execução do TesteServico.

## Build

```bash
cd Atlas
mvn -B -ntp install
```
