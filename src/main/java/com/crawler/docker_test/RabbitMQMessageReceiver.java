package com.crawler.docker_test;

import java.io.IOException;
import java.net.InetAddress;

import lombok.extern.slf4j.Slf4j;

import com.crawler.docker_test.MessageReceiver;
import com.crawler.util.Config;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

@Slf4j
public class RabbitMQMessageReceiver implements MessageReceiver{

	// 连接
	private RabbitMQQueueConnecter connecter;

	// 运行标志
	private boolean runFlag;

	public RabbitMQMessageReceiver(String brokers, String queueName)
			throws IOException {
		this(brokers, queueName, "guest", "guest");
	}

	public RabbitMQMessageReceiver(String brokers, String queueName,
			String user, String password) throws IOException {
		this(brokers, queueName, user, password, new String[] {});
	}

	public RabbitMQMessageReceiver(String brokers, String queueName,
			String user, String password, String[] routingKeys)
			throws IOException {
		this(brokers, queueName, user, password, routingKeys, true);
	}

	public RabbitMQMessageReceiver(String brokers, String queueName,
			String user, String password, String[] routingKeys, boolean durable)
			throws IOException {
		this(brokers, queueName, user, password, routingKeys, durable, true);
	}

	public RabbitMQMessageReceiver(String brokers, String queueName,
			String user, String password, String[] routingKeys,
			boolean durable, boolean autoDelete) throws IOException {
		// 1 建立连接
		connecter = new RabbitMQQueueConnecter(brokers, queueName);
		connecter.initConnection(user, password);
		connecter.initExchange("direct", durable);
		connecter.declareQueue(durable, autoDelete, routingKeys, null);
		runFlag = true;
	}

	public void handleMessage(MessageHandler handler) throws Exception {
		if (handler == null) {
			log.info("RabbitMQMessageReceiver handle is NULL , thread "
					+ Thread.currentThread().getId());
			return;
		}
		do {
			Channel channel = connecter.getChannel();
			if (channel == null) {
				log.info("RabbitMQMessageReceiver channel is NULL , thread "
						+ Thread.currentThread().getId());
				break;
			}
			try {
				boolean noAck = false;

				if (!noAck) {
					channel.basicQos(100);
				}
				QueueingConsumer consumer = new QueueingConsumer(channel);
				channel.basicConsume(connecter.getQueueName(), noAck, consumer);
				for (; runFlag;) {
					try {
						QueueingConsumer.Delivery delivery = consumer
								.nextDelivery();
						log.info("get message from rabbitmq **");
						if (delivery != null) {
							handler.onMessage(delivery.getBody(), delivery
									.getEnvelope().getRoutingKey());
						}
						if (!noAck) {
							channel.basicAck(delivery.getEnvelope()
									.getDeliveryTag(), false);
						}
						delivery = null;
					} catch (InterruptedException e) {
						log.warn("RabbitMQMessageReceiver Thread "
								+ Thread.currentThread().getId()
								+ " occer exception : ", e);
					}
				}
			} finally {
				if (channel != null) {
					channel.abort();
				}
			}
		} while (runFlag);
		log.info("RabbitMQMessageReceiver handle message stop , thread "
				+ Thread.currentThread().getId());
	}

	public void shutdown() {
		stopHandle();
		connecter.shutdown();
	}

	public void stopHandle() {
		runFlag = false;
	}
	
	public static void main(String[] args) throws Exception {
		String ip=InetAddress.getByName(Config.getProperty("host")).getHostAddress();
		Integer port=Config.getIntProperty("port");
		String user="guest";
		String password="guest";
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-ip")) {
				ip = args[++i];
		    }else if (args[i].equals("-port")) {
		        port = Integer.parseInt(args[++i]);
		    }else if (args[i].equals("-user")) {
		    	user = args[++i];
		    }else if (args[i].equals("-password")) {
		    	password = args[++i];
		    }
		}
		System.out.println("获取IP为："+ip);
		String brokers = ip +":"+port;
		
		String queue = "LocalCrawlTaskQueue";
		try {
			RabbitMQMessageReceiver messageSender = new RabbitMQMessageReceiver(
					brokers, queue, user,password);
			messageSender.handleMessage(new MessageHandler() {

				public void onMessage(byte[] message, String routingKey) {
					byte b[] = message;
					System.out.println(new String(b));
					b = null;

				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public static void main(String[] args) throws Exception {
//		class ChildThread extends Thread{
//			@Override
//			public void run(){
//				String brokers = Config.getProperty("host")+":"+Config.getIntProperty("port");
//				String queue = "LocalCrawlTaskQueue";
//				try {
//					RabbitMQMessageReceiver messageSender = new RabbitMQMessageReceiver(
//							brokers, queue, "guest", "guest");
//					messageSender.handleMessage(new MessageHandler() {
//
//						public void onMessage(byte[] message, String routingKey) {
//							byte b[] = message;
//							System.out.println(new String(b));
//							b = null;
//
//						}
//					});
//				} catch (IOException e) {
//					e.printStackTrace();
//				}catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		Thread childthead=new ChildThread();
//		childthead.start();
//		
//		
//	}

}
