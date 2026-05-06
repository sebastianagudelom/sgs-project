#!/bin/bash
# ═══════════════════════════════════════════════════════════════
# SGS - Setup del stack de monitoreo (Prometheus + Grafana)
# Ejecutar en la VM de GCP después de setup-server.sh
#
# Requisito: GCP e2-small (2 vCPU / 2 GB RAM) mínimo recomendado.
#            En e2-micro (1 GB) puede funcionar con swap configurado.
#
# Uso: bash setup-monitoring.sh <IP_GCP> <SSH_KEY>
# ═══════════════════════════════════════════════════════════════
set -euo pipefail

GCP_IP=${1:?"Uso: bash setup-monitoring.sh <IP_GCP> <SSH_KEY>"}
SSH_KEY=${2:?"Uso: bash setup-monitoring.sh <IP_GCP> <SSH_KEY>"}
SSH_USER="ubuntu"
PROJECT_DIR="$(cd "$(dirname "$0")/.." && pwd)"

SSH_OPTS="-i $SSH_KEY -o StrictHostKeyChecking=no"

echo ">>> Subiendo configuración de monitoreo..."
ssh $SSH_OPTS $SSH_USER@$GCP_IP "mkdir -p /tmp/sgs-monitoring/grafana/provisioning/datasources /tmp/sgs-monitoring/grafana/provisioning/dashboards"

scp $SSH_OPTS "$PROJECT_DIR/deploy/docker-compose.monitoring.yml" $SSH_USER@$GCP_IP:/tmp/sgs-monitoring/
scp $SSH_OPTS "$PROJECT_DIR/deploy/monitoring/prometheus.yml" $SSH_USER@$GCP_IP:/tmp/sgs-monitoring/
scp $SSH_OPTS "$PROJECT_DIR/deploy/monitoring/grafana/provisioning/datasources/prometheus.yml" $SSH_USER@$GCP_IP:/tmp/sgs-monitoring/grafana/provisioning/datasources/
scp $SSH_OPTS "$PROJECT_DIR/deploy/monitoring/grafana/provisioning/dashboards/dashboard-provider.yml" $SSH_USER@$GCP_IP:/tmp/sgs-monitoring/grafana/provisioning/dashboards/
scp $SSH_OPTS "$PROJECT_DIR/deploy/monitoring/grafana/provisioning/dashboards/sgs-dashboard.json" $SSH_USER@$GCP_IP:/tmp/sgs-monitoring/grafana/provisioning/dashboards/

echo ">>> Configurando monitoreo en GCP..."
ssh $SSH_OPTS $SSH_USER@$GCP_IP << 'REMOTE'
set -euo pipefail

# Instalar Docker si no existe
if ! command -v docker &>/dev/null; then
  echo "  Instalando Docker..."
  curl -fsSL https://get.docker.com | sh
  sudo usermod -aG docker ubuntu
fi

# Estructura en /opt/sgs/monitoring
sudo mkdir -p /opt/sgs/monitoring/grafana/provisioning/datasources
sudo mkdir -p /opt/sgs/monitoring/grafana/provisioning/dashboards
sudo chown -R ubuntu:ubuntu /opt/sgs/monitoring

# Copiar archivos
cp /tmp/sgs-monitoring/docker-compose.monitoring.yml /opt/sgs/monitoring/
cp /tmp/sgs-monitoring/prometheus.yml /opt/sgs/monitoring/

# En GCP el backend corre en el mismo host, ajustar target
sed -i 's/host.docker.internal:8080/172.17.0.1:8080/' /opt/sgs/monitoring/prometheus.yml

cp /tmp/sgs-monitoring/grafana/provisioning/datasources/prometheus.yml /opt/sgs/monitoring/grafana/provisioning/datasources/
cp /tmp/sgs-monitoring/grafana/provisioning/dashboards/dashboard-provider.yml /opt/sgs/monitoring/grafana/provisioning/dashboards/
cp /tmp/sgs-monitoring/grafana/provisioning/dashboards/sgs-dashboard.json /opt/sgs/monitoring/grafana/provisioning/dashboards/

# Crear .env con password de Grafana si no existe
if [ ! -f /opt/sgs/monitoring/.env ]; then
  GRAFANA_PASS=$(openssl rand -base64 16)
  cat > /opt/sgs/monitoring/.env << EOF
GRAFANA_USER=admin
GRAFANA_PASSWORD=$GRAFANA_PASS
GRAFANA_ROOT_URL=https://sgsmarket.duckdns.org/grafana
EOF
  echo ""
  echo "  ⚠  Guarda esta contraseña de Grafana:"
  echo "     Usuario:    admin"
  echo "     Contraseña: $GRAFANA_PASS"
  echo ""
fi

# Activar swap de 1 GB si no existe (necesario en e2-micro)
if [ ! -f /swapfile ]; then
  sudo fallocate -l 1G /swapfile
  sudo chmod 600 /swapfile
  sudo mkswap /swapfile
  sudo swapon /swapfile
  echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab
  echo "  Swap de 1 GB activado."
fi

# Iniciar stack
cd /opt/sgs/monitoring
docker compose -f docker-compose.monitoring.yml --env-file .env up -d

echo "  Prometheus: http://localhost:9090"
echo "  Grafana:    https://sgsmarket.duckdns.org/grafana"
echo ""
echo "  Stack de monitoreo levantado correctamente."
REMOTE

echo ""
echo "═══════════════════════════════════════════════════"
echo "  Monitoreo listo!"
echo "  Grafana en: https://$GCP_IP/grafana"
echo "═══════════════════════════════════════════════════"
