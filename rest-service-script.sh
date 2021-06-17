#!/bin/sh
#cd "$(dirname "$0")"
echo "---------------------------------------------------------"
echo "           Removing all container"
echo "---------------------------------------------------------"
docker rm -f $(docker ps -a -q)
echo "---------------------------------------------------------"
echo "           I'm creating a Docker Network"
echo "---------------------------------------------------------"
docker network create mynet

echo "#########################################################"
echo "###########        STARTING JAVA SERVICE      ###########"
cd java-service
echo "---------------------------------------------------------"
echo "1. Now I'm compiling each module from scratch and install"
echo "---------------------------------------------------------"
mvn clean install
echo "---------------------------------------------------------"
echo "2. I'm building my image starting from the Dockerfile"
echo "---------------------------------------------------------"
docker build -t java-service .
echo "----------------------------------------------------------------------------"
echo "3. I'm running my image as a daemon on port 9090 and adding it to MyNetwork"
docker run -d -p 9090:8080 --name java-container --network mynet java-service
echo "----------------------------------------------------------------------------"
echo "4. Java Service is up!"

echo "#########################################################"
echo "###########        STARTING PYTHON SERVICE    ###########"
cd ..
cd python-service
echo "---------------------------------------------------------"
echo "1. I'm building my image starting from the Dockerfile"
echo "---------------------------------------------------------"
docker build -t python-service .
echo "2. I'm running my image as a daemon on port 5000 and adding it to MyNetwork"
# use -dt to make stdout line buffered (print log of python service)
docker run -dt -p 5000:5000 --name python-container --network mynet python-service
