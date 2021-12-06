```shell
#! /bin/bash
# nginx 文件夹下载地址 https://github.com/q920447939/java-study/releases/tag/nginx_offline

source /etc/profile
base_path=/tmp/download

echo "开始安装离线nginx...请将nginx文件夹放到"${base_path}"目录下"
if [ "$(whoami)" != "root" ]
then
    echo "当前操作用户必须为root！";
    exit
fi


if [ ! -d  ${base_path} ]; then
    echo "nginx文件夹不存在"
    exit
fi

command=tar
which ${command} > /dev/null
if [ $? -ne 0 ]
then
  echo ${command} not exist
  exit
fi


cd ${base_path}/nginx/gcc
rpm -Uvh *.rpm --nodeps --force

cd ${base_path}/nginx/gcc-c++
rpm -Uvh *.rpm --nodeps --force

cd ${base_path}/nginx
tar -zxf pcre-8.35.tar.gz
cd pcre-8.35
./configure
make
make install

cd ${base_path}/nginx
tar -zxf libtool-2.4.2.tar.gz
cd libtool-2.4.2
./configure
make
make install

cd ${base_path}/nginx
tar -zxf nginx-1.13.9.tar.gz
cd nginx-1.13.9
./configure
make
make install

echo  "nginx安装完成！"

cd ${base_path}/nginx
mv /usr/local/nginx/conf/nginx.conf /usr/local/nginx/conf/nginx.conf.bak
mv ./nginx.conf  /usr/local/nginx/conf/nginx.conf
/usr/local/nginx/sbin/nginx -c /usr/local/nginx/conf/nginx.conf
echo "nginx启动成功"

echo "/usr/local/nginx/sbin/nginx -c /usr/local/nginx/conf/nginx.conf" >> /etc/rc.local
echo "加入开机自启成功！"
```
