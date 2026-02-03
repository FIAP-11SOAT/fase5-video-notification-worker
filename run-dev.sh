#!/usr/bin/env bash
set -e

echo "=== Encerrando containers antigos ==="
docker-compose down -v --remove-orphans

echo "=== Subindo ambiente com docker-compose ==="
docker-compose up -d

echo "Aguardando alguns segundos para serviços inicializarem..."
sleep 15

echo "=== Iniciando aplicação Spring Boot (profile dev) ==="
SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run