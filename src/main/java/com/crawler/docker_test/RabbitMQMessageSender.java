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
		String ip=InetAddress.getByName(Config.getProperty("host")).getHostAddress();
		System.out.println("通过DNS："+Config.getProperty("host")+"获取IP为："+ip);
		String brokers =ip +":"+Config.getIntProperty("port");
		String queue = "LocalCrawlTaskQueue";

		String message = "http://item.jd.com/12345678.html";
		if(args.length>0)message=args[0];

		MessageSender messageSender = new RabbitMQMessageSender(brokers, queue);
		
		messageSender.sendMessage(message.getBytes());
		
		messageSender.shutdown();
	}

}
