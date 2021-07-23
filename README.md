# CurseGradle - Updated

[![Build Status](https://ci.explodingcreeper.me/buildStatus/icon?job=CurseGradle)](https://ci.explodingcreeper.me/view/HypherionMC's%20Projects/job/CurseGradle/)

An updated fork of the CurseGralde gradle plugin for publishing artifacts to [CurseForge](https://curseforge.com/).

This project was originally created by Matthew Prenger, but hasn't been updated since 2020, and with Minecraft Switching to Java 16, this could pose some problems. So I forked and updated the project to support Gradle 7 and Java 16.

___

* Discord: [Hypherion Development](https://discord.gg/PdVnXf9)
* [Wiki](https://github.com/matthewprenger/CurseGradle/wiki/)
* [Changelog](https://github.com/exploding-creeper/CurseGradle/releases)

___

## Simple Quickstart with ForgeGradle
If you're using ForgeGradle, which you probably are, the following script is a bare-minimum. For more details about customization, check out the [Wiki](https://github.com/matthewprenger/CurseGradle/wiki).

### This plugin is still pending approval on the Gradle Plugin registry, so for now, please use the MAVEN method.

To find out which versions are available, check [HERE](https://maven.explodingcreeper.me/me/hypherionmc/CurseGradle/).

___

### Installation
#### Via Gradle Plugins

```gradle
plugins {
    id 'net.minecraftforge.gradle.forge' version '2.0.2'
    id 'me.hypherionmc.cursegradle' version '<VERSION>'
}
```

#### Via My Maven (Recommended)
```gradle
buildscript {
    repositories {
        // These repositories are only for Gradle plugins, put any other repositories in the repository block further below
        maven { url = 'https://maven.minecraftforge.net' }
        maven { url = "https://maven.explodingcreeper.me" }
        mavenCentral()
    }
    dependencies {
        classpath group: 'net.minecraftforge.gradle', name: 'ForgeGradle', version: '5.1.+', changing: true
    }
}
plugins {
    id 'me.hypherionmc.cursegradle' version '<VERSION>'
}
apply plugin: 'net.minecraftforge.gradle'
```

#### Task Setup
```gradle
curseforge {
  apiKey = '123-456' // This should really be in a gradle.properties file
  project {
    id = '12345'
    changelog = 'Changes' // A file can also be set using: changelog = file('changelog.txt')
    releaseType = 'beta'
  }
}
```
___

Thanks to Matthew for creating the original plugin and if you want to contribute to this fork, you are welcome to do so.
