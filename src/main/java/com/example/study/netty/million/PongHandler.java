package com.example.study.netty.million;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PongHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static final ByteBuf PONG_BUF = Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer("pong".getBytes()));

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        String str = new String(data);
        if ("ping".equals(str)) {
            ctx.writeAndFlush(PONG_BUF.duplicate());
        }
    }
}
