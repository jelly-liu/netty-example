package com.jelly.openProject.netty.uptime_example;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Sharable
public class UptimeClientHandler extends ChannelInboundHandlerAdapter {
    private static final String LSP = System.getProperty("line.separator");

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while (true){
                        String res = "request server time" + LSP;
                        System.out.print(res);
                        ctx.writeAndFlush(res);
                        Thread.sleep(1000);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("receive time from server: " + msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
