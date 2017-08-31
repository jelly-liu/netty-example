package com.jelly.openProject.netty.uptime_example;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public final class UptimeClient {

    static final String HOST = "127.0.0.1";
    static final int PORT = 8080;

    private static final Bootstrap bs = new Bootstrap();

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        bs.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(HOST, PORT)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //outbound:对于socket write操作，将会从Tail-->Head依次执行pipeline中的每个Encoder
                        ch.pipeline().addLast(new ByteArrayEncoder());//可以不写这个，底层会用ByteBuf
                        ch.pipeline().addLast(new StringEncoder());
                        //inbound:对于read操作，将会从Head-->Tail依次执行Decoder
                        ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new UptimeClientHandler());
                    }
                });
        bs.connect();
    }
}
