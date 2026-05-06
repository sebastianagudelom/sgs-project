pipeline {
  agent any

  environment {
    DEPLOY_IP      = '35.225.120.34'
    DEPLOY_USER    = 'sebastianagudelomendez'
    SSH_KEY        = credentials('gcp-ssh-key')
    TESTRAIL_HOST  = credentials('testrail-host')
    TESTRAIL_USER  = credentials('testrail-user')
    TESTRAIL_KEY   = credentials('testrail-api-key')
    TESTRAIL_PID   = credentials('testrail-project-id')
    TESTRAIL_SID   = credentials('testrail-suite-id')
    ADMIN_EMAIL    = credentials('sgs-admin-email')
    ADMIN_PASSWORD = credentials('sgs-admin-password')
    TEST_USER_EMAIL = credentials('sgs-test-user-email')
    TEST_USER_PASS  = credentials('sgs-test-user-pass')
  }

  options {
    timeout(time: 30, unit: 'MINUTES')
    disableConcurrentBuilds()
  }

  stages {
    stage('Backend: Build') {
      steps {
        dir('sgs-backend') {
          sh './mvnw clean package -DskipTests -q'
        }
      }
    }

    stage('Backend: Unit Tests') {
      steps {
        dir('sgs-backend') {
          sh './mvnw test -Dtest=\'!SgsBackendApplicationTests\''
        }
      }
      post {
        always {
          junit testResults: 'sgs-backend/target/surefire-reports/*.xml',
                allowEmptyResults: true
        }
      }
    }

    stage('Frontend: Build') {
      steps {
        dir('sgs-frontend') {
          sh 'npm ci --prefer-offline'
          sh 'npm run build'
        }
      }
    }

    stage('Frontend: Unit Tests') {
      steps {
        dir('sgs-frontend') {
          sh 'npm run test:ci'
        }
      }
    }

    stage('E2E: Cypress') {
      steps {
        dir('sgs-frontend') {
          sh """
            TESTRAIL_HOST=\${TESTRAIL_HOST} \\
            TESTRAIL_USER=\${TESTRAIL_USER} \\
            TESTRAIL_API_KEY=\${TESTRAIL_KEY} \\
            TESTRAIL_PROJECT_ID=\${TESTRAIL_PID} \\
            TESTRAIL_SUITE_ID=\${TESTRAIL_SID} \\
            ADMIN_EMAIL=\${ADMIN_EMAIL} \\
            ADMIN_PASSWORD=\${ADMIN_PASSWORD} \\
            TEST_USER_EMAIL=\${TEST_USER_EMAIL} \\
            TEST_USER_PASS=\${TEST_USER_PASS} \\
            npm run cy:run
          """
        }
      }
      post {
        always {
          archiveArtifacts(
            artifacts: 'sgs-frontend/cypress/screenshots/**,sgs-frontend/cypress/videos/**',
            allowEmptyArchive: true
          )
        }
      }
    }

    stage('Deploy') {
      when {
        branch 'main'
      }
      steps {
        sh """
          bash deploy/deploy.sh \${DEPLOY_IP} \${SSH_KEY}
        """
      }
    }
  }

  post {
    success {
      echo '✅ Pipeline completado — tests en TestRail y deploy realizado'
    }
    failure {
      echo '❌ Pipeline fallido — revisa logs y screenshots en artefactos'
    }
  }
}
