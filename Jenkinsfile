pipeline {
  agent any

  tools {
    maven 'maven'
    jdk 'JDK8'
  }

  stages {
    stage('Build') {
      steps {
        bat 'mvn -f ./examination-server/pom.xml clean package -P prod'
        bat 'path'
        bat 'java -jar ./examination-server/target/examination-0.0.1-SNAPSHOT.jar'
      }
    }
  }

  post {
    always {
        echo 'post always'
    }
    success {
        echo 'post success'
    }
    cleanup {
        echo 'post cleanup'
    }
  }
}
