#!groovy

node {
    def mvnHome
    stage('\u27A1 Preparation') {
      git 'git@github.com:spring-labs/org.openwms.gateway.git'
      mvnHome = tool 'M3'
      cmdLine = '-Dci.buildNumber=${BUILD_NUMBER} -Ddocumentation.dir=${WORKSPACE}/target'
    }
    stage('\u27A1 Build') {
      configFileProvider(
          [configFile(fileId: 'maven-local-settings', variable: 'MAVEN_SETTINGS')]) {
            sh "'${mvnHome}/bin/mvn' -s $MAVEN_SETTINGS clean deploy ${cmdLine} -Psonatype -U"
      }
    }
    stage('\u27A1 Results') {
      archive 'target/*.jar'
    }
}