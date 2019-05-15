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
        bat 'npm run build --prefix ./examination-admin'
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
