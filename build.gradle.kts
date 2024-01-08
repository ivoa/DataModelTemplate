plugins {
    // this plugin provides all the vo-dml functionality
    id("net.ivoa.vo-dml.vodmltools") version "0.4.2"
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

    testImplementation("org.apache.derby:derby:10.15.2.1")
    testImplementation("org.javastro:jaxbjpa-utils:0.1.2")
    testImplementation("org.javastro:jaxbjpa-utils:0.1.2:test")
}

tasks.register<Copy>("copyJavaDocForSite") {
    from(layout.buildDirectory.dir("docs/javadoc"))
    into(vodml.outputSiteDir.dir("javadoc"))
    dependsOn(tasks.javadoc)

}

tasks.register<Exec>("makeSiteNav")
{
    commandLine("yq","eval",  "(.nav.[]|select(has(\"AutoGenerated Documentation\"))|.[\"AutoGenerated Documentation\"]) += load(\"allnav.yml\")", "mkdocs_template.yml")
    standardOutput= file("mkdocs.yml").outputStream()
    dependsOn("vodmlSite")
    dependsOn("copyJavaDocForSite")

}
tasks.register<Exec>("testSite"){
    commandLine("mkdocs", "serve")
    dependsOn("makeSiteNav")
}
tasks.register<Exec>("doSite"){
    commandLine("mkdocs", "gh-deploy", "--force")
    dependsOn("makeSiteNav")
}
