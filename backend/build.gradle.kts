import com.google.protobuf.gradle.id

plugins {
    java
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.google.protobuf") version "0.9.4"
}

group = "com.example"
version = "1.0"
val grpcVersion = "1.62.2"
val protocVersion = "3.25.3"
val osClassifier = "osx-x86_64"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.2.3")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("io.micrometer:micrometer-registry-prometheus:1.12.3")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.modelmapper:modelmapper:3.1.1")
    implementation("com.auth0:java-jwt:4.4.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.8.6")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.mockito:mockito-core:4.11.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    implementation("io.grpc:grpc-stub:${grpcVersion}")
    runtimeOnly("io.grpc:grpc-netty-shaded:${grpcVersion}")
    implementation("io.grpc:grpc-protobuf:${grpcVersion}")
    implementation("io.grpc:protoc-gen-grpc-java:${grpcVersion}")
    implementation("io.grpc:grpc-services:${grpcVersion}")
    implementation("com.google.protobuf:protobuf-java:${protocVersion}")
    compileOnly("org.apache.tomcat:annotations-api:6.0.53")
    protobuf("com.google.protobuf:protoc:$protocVersion")
    protobuf("io.grpc:protoc-gen-grpc-java:$grpcVersion")
}


repositories {
    mavenCentral()
}

sourceSets {
    main {
        proto {
            srcDir("${projectDir}/../proto")
        }
    }
}

protobuf{
    protoc {
        artifact = "com.google.protobuf:protoc:${protocVersion}"
    }
    plugins{
        id("grpc"){
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
    }
    generatedFilesBaseDir = "${buildDir}/generated/protobuf"
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
            }
        }
    }
}

java.sourceSets["main"].java{
    srcDir("${buildDir}/generated/openapi/src/main/java")
}

val compileJava by tasks.getting(JavaCompile::class){
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    useJUnitPlatform()
}

configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.junit.platform") {
            useVersion("1.10.2")
        }
    }
}
