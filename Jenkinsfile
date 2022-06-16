#!groovy

pipeline {
  agent any
  // save some io during the build
  options { durabilityHint('PERFORMANCE_OPTIMIZED') }
  
  stages {
    stage("Parallel Stage") {
      parallel {
        stage("Build / Test - mvn latest - JDK8 - ubuntu") {
          agent { node { label 'ubuntu' } }
          steps {
              timeout( time: 180, unit: 'MINUTES' ) {
                mavenBuild( "jdk_1.8_latest", "maven_latest")
            }
          }
        }
        stage("Build / Test - mvn latest - JDK11 - ubuntu") {
          agent { node { label 'ubuntu' } }
          steps {
              timeout( time: 180, unit: 'MINUTES' ) {
                mavenBuild( "jdk_11_latest", "maven_latest")
            }
          }
        }
        stage("Build / Test - mvn latest - JDK8 - windowx") {
          agent { node { label 'Windows' } }
          steps {
              timeout( time: 180, unit: 'MINUTES' ) {
                mavenBuild( "jdk_1.8_latest", "maven_latest")
            }
          }
        }
        stage("Build / Test - mvn latest - JDK11 - windows") {
          agent { node { label 'Windows' } }
          steps {
              timeout( time: 180, unit: 'MINUTES' ) {
                mavenBuild( "jdk_11_latest", "maven_latest")
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
        sh "mvn -V clean install -Prun-its,embedded -Dmaven.repo.local=`pwd`/repo -B"
      }
    }
    finally
    {
      junit testResults: 'core-it-suite/target/surefire-reports/*.xml', allowEmptyResults: true
    }
  }
}
