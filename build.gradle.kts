plugins {
    id("java")
}

group = "org.PrintHouse"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    // SLF4J API (Facade)
    implementation("org.slf4j:slf4j-api:1.7.32")

    // Logback (SLF4J Implementation)
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("ch.qos.logback:logback-core:1.4.11")   // Updated version
}

tasks.test {
    useJUnitPlatform()
}

