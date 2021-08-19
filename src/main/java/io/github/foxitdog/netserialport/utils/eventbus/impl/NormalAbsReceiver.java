package io.github.foxitdog.netserialport.utils.eventbus.impl;


import io.github.foxitdog.netserialport.utils.eventbus.Publisher;
import io.github.foxitdog.netserialport.utils.eventbus.Receiver;

import lombok.CustomLog;

/**
 * 
 * @author foxitdog
 */
@CustomLog
public abstract class NormalAbsReceiver<T,C> implements Receiver {

    Publisher publisher;

    public NormalAbsReceiver(Publisher publisher){
        this.publisher=publisher;
    }

    @Override
    public void receive(Object type, Object content) {
        T t= (T) type;
        C c= (C) content;
        handle(t,c);
    }

    public abstract void handle(T type, C content);

    @Override
    public void disSubscribe() {
        publisher.disSubscribe(this);
        log.info("取消订阅所有信息");
    }

    public NormalAbsReceiver subscribe(T type){
        publisher.subscribe(type,this);
        return this;
    }

    public void setCommonPublisher(Publisher publisher) {
        this.publisher = publisher;
    }
}
