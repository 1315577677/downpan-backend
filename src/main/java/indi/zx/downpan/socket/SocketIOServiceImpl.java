package indi.zx.downpan.socket;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.*;
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
    private static Map<String, SocketIOClient> roomChannel = new ConcurrentHashMap<>();
    private static Map<String, SocketIOClient> listChannel = new ConcurrentHashMap<>();


    @OnEvent("handleMsg")
    public void handleMsg(SocketIOClient client, JSONObject data,String uid,String fid ){
        client.sendEvent("dealMsg", new VoidAckCallback() {
            @Override
            protected void onSuccess() {

            }
        },data,uid,1);
    }

    @OnEvent("login")
    public void login(SocketIOClient client,String uid,Integer channel){
      if (channel == 0){
          roomChannel.remove(uid);
          listChannel.put(uid,client);
      }else {
          listChannel.remove(uid);
          roomChannel.put(uid,client);
      }
    }
}
