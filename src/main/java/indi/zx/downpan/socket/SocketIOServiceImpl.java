package indi.zx.downpan.socket;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

@Slf4j
@Service
public class SocketIOServiceImpl {

    /**
     * 存放已连接的客户端
     */
    private static Map<String, SocketIOClient> roomChannel = new ConcurrentHashMap<>();
    private static Map<String, SocketIOClient> listChannel = new ConcurrentHashMap<>();
    private static Map<String,ChetInfo> chetInfos =  new ConcurrentHashMap<>();

    @OnEvent("handleMsg")
    public void handleMsg(SocketIOClient client, JSONObject data,String uid,String fid ){
        SocketIOClient friendClient = roomChannel.get(fid) == null ? listChannel.get(fid) : roomChannel.get(fid);
        ChetInfo chetInfo = JSONObject.toJavaObject(data, ChetInfo.class);
        chetInfo.setUid(uid);
        chetInfo.setFromId(uid);
        if (friendClient == null){
            chetInfos.put(fid,chetInfo);
        }else {
            friendClient.sendEvent("dealMsg", new VoidAckCallback() {
                @Override
                protected void onSuccess() {

                }

                @Override
                public void onTimeout() {
                    super.onTimeout();
                    ChetInfo chetInfo = JSONObject.toJavaObject(data, ChetInfo.class);
                    chetInfo.setUid(uid);
                    chetInfos.put(fid,chetInfo);
                }
            },chetInfo,uid,1);
        }
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

    @PostConstruct
    private void schedule(){
        Executors.newSingleThreadScheduledExecutor().schedule(()->{
            chetInfos.forEach((fid, value) -> {
                SocketIOClient friendClient = roomChannel.get(fid) == null ? listChannel.get(fid) : roomChannel.get(fid);
                friendClient.sendEvent("dealMsg", new VoidAckCallback() {
                    @Override
                    protected void onSuccess() {

                    }

                    @Override
                    public void onTimeout() {
                        chetInfos.put(fid,value);
                    }
                },value,value.getUid(),1);
            });
        }, 1, TimeUnit.SECONDS);
    }
}
