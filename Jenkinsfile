properties([gitLabConnection('gitlab.usu.edu')])

node {
  def javaHome = tool 'OracleJDK8'
  def baseImage = docker.image('harbor.usu.edu/banner/base-bannerselfservice:oraclelinux6-tomcat8-java8')

  stage 'Checkout'
    checkout scm
    echo "Branch Name ${env.BRANCH_NAME} Build ID ${env.BUILD_ID} Build Number ${env.BUILD_NUMBER} Job Name ${env.JOB_NAME}"
    withDockerRegistry([credentialsId: 'docker-registry-credentials', url: "https://harbor.usu.edu"]){
      baseImage.pull()
    }

  stage 'Build War'
    gitlabCommitStatus("Build War"){
      if (env.BRANCH_NAME == "master"){
        withAWS(credentials:"Jenkins-S3", region:'us-east-1'){
          s3Download(file:'BannerExtensibility.war', bucket:'usu-banner-builds', path:"banner/input/bannerextensibility/9.6/BannerExtensibility.war", force:true)
        }
      } else {
        withAWS(credentials:"Jenkins-S3", region:'us-east-1'){
          s3Download(file:'BannerExtensibility.war', bucket:'usu-banner-builds', path:"banner/input/bannerextensibility/${env.BRANCH_NAME}/BannerExtensibility.war", force:true)
        }
      }
      sh "${javaHome}/bin/jar uvf BannerExtensibility.war WEB-INF"
      sh "${javaHome}/bin/jar uvf BannerExtensibility.war js"
    }


    stage 'Build Image'
      gitlabCommitStatus("Build Image"){
      def img
      withDockerRegistry([credentialsId: 'docker-registry-credentials', url: "https://harbor.usu.edu/"]){
        if (env.BRANCH_NAME == "master"){
          img = docker.build('harbor.usu.edu/banner/bannerextensibility:latest')
        } else {
          img = docker.build("harbor.usu.edu/banner/bannerextensibility:${env.BRANCH_NAME}", "--no-cache")
          img.push()
        }
      }
    }
}
