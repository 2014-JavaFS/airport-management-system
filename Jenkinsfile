pipeline{
    agent any

    environment{
        dockerHub = credentials('dockerHub')
    }

    stages{

        stage('Build & Deliver'){

            steps{
                sh "docker login -u ${dockerHub_USR} -p ${dockerHub_PSW}"
                sh 'docker build -t jestercharles/ams-jenkins:1.0.0 .'
                sh 'docker push jestercharles/ams-jenkins:1.0.0'
            }

        }

        stage('Deploy'){

            steps{
                sh 'docker run -d -p 8888:9999 jestercharles/ams-jenkins:1.0.0'
            }

        }

    }


}