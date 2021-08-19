package io.github.foxitdog.netserialport.utils.eventbus;

/**
 * 
 * @author foxitdog
 */
public interface Publisher<T,C> {

	/**
	 * 订阅者receiver订阅type类型的数据
	 *
	 * @param type
	 *            类型
	 * @param receiver
	 *            订阅者
	 */
	void subscribe(T type, Receiver receiver);

	/**
	 * 订阅者取消订阅所有的订阅类型
	 *
	 * @param receiver
	 *            订阅者
	 */
	void disSubscribe(Receiver receiver);

	/**
	 * 订阅者receiver的取消订阅的类型type数据
	 *
	 * @param type
	 *            订阅的类型
	 * @param receiver
	 *            订阅人
	 */
	void disSubscribe(T type, Receiver receiver);

	/**
	 * 将type类型的数据发送给所有的订阅者
	 *
	 * @param type
	 *            类型数据
	 * @param content
	 *            数据内容
	 */
	void publish(T type, C content);
}
