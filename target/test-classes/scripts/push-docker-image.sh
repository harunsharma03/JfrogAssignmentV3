#!/bin/bash

# Usage: ./push-docker-image.sh <dockerPath> <repo> <username> <password> <baseImage> <customImage> <tag>

dockerPath=$1
repo=$2
username=$3
password=$4
baseImage=$5
customImage=$6
tag=$7

echo ">>> Logging into Docker registry using --password-stdin..."
echo "$password" | "$dockerPath" login "$repo" -u "$username" --password-stdin
if [ $? -ne 0 ]; then
  echo "Docker login failed"
  exit 1
fi

echo ">>> Pulling base image: $baseImage"
"$dockerPath" pull "$baseImage"
if [ $? -ne 0 ]; then
  echo "Failed to pull base image"
  exit 1
fi

targetImage="$repo/$customImage:$tag"
echo ">>> Tagging image as: $targetImage"
"$dockerPath" tag "$baseImage" "$targetImage"
if [ $? -ne 0 ]; then
  echo "Failed to tag image"
  exit 1
fi

echo ">>> Pushing image to Artifactory..."
"$dockerPath" push "$targetImage"
if [ $? -ne 0 ]; then
  echo "Docker push failed"
  exit 1
fi

echo ">>> Docker push completed successfully: $targetImage"