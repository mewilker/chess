package serverFacade;

import javax.websocket.*;
import java.net.*;

public class
WSClient extends Endpoint {
    private Session session;
    private MessageHandler handler;

    public WSClient () throws Exception {
        URI uri = new URI("http://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);
    }

    public WSClient(String url, MessageHandler handler) throws Exception{
        URI uri = new URI(url+"/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);
        this.handler = handler;
        this.session.addMessageHandler(handler);

    }
    public void send(String msg) throws java.io.IOException{
        this.session.getBasicRemote().sendText(msg);
    }

    public MessageHandler getHandler(){
        return handler;
    }

    @Override
    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
}
