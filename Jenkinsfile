pipeline{

  agent any

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
      }
    }
    stage('Build Image'){
      steps{
        echo 'Build Image'
      }
    }
    stage('Push Image'){
      steps{
        echo 'Push Image'
      }
    }
  }
}
