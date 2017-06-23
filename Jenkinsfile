pipeline{

  agent any

  tools {
    jdk 'Java 8u131'
  }
  stages {
    stage('Checkout'){
      steps{
        checkout scm
      }
    }
    stage('Build War'){
      steps{
        echo 'Copy Application'
        sh 'scp jenkins@build.banner.usu.edu:/u01/deploy/zdevl/self-service/BannerFinanceSSB.war .'
        echo 'Add Config'
        sh 'jar uvf BannerFinanceSSB.war WEB-INF'
      }
    }
    node('build'){
      stage 'Build Image'
        def img
        img = docker.build("banner/financeselfservice")

      stage 'Push Image'
      echo 'Push Image'
    }

  }
}
