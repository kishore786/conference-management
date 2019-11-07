pipeline {
    agent any
    environment {        
        DOCKER_IMAGE_NAME = ${env.DOCKER_IMAGE}
    }
    stages {
        stage('Build Docker image') {
            when {
                branch 'master'                
            }
			steps {
                echo 'Building image..'
				script {
					app = docker.build(${env.DOCKER_IMAGE})										
				}
            }
        }        
        stage('Push Docker image') {
            when {
                branch 'master'                
            }
			steps {
                echo 'Push docker image..'
				script {
					docker.withRegistry('https://registry.hub.docker.com', 'docker_hub_login'){
						app.push('${env.BUILD_NUMBER}')
						app.push('latest')					      
					}					
				}
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploy into cloud'			
            }
        }
    }
}