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
        sh 'jar uvf BannerFinanceSSB.war WEB-INF'
      }
    }
    stage('Build Image'){
      steps{
        echo 'Build Image'
        sh 'docker.build("banner/financeselfservice")'
      }
    }
    stage('Push Image'){
      steps{
        echo 'Push Image'
      }
    }
  }
}
