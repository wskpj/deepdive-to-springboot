Write-Host "--- 1. Building JAR locally ---" -ForegroundColor Cyan
./gradlew bootJar -x test

if ($LASTEXITCODE -ne 0) {
    Write-Host "--- Local Build Failed! Stopping process. ---" -ForegroundColor Red
    exit $LASTEXITCODE
}

Write-Host "--- 2. Starting with Docker Compose ---" -ForegroundColor Cyan
docker-compose up -d --build

Write-Host "--- 3. Application is starting! ---" -ForegroundColor Green
docker logs -f "deepdive-app"
