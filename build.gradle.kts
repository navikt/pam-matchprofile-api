version = "0.1"
group "no.nav.arbeidsplassen"

val kotlinVersion= project.properties["kotlinVersion"]
val micronautKafkaVersion= project.properties["micronautKafkaVersion"]
val micronautMicrometerVersion= project.properties["micronautMicrometerVersion"]
val logbackEncoderVersion= project.properties["logbackEncoderVersion"]
val jakartaPersistenceVersion= project.properties["jakartaPersistenceVersion"]
val postgresqlVersion= project.properties["postgresqlVersion"]
val tcVersion= project.properties["tcVersion"]
val javaVersion= project.properties["javaVersion"]
val openSearchRestClientVersion= project.properties["openSearchRestClientVersion"]
val jakartaJsonVersion= project.properties["jakartaJsonVersion"]
val pamGeographyVersion= project.properties["pamGeographyVersion"]

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.21"
    id("org.jetbrains.kotlin.kapt") version "1.6.21"
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("io.micronaut.application") version "3.3.2"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.6.21"
}

configurations.all {
    resolutionStrategy {
        failOnChangingVersions()
    }
}


repositories {
    mavenLocal()
    mavenCentral()
    maven("https://jcenter.bintray.com")
    maven("https://github-package-registry-mirror.gc.nav.no/cached/maven-release")
}

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("no.nav.arbeidsplassen.*")
    }
}

dependencies {
    kapt("io.micronaut:micronaut-http-validation")
    kapt("io.micronaut.data:micronaut-data-processor")
    implementation("io.micronaut:micronaut-http-client")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("javax.annotation:javax.annotation-api")
    implementation ("jakarta.persistence:jakarta.persistence-api:${jakartaPersistenceVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib") {
        version {
            strictly("$kotlinVersion")
        }
    }
    implementation("org.jetbrains.kotlin:kotlin-reflect") {
        version {
            strictly("$kotlinVersion")
        }
    }
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8") {
        version {
            strictly("$kotlinVersion")
        }
    }
    implementation("org.jetbrains.kotlin:kotlin-stdlib-common") {
        version {
            strictly("$kotlinVersion")
        }
    }
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7") {
        version {
            strictly("$kotlinVersion")
        }
    }
    implementation("io.micronaut.kafka:micronaut-kafka:${micronautKafkaVersion}")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("net.logstash.logback:logstash-logback-encoder:${logbackEncoderVersion}")
    implementation("io.micronaut:micronaut-validation")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.micronaut.micrometer:micronaut-micrometer-core")
    implementation("io.micronaut.micrometer:micronaut-micrometer-registry-prometheus")
    implementation("io.micronaut:micronaut-management")
    implementation("io.micronaut.data:micronaut-data-jdbc")
    implementation("org.postgresql:postgresql:${postgresqlVersion}")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.flyway:micronaut-flyway")
    implementation("org.opensearch.client:opensearch-rest-high-level-client:${openSearchRestClientVersion}")
    implementation("jakarta.json:jakarta.json-api:${jakartaJsonVersion}")
    implementation("no.nav.pam.geography:pam-geography:${pamGeographyVersion}")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.testcontainers:postgresql:${tcVersion}")

}


application {
    mainClass.set("no.nav.arbeidsplassen.matchprofile.Application")
}
java {
    sourceCompatibility = JavaVersion.toVersion("$javaVersion")
}


tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "$javaVersion"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "$javaVersion"
        }
    }

    test {
        exclude("**/*IT.class")
    }

}
