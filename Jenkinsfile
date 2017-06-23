pipeline{



tools {
  jdk 'Java 8u131'
}

  stage 'Checkout'
  node {
        checkout scm
  }


  stage 'Build War'
  node {
    echo 'Copy Application'
    sh 'scp jenkins@build.banner.usu.edu:/u01/deploy/zdevl/self-service/BannerFinanceSSB.war .'
    echo 'Add Config'
    sh 'jar uvf BannerFinanceSSB.war WEB-INF'
  }

  stage 'Build Image'

  node {
    def img
    img = docker.build('banner/financeselfservice')

    echo 'Push Image'
  }
}
