package io.github.foxitdog.netserialport.impl;

import io.github.foxitdog.netserialport.utils.StringUtils;

import lombok.CustomLog;

@CustomLog
public class UncaughtExceptionHandlerImpl implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("Thread:{},has uncaughtException:{}", t.getName(), StringUtils.getExceptionStack(e));
    }

}
