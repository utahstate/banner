pipeline{

  agent any

  stages {
    stage('Checkout'){
      steps{
        checkout scm
      }
    }
    stage('Copy Application'){
      steps{
        echo 'Copy Application'
      }
    }
    stage('Add Config'){
      steps{
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
