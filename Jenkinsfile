properties([gitLabConnection('gitlab.usu.edu')])

pipeline {
  agent any
  
  def javaHome = tool 'OracleJDK8'
  def baseImage = docker.image('edurepo/banner9-selfservice:tomcat8.5-jre8-alpine')


  stages {

  
  stage ('Checkout'){
    checkout scm
    echo "Branch Name ${env.BRANCH_NAME} Build ID ${env.BUILD_ID} Build Number ${env.BUILD_NUMBER} Job Name ${env.JOB_NAME}"
    withDockerRegistry([credentialsId: 'docker-registry-credentials', url: "https://harbor.usu.edu"]){
      baseImage.pull()
    }
  }

  stage ('Build War'){
    gitlabCommitStatus("Build War"){
      if (env.BRANCH_NAME == "master"){
        withAWS(credentials:"Jenkins-S3", region:'us-east-1'){
          s3Download(file:'applicationNavigator.war', bucket:'usu-banner-builds', path:"banner/input/applicationnavigator/2.1/applicationnavigator.war", force:true)
        }
      } else {
        withAWS(credentials:"Jenkins-S3", region:'us-east-1'){
          s3Download(file:'applicationNavigator.war', bucket:'usu-banner-builds', path:"banner/input/applicationnavigator/${env.BRANCH_NAME}/applicationNavigator.war", force:true)
        }
      }
      sh "mkdir applicationNavigator  && cd applicationNavigator && ${javaHome}/bin/jar xvf ../applicationNavigator.war"
      sh "cp WEB-INF/classes/* applicationNavigator/WEB-INF/classes/"
      sh "cp css/* applicationNavigator/css/"
   }
  }


  stage ('Build Image'){
    gitlabCommitStatus("Build Image"){
     def img
     withDockerRegistry([credentialsId: 'docker-registry-credentials', url: "https://harbor.usu.edu/"]){
      if (env.BRANCH_NAME == "master"){
                  img = docker.build('harbor.usu.edu/banner/applicationnavigator:latest')
        } else {
          img = docker.build("harbor.usu.edu/banner/applicationnavigator:${env.BRANCH_NAME}")
          img.push()
        }
      }
    }
  }

  stage ('Clean Workspace'){
    cleanWs()
  }
  }
}
