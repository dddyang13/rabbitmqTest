package com.crawler.docker_test;

import java.io.IOException;
import java.net.InetAddress;

import lombok.extern.slf4j.Slf4j;

import com.crawler.util.Config;
import com.rabbitmq.client.MessageProperties;

/**
 * RabbitMQ 队列消息发
 * 
 * @author shilei
 * 
 */
@Slf4j
public class RabbitMQMessageSender implements MessageSender {
	// RabbitMQ 队列连接
	private RabbitMQQueueConnecter connecter;

	public RabbitMQMessageSender(String brokers, String queueName) throws IOException {
		this(brokers, queueName, "guest", "guest");
	}

	public RabbitMQMessageSender(String brokers, String queueName, String user, String password) throws IOException {
		this(brokers, queueName, user, password, true);
	}

	public RabbitMQMessageSender(String brokers, String queueName, String user, String password, boolean durable)
			throws IOException {
		this(brokers, queueName, user, password, durable, new String[] { queueName });
	}

	public RabbitMQMessageSender(String brokers, String queueName, String user, String password, boolean durable,
			String[] routingKeys) throws IOException {
		// 1 建立连接
		connecter = new RabbitMQQueueConnecter(brokers, queueName);
		connecter.initConnection(user, password);
		connecter.initExchange("direct", durable);
		connecter.declareQueue(durable, true, routingKeys, null);
	}

	public void sendMessage(byte[] message, String routingKey) throws Exception {
		try {
			connecter.getChannel().basicPublish(connecter.getExchange(), routingKey, null, message); // 处理结束
		} catch (IOException e) {
			log.warn("RabbitMQ connect error when get message: ", e);
		}
	}

	/**
	 * 补充一个参数的方法
	 */
	public void sendMessage(byte[] message) throws Exception {
		try {
			// 修改第3个参数，使数据可以持久化
			connecter.getChannel().basicPublish(connecter.getExchange(), connecter.getQueueName(),
					MessageProperties.PERSISTENT_TEXT_PLAIN, message); // 处理结束
		} catch (IOException e) {
			log.warn("RabbitMQ connect error when get message: ", e);
		}

	}

	public void shutdown() {
		connecter.shutdown();
	}

	public static void main(String[] args) throws Exception {
		//通过配置文件获取配置项
		String ip=Config.getProperty("host");
		Integer port=Config.getIntProperty("port");
		String user="guest";
		String password="guest";
		String message = "http://item.jd.com/12345678.html";
		//通过参数获取获取配置项
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-ip")) {
				ip = InetAddress.getByName(args[++i]).getHostAddress();
		    }else if (args[i].equals("-port")) {
		        port = Integer.parseInt(args[++i]);
		    }else if (args[i].equals("-user")) {
		    	user = args[++i];
		    }else if (args[i].equals("-password")) {
		    	password = args[++i];
		    }else if (args[i].equals("-message")) {
		    	message = args[++i];
		    }
		}
		//通过系统环境变量获取配置项
		if(System.getenv("rabbitmq_ip")!=null)
			ip=System.getenv("rabbitmq_ip");
		if(System.getenv("rabbitmq_port")!=null)
			port=Integer.parseInt(System.getenv("rabbitmq_port"));
		if(System.getenv("rabbitmq_user")!=null)
			user=System.getenv("rabbitmq_user");
		if(System.getenv("rabbitmq_password")!=null)
			password=System.getenv("rabbitmq_password");
		System.out.println("获取ip为："+ip);
		String brokers =InetAddress.getByName(ip).getHostAddress() +":"+port;
		System.out.println("获取brokers为："+brokers);
		
//		brokers="10.1.235.30:10001";
		String queue = "LocalCrawlTaskQueue";

		MessageSender messageSender = new RabbitMQMessageSender(brokers, queue,user,password);
		int num=0;
		try{
			while(true){
				messageSender.sendMessage((message+num).getBytes());
				System.out.println("发送消息为:"+message+num);
				num++;
				new Thread().sleep(10000);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			messageSender.shutdown();
		}
	}
}
