plugins {
    // this plugin provides all the vo-dml functionality
    id("net.ivoa.vo-dml.vodmltools") version "0.3.19"
}

vodml {
    vodmlDir.set(file("vo-dml"))
    vodslDir.set(file("model"))
    bindingFiles.setFrom(file("vo-dml/TemplateDM-v1.vodml-binding.xml"))

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
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.1")

    implementation("org.slf4j:slf4j-api:1.7.32")
    testRuntimeOnly("ch.qos.logback:logback-classic:1.2.3")

    testImplementation("org.apache.derby:derby:10.14.2.0")
    testImplementation("org.javastro:jaxbjpa-utils:0.1.2")
    testImplementation("org.javastro:jaxbjpa-utils:0.1.2:test")
}
