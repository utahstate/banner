
node{
  def javaHome = tool 'Java 8u131'

  stage 'Checkout'
    checkout scm
    echo "Branch Name ${env.BRANCH_NAME} Build ID ${env.BUILD_ID} Build Number ${env.BUILD_NUMBER} Job Name ${env.JOB_NAME}"

  stage 'Build War'
    echo 'Copy Application'
    sh 'scp jenkins@build.banner.usu.edu:/u01/deploy/zdevl/self-service/BannerFinanceSSB.war .'
    echo 'Add Config'
    sh "${javaHome}/bin/jar uvf BannerFinanceSSB.war WEB-INF"

  stage 'Build Image'
    def img
    withDockerRegistry([credentialsId: 'docker-registry-credentials', url: "https://harbor.usu.edu/"]) {
    if ($env.BRANCH_NAME == "master") {
        img = docker.build('banner/financeselfservice:latest')
      }
    } else {
      img = docker.build("banner/financeselfservice:${env.BRANCH_NAME}")
    }

  echo 'Push Image'

}
