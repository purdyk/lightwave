plugins {
  java
  kotlin("jvm") version "1.3.60"
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  implementation("com.squareup.okhttp3:okhttp:4.2.1")
  implementation("com.google.code.gson:gson:2.8.6")

}

configure<JavaPluginConvention> {
  sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
  compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
  }
}

//sourceSets {
//  main {
//    java {
//      setSrcDirs(listOf("src"))
//    }
//  }
//}

repositories {
  jcenter()
}

tasks.jar {
  manifest {
    attributes(
      "Main-Class" to "lightwave.CLIKt"
    )
  }

  dependsOn(configurations.runtimeClasspath)

  // Pull in all dependencies for a standalone
  from({
    configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
  })
}