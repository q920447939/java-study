## Linux常用命令和操作

- 显示时间`data`

  ```shell
  [root@liming_linux_centos7_host_name ~]# date
  Tue Jul 16 12:30:35 EDT 2019
  
  ```

- 显示日期/每年的日期 `cal`

  ```shell
  [root@liming_linux_centos7_host_name ~]# cal
      July 2019     
  Su Mo Tu We Th Fr Sa
    1  2  3  4  5  6
   7  8  9 10 11 12 13
  14 15 16 17 18 19 20
  21 22 23 24 25 26 27
  28 29 30 31
  
  
  
  [root@liming_linux_centos7_host_name ~]# cal 2019
                               2019                               

       January               February                 March       
Su Mo Tu We Th Fr Sa   Su Mo Tu We Th Fr Sa   Su Mo Tu We Th Fr Sa
       1  2  3  4  5                   1  2                   1  2
 6  7  8  9 10 11 12    3  4  5  6  7  8  9    3  4  5  6  7  8  9
13 14 15 16 17 18 19   10 11 12 13 14 15 16   10 11 12 13 14 15 16
20 21 22 23 24 25 26   17 18 19 20 21 22 23   17 18 19 20 21 22 23
27 28 29 30 31         24 25 26 27 28         24 25 26 27 28 29 30
                                              31
        April                   May                   June        
Su Mo Tu We Th Fr Sa   Su Mo Tu We Th Fr Sa   Su Mo Tu We Th Fr Sa
    1  2  3  4  5  6             1  2  3  4                      1
 7  8  9 10 11 12 13    5  6  7  8  9 10 11    2  3  4  5  6  7  8
14 15 16 17 18 19 20   12 13 14 15 16 17 18    9 10 11 12 13 14 15
21 22 23 24 25 26 27   19 20 21 22 23 24 25   16 17 18 19 20 21 22
28 29 30               26 27 28 29 30 31      23 24 25 26 27 28 29
                                              30
        July                  August                September     
Su Mo Tu We Th Fr Sa   Su Mo Tu We Th Fr Sa   Su Mo Tu We Th Fr Sa
    1  2  3  4  5  6                1  2  3    1  2  3  4  5  6  7
 7  8  9 10 11 12 13    4  5  6  7  8  9 10    8  9 10 11 12 13 14
14 15 16 17 18 19 20   11 12 13 14 15 16 17   15 16 17 18 19 20 21
21 22 23 24 25 26 27   18 19 20 21 22 23 24   22 23 24 25 26 27 28
28 29 30 31            25 26 27 28 29 30 31   29 30

       October               November               December      
Su Mo Tu We Th Fr Sa   Su Mo Tu We Th Fr Sa   Su Mo Tu We Th Fr Sa
       1  2  3  4  5                   1  2    1  2  3  4  5  6  7
 6  7  8  9 10 11 12    3  4  5  6  7  8  9    8  9 10 11 12 13 14
13 14 15 16 17 18 19   10 11 12 13 14 15 16   15 16 17 18 19 20 21
20 21 22 23 24 25 26   17 18 19 20 21 22 23   22 23 24 25 26 27 28
27 28 29 30 31         24 25 26 27 28 29 30   29 30 31

  ```

- 显示某年的第n个月 `cal 8 2019`

  ```shell
  [root@liming_linux_centos7_host_name ~]# cal 8 2019
       August 2019    
  Su Mo Tu We Th Fr Sa
               1  2  3
   4  5  6  7  8  9 10
  11 12 13 14 15 16 17
  18 19 20 21 22 23 24
  25 26 27 28 29 30 31
  
  ```

- 退出`exit`,`ctrl+d`

- 计算器 `bc`

- 往前翻页 `shift+pageUP`,往后翻页`shfit+PageDn`

- 获取命令帮助 `man 命令`

- linux 文件属性：
 ```shell
[root@liming_linux_centos7_host_name ftp_down]# ll
total 20004
-rw-r--r--.  1 root root 10237385 May 18 20:48 rabbitmq-server-3.7.15-1.el6.noarch.rpm
-rw-r--r--.  1 root root 10238187 May 18 20:48 rabbitmq-server-3.7.15-1.el7.noarch.rpm
drwxr-xr-x.  3 root root       80 Jul 11 08:19 spring-boot-web-demo01
drwxr-xr-x. 11 1000 1000     4096 May 15 12:22 zookeeper3.4


 ```

![1563263825130](../md-file/1563263825130.png)



- ​    第一个字符代表这个文件是『目录、文件或链接文件等等』：
    o  当为[ d ]则是目录，例如上表档名为『spring-boot-web-demo01』的那一行；
    o  当为[ - ]则是文件，例如上表档名为『rabbitmq-server-3.7.15-1.el7.noarch.rpm』那一行；
    o  若是[ l ]则表示为连结档(link file)；
    o  若是[ b ]则表示为装置文件里面的可供储存的接口设备(可随机存取装置)；
    o  若是[ c ]则表示为装置文件里面的串行端口设备，例如键盘、鼠标(一次性读取装置)。



- 删除文件夹`rmdir`：

- 查看`cat`

  - 打印行号`cat -n 文件名字`

  ```shell
  [root@liming_linux_centos7_host_name ftp_down]# cat -n test.jsp 
       1	a
       2	as
       3	d
       4	ada
       5	s
       6	
  ```

  - ​	去掉空白行`cat -b 文件名字 `

    ```shell
    [root@liming_linux_centos7_host_name ftp_down]# cat -b test.jsp 
         1	a
         2	as
         3	d
         4	ada
         5	s
    
    ```

- 反向查看`tac`

  ```shell
  [root@liming_linux_centos7_host_name ftp_down]# tac test.jsp 
  
  s
  ada
  d
  as
  a
  
  ```

- 分页查看-`more`

- 分页查看-`less` ,向前`PageUP`,向后`PageDown`

- `head`

  -  取前20行,默认10行

    ```shell
    [root@liming_linux_centos7_host_name ~]# head -20 /etc/man_db.conf 
    ```
    
  - 取第3行-第6行记录 (先取出6条记录,然后使用tail 只取后面的4条 ,得到 3-6  `|`是管道分隔符,表示前面的记录交给后面的处理)
  
    ```shell
    [root@liming_linux_centos7_host_name ftp_down]# cat -n test.jsp 
         1	1a
         2	2b
         3	3c
         4	4d
         5	5e
         6	6f
         7	7g
         8	8h
         9	9i
    [root@liming_linux_centos7_host_name ftp_down]# head -n 6  test.jsp | tail -n 4
    3c
    4d
    5e
    6f
    ```
  
- 获取用户输入

  ```shell
  [root@liming_linux_centos7_host_name tmp]# vim showme.sh 
  
  #!/bin/bash
  read -p "Please input your first name:" firstname       #获取用户输入
  echo -e "Your firstName is ${firstname}"   #打印用户输入
  
  
  [root@liming_linux_centos7_host_name tmp]# sh showme.sh 
  Please input your first name:Jack
  Your firstName is Jack
  ```

- `source  xxx.sh`和`sh xxx.sh`的区别

  - `source`会保存变量的值,xxx.sh 执行完还能够获取到,相当于加入到了Path了
  - sh 执行完的话,后续就不能再次获取变量了

- 测试文件夹是否存在

  ```shell
  [root@liming_linux_centos7_host_name tmp]# vim file_perm.sh 
  #!/bin/bash
  read -p "输入文件名: "  filename
  
  #test -e 判断文件是否存在    ;  test ! -e 不存在
  test ! -e ${filename} && echo "你输入的文件名不存在" && exit 0
  
  #判断是否是一个文件
  test -f ${filename} && filetype="regulare file"
  
  echo "the filename is ${filename},and filetype is ${filetype}"
  
  
  [root@liming_linux_centos7_host_name tmp]# sh file_perm.sh 
  输入文件名: bbq
  你输入的文件名不存在
  [root@liming_linux_centos7_host_name tmp]# sh file_perm.sh 
  输入文件名: ladname20195917
  the filename is ladname20195917,and filetype is regulare file
  
  ```

- shell脚本获取参数等

  ```shell
  [root@liming_linux_centos7_host_name tmp]# vim get_into_params.sh -n
  
  #!/bin/bash
  
  
  echo "the script name  is   ==> ${0} "
  
  echo "Total paramter number is ==> $#"
  
  [ "$#" -lt 2 ] && echo "The number of paramter is less than 2.Stop here ." && exit 0
  
  echo "Your whole params is ==> $@"
  
  echo "the 1st name is ==> ${1}"
  echo "the 2sd name is ==> ${2}"
  
  
  [root@liming_linux_centos7_host_name tmp]# sh get_into_params.sh  key rose bullet
  the script name  is   ==> get_into_params.sh 
  Total paramter number is ==> 3
  Your whole params is ==> key rose bullet
  the 1st name is ==> key
  the 2sd name is ==> rose
  
  
  ```

- shell `if `

```shell
[root@liming_linux_centos7_host_name tmp]# cat testif.sh 
#!/bin/bash

read -p "receice input string :" str

if [ "${str}" == "Y" ] || [ "${str}" == "y" ] ; then
   	echo "you input str is Y or y." 
	exit 0
fi

if [ "${str}" == "N" ] || [ "${str}" == "n" ]  ; then
	echo "you input str is N or n."
	exit 0
fi

echo -e "i don't know what your chose is " && exit 0	



[root@liming_linux_centos7_host_name tmp]# sh testif.sh 
receice input string :n
you input str is N or n.
[root@liming_linux_centos7_host_name tmp]# sh testif.sh 
receice input string :q
i don't know what your chose is
```

- shell `if-elseif -else`

```shell
[root@liming_linux_centos7_host_name tmp]# cat ifelseif.sh 
#!/bin/bash

read -p "please input a str :" str

if [ "${str}" == "0" ] ; then 
	echo -e "your input number less than zero"
	exit 0
elif [ "${str}" == "1" ] || [  "${str}" == "2" ] ; then
	echo -e "your input number between first and second"
	exit 0
else
	echo -e "i don't know you input string... please retry run the script..thanks" 
        exit 0
fi

```

- shell case 语法练习

  ```shell
  #!/usr/bin/env bash
  
  #case 语法练习
  case ${1} in
      "hello" )
          echo "hello ,how are you ?"
          ;;
      "") #为空
          echo "you Must input paramters, ex> {${0} someword}" ;;
      "--help")
          echo "Usage: ${1} [OPTION]... "
          echo "${0} hello  print hello.how are you";;
      *) #任意个
          echo "Usage ${0} "
          ;;
  esac
  
  ```

- shell while循环练习

  ```shell
  #!/bin/bash
  
  while [ "${yn}" != "yes" -a  "${yn}" != "YES"  ]
  do
          read -p "Please input yes or YES" yn
  done
  
  echo "OK, finally"
  ```

  


- shell 检测端口是否运行

  ```shell
  [root@liming_linux_centos7_host_name tmp]# cat detect_server.sh 
  #!/bin/bash
  
  if [ "${1}" == "" ] ; then
  	echo "please input a port ,example 22"
  	exit 0
  fi
  
  detect_file=./netstat_checking.txt
  netstat -tuln >> ${detect_file}
  
  str=$(grep ":${1}" ${detect_file} )
  
  if [ "${str}" != ""  ] ; then
  	echo "${1} port is started..."
  	exit 0
  
  else 
  	echo "SB,没有这个端口"
  	exit 0
  
  fi
  
  ```

- shell 求和

  ```shell
  #!/bin/bash
  
  
  
  total=0
  
  
  n_start=$(echo ${1} | grep '[0-9]\{1\}'  )
  
  n_end=$(echo ${2} | grep '[0-9]\{1\}'  )
  
  
  if [ "${n_start}" == "" ] ; then
          echo -e "please input 1st number ,isn't "${1}" "
          exit 1
  
  elif [ "${n_end}" == "" ] ; then
          echo -e "please input 2sec number ,isn't "${2}" "
          exit 1
  
  elif [ "${n_end}" -lt  "${n_start}"  ] ; then
          echo -e "please input correct number and 2se number  must greet than 1st number,current 1st number is "${n_start}" and 2sec is "${n_end}"  "
          exit 1
  fi
  
  i=${end}
  
  while [ "${i}" != "${n_end}" ]
  
  do
          i=$(($i+1))
          total=$(($i+$total))
  done
  echo -e "result is  "${total}" "
  
  ```




- shell for循环获取用户名称

  ```shell
  #!/bin/bash
  
  users=$(cut -d ':' -f1 /etc/passwd)
  
  for u in ${users}
  do
          echo -e "u is ${u} "
  done
  ```

  



- shell for循环ping ip  

  ```shell
  #!/bin/bash
  
  network_pre="192.168.1"
  
  for ip_suff in $(seq 1 255)
  do
  
          ping -c 1 -w 1 ${network_pre}.${ip_suff} &> /dev/null && result=0 || result=1
  
          if [ "${result}" == 0 ] ; then
                  echo -e "this ping ip is ${network_pre}.${ip_suff} is UP !"
          else
                  echo -e "this ping ip is ${network_pre}.${ip_suff} is DOWN.... !"
          fi
  
  done
  
  ```

- shell 检测文件夹下面的文件权限

  ```shell
  #!/bin/bash
  
  #查看目录是否存在
  
  read -p "please input a directory :" dir
  
  if [ "${dir}" == "" -o !  -d "${dir}" ] ; then
          echo "${dir} is not a diretory...pelase retry input a diretory..thanks "
          exit 1
  fi
  
  #写入文件
  filelist=$(ls ${dir})
  for filename in ${filelist}
  do
          perm=""
          test -r "${dir}/${filename}" && perm="${perm} readable"
          test -w "${dir}/${filename}" && perm="${perm} writer"
          test -x "${dir}/${filename}" && perm="${perm} executedable"
          echo "The ${dir}/${filename}'s permission is ${perm}"
  done
  
  ```

  

- 将程序放置后台进行,并且记录日志到执行目录下

  ```shell
  [root@liming_linux_centos7_host_name tmp]# tar -zpcvf rabbitmq-server-3.7.15-1.el6.noarch.rpm  /etc/ > /tmp/log.txt 2>&1 &
  ```

  

- 将`vim`放置后台运行,随后继续vim

  1.  `ctrl+z`  从vim模式切出来,但是vim的内容此时是被保存的了.

  2. 用`jobs -l` 查看后台挂起的所有vim

  3. `%1`  1代表的是挂起的数字, 输入即可之前编辑的vim

     

- `nohup` 后台进行(使用`nohup`就算退出当前终端程序也还是会继续执行,如果是用上方的命令退出终端,那么下次进入的话将不会有原来的任务)

  -  

    ```shell
    [root@liming_linux_centos7_host_name tmp]# nohup ./sleep.sh &
    ```

    