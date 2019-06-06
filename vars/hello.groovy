
def call(){

def server = Artifactory.server('artifactory')
def uploadSpec

pipeline{
agent {label 'MacBuilder'}
stages{
stage('Build'){
            steps {
                sh 'mvn install -e -DskipTests=true'
            }
        }

  stage('Server Ready?'){

    steps{
      script{
      	def commitHas = sh(returnStdout: true,script: 'git rev-parse HEAD').trim().take(7)
        echo "${commitHas}"
        echo "${env.GIT_COMMIT}".trim().take(7)
      }
    }
  }
stage('SonarQube Scan'){
            steps {
                sh 'mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=c4b43a28de7701e871fbc12a0f828f45fab23842'
            }
        }
stage ('Deliver for development'){

when {
branch 'development'
}
            steps{
                script {

                    uploadSpec = """{
  "files": [
    {
      "pattern": "target/*.jar",
      "target": "libs-release/Dev-#${env.BUILD_NUMBER}/"
    }
 ]
}"""
server.upload(uploadSpec)
                }
            }
        }

stage ('Deliver for master'){
when {
branch 'master'
}
            steps{
                script {

                  uploadSpec = """{
  "files": [
    {
      "pattern": "target/*.jar",
      "target": "libs-release/Master-#${env.BUILD_NUMBER}/"
    }
 ]
}"""
server.upload(uploadSpec)
                }
            }
        }
stage ('Deliver for production'){
when {
branch 'production'
}
            steps{
                script {
                    uploadSpec = """{

  "files": [
    {
      "pattern": "target/*.jar",
      "target": "libs-release/Prod-#${env.BUILD_NUMBER}/"
    }
 ]
}"""
server.upload(uploadSpec)
                }
            }
        }
}

}
}
