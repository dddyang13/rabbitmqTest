package com.crawler.docker_test;

/**
 * 消息发送接口
 * 
 * @author shilei
 * 
 */
public interface MessageSender extends MessageConnectionCloseable {

	/**
	 * 发送消息
	 * 
	 * @param message
	 *            消息实体
	 * @throws Exception
	 */
	public void sendMessage(byte[] message, String routingKey) throws Exception;

	/**
	 * 
	 * @Title: sendMessage
	 * @Description: TODO(发送消息)
	 * @param @param message
	 * @param @throws Exception 参数
	 * @return void 返回类型
	 * @throws
	 */
	public void sendMessage(byte[] message) throws Exception;
}
