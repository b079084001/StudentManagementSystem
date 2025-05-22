#!/usr/bin/env bash
set -ex
set -o pipefail

BASE_PATH=$(cd $(dirname $0); pwd)
PROJECT_PATh=$(cd $(dirname $0)/..; pwd)

#打印日志
function write_log(){
  echo $(date +"%Y-%m-%d %T") "$@"
}
write_log "具体日志"

#拷贝多个文件
cp xxx/{test1.txt,test2.txt} .
#修改文件格式
find . -type f | grep -E '\.xml$ | \.txt$' | xargs dos2unix

#打包
#mvn clean install package -DskipTests=true -f xxx/pom.xml -gs xxx/settings.xml
mvn clean install package -Dmaven.test.skip=true -f xxx/pom.xml -gs xxx/settings.xml