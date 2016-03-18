package com.crawler.docker_test;

/**
 * @ClassName: AdaptorConfig
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author ai
 * @date 2015年8月28日
 * 
 */

public class AdaptorConfig {

	private static String rabbitmqAddress;
	private static String rabbitmqQueue;
	private static String rabbitmqUser;
	private static String rabbitmqPassword;
	private static Boolean rabbitmqDurable;
	private static String inputPath;
	
	private static Boolean useRabbitMQ;
	
	private static Boolean useInputFile;

	/**
	 * @return rabbitmqAddress
	 */

	public static String getRabbitmqAddress() {
		return rabbitmqAddress;
	}

	/**
	 * @param rabbitmqAddress
	 *            the rabbitmqAddress to set
	 */

	public static void setRabbitmqAddress(String rabbitmqAddress) {
		AdaptorConfig.rabbitmqAddress = rabbitmqAddress;
	}

	/**
	 * @return rabbitmqQueue
	 */

	public static String getRabbitmqQueue() {
		return rabbitmqQueue;
	}

	/**
	 * @param rabbitmqQueue
	 *            the rabbitmqQueue to set
	 */

	public static void setRabbitmqQueue(String rabbitmqQueue) {
		AdaptorConfig.rabbitmqQueue = rabbitmqQueue;
	}

	/**
	 * @return rabbitmqUser
	 */

	public static String getRabbitmqUser() {
		return rabbitmqUser;
	}

	/**
	 * @param rabbitmqUser
	 *            the rabbitmqUser to set
	 */

	public static void setRabbitmqUser(String rabbitmqUser) {
		AdaptorConfig.rabbitmqUser = rabbitmqUser;
	}

	/**
	 * @return rabbitmqPassword
	 */

	public static String getRabbitmqPassword() {
		return rabbitmqPassword;
	}

	/**
	 * @param rabbitmqPassword
	 *            the rabbitmqPassword to set
	 */

	public static void setRabbitmqPassword(String rabbitmqPassword) {
		AdaptorConfig.rabbitmqPassword = rabbitmqPassword;
	}

	/**
	 * @return rabbitmqDurable
	 */

	public static Boolean getRabbitmqDurable() {
		return rabbitmqDurable;
	}

	/**
	 * @param rabbitmqDurable
	 *            the rabbitmqDurable to set
	 */

	public static void setRabbitmqDurable(Boolean rabbitmqDurable) {
		AdaptorConfig.rabbitmqDurable = rabbitmqDurable;
	}

	public static String getInputPath() {
		return inputPath;
	}

	public static void setInputPath(String inputPath) {
		AdaptorConfig.inputPath = inputPath;
	}

	public static Boolean getUseRabbitMQ() {
		return useRabbitMQ;
	}

	public static void setUseRabbitMQ(Boolean useRabbitMQ) {
		AdaptorConfig.useRabbitMQ = useRabbitMQ;
	}

	public static Boolean getUseInputFile() {
		return useInputFile;
	}

	public static void setUseInputFile(Boolean useInputFile) {
		AdaptorConfig.useInputFile = useInputFile;
	}

}
