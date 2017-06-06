#!groovy

node {
    def mvnHome
    stage('\u27A1 Preparation') {
      git 'git@github.com:spring-labs/org.openwms.gateway.git'
      mvnHome = tool 'M3'
    }
    stage('\u27A1 Build') {
      sh "'${mvnHome}/bin/mvn' clean deploy -Psonatype -U"
    }
    stage('\u27A1 Results') {
      archive 'target/*.jar'
    }
}