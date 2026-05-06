#!/bin/bash
set -euo pipefail

# ═══════════════════════════════════════════════════════
# SGS - Instalar Jenkins en la VM (Ubuntu 22.04)
# Ejecutar como ubuntu: bash setup-jenkins.sh
# ═══════════════════════════════════════════════════════

echo ">>> Agregando repositorio Jenkins LTS..."
curl -fsSL https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key \
  | sudo tee /usr/share/keyrings/jenkins-keyring.asc > /dev/null
echo "deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] https://pkg.jenkins.io/debian-stable binary/" \
  | sudo tee /etc/apt/sources.list.d/jenkins.list > /dev/null

sudo apt update
sudo apt install -y jenkins

echo ">>> Configurando Jenkins en puerto 8090 con prefijo /jenkins..."
sudo mkdir -p /etc/systemd/system/jenkins.service.d/
sudo tee /etc/systemd/system/jenkins.service.d/override.conf > /dev/null <<'EOF'
[Service]
Environment="JENKINS_PORT=8090"
Environment="JENKINS_OPTS=--prefix=/jenkins"
EOF

sudo systemctl daemon-reload
sudo systemctl enable jenkins
sudo systemctl start jenkins

echo ">>> Instalando Chromium (para Cypress headless)..."
sudo apt install -y chromium-browser

echo ">>> Instalando Node.js 22 LTS (para builds del frontend)..."
curl -fsSL https://deb.nodesource.com/setup_22.x | sudo -E bash -
sudo apt install -y nodejs

echo ">>> Instalando Maven (para builds del backend)..."
sudo apt install -y maven

echo ""
echo "═══════════════════════════════════════════════════"
echo "  Jenkins instalado!"
echo ""
echo "  Contraseña inicial de admin:"
sudo cat /var/lib/jenkins/secrets/initialAdminPassword 2>/dev/null || echo "  (archivo aun no disponible, espera 30s y ejecuta: sudo cat /var/lib/jenkins/secrets/initialAdminPassword)"
echo ""
echo "  Acceso: https://sgsmarket.duckdns.org/jenkins/"
echo "═══════════════════════════════════════════════════"
echo ""
echo "Pasos siguientes:"
echo "  1. Abre https://sgsmarket.duckdns.org/jenkins/"
echo "  2. Ingresa la contraseña inicial"
echo "  3. Instala plugins sugeridos + 'GitHub Integration'"
echo "  4. Crea usuario admin definitivo"
echo "  5. En Manage Jenkins -> System: setea Jenkins URL a https://sgsmarket.duckdns.org/jenkins/"
echo "  6. Agrega las credenciales (ver README del proyecto)"
