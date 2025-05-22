#!/usr/bin/env bash
set -ex
set -o pipefail

BASE_PATH=$(cd $(dirname $0); pwd)
PROJECT_PATh=$(cd $(dirname $0)/..; pwd)

#变量
DOCKER_NAME=xxx
TARGET_PATH=.

#打包
source ../vm/build_xx.sh
VM_APTH=$(ls -l xxx)
#镜像地址
IMAGE_DIR=$TARGET_PATH/image
unzip VM_APTH -d $IMAGE_DIR/DOCKER_NAME

#获取参数
xxx=$(cat ../env.conf | grep 'xxx' | cut -d"=" -f2 |sed 's/\r//')

#基础镜像
#参考：https://blog.csdn.net/10km/article/details/103333098
IMAGE_VERSION="$(mvn org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -D"expression=project.version" -q -DforceStdout -f "pom.xml")"
IMAGE_PATH="http://myregistryvip.com:5000/centos:${IMAGE_VERION}"

#制作业务镜像
cd $IMAGE_DIR/DOCKER_NAME
docker pull ${IMAGE_PATH}
copy $BASE_PATH/Dockerfile .

#docker build ${DOCKER_BUILD_ARG} -t ${image_name_tag} -f ${dockerfile_file} ${dockerfile_path}
docker build -t "${DOCKER_NAME}:${IMAGE_VERSION}" --build-arg IMAGE_NAME=centos:7.9.2009 .
