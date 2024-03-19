package org.codeman.channel;

import org.codeman.utils.concurrent.GenericFutureListener;
import lombok.Data;

import java.util.Objects;
import java.util.concurrent.Future;

/**
 * @author hdgaadd
 * created on 2022/04/14
 */
@Data
public abstract class ChannelPromise implements ChannelFuture {

    protected Object listener;

    @Override
    public ChannelFuture addListener(GenericFutureListener<? extends Future<? super Void>> listener) {
        if (!Objects.isNull(listener)) {
            this.listener = listener;
        }
        return this;
    }

}
