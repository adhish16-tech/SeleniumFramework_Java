pipeline {
  agent any
  tools {
    maven 'Maven_3'
    jdk 'jdk17'
  }
  stages {
    stage('Checkout') {
      steps {
        git branch: 'main', url: 'git@github.com:adhish16-tech/SeleniumFramework.git'
      }
    }
    stage('Build & Test') {
      steps {
        sh 'mvn clean test -Dsurefire.suiteXmlFiles=testSuites/testng.xml -Dbrowser=chrome'
      }
    }
    stage('Reports') {
      steps {
        junit '**/target/surefire-reports/*.xml'
        archiveArtifacts artifacts: 'reports/*.png', allowEmptyArchive: true
      }
    }
  }
}
