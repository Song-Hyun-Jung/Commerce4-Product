pipeline {

  environment {
    registry = "Song-Hyun-Jung/Commerce4-Product"
    dockerImage = ""
  }

  agent any

  stages {

    stage('Checkout Source') {
      steps {
        echo "Checkout Source START"
        git 'https://github.com/Song-Hyun-Jung/Commerce4-Product.git'
        echo "Checkout Source END"
      }
    }

    stage('Build image') {
      steps{
        script {
          echo "Build image START $BUILD_NUMBER"
          sh "docker build --no-cache -f ProductVer1Dockerfile -t 192.168.100.12/commerce-hj/commerce-hj-product:v$BUILD_NUMBER ."
          echo "Build image END"
        }
      }
    }

    stage('Push Image') {
      environment {
               registryCredential = 'harbor'
           }
      steps{
        script {
          echo "Push Image START"
          sh "docker login 192.168.100.12 -u admin -p Unipoint11"
          sh "docker push 192.168.100.12/commerce-hj/commerce-hj-product:v$BUILD_NUMBER"
          }
        echo "Push Image END"
      }
    }
    

    stage('Deploy App') {
      steps {
        script {
          echo "Deploy App START $BUILD_NUMBER"
          sh "/usr/local/bin/kubectl --kubeconfig=/home/jenkins/acloud-client.conf create -f commerce-hj-product.yaml"
          sh "/usr/local/bin/kubectl --kubeconfig=/home/jenkins/acloud-client.conf set image -n commerce-hj deployment/commerce-hj-product commerce-hj-product=192.168.100.12/commerce-hj/commerce-hj-product:v$BUILD_NUMBER"
          echo "Deploy App END"
        }
      }
    }

  }

}

