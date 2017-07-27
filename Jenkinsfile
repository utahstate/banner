properties([gitLabConnection('gitlab.usu.edu')])

node {
  def javaHome = tool 'OracleJDK8'
  def baseImage = docker.image('harbor.usu.edu/banner/base-banneradmin:oraclelinux6-tomcat8-java8')

  stage 'Checkout'
    checkout scm
    echo "Branch Name ${env.BRANCH_NAME} Build ID ${env.BUILD_ID} Build Number ${env.BUILD_NUMBER} Job Name ${env.JOB_NAME}"
    withDockerRegistry([credentialsId: 'docker-registry-credentials', url: "https://harbor.usu.edu"]){
      baseImage.pull()
    }

  stage 'Build Wars'
    gitlabCommitStatus("Build War"){
      if (env.BRANCH_NAME == "master"){
        withAWS(credentials:"Jenkins-S3", region:'us-east-1'){
          s3Download(file:'BannerAdmin.war', bucket:'usu-banner-builds', path:"banner/input/banneradmin/BannerAdmin.war", force:true)
        }
      } else {
        withAWS(credentials:"Jenkins-S3", region:'us-east-1'){
          s3Download(file:'BannerAdmin.war', bucket:'usu-banner-builds', path:"banner/input/banneradmin/${env.BRANCH_NAME}/BannerAdmin.war", force:true)
          s3Download(file:'BannerAdmin.ws.war', bucket:'usu-banner-builds', path:"banner/input/banneradmin/${env.BRANCH_NAME}/BannerAdmin.ws.war", force:true)
          s3Download(file:'bannerHelp.war', bucket:'usu-banner-builds', path:"banner/input/banneradmin/${env.BRANCH_NAME}/bannerHelp.war", force:true)
        }
      }
      sh "cd BannerAdmin; ${javaHome}/bin/jar uvf BannerAdmin.war config.xml; cd .."
      sh "cd BannerAdmin.ws; ${javaHome}/bin/jar uvf BannerAdmin.ws.war WEB-INF; cd .."
    }


    stage 'Build Image'
      gitlabCommitStatus("Build Image"){
        def img
      withDockerRegistry([credentialsId: 'docker-registry-credentials', url: "https://harbor.usu.edu/"]){
        if (env.BRANCH_NAME == "master"){
          img = docker.build('harbor.usu.edu/banner/banneradmin:latest')
        } else {
          img = docker.build("harbor.usu.edu/banner/banneradmin:${env.BRANCH_NAME}")
          img.push()
        }
      }
    }
}
