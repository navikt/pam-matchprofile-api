pam-matchprofile-api
================

REST API for match-profiler.

Applikasjonen for å motta stillingsannonser og mappe dem til match-profiler via parser hos Janzz.
Match-profilene tilgjengeliggjøres via REST API og Kafka.

# Komme i gang
## Kjøre applikasjonen

Applikasjonen kan startes lokalt i terminal fra prosjektets rotmappe:

```
./gradlew run
```

Eventuelt kan man lage en `Micronaut`-konfigurasjon med Main class `no.nav.arbeidsplassen.matchprofile.Application`
i IntelliJ.

API-et eksponeres på port `8080`.

## Lokalt utviklingsmiljø

Lokalt utviklingsmiljø kan settes opp med `Docker`. Bygg og start med følgende kommandoer fra
prosjektets rotmappe:

```
docker compose build
```
```
docker compose up
```

API-et eksponeres via Docker-container på port `9090`.

---

# Henvendelser

Spørsmål knyttet til koden eller prosjektet kan stilles som issues her på GitHub.
