package com.simple.wstest;



import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager;

public class WebSocketSimple {

	//public static final String WS_SERVER_URI = "wss://websocketi041148trial.hanatrial.ondemand.com/websocket/echo";
	
	public static final String WS_SERVER_URI = "wss://iotcmsiot.neo.ondemand.com/com.sap.iot.cms/wSRouteNotificationServer";
	private Session session;

	public static void main (String args[]) {
		WebSocketSimple client = new WebSocketSimple();
		client.connect();
		try {
			client.sendMessage("{ \"mode\": \"sync\", \"messageType\": \"953c86d268c58db6f3a5\", \"messages\": [ { \"name\": \"gps1\", \"lat\": 10.4, \"long\": 10.2},{\"name\": \"gps2\", \"lat\": 10.4, \"long\": 10.2}]}");
			Thread.sleep(20000);
			while (true) {}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void connect() {
		try {
			
//			ClientEndpointConfig.Configurator configurator = new ClientEndpointConfig.Configurator() {
//				public void beforeRequest(Map<String, List<String>> headers) {
//					headers.put("Authorization", asList("Bearer xxxxxxxxxxxxxxxxxxxx"));
//					headers.put("Content-Type", asList("application/json;charset=utf-8"));
//				}
//			};

			ClientEndpointConfig.Configurator configurator = new ClientEndpointConfig.Configurator(); 

			ClientEndpointConfig configuration = ClientEndpointConfig.Builder.create()
					.configurator(configurator)
					.build();

			ClientManager client = ClientManager.createClient();

			// proxy
			client.getProperties().put(ClientManager.WLS_PROXY_HOST,    "proxy");
			client.getProperties().put(ClientManager.WLS_PROXY_PORT,    8080);

			// websocket connection
			client.connectToServer(
					new Endpoint() {
						@Override
						public void onOpen(Session session, EndpointConfig config) {
							WebSocketSimple.this.session = session;
							WebSocketSimple.this.session.addMessageHandler(new MessageHandler.Whole<String>() {

								public void onMessage(String message) {
									System.out.println("Received message: " + message);
								}
							});
							WebSocketSimple.this.session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {

								public void onMessage(ByteBuffer message) {
									System.out.println("Received message: " + message);
								}
							});
						}
					},
					configuration, new URI(WS_SERVER_URI));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String message) throws IOException, InterruptedException{
		if(session.isOpen())
		{
		  session.getBasicRemote().sendText(message);
		}
	}

}