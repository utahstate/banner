pipeline{

  agent any

tools {
  jdk 'Java 8u131'
}
node{
  stage 'Checkout'
    checkout scm

  stage 'Build War'
    echo 'Copy Application'
    sh 'scp jenkins@build.banner.usu.edu:/u01/deploy/zdevl/self-service/BannerFinanceSSB.war .'
    echo 'Add Config'
    sh 'jar uvf BannerFinanceSSB.war WEB-INF'

  stage 'Build Image'
  def img
  img = docker.build('banner/financeselfservice')

  echo 'Push Image'
}
}
