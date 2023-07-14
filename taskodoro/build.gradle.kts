/*
 *    Copyright 2023 Felipe Joglar
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.sqlDelight)
}

kotlin {

    android()

    listOf(
        iosArm64(),
        iosX64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "Taskodoro"
        }
    }

    sourceSets {

        /* Main source sets */
        val commonMain by getting
        val androidMain by getting {
            dependencies {
                implementation(libs.sqlDelight.android.driver)
            }
        }
        val nativeMain by creating {
            dependencies {
                implementation(libs.sqlDelight.native.driver)
            }
        }
        val iosMain by creating
        val iosArm64Main by getting
        val iosX64Main by getting
        val iosSimulatorArm64Main by getting

        /* Main hierarchy */
        androidMain.dependsOn(commonMain)
        nativeMain.dependsOn(commonMain)
        iosMain.dependsOn(nativeMain)
        iosX64Main.dependsOn(iosMain)
        iosArm64Main.dependsOn(iosMain)
        iosSimulatorArm64Main.dependsOn(iosMain)

        /* Test source sets */
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(libs.sqlDelight.jvm.driver)
            }
        }
        val iosArm64Test by getting
        val iosX64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating
        val nativeTest by creating

        /* Test hierarchy */
        androidUnitTest.dependsOn(commonTest)
        nativeTest.dependsOn(commonTest)
        iosTest.dependsOn(nativeTest)
        iosArm64Test.dependsOn(iosTest)
        iosX64Test.dependsOn(iosTest)
        iosSimulatorArm64Test.dependsOn(iosTest)
    }

    // Enable concurrent sweep phase in new native memory manager.
    // https://kotlinlang.org/docs/whatsnew1620.html#concurrent-implementation-for-the-sweep-phase-in-new-memory-manager
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries.all {
            freeCompilerArgs = freeCompilerArgs.plus("-Xgc=cms")
        }
    }
}

android {
    namespace = "com.taskodoro"

    compileSdk = config.versions.compileSdk.get().toInt()
    buildToolsVersion = config.versions.buildTools.get()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    defaultConfig {

        minSdk = config.versions.minSdk.get().toInt()
        targetSdk = config.versions.targetSdk.get().toInt()
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

sqldelight {
    databases {
        create("TaskodoroDB") {
            packageName.set("com.taskodoro.storage.db")
        }
    }
}

// As of today `allTest` task does not launches android unit tests.
tasks.register("allTestsWithAndroid") {
    group = "verification"
    description = "Runs the tests for all targets in this module."

    dependsOn("iosX64Test")
    dependsOn("testDebugUnitTest")
    dependsOn("testReleaseUnitTest")
}
