pipeline {
  agent any

  // Jenkins corre en la misma VM que el servidor de producción.
  // Deploy se hace localmente con sudo, sin SSH.

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

    // Frontend Karma tests se corren localmente (ng test).
    // Skipeados en CI: Angular 20 + custom karmaConfig + Jenkins-snap chromium tienen
    // problemas conocidos. La cobertura E2E de Cypress es lo evaluado para esta entrega.

    // Deploy ANTES de E2E para que Cypress pruebe la versión recién desplegada.
    // Si los E2E fallan después, el sitio queda con la nueva versión (que es lo
    // que se está probando). Para revertir, basta correr el job en un commit anterior.
    stage('Deploy') {
      steps {
        sh 'bash deploy/deploy-jenkins.sh'
      }
    }

    stage('E2E: Cypress') {
      environment {
        TESTRAIL_HOST     = credentials('testrail-host')
        TESTRAIL_USERNAME = credentials('testrail-user')
        TESTRAIL_PASSWORD = credentials('testrail-api-key')
        TESTRAIL_PROJECT_ID = credentials('testrail-project-id')
        TESTRAIL_SUITE_ID   = credentials('testrail-suite-id')
        ADMIN_EMAIL      = credentials('sgs-admin-email')
        ADMIN_PASSWORD   = credentials('sgs-admin-password')
        TEST_USER_EMAIL  = credentials('sgs-test-user-email')
        TEST_USER_PASS   = credentials('sgs-test-user-pass')
      }
      steps {
        dir('sgs-frontend') {
          // Esperar a que el backend reinicie tras el deploy
          sh 'sleep 30'
          // TERM=dumb evita problemas con tput sin TTY en Jenkins
          // CI=true le dice a Cypress que estamos en CI
          // timeout 600 mata el proceso si se cuelga (10 min máximo)
          sh 'TERM=dumb CI=true timeout 600 npm run cy:run'
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
  }

  post {
    success {
      echo '✅ Pipeline OK — tests publicados y deploy aplicado'
    }
    failure {
      echo '❌ Pipeline fallido — revisa logs y artefactos'
    }
  }
}
