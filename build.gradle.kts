/*
 * Copyright (c) 2019 NetFoundry, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val zitiBuildnum by extra { System.getenv("BUILD_NUMBER") ?: "local" }

plugins {
    kotlin("jvm") version "1.3.50"
    id("io.wusa.semver-git-plugin") version "1.2.1"
}

repositories {
    mavenCentral()
}


group = "io.netfoundry.ziti"

semver {
    nextVersion = "patch"
    snapshotSuffix = "<count>.<sha><dirty>"
    dirtyMarker = "-dirty"
    initialVersion = "0.1.0"
}

if (semver.info.branch.id == "master") {
    version = "${semver.info.version}-${zitiBuildnum}"
} else {
    version = "${semver.info.branch.id}-${semver.info.version}"
}
val gitCommit by extra { semver.info.shortCommit }
val gitBranch by extra { semver.info.branch.name }

subprojects {
    group = "io.netfoundry.ziti"
    version = rootProject.version
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}