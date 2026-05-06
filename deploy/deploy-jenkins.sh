#!/bin/bash
set -euo pipefail

# ═══════════════════════════════════════════════════════
# SGS - Deploy local desde Jenkins (Jenkins está en la misma VM)
# Uso: bash deploy-jenkins.sh
# Requiere: jenkins user con sudo NOPASSWD para cp/rm/chown/systemctl
# ═══════════════════════════════════════════════════════

PROJECT_DIR="$(cd "$(dirname "$0")/.." && pwd)"

echo ">>> Validando artefactos..."
JAR_FILE=$(ls "$PROJECT_DIR/sgs-backend/target"/*.jar | grep -v original | head -1)
DIST_DIR="$PROJECT_DIR/sgs-frontend/dist/sgs-frontend/browser"
[ -f "$JAR_FILE" ] || { echo "ERROR: JAR no encontrado"; exit 1; }
[ -d "$DIST_DIR" ] || { echo "ERROR: dist del frontend no encontrado en $DIST_DIR"; exit 1; }
echo "    JAR: $JAR_FILE"
echo "    DIST: $DIST_DIR"

echo ">>> Deteniendo backend..."
sudo systemctl stop sgs-backend || true

echo ">>> Desplegando backend..."
sudo cp "$JAR_FILE" /opt/sgs/sgs-backend.jar
sudo chown sgs:sgs /opt/sgs/sgs-backend.jar

echo ">>> Desplegando frontend..."
sudo rm -rf /var/www/sgs-frontend
sudo mkdir -p /var/www/sgs-frontend
sudo cp -r "$DIST_DIR"/. /var/www/sgs-frontend/

echo ">>> Reiniciando servicios..."
sudo systemctl start sgs-backend
sudo systemctl reload nginx

echo ""
echo "Backend: $(sudo systemctl is-active sgs-backend)"
echo "Nginx:   $(sudo systemctl is-active nginx)"
echo ""
echo "Deploy OK -> https://sgsmarket.duckdns.org"
