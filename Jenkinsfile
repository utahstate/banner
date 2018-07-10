properties([gitLabConnection('gitlab.usu.edu')])

node {
  def javaHome = tool 'OracleJDK8'
  def baseImage = docker.image('edurepo/banner9-selfservice:tomcat8.5-jre8-alpine')

  stage 'Checkout'
    checkout scm
    echo "Build Name ${env.BRANCH_NAME} Build ID ${env.BUILD_ID} Build Number ${env.BUILD_NUMBER} Job Name ${env.JOB_NAME}"

    withDockerRegistry([credentialsId: 'docker-registry-credentials', url: "https://harbor.usu.edu"]){
      baseImage.pull()
    }

  stage 'Build War'
    gitlabCommitStatus("Build War"){
      if(env.BRANCH_NAME == "master"){
        withAWS(credentials:"Jenkins-S3", region:'us-east-1'){
          s3Download(file: 'IntegrationApi.war', bucket:'usu-banner-builds', path:"banner/input/integrationapi/9.8/IntegrationApi.war", force:true)
        }
      } else {
        withAWS(credentials:"Jenkins-S3", region:'us-east-1'){
          s3Download(file: 'IntegrationApi.war', bucket:'usu-banner-builds', path:"banner/input/integrationapi/${env.BRANCH_NAME}/IntegrationApi.war", force:true)
        }
      }
      sh "mkdir IntegrationApi"
      sh "cd IntegrationApi && ${javaHome}/bin/jar xvf ../IntegrationApi.war"
      sh "cp -r WEB-INF/classes/ IntegrationApi/WEB-INF/classes/"
    }

  stage 'Build Image'
    gitlabCommitStatus("Build Image"){
      def img
      withDockerRegistry([credentialsId: 'docker-registry-credentials', url: "https://harbor.usu.edu"]){
        if (env.BRANCH_NAME == "master"){
          img = docker.build('harbor.usu.edu/banner/integrationapi:latest')
        } else {
          img = docker.build("harbor.usu.edu/banner/integrationapi:${env.BRANCH_NAME}")
          img.push()
        }
      }
    }
}
