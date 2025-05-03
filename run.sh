#!/bin/bash

set -e  # Exit immediately on error

DOCKER_USERNAME=amit8614136
IMAGE_NAME=usermanagement
FULL_IMAGE_NAME="$DOCKER_USERNAME/$IMAGE_NAME"
CONTAINER_NAME=usermanagement-container
PORT=7002

echo "🧼 Cleaning up any existing containers..."
if docker ps -aq -f name="$CONTAINER_NAME" > /dev/null; then
  docker stop "$CONTAINER_NAME" || true
  docker rm "$CONTAINER_NAME" || true
fi

echo "🧹 Removing existing local image (if any)..."
if docker images -q "$FULL_IMAGE_NAME" > /dev/null; then
  docker rmi "$FULL_IMAGE_NAME" --force || true
fi

echo "🔨 Building the Docker image..."
docker build --platform=linux/amd64 -t "$FULL_IMAGE_NAME" .

echo "📤 Pushing the image to Docker Hub..."
docker push "$FULL_IMAGE_NAME"

echo "🧹 Removing local image to save space..."
docker rmi "$FULL_IMAGE_NAME" --force || true

echo "📥 Pulling image from Docker Hub..."
docker pull "$FULL_IMAGE_NAME"

echo "🚀 Running the container..."
docker run --platform=linux/amd64 -d \
  --name "$CONTAINER_NAME" \
  -p "$PORT":7083 \
  "$FULL_IMAGE_NAME"

echo "✅ Deployment complete! App is running at: http://localhost:$PORT"
