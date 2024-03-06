package com.hrpc.http2;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;

public class Http2ClientInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;
    private final int maxContentLength;
    private Http2SettingsHandler settingsHandler;
    private Http2ClientHandler responseHandler;
    private String host;
    private int port;

    public Http2ClientInitializer(SslContext sslCtx, int maxContentLength, String host, int port) {
        this.sslCtx = sslCtx;
        this.maxContentLength = maxContentLength;
        this.host = host;
        this.port = port;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {

        settingsHandler = new Http2SettingsHandler(ch.newPromise());
        responseHandler = new Http2ClientHandler();
        
        if (sslCtx != null) {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast(sslCtx.newHandler(ch.alloc(), host, port));
            pipeline.addLast(Http2Util.getClientAPNHandler(maxContentLength, settingsHandler, responseHandler));
        }
    }

    public Http2SettingsHandler getSettingsHandler() {
        return settingsHandler;
    }
    
    public Http2ClientHandler getResponseHandler() {
        return responseHandler;
    }
}