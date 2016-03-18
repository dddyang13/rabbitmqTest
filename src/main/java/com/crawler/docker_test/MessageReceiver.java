package com.crawler.docker_test;

/**
 * 消息接收接口
 * 
 * @author shilei
 * 
 */
public interface MessageReceiver extends MessageConnectionCloseable {

	/**
	 * 循环接受并处理消息
	 * 
	 * @param handler
	 */
	public void handleMessage(MessageHandler handler) throws Exception;

	/**
	 * 停止循环处理
	 */
	public void stopHandle();

	/**
	 * 消息处理器
	 * 
	 * @author shilei
	 * 
	 */
	public interface MessageHandler {
		/**
		 * 处理消息
		 * 
		 * @param message
		 */
		public void onMessage(byte[] message,String routingKey);
	}
}
