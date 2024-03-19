package org.codeman.channel;

import org.codeman.utils.concurrent.GenericFutureListener;

import java.util.concurrent.Future;

/**
 * @author hdgaadd
 * created on 2022/04/11
 */
public interface ChannelFuture extends Future<Void> { // Future、Void??
    Channel channel();

    ChannelFuture addListener(GenericFutureListener<? extends Future<? super Void>> future); // 下界通配符
}
