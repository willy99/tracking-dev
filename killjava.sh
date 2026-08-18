#!/usr/bin/env bash
# Збірка й запуск бекенду. Гасить усе, що вже висить на 8080 (типова
# причина "Port 8080 was already in use" — забутий процес з попереднього
# запуску), піднімає Postgres, білдить і стартує platform-app.
set -euo pipefail

cd "$(dirname "$0")"

echo "==> Звільняю порт 8080, якщо зайнятий..."
PID=$(lsof -ti:8080 || true)
if [ -n "$PID" ]; then
    echo "    знайдено процес(и): $PID — гашу"
    kill -9 $PID
    sleep 1
else
    echo "    порт вільний"
fi
