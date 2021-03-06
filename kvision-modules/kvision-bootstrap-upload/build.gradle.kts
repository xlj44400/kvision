buildscript {
    extra.set("production", (findProperty("prod") ?: findProperty("production") ?: "false") == "true")
}

plugins {
    kotlin("js")
    id("maven-publish")
}

repositories()

val coroutinesVersion: String by project

kotlin {
    kotlinJsTargets()
}

dependencies {
    implementation(kotlin("stdlib-js"))
    api(project(":kvision-modules:kvision-common-types"))
    api(rootProject)
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:$coroutinesVersion")
    implementation(npm("bootstrap-fileinput", "5.0.8"))
    testImplementation(kotlin("test-js"))
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(kotlin.sourceSets.main.get().kotlin)
}

publishing {
    publications {
        create<MavenPublication>("kotlin") {
            from(components["kotlin"])
            artifact(tasks["sourcesJar"])
            pom {
                defaultPom()
            }
        }
    }
}

setupPublication()

fun copyResources() {
    copy {
        from("$buildDir/processedResources/Js/main")
        into("${rootProject.buildDir}/js/packages/kvision-${project.name}/kotlin")
    }
    copy {
        from("$buildDir/processedResources/Js/main")
        into("${rootProject.buildDir}/js/packages/kvision-${project.name}/kotlin-dce")
    }
}

tasks {
    getByName("JsJar", Jar::class) {
        from("${rootProject.buildDir}/js/packages/kvision-${project.name}/package.json") {
            filter { it.replace("\"main\": \"kotlin/kvision-kvision", "\"main\": \"kvision-kvision") }
        }
    }
    getByName("compileTestKotlinJs") {
        doLast {
            copyResources()
        }
    }
    getByName("processDceKotlinJs") {
        doLast {
            copyResources()
        }
    }
}
