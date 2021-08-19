package io.github.foxitdog.netserialport.utils.eventbus.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.github.foxitdog.netserialport.utils.eventbus.Publisher;
import io.github.foxitdog.netserialport.utils.eventbus.Receiver;

import lombok.CustomLog;
import lombok.Getter;
import lombok.Setter;

@CustomLog
public class CommonPublisher<T, C> implements Publisher<T, C> {

	private Object lock = new Object();

	/**
	 * 每个订阅类型的订阅者集合 Object type List<Receiver> receivers
	 */
	@Getter
	@Setter
	private ConcurrentHashMap<T, List<Receiver>> subscribeMap = new ConcurrentHashMap<>();

	/**
	 * 每个订阅者的订阅类型集合 Receiver List<Object>
	 */
	@Getter
	@Setter
	private ConcurrentHashMap<Receiver, List<T>> targetMap = new ConcurrentHashMap<>();

	/**
	 * 
	 * 将订阅类型type添加到订阅者receiver的订阅列表中
	 *
	 * @param type
	 *            订阅的类型
	 * @param receiver
	 *            接受者
	 */
	private void addToTargetMap(T type, Receiver receiver) {
		List<T> l = targetMap.get(receiver);
		if (l == null) {
			targetMap.put(receiver, Collections.synchronizedList(new ArrayList<>(Arrays.asList(type))));
		} else {
			if (!l.contains(type)) {
				l.add(type);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disSubscribe(T type, Receiver receiver) {
		synchronized (lock) {
			List<Receiver> l = subscribeMap.get(type);
			if (l != null) {
				if (l.contains(receiver)) {
					l.remove(receiver);
				}
			}
			List<T> tl = targetMap.get(receiver);
			if (tl != null) {
				if (tl.contains(type)) {
					tl.remove(type);
				}
			}
		}
		afterDisSubscribe(type, receiver);
	}

	public void afterDisSubscribe(T type, Receiver receiver) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void publish(T type, C content) {
		List<Receiver> list = subscribeMap.get(type);
		if (list != null) {
			list.forEach(receiver -> receiver.receive(type, content));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void subscribe(T type, Receiver receiver) {
		beforeSubscribe(type, receiver);
		synchronized (lock) {
			List<Receiver> l = subscribeMap.get(type);
			if (l == null) {
				subscribeMap.put(type, Collections.synchronizedList(new ArrayList<>(Arrays.asList(receiver))));
				addToTargetMap(type, receiver);
			} else {
				if (!l.contains(receiver)) {
					l.add(receiver);
					addToTargetMap(type, receiver);
				}
			}
		}
	}

	public void beforeSubscribe(T type, Receiver receiver) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disSubscribe(Receiver receiver) {
		synchronized (lock) {
			List<T> l = targetMap.get(receiver);
			if (l != null) {
				T[] types = (T[]) l.toArray();
				for (int i = 0; i < types.length; i++) {
					disSubscribe(types[i], receiver);
				}
			}
			targetMap.remove(receiver);
		}
	}

	public boolean hasSubscribeType(T type){
		synchronized (lock){
			List<Receiver> l = subscribeMap.get(type);
			return l!=null&&!l.isEmpty();
		}
	}
}
