#SpringBoot+Vue项目部署上线
https://wwww.wangeditor.com/v4
https://vue.ruoyi.vip/index
https://doc.hutool.cn/pages/index/
https://element-plus.org/zh-CN/component/overview.html
https://element.eleme.cn/#/zh-CN
https://mvnrepository.com/artifact/org.apache.poi/poi-ooxml
https://www.xshell.com/zh/xshell-download/

1、部署java
mkdir -p /home/server/test2024/java
上传ja包到服务器/home/server/test2024/java目录
设置application.yml覆盖

2、启停脚本start.sh与stop.sh
#去除脚本文件里的特殊符号
#sed -i 's/\r//' start.sh
#sed -i 's/\r//' stop.sh

3、上传前端dist文件到vue目录
mkdir -p /home/server/test2024/vue

'''java目录
start.sh
stop.sh
application.yml
springboot-0.0.1-SNAPSHOT.jar
'''
'''vue/dist目录
css
fonts
img
js
index.html
favicon.ico
'''

4、安装jdk1.8(jdk-8u371-linux-x64.tar.gz)
tar -zxvf /tmp/jdk-8u371-linux-x64.tar.gz -C /usr/local
mv /usr/local/jdk1.8.0_371 /usr/local/java

vi /etc/profile
添加
export JAVA_HOME=/usr/local/java
export PATH=$JAVA_HOME/bin:$PATH
刷新生效
source /etc/profile
验证
java -version

5、安装nginx(nginx-1.24.0.tar.gz)
#安装gcc
yum install gcc-c++
#安装PCRE pcre-devel
yum install pcre pcre-devel
#安装zlib
yum install -y zlib zlib-devel
#安装Open SSL
yum install -y openssl openssl-devel

#下载nginx-1.24.0.tar.gz到/tmp
mkdir /usr/local/nginx
tar -zxvf /tmp/nginx-1.24.0.tar.gz -C /usr/local/nginx
cd /usr/local/nginx/nginx-1.24.0
#编译安装
./configure --with-http_stub_status_module --with-http_ssl_module
make && make install

#启动 ./nginx
#查看  ps -ef | grep nginx
#停止 ./nginx -s stop
#重启 ./nginx -s reload

6、安装mysql(mysql-5.7.42-el7-x86_64.tar.gz)
查看mariadb并卸载
rpm -qa | grep mariadb
yum remove -y mariadb-connector-c-3.1.11-2.oc8.1.x86_64
yum remove -y mariadb-connector-c-config-3.1.11-2.oc8.1.noarch
安装mysql
mkdir /data/mysql
tar -zxvf /tmp/mysql-5.7.42-el7-x86_64.tar.gz -C /usr/local
mv /usr/local/mysql-5.7.42-el7-x86_64 /usr/local/mysql

#添加用户组
groupadd mysql
useradd -r -g mysql mysql
chown -R mysql.mysql /usr/local/mysql
chown -R mysql.mysql /data/mysql

cd /usr/local/mysql
/usr/local/mysql/bin/mysqld --user=mysql --basedir=/usr/local/mysql --datadir=/data/mysql --initialize
#将mysql加入服务组
cp /usr/local/mysql/support-files/mysql-server /etc/init.d/mysql
#mysql快捷方式
ln -s /usr/local/mysql/bin/mysql /usr/bin
ln -s /usr/lib64/libtinfo.so.6.1 /usr/lib64/libtinfo.so.5
ln -s /usr/lib64/libncurses.so.6.1 /usr/lib64/libncurses.so.5

记住临时密码

#启动：service nysql start
#开机启动：chkconfig mysql on
#登录：mysql -uroot -p

修改密码并设置权限
mysql> SET PASSWORD = PASSWORD('123456');
mysql> use mysql;
mysql> update user set host='%' where user='root' and host='localhost';
mysql> flush privileges;
然后可以使用Navicat16 登录

7、登录腾讯云
https://cloud.tencent.com/act/pro/Featured?from=22336
防火墙设置放通3306(MySQL)和9090(TCP协议)端口
修改application.yml中的ip为云服务器公网IP
#启动java
cd /home/server/test2024/java
./start.sh



