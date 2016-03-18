package com.crawler.docker_test;

import java.io.IOException;
import java.util.Map;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * RabbitMQ集群连接器
 * 
 * @author shilei
 * 
 */
@Slf4j
public class RabbitMQQueueConnecter {
	// 集群列表
	@Getter
	private String brokers;
	// 交换机名
	@Getter
	private String exchange;
	// 队列名称
	@Getter
	private String queueName;
	// 连接对象
	private Connection connection;
	// 通道
	private Channel channel;

	/**
	 * 初始化rabbitmq 连接，包括： 1）创建Connection 2）创建Channel 3）创建Exchange
	 * 
	 * @param brokers
	 * @param queueName
	 * @throws IOException
	 * 
	 */
	public RabbitMQQueueConnecter(String brokers) throws IOException {
		this(brokers, "SCA_Queue", "SCA_Exchange");
	}

	public RabbitMQQueueConnecter(String brokers, String queueName) throws IOException {
		this(brokers, queueName, "SCA_Exchange");
	}

	public RabbitMQQueueConnecter(String brokers, String queueName, String exchange) throws IOException {
		this.brokers = brokers;
		this.queueName = queueName;
		this.exchange = exchange;
	}

	public Channel getChannel() {
		return channel;
	}

	/**
	 * 初始化连接
	 * 
	 * @throws IOException
	 */
	public void initConnection(String user, String password) throws IOException {
		ConnectionFactory factory = new ConnectionFactory();
		Address[] brokerAddressArray = Address.parseAddresses(brokers);
		factory.setUsername(user);
		factory.setPassword(password);
		// create connection
		connection = factory.newConnection(brokerAddressArray);
		// create channel
		channel = connection.createChannel();
		log.info("URLMessageProcesser Connect Borker : " + connection.getAddress().getHostAddress() + ":"
				+ connection.getPort());
	}

	/**
	 * 初始化路由
	 * 
	 * @param type
	 *            the exchange type
	 * @param durable
	 *            true if we are declaring a durable queue (the queue will
	 *            survive a server restart)
	 * @param autoDelete
	 *            true if we are declaring an exclusive queue (restricted to
	 *            this connection)
	 * @throws IOException
	 */
	public void initExchange(String type, boolean durable) throws IOException {
		channel.exchangeDeclare(exchange, type, durable, false, false, null);
	}

	/**
	 * 初始化队列
	 * 
	 * @param durable
	 *            true if we are declaring a durable queue (the queue will
	 *            survive a server restart)
	 * @param exclusive
	 *            true if we are declaring an exclusive queue (restricted to
	 *            this connection)
	 * @param autoDelete
	 *            true if we are declaring an autodelete queue (server will
	 *            delete it when no longer in us e)
	 * @throws IOException
	 */
	public void declareQueue(boolean durable, boolean autoDelete, String[] routingKeys, Map<String, Object> args)
			throws IOException {
		// 声明队列
		channel.queueDeclare(queueName, durable, false, autoDelete, args);
		// if (routingKeys.length == 0) {
		if (routingKeys == null || routingKeys.length == 0) {
			channel.queueBind(queueName, exchange, queueName);
			return;
		}
		for (String routingKey : routingKeys) {
			channel.queueBind(queueName, exchange, routingKey);
		}
	}

	/**
	 * 关闭
	 */
	public void shutdown() {
		try {
			if (channel != null) {
				channel.abort();
			}
		} catch (IOException e) {
		}
		if (connection != null) {
			connection.abort();
		}
	}
}
