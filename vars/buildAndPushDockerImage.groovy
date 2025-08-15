// File: vars/buildAndPushDockerImage.groovy

//
// This is a custom pipeline step for building and pushing a Docker image.
//
def call(Map config) {
    // Default values
    def dockerhubUser = config.dockerhubUser
    def imageName = config.imageName
    def credentialsId = config.credentialsId ?: 'dockerhub-credentials'
    def imageTag = config.imageTag ?: env.BUILD_NUMBER

    // The logic is wrapped in a script block
    script {
        echo "Building Docker image: ${dockerhubUser}/${imageName}:${imageTag}"
        sh "docker build -t ${dockerhubUser}/${imageName}:${imageTag} ."

        echo "Pushing image to Docker Hub..."
        withCredentials([usernamePassword(credentialsId: credentialsId, passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
            sh "docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}"
            sh "docker push ${dockerhubUser}/${imageName}:${imageTag}"
        }
    }
}