package io.github.foxitdog.netserialport.utils.eventbus;

/**
 * 
 * @author foxitdog
 */
public interface Receiver {
    void receive(Object type, Object content);
    void disSubscribe();
}
