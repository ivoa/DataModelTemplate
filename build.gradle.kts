plugins {
    // this plugin provides all the vo-dml functionality
    id("net.ivoa.vo-dml.vodmltools") version "0.5.16"
}

vodml {
    vodmlDir.set(file("vo-dml"))
    vodslDir.set(file("model"))
    bindingFiles.setFrom(file("vo-dml/TemplateDM-v1.vodml-binding.xml"))
    outputDocDir.set(layout.projectDirectory.dir("doc/std/vodml-generated"))
    outputSiteDir.set(layout.projectDirectory.dir("doc/site/generated")) // N.B the last part of this path must be "generated"

}
/* uncomment below to run the generation of vodml from vodsl automatically */
//tasks.named("vodmlJavaGenerate") {
//    dependsOn("vodslToVodml")
//}
//tasks.named("vodmlSchema") {
//    dependsOn("vodslToVodml")
//}
//tasks.named("vodmlSite") {
//    dependsOn("vodslToVodml")
//}


tasks.test {
    useJUnitPlatform()
}

dependencies {
    //all data models will want to depend on the base model at least
    api("org.javastro.ivoa.vo-dml:ivoa-base:1.0-SNAPSHOT") // IMPL using API so that it appears in transitive compile

    // the dependencies below are related to testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")

    implementation("org.slf4j:slf4j-api:1.7.32")
    testRuntimeOnly("ch.qos.logback:logback-classic:1.4.12")

    testImplementation("com.h2database:h2:2.2.220")
    testImplementation("org.javastro:jaxbjpa-utils:0.2.3")
    testImplementation("org.javastro:jaxbjpa-utils:0.2.3:test")
}


// site tasks
tasks.register<Copy>("copyJavaDocForSite") {
    from(layout.buildDirectory.dir("docs/javadoc"))
    into(vodml.outputSiteDir.dir("javadoc"))
    dependsOn(tasks.javadoc)

}


tasks.register<Copy>("copySchemaForSite") {
    from(layout.buildDirectory.dir("generated/sources/vodml/schema"))
    into(vodml.outputSiteDir.dir("schema"))
    dependsOn("vodmlSchema")

}


tasks.register<Exec>("makeSiteNav")
{
    commandLine("yq","eval",  "(.nav | .. | select(has(\"AutoGenerated Documentation\"))|.[\"AutoGenerated Documentation\"]) += (load(\"doc/site/generated/allnav.yml\")|sort_by(keys|.[0]))", "mkdocs_template.yml")
    standardOutput= file("mkdocs.yml").outputStream()
    dependsOn("vodmlSite")
    dependsOn("copyJavaDocForSite")
    dependsOn("copySchemaForSite")

}
tasks.register<Exec>("testSite"){
    commandLine("mkdocs", "serve")
    dependsOn("makeSiteNav")
}
tasks.register<Exec>("doSite"){
    commandLine("mkdocs", "gh-deploy", "--force")
    dependsOn("makeSiteNav")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}