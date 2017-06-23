
node{
  def javaHome = tool 'Java 8u131'

  stage 'Checkout'
    checkout scm

  stage 'Build War'
    echo 'Copy Application'
    sh 'scp jenkins@build.banner.usu.edu:/u01/deploy/zdevl/self-service/BannerFinanceSSB.war .'
    echo 'Add Config'
    sh "${javaHome}/bin/jar uvf BannerFinanceSSB.war WEB-INF"

  stage 'Build Image'
  withDockerRegistry([credentialsId: 'docker-registry-credentials', url: "https://harbor.usu.edu/"]) {
    def img
    img = docker.build('banner/financeselfservice')
  }


  echo 'Push Image'
}
