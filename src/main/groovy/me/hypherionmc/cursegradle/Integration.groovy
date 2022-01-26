package me.hypherionmc.cursegradle

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.plugins.JavaPluginExtension

class Integration {

    private static final Logger log = Logging.getLogger(Integration)

    static void checkJava(Project project, CurseProject curseProject) {
        try {
            if (project.plugins.hasPlugin('java')) {
                log.info 'Java plugin detected, adding integration...'
                if (curseProject.mainArtifact == null) {
                    Task jarTask = project.tasks.getByName('jar')
                    log.info "Setting main artifact for CurseForge Project $curseProject.id to the java jar"
                    CurseArtifact artifact = new CurseArtifact()
                    artifact.artifact = jarTask
                    curseProject.mainArtifact = artifact
                    curseProject.uploadTask.dependsOn jarTask
                }
            }
        } catch (Throwable t) {
            log.warn("Failed Java integration", t)
        }
    }

    static void checkJavaVersion(Project project, CurseProject curseProject) {

        try {
            JavaPluginExtension javaConv = (JavaPluginExtension) project.getExtensions().findByName("java")
            JavaVersion javaVersion = JavaVersion.toVersion(javaConv.targetCompatibility)

            if (JavaVersion.VERSION_1_6 >= javaVersion) {
                curseProject.addGameVersion('Java 6')
            }
            if (JavaVersion.VERSION_1_7 >= javaVersion) {
                curseProject.addGameVersion('Java 7')
            }
            if (JavaVersion.VERSION_1_8 >= javaVersion) {
                curseProject.addGameVersion('Java 8')
            }
            if (project.extensions.getByType(CurseExtension).curseGradleOptions.detectNewerJava) {
                if (JavaVersion.VERSION_1_9 >= javaVersion) {
                    curseProject.addGameVersion('Java 9')
                }
                if (JavaVersion.VERSION_16 >= javaVersion) {
                    curseProject.addGameVersion('Java 16')
                }
                if (JavaVersion.VERSION_17 >= javaVersion) {
                    curseProject.addGameVersion('Java 17')
                }
            }

        } catch (Throwable t) {
            log.warn("Failed to check Java Version", t)
        }
    }

    static void checkForgeGradle(Project project, CurseProject curseProject) {
        if (project.hasProperty('minecraft')) {
            log.info "ForgeGradle plugin detected, adding integration..."

            // FG 3+ doesn't set MC_VERSION until afterEvaluate
            project.gradle.taskGraph.whenReady {
                try {
                    if (project.minecraft.hasProperty('version')) {
                        log.info 'Found Minecraft version in FG < 3'
                        curseProject.addGameVersion(project.minecraft.version)
                        curseProject.addGameVersion('Forge')
                    } else if (project.getExtensions().getExtraProperties().has('MC_VERSION')) {
                        log.info 'Found Minecraft version in FG >= 3'
                        curseProject.addGameVersion(project.getExtensions().getExtraProperties().get('MC_VERSION'))
                        curseProject.addGameVersion('Forge')
                    } else {
                        log.warn 'Unable to extract Minecraft version from ForgeGradle'
                    }
                } catch (Throwable t) {
                    log.warn('Failed ForgeGradle integration', t)
                }
            }
        }
    }

    // Experimental Fabric Integration
    static void checkFabric(Project project, CurseProject curseProject, boolean detectApi) {
        if (project.configurations.hasProperty("minecraft") && project.configurations.hasProperty("mappings")) {
            log.info "Fabric mod detected, adding integration..."
            curseProject.addGameVersion("Fabric")

            project.gradle.taskGraph.whenReady {
                try {
                    String mcver = project.configurations.minecraft.allDependencies[0].version;
                    log.info("Found Minecraft Version: " + mcver)
                    curseProject.addGameVersion(mcver)

                    // Check for Fabric API and add dependency
                    if (project.configurations.hasProperty("modImplementation") && detectApi) {
                        if (project.configurations.modImplementation.allDependencies.find { it.name == 'fabric-api' }) {
                            String fabricApiVer = project.configurations.modImplementation.allDependencies.find { it.name == 'fabric-api' }.version;
                            log.info("Found Fabric API Version: " + fabricApiVer)
                            if (curseProject.mainArtifact.curseRelations != null) {
                                curseProject.mainArtifact.curseRelations.requiredDependency("fabric-api")
                            } else {
                                CurseRelation curseRelation = new CurseRelation();
                                curseRelation.requiredDependency("fabric-api")
                                curseProject.mainArtifact.setCurseRelations(curseRelation)
                            }
                        }
                    }
                } catch (Throwable t) {
                    log.warn("Failed Fabric integration", t)
                }
            }
        }
    }


}
