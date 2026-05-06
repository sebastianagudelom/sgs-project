#!/bin/bash
set -euo pipefail

# ═══════════════════════════════════════════════════════
# SGS - Deploy desde Jenkins (asume artefactos ya compilados)
# Uso: bash deploy-jenkins.sh <SERVER_IP> <SSH_USER>
# Requiere: ssh-agent con la llave cargada (sshagent block en Jenkins)
# ═══════════════════════════════════════════════════════

SERVER_IP=${1:?"Uso: bash deploy-jenkins.sh <SERVER_IP> <SSH_USER>"}
SSH_USER=${2:?"Uso: bash deploy-jenkins.sh <SERVER_IP> <SSH_USER>"}
PROJECT_DIR="$(cd "$(dirname "$0")/.." && pwd)"

SSH_OPTS="-o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null"
SSH="ssh $SSH_OPTS $SSH_USER@$SERVER_IP"
SCP="scp $SSH_OPTS"

echo ">>> Validando artefactos..."
JAR_FILE=$(ls "$PROJECT_DIR/sgs-backend/target"/*.jar | grep -v original | head -1)
DIST_DIR="$PROJECT_DIR/sgs-frontend/dist/sgs-frontend/browser"
[ -f "$JAR_FILE" ] || { echo "ERROR: JAR no encontrado"; exit 1; }
[ -d "$DIST_DIR" ] || { echo "ERROR: dist del frontend no encontrado en $DIST_DIR"; exit 1; }
echo "    JAR: $JAR_FILE"
echo "    DIST: $DIST_DIR"

echo ">>> Preparando carpetas temporales en servidor..."
$SSH "sudo rm -rf /tmp/sgs-deploy /tmp/sgs-frontend && \
      sudo mkdir -p /tmp/sgs-deploy/nginx /tmp/sgs-frontend && \
      sudo chown -R $SSH_USER:$SSH_USER /tmp/sgs-deploy /tmp/sgs-frontend"

echo ">>> Subiendo configuraciones..."
$SCP "$PROJECT_DIR/deploy/nginx/sgs.conf"        $SSH_USER@$SERVER_IP:/tmp/sgs-deploy/nginx/
$SCP "$PROJECT_DIR/deploy/sgs-backend.service"   $SSH_USER@$SERVER_IP:/tmp/sgs-deploy/

echo ">>> Subiendo JAR del backend..."
$SCP "$JAR_FILE" $SSH_USER@$SERVER_IP:/tmp/sgs-backend.jar

echo ">>> Subiendo frontend..."
$SCP -r "$DIST_DIR"/* $SSH_USER@$SERVER_IP:/tmp/sgs-frontend/

echo ">>> Aplicando deploy en servidor..."
$SSH bash -s <<'REMOTE'
set -euo pipefail

sudo systemctl stop sgs-backend || true

sudo cp /tmp/sgs-backend.jar /opt/sgs/sgs-backend.jar
sudo chown sgs:sgs /opt/sgs/sgs-backend.jar

sudo rm -rf /var/www/sgs-frontend/*
sudo cp -r /tmp/sgs-frontend/* /var/www/sgs-frontend/

sudo systemctl start sgs-backend
sudo systemctl reload nginx

sudo rm -rf /tmp/sgs-deploy /tmp/sgs-backend.jar /tmp/sgs-frontend

echo "Backend: $(sudo systemctl is-active sgs-backend)"
echo "Nginx:   $(sudo systemctl is-active nginx)"
REMOTE

echo ""
echo "Deploy OK -> https://sgsmarket.duckdns.org"
