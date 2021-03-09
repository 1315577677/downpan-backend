package indi.zx.downpan.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SocketIOServiceImpl {

    /**
     * 存放已连接的客户端
     */
    private static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();


    @OnConnect
    public void connect(SocketIOClient client){
        UUID sessionId = client.getSessionId();
        clientMap.put(sessionId.toString(),client);
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client){
        UUID sessionId = client.getSessionId();
        clientMap.remove(sessionId.toString());
    }

    @OnEvent("handleMsg")
    public void handleMsg(SocketIOClient client, AckRequest request, Object data ){
        System.out.println(data);
    }
}
