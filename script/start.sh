NAME=springboot-0.0.1-SNAPSHOT.jar
#nohup java -jar $NAME --spring.profiles.active=prod > server.log 2>&1 &
nohup java -jar $NAME  > server.log 2>&1 &
echo 'start success'