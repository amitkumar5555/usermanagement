#!/bin/bash

IMAGE_NAME=usermanagement
CONTAINER_NAME=usermanagement-container
PORT=7002

echo "🧼 Cleaning up any existing containers..."
# Stop and remove running container if exists
if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
  docker stop $CONTAINER_NAME
  docker rm $CONTAINER_NAME
fi

echo "🧹 Removing existing image if it exists..."
# Remove image if it exists
if [ "$(docker images -q $IMAGE_NAME)" ]; then
  docker rmi $IMAGE_NAME --force
fi

echo "🔨 Building the Docker image..."
# Build the new image with platform compatibility for M1/M2 Mac
docker build --platform=linux/amd64 -t $IMAGE_NAME .

echo "🚀 Running the container..."
# Run the container
docker run --platform=linux/amd64 -d \
  --name $CONTAINER_NAME \
  -p $PORT:7083 \
  $IMAGE_NAME

# need to keep port same as all places
#docker run --platform=linux/amd64 -d \
#   --name $CONTAINER_NAME \
#   -p $PORT:$PORT \
#   $IMAGE_NAME

echo "✅ Done. App is running at http://localhost:$PORT"
