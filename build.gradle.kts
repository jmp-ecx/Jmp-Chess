plugins {
    kotlin("jvm") version "2.0.10"
}

allprojects {
    group = "com.xenon"
    version = "0.1.0"

    repositories {
        mavenCentral()
    }
}

kotlin {
    jvmToolchain(17)
}
