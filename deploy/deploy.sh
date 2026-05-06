#!/bin/bash
set -euo pipefail

# ═══════════════════════════════════════════════════════
# SGS - Script de despliegue (ejecutar desde tu Mac)
# Uso: bash deploy.sh <IP_EC2> <PEM_FILE>
# Ej:  bash deploy.sh 54.123.45.67 ~/mi-key.pem
# ═══════════════════════════════════════════════════════

EC2_IP=${1:?"Uso: bash deploy.sh <IP_EC2> <PEM_FILE>"}
PEM_FILE=${2:?"Uso: bash deploy.sh <IP_EC2> <PEM_FILE>"}
SSH_USER="ubuntu"
PROJECT_DIR="$(cd "$(dirname "$0")/.." && pwd)"

echo ">>> Compilando backend..."
cd "$PROJECT_DIR/sgs-backend"
./mvnw clean package -DskipTests -q
JAR_FILE=$(ls target/*.jar | grep -v original | head -1)
echo "    JAR: $JAR_FILE"

echo ">>> Compilando frontend..."
cd "$PROJECT_DIR/sgs-frontend"
npx ng build --configuration=production
DIST_DIR="$PROJECT_DIR/sgs-frontend/dist/sgs-frontend/browser"

echo ">>> Subiendo archivos al servidor..."
SSH_OPTS="-i $PEM_FILE -o StrictHostKeyChecking=no"

# Subir archivos de deploy
ssh $SSH_OPTS $SSH_USER@$EC2_IP "sudo rm -rf /tmp/sgs-frontend && sudo mkdir -p /tmp/sgs-deploy/nginx /tmp/sgs-frontend && sudo chown -R $SSH_USER:$SSH_USER /tmp/sgs-frontend"
scp $SSH_OPTS "$PROJECT_DIR/deploy/nginx/sgs.conf" $SSH_USER@$EC2_IP:/tmp/sgs-deploy/nginx/
scp $SSH_OPTS "$PROJECT_DIR/deploy/sgs-backend.service" $SSH_USER@$EC2_IP:/tmp/sgs-deploy/

# Subir JAR
scp $SSH_OPTS "$PROJECT_DIR/sgs-backend/$JAR_FILE" $SSH_USER@$EC2_IP:/tmp/sgs-backend.jar

# Subir frontend
scp -r $SSH_OPTS "$DIST_DIR"/* $SSH_USER@$EC2_IP:/tmp/sgs-frontend/

# Desplegar en el servidor
ssh $SSH_OPTS $SSH_USER@$EC2_IP << 'REMOTE'
sudo systemctl stop sgs-backend || true

# Backend
sudo cp /tmp/sgs-backend.jar /opt/sgs/sgs-backend.jar
sudo chown sgs:sgs /opt/sgs/sgs-backend.jar

# Frontend
sudo rm -rf /var/www/sgs-frontend/*
sudo cp -r /tmp/sgs-frontend/* /var/www/sgs-frontend/

# Reiniciar servicios
sudo systemctl start sgs-backend
sudo systemctl reload nginx

# Limpiar temporales
sudo rm -rf /tmp/sgs-deploy /tmp/sgs-backend.jar /tmp/sgs-frontend

echo ""
echo "Despliegue completado!"
echo "Backend: $(sudo systemctl is-active sgs-backend)"
echo "Nginx:   $(sudo systemctl is-active nginx)"
REMOTE

echo ""
echo "═══════════════════════════════════════════════════"
echo "  Despliegue finalizado!"
echo "  URL: https://$EC2_IP"
echo "═══════════════════════════════════════════════════"
