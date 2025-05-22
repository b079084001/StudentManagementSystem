PORT=9090
pid=`netstat -tnlp | grep $PORT | grep -v grep | awk '{print $7}' | awk -F/ '{print $1}'`
if [ ${pid} ]; then
  kill -9 $pid
  echo "kill $PORT"
else
  echo "stop success"
fi

#去除脚本文件里的特殊符号
#sed -i 's/\r//' start.sh
#sed -i 's/\r//' stop.sh