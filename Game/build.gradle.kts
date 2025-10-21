plugins {
    id("kotlin")
}

sourceSets.main {
    java.srcDir("src")
}

dependencies {
    implementation(project(":Board"))
}
