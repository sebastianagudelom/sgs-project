#!/bin/bash
set -euo pipefail

# ═══════════════════════════════════════════════════════
# SGS - Script de configuracion del servidor EC2 (Ubuntu)
# Ejecutar como root: sudo bash setup-server.sh
# ═══════════════════════════════════════════════════════

echo ">>> Actualizando sistema..."
apt update && apt upgrade -y

# ── Java 21 ──
echo ">>> Instalando Java 21..."
apt install -y wget apt-transport-https gpg
wget -qO - https://packages.adoptium.net/artifactory/api/gpg/key/public | gpg --dearmor -o /usr/share/keyrings/adoptium.gpg
echo "deb [signed-by=/usr/share/keyrings/adoptium.gpg] https://packages.adoptium.net/artifactory/deb $(lsb_release -cs) main" | tee /etc/apt/sources.list.d/adoptium.list
apt update
apt install -y temurin-21-jdk

# ── MySQL 8 ──
echo ">>> Instalando MySQL..."
apt install -y mysql-server
systemctl enable mysql
systemctl start mysql

echo ">>> Configurando MySQL..."
read -sp "Ingresa la password para el usuario sgs_user de MySQL: " DB_PASS
echo
mysql -u root <<EOF
CREATE DATABASE IF NOT EXISTS sgs_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'sgs_user'@'localhost' IDENTIFIED BY '${DB_PASS}';
GRANT ALL PRIVILEGES ON sgs_db.* TO 'sgs_user'@'localhost';
FLUSH PRIVILEGES;
EOF

# ── Nginx ──
echo ">>> Instalando Nginx..."
apt install -y nginx
systemctl enable nginx

# ── Certificado SSL autofirmado ──
echo ">>> Generando certificado SSL autofirmado..."
mkdir -p /etc/nginx/ssl
openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
  -keyout /etc/nginx/ssl/sgs.key \
  -out /etc/nginx/ssl/sgs.crt \
  -subj "/C=CO/ST=Quindio/L=Armenia/O=SGS/CN=$(curl -s ifconfig.me)"

# ── Usuario y directorios ──
echo ">>> Creando usuario y directorios..."
useradd -r -s /bin/false sgs || true
mkdir -p /opt/sgs/uploads
mkdir -p /var/www/sgs-frontend
chown -R sgs:sgs /opt/sgs

# ── Copiar configuraciones ──
echo ">>> Copiando configuraciones de Nginx..."
cp /tmp/sgs-deploy/nginx/sgs.conf /etc/nginx/sites-available/sgs
ln -sf /etc/nginx/sites-available/sgs /etc/nginx/sites-enabled/sgs
rm -f /etc/nginx/sites-enabled/default
nginx -t && systemctl restart nginx

# ── Servicio systemd ──
echo ">>> Configurando servicio systemd..."
cp /tmp/sgs-deploy/sgs-backend.service /etc/systemd/system/
systemctl daemon-reload
systemctl enable sgs-backend

# ── Firewall ──
echo ">>> Configurando firewall..."
ufw allow 22/tcp
ufw allow 80/tcp
ufw allow 443/tcp
ufw --force enable

echo ""
echo "═══════════════════════════════════════════════════"
echo "  Servidor configurado!"
echo "═══════════════════════════════════════════════════"
echo ""
echo "Pasos siguientes:"
echo "  1. Copia el .env a /opt/sgs/.env y edita los valores"
echo "  2. Copia el JAR a /opt/sgs/sgs-backend.jar"
echo "  3. Copia el frontend a /var/www/sgs-frontend/"
echo "  4. sudo systemctl start sgs-backend"
echo "  5. Verifica: sudo journalctl -u sgs-backend -f"
echo ""
