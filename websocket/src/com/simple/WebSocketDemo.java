package com.simple;

import java.io.IOException;

import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/echo")
public class WebSocketDemo{

    @OnMessage
    public void echoTextMessage(Session session, String msg, boolean last) {
        try {
            if (session.isOpen()) {
                session.getBasicRemote().sendText("Message Recieved : " + msg, last);
            }
            
        } catch (IOException e) {
            try {
                session.close();
            } catch (IOException e1) {
                // Ignore
            }
        }
    }
    
    @OnOpen
    public void open(Session session, EndpointConfig config) {
    	
    	 try {
             if (session.isOpen()) {
                 session.getBasicRemote().sendText("Web Socket Opened\n", false);
             }
            
         } catch (IOException e) {
             try {
                 session.close();
             } catch (IOException e1) {
                 // Ignore
             }
         }
    }
}