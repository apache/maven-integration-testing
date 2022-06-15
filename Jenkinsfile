#!groovy

pipeline {
  agent any
  // save some io during the build
  options { durabilityHint('PERFORMANCE_OPTIMIZED') }
  
  stages {
    stage("Parallel Stage") {
      parallel {
        stage("Build / Test - mvn 3.8.5 - JDK7") {
          agent { node { label 'ubuntu' } }
          steps {
              timeout( time: 180, unit: 'MINUTES' ) {
                mavenBuild( "jdk_1.7_latest", "maven_3.8.5")
            }
          }
        }
        stage("Build / Test - mvn 3.8.5 - JDK8") {
          agent { node { label 'ubuntu' } }
          steps {
              timeout( time: 180, unit: 'MINUTES' ) {
                mavenBuild( "jdk_1.8_latest", "maven_3.8.5")
            }
          }
        }
        stage("Build / Test - mvn 3.8.5 - JDK11") {
          agent { node { label 'ubuntu' } }
          steps {
              timeout( time: 180, unit: 'MINUTES' ) {
                mavenBuild( "jdk_11_latest", "maven_3.8.5")
            }
          }
        }

      }
    }
  }
}  

def mavenBuild(jdk, mvnName) {
  script {
    try {
      withEnv(["JAVA_HOME=${ tool "$jdk" }",
               "PATH+MAVEN=${ tool "$jdk" }/bin:${tool "$mvnName"}/bin",
               "MAVEN_OPTS=-Xms2g -Xmx4g -Djava.awt.headless=true"]) {
        sh "rm -rf `pwd`/repo"
        sh "mvn clean install -Prun-its,embedded -Dmaven.repo.local=`pwd`/repo -B"
      }
    }
    finally
    {
      junit testResults: 'core-it-suite/target/surefire-reports/*.xml', allowEmptyResults: true
    }
  }
}
