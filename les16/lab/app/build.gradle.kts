plugins {
    id("war")
    id("java")
    id("jacoco")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.security:spring-security-web:6.4.2")
    implementation("org.springframework.security:spring-security-config:6.4.2")

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")

    implementation("org.springframework:spring-webmvc:6.2.2")
    implementation("org.thymeleaf:thymeleaf-spring6:3.1.3.RELEASE")

    implementation("org.springframework:spring-web:6.2.2")
    implementation("org.springframework:spring-context:6.2.2")
    implementation("org.springframework:spring-tx:6.2.2")

    implementation("org.springframework.data:spring-data-jpa:3.4.2")
    implementation("org.hibernate.orm:hibernate-core:6.6.5.Final")

    implementation("com.zaxxer:HikariCP:6.2.1")
    implementation("com.h2database:h2:2.3.232")

    implementation("jakarta.annotation:jakarta.annotation-api:3.0.0")
    implementation("org.aspectj:aspectjweaver:1.9.22.1")
    implementation("ch.qos.logback:logback-classic:1.5.16")

    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")

    implementation(libs.guava)
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.11.1")

            dependencies {
                implementation("org.springframework:spring-test:6.2.2")
                implementation("org.mockito:mockito-core:5.15.2")
                implementation("org.mockito:mockito-junit-jupiter:5.15.2")
            }
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

jacoco {
    toolVersion = "0.8.13"
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        html.required.set(true)
        xml.required.set(true)
        csv.required.set(false)
    }
}