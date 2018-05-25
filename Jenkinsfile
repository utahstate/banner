properties([gitLabConnection('gitlab.usu.edu')])

node{
  def javaHome = tool 'OracleJDK8'
  def baseImage = docker.image('edurepo/banner9-selfservice:tomcat8-jre8-alpine')

  stage 'Checkout'
    checkout scm
    echo "Branch Name ${env.BRANCH_NAME} Build ID ${env.BUILD_ID} Build Number ${env.BUILD_NUMBER} Job Name ${env.JOB_NAME}"
    withDockerRegistry(){
      baseImage.pull()
    }

  stage 'Build War'
    gitlabCommitStatus("Build War"){
      withAWS(credentials:"Jenkins-S3", region:'us-east-1'){
        s3Download(file:'FinanceSelfService.war', bucket:'usu-banner-builds', path:"banner/input/financeselfservice/${env.BRANCH_NAME}/FinanceSelfService.war", force:true)
      }

      sh 'mkdir FinanceSelfService && cd FinanceSelfService && ${javaHome}/bin/jar xvf ../FinanceSelfService.war'
      sh "cp WEB-INF/classes/* FinanceSelfService/WEB-INF/classes"
    }

  stage 'Build Image'
    gitlabCommitStatus("Build Image"){
    def img
    withDockerRegistry([credentialsId: 'docker-registry-credentials', url: "https://harbor.usu.edu"]) {
      if (env.BRANCH_NAME == "master") {
        img = docker.build('harbor.usu.edu/banner/financeselfservice:latest')
      } else {
        img = docker.build("harbor.usu.edu/banner/financeselfservice:${env.BRANCH_NAME}")
        img.push()
      }
    }
    }

}
