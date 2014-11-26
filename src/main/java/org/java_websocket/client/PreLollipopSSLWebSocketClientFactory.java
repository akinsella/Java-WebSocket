package org.java_websocket.client;
import java.io.IOException;
import java.net.Socket;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import org.java_websocket.WebSocketAdapter;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.client.WebSocketClient.WebSocketClientFactory;
import org.java_websocket.drafts.Draft;
import org.java_websocket.PreLollipopSSLSocketChannel2;


public class PreLollipopSSLWebSocketClientFactory implements WebSocketClientFactory {
	protected SSLContext sslcontext;
	protected ExecutorService exec;

	public PreLollipopSSLWebSocketClientFactory(SSLContext sslContext) {
		this( sslContext, Executors.newSingleThreadScheduledExecutor() );
	}

	public PreLollipopSSLWebSocketClientFactory(SSLContext sslContext, ExecutorService exec) {
		if( sslContext == null || exec == null )
			throw new IllegalArgumentException();
		this.sslcontext = sslContext;
		this.exec = exec;
	}

	@Override
	public ByteChannel wrapChannel( SocketChannel channel, SelectionKey key, String host, int port ) throws IOException {
		SSLEngine e = sslcontext.createSSLEngine( host, port );
		e.setUseClientMode( true );
		return new PreLollipopSSLSocketChannel2( channel, e, exec, key );
	}

	@Override
	public WebSocketImpl createWebSocket( WebSocketAdapter a, Draft d, Socket c ) {
		return new WebSocketImpl( a, d, c );
	}

	@Override
	public WebSocketImpl createWebSocket( WebSocketAdapter a, List<Draft> d, Socket s ) {
		return new WebSocketImpl( a, d, s );
	}
}