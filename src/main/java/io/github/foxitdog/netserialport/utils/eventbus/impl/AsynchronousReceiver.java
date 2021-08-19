package io.github.foxitdog.netserialport.utils.eventbus.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;

import io.github.foxitdog.netserialport.utils.eventbus.Publisher;


/**
 * 
 * @author foxitdog
 */
public class AsynchronousReceiver<T,C> extends NormalAbsReceiver<T,C> {

    Map<T,BiConsumer<T,C>> map=new HashMap<>();

    Executor executor;

    public AsynchronousReceiver(Executor executor, Publisher publisher){
        super(publisher);
        this.executor=executor;
    }

    @Override
    public void handle(T type, C content) {
        executor.execute(()->{
            BiConsumer c=map.get(type);
           if(c!=null){
               c.accept(type,content);
           }
        });
    }

    public NormalAbsReceiver subscribe(T type, BiConsumer<T,C> c) {
        map.put(type,c);
        return super.subscribe(type);
    }

    public void disSubscribe(T type) {
        map.remove(type);
        publisher.disSubscribe(type,this);
    }

    @Override
    public void disSubscribe() {
        map.clear();
        map=null;
        super.disSubscribe();
    }
}
