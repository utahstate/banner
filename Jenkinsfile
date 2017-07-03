properties([gitLabConnection('gitlab.usu.edu')])

node {
  def javaHome = tool 'OracleJDK8'
  def baseImage = docker.image('harbor.usu.edu/banner/base-bannerselfservice:oraclelinux6-tomcat8-java8')

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
          s3Download(file: 'IntergrationApi.war', bucket:'usu-banner-builds', path:"banner/input/intergrationapi/9.8/IntergrationApi.war", force:true)
        }
      } else {
        withAWS(credentials:"Jenkins-S3", region:'us-east-1'){
          s3Download(file: 'IntergrationApi.war', bucket:'usu-banner-builds', path:"banner/input/intergrationapi/${env.BRANCH_NAME}/IntergrationApi.war", force:true)
        }
      }
      sh "${javaHome}/bin/jar uvf IntergrationApi.war WEB-INF"
    }

  stage 'Build Image'
    gitlabCommitStatus("Build Image"){
      def img
      withDockerRegistry([credentialsId: 'docker-registry-credentials', url: "https://harbor.usu.edu"]){
        if (env.BRANCH_NAME == "master"){
          img = docker.build('harbor.usu.edu/banner/intergrationapi:latest')
        } else {
          img = docker.build("harbor.usu.edu/banner/intergrationapi:${env.BRANCH_NAME}")
          img.push()
        }
      }
    }
}
