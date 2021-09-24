# RabbitMQ生产者消费者demo

### 安装教程

https://blog.csdn.net/qq_39135287/article/details/95725385  rabbitMQ 安装教程

https://blog.csdn.net/u013256816/article/details/76039201 rabbitMQ 增加trace插件

### 代码

生产者：

1.pom 文件导入依赖

```xml
		<dependency>
			<groupId>com.rabbitmq</groupId>
			<artifactId>amqp-client</artifactId>
			<version>5.12.0</version>
		</dependency>

```



2.生产者代码

```java


import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class EmitLog {

    private static final String EXCHANGE_NAME = "test_fanout_exchange";
    private static final String QUEUE_NAME = "queue_logs";
    private static final String ROUTING_KEY_NAME = "routing_key_name_log";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.7.22");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String nextLine = scanner.nextLine();
            if (null != nextLine && !"".equals(nextLine)) {
                try (Connection connection = factory.newConnection();
                     Channel channel = connection.createChannel()) {
                    // channel.exchangeDeclare(EXCHANGE_NAME, "fanout",true);
                    //5.创建交换机
                    /*
                    String exchange:交换机名称
                    BuiltinExchangeType type:交换机类型，枚举，
                    boolean durable:是否持久化
                    boolean autoDelete:是否自动删除，当没有消费者的时候自动删除
                    boolean internal:是否内部使用，一般都是false，true代表给MQ内部使用的，比如给MQ开发的插件使用
                    Map<String, Object> arguments:相关参数
                     */
                    channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT, false, false, false, null);

                    //6.创建队列
                    /*
                     String queue:队列名称
                     boolean durable:是否持久化
                     boolean exclusive: 有如下两个意义
                        是否独占，只有一个消费者监听这个队列
                        当connection关闭时，是否删除队列
                     boolean autoDelete:是否自动删除，当没有消费者的时候自动删除
                     Map<String, Object> arguments: 一些配置参数
                     */
                    //如果没有一个名字叫xxx的队列，则会创建，如果存在该队列，则复用
                    String queueName1 = "test_fanout_queue1";
                    String queueName2 = "test_fanout_queue2";
                    //设置队列里消息的ttl的时间30s
                    //https://blog.csdn.net/qq_18671415/article/details/105563042
                    Map<String, Object> argss = new HashMap<String , Object>();
                    argss.put("x-message-ttl" , 30*1000);

                    channel.queueDeclare(queueName1, true, false, false, argss);
                    channel.queueDeclare(queueName2, true, false, false, argss);

                    //7.绑定队列和交换机
                    /*
                    String queue:队列名称
                    String exchange:交换机名称
                    String routingkey:路由键，绑定规则
                        如果交换机的类型为 fanout，routingkey设置为空值""
                    */
                    channel.queueBind(queueName1, EXCHANGE_NAME, "");
                    channel.queueBind(queueName2, EXCHANGE_NAME, "");
                    String message = nextLine;

                    AMQP.BasicProperties prop = new AMQP.BasicProperties();
                    AMQP.BasicProperties properties = prop.builder().messageId(new Random().nextInt() + "").build();
                    channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY_NAME, properties, message.getBytes("UTF-8"));
                    System.out.println(" [x] Sent '" + message + "'");
                }
            }
        }

    }
}

```



消费者代码：

```java
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

public class ReceiveLogs1 {
   static String queueName1 = "test_fanout_queue1";
    private static final String EXCHANGE_NAME = "test_fanout_exchange";

    private static final String WORK_GROUP = "WORK_1";


    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.7.22");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();


        System.out.println(WORK_GROUP + " [*] Waiting for messages. To exit press CTRL+C");

        //6.接收消息
        /*
         * String queue:队列名称
         * boolean autoAck:是否自动确认，当消费者收到消息之后会自动给MQ一个回执，告诉MQ消息已经收到
         * Consumer callback:回调方法
         */
        Consumer consumer = new DefaultConsumer(channel){

            /*
             * 功能描述: <br>
             * 〈回调方法〉当客户端收到消息并向MQ确认消息已经收到，将回调该方法
             * @Param: [consumerTag消息唯一标识,
             *  envelope获取一些信息，包含交换机信息、routing key...等,
             *  properties配置信息，生产者发送消息时候的配置,
             *  body数据]
             * @Return: void
             * @Author: LeoLee
             * @Date: 2020/11/5 11:56
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                /*System.out.println("consumerTag:" + consumerTag);
                System.out.println("envelope.exchange:" + envelope.getExchange());
                System.out.println("envelope.routingKey:" + envelope.getRoutingKey());
                System.out.println("properties:" + properties);*/
                System.out.println("消费者1从队列" + queueName1 + "接收到body:" + new String(body));

            }
        };
        channel.basicConsume(queueName1, true, consumer);
    }
}

```



### 处理消息积压

 配置定时任务处理 rabbitMQ 消息太多问题

配置定时任务：

```shell
#!/bin/bash
systemctl enable crond
systemctl start crond
echo "* * * * * /home/clean_rabbit_queue.sh"  >> /etc/crontab
crontab /etc/crontab
echo "systemctl start crond.service" >> /etc/rc.d/rc.local
chmod 755 /etc/rc.d/rc.local
```



清理脚本：

```shell
#!/bin/bash
QUE=`rabbitmqctl list_queues  messages_ready name durable|grep -v "^Listing" |grep -v "^Timeout"`
echo "$QUE" | while read line
do
ready=`echo "$line" | awk -F' ' '{print $1}'`
name=`echo "$line" | awk -F' ' '{print $2}'`
durable=`echo "$line" | awk -F' ' '{print $3}'`
if (( "$ready" >= 20 )) && (( "$durable" == true ));then
      echo "--------------大于20 准备开始删除。。。"${name}""
      #这里把运行rabbitmqadmin 写死了， 可能需要运行  find / -name 'rabbitmqadmin'  找到具体的路径  
      /usr/local/software/rabbitmq_software/rabbitmq_server-3.7.16/var/lib/rabbitmq/mnesia/rabbit@tzdata-plugins-expand/rabbitmq_management-3.7.16/priv/www/cli/rabbitmqadmin -q delete queue name="$name"  && echo "删除队列超多20:成功！"
fi
done
```



