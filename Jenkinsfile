// root executable for the groovy file to know it's working with a pipeline
pipeline{

// will find any available agent
    agent any

//     Setup environment to pull from the jenkins global for the credentials
    environment{
        dockerHub = credentials('dockerHub')
        DBPASS = credentials('DBPASS')
        VERSION = "${env.BUILD_ID}"
        CONTAINER = "ams-backend-server"
    }

// definte all of the stages
    stages{

// First stage is the build & deliver and name it
        stage('Build & Deliver'){

// Steps are all of the executables on shell, use "" when using environment variables
            steps{
                sh "docker login -u ${dockerHub_USR} -p ${dockerHub_PSW}"
                sh "docker build -t jestercharles/ams-jenkins:${VERSION} ."
//pushes image to dockerHub
                sh "docker push jestercharles/ams-jenkins:${VERSION}"
            }

        }

//         stage to stop all running contianers & remove them
        stage("Stop & Destroy"){
            steps{
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE'){
                    sh "docker rm ${CONTAINER}"
                }
            }
        }

        stage('Deploy'){

            steps{
// docker run, but we've add the -e flag to pass the environment variable defined in Jenkins to the our Docker Container
// so our application.yml has context for what's there
                sh "docker run --name ${CONTAINER} -e DBPASS=${DBPASS} -d -p 8888:9999 jestercharles/ams-jenkins:${VERSION}"
            }

        }

    }


}