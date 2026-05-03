plugins {
    id("java-library")
    
    // https://docs.gradle.org/current/userguide/publishing_maven.html
    id("maven-publish")
    
    id("uk.co.magictractor.magictractor-project-plugin") version "0.0.1-SNAPSHOT"
}

group = "uk.co.magictractor"
version = "0.0.1-SNAPSHOT"

// https://docs.gradle.org/current/userguide/publishing_maven.html
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        
            pom {
                name = "${project.name.replaceFirstChar(Char::titlecase)}"
                description = "Create PDFs and other documents using a builder that abstracts use of Apache FOP."
                url = "https://github.com/magictractor/${project.name}"
                inceptionYear = "2026"

                licenses {
                    license {
                        name = "Apache License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0"
                    }
                }
                developers {
                    developer {
                        id = "kend"
                        name = "Ken Dobson"
                       // email = "me@gmail.com"
                    }
                }
                scm {
                    // connection = "scm:git:git:github.com/magictractor/${project.name}.git"
                    // developerConnection = "scm:git:ssh://github.com/magictractor/${project.name}.git"
                    url = "https://github.com/magictractor/${project.name}"
                }
            }
        }
    }

    repositories {
        //maven {
        //    name = "myRepo"
        //    url = layout.buildDirectory.dir("repo")
        //}
    }
}

// https://docs.gradle.org/current/userguide/declaring_repositories.html
repositories {
    mavenCentral()
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

// https://docs.gradle.org/current/userguide/building_java_projects.html
// https://docs.gradle.org/current/userguide/java_plugin.html
// TODO! revisit withType() here - JavaCompile is not correct?
// tasks.withType<JavaCompile>().configureEach {
java {
//tasks.withType<JavaPlugin>().configureEach {
    // task: extension 'java'  class org.gradle.api.plugins.internal.DefaultJavaPluginExtension_Decorated
    //logger.lifecycle("task: " + this + "  " + this.javaClass)

    toolchain {
        languageVersion = JavaLanguageVersion.of(8)
    }
    
    withSourcesJar()
    //withJavadocJar()
}

// :compileJava
tasks.withType<JavaCompile>().configureEach {
    // Include details about deprecated code in build/reports/problems/problems-report.html
    // options.compilerArgs.add("-Xlint:unchecked")
    options.setDeprecation(true)
}

// :jar
tasks.withType<Jar>().configureEach {
    destinationDirectory.set(file("$rootDir/jars"))
}

// :clean
tasks.withType<Delete>().configureEach {
    // Before doFirst for caching.
    // https://docs.gradle.org/9.5.0/userguide/configuration_cache_requirements.html#config_cache:requirements:disallowed_types
    val jarDir = File("$rootDir/jars")
     
    doFirst {
        val deleted = jarDir.deleteRecursively()
        if (deleted) {
            logger.lifecycle("jars deleted")
        } else {
            logger.warn("Failed to delete " + jarDir)
        }
    }
}


// "libs.xxx" refers to libraries configured in version catalog in settings.gradle.
dependencies {
    // Logger API.
    implementation(libs.slf4j.api)
    // Logger implementation for unit tests.
    runtimeOnly(libs.logback.classic)

    /**
        This project should have minimal dependencies other than testing libraries. 
        Dependencies have compileOnly scope because some util packages will not 
        require the dependency.
     */
    
    // Joiners and Splitters are used by util.converters.
    compileOnly(libs.guava)
    
    testImplementation(libs.junit.jupiter);
    testRuntimeOnly(libs.junit.jupiter.platform);
    testImplementation(libs.assertj);
}
