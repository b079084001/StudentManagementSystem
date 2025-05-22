PORT=9090
pid=`netstat -tnlp | grep $PORT | grep -v grep | awk '{print $7}' | awk -F/ '{print $1}'`
if [ ${pid} ]; then
  kill -9 $pid
  echo "kill $PORT"
else
  echo "stop success"
fi