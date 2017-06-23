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
