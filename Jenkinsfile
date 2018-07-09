properties([gitLabConnection('gitlab.usu.edu')])

node {
  def javaHome = tool 'OracleJDK8'
  def baseImage = docker.image('edurepo/banner9-selfservice:tomcat8.5-jre8-alpine')

  stage 'Checkout'
    checkout scm
    echo "Branch Name ${env.BRANCH_NAME} Build ID ${env.BUILD_ID} Build Number ${env.BUILD_NUMBER} Job Name ${env.JOB_NAME}"
    withDockerRegistry(){
      baseImage.pull()
    }

  stage 'Build War'
    gitlabCommitStatus("Build War"){
      if (env.BRANCH_NAME == "master"){
        withAWS(credentials:"Jenkins-S3", region:'us-east-1'){
          s3Download(file:'StudentRegistrationSsb.war', bucket:'usu-banner-builds', path:"banner/input/studentregistrationssb/9.6/StudentRegistrationSsb.war", force:true)
        }
      } else {
        withAWS(credentials:"Jenkins-S3", region:'us-east-1'){
          s3Download(file:'StudentRegistrationSsb.war', bucket:'usu-banner-builds', path:"banner/input/studentregistrationssb/${env.BRANCH_NAME}/StudentRegistrationSsb.war", force:true)
        }
      }
      sh "mkdir StudentRegistrationSsb && cd StudentRegistrationSsb && ${javaHome}/bin/jar xvf ../StudentRegistrationSsb.war"
      sh "cp WEB-INF/classes/* StudentRegistrationSsb/WEB-INF/classes"
    }


    stage 'Build Image'
      gitlabCommitStatus("Build Image"){
      def img
      withDockerRegistry([credentialsId: 'docker-registry-credentials', url: "https://harbor.usu.edu/"]){
        if (env.BRANCH_NAME == "master"){
          img = docker.build('harbor.usu.edu/banner/studentregistrationssb:latest')
        } else {
          img = docker.build("harbor.usu.edu/banner/studentregistrationssb:${env.BRANCH_NAME}")
          img.push()
        }
      }
    }
}
