#!/bin/sh
#cd "$(dirname "$0")"
cd java-service
echo "1. Now I'm compiling each module from scratch and install"
mvn clean install
echo "2. I'm building my image starting from the Dockerfile"
docker build -t rest-service.jar .
echo "3. I'm running my image as a daemon on port 9090"
docker run -d -p 9090:8080 rest-service.jar
echo "4. Java Service is up!"
