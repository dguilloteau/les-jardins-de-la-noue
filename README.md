# les-jardins-de-la-noue

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/les-jardins-de-la-noue-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)




Détection des items pour l'imortation des anciens formulaires :
    "ENTETE"
    "IMAGE_BANDEAU"     image avec titre à null
    "E_MAIL"            question avec titre renseigné qui contient MAIL
    "NOM_PRENOM"        question avec titre renseigné qui contient NOM
    "PANIER"            liste avec titre renseigné qui contient PANIER
    "LOCALISATION"      image avec titre renseigné qui contient LOCALISATION
    "CAGETTE"           liste avec titre renseigné qui contient CAGETTE
    "FORMULE"           liste avec titre renseigné qui contient FORMULE
    "COMPOSITION"       text avec description renseigné qui contient COMPOSEZ
    "AUTRE"

    Les nouveaux fomulaires créé seront tester avec leur ID
