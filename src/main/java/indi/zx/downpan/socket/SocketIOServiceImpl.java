package indi.zx.downpan.socket;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.annotation.OnEvent;
import indi.zx.downpan.entity.ChatInfo;
import indi.zx.downpan.service.ChatInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SocketIOServiceImpl {

    /**
     * 存放已连接的客户端
     */
    private static Map<String, SocketIOClient> roomChannel = new ConcurrentHashMap<>();
    private static Map<String, SocketIOClient> listChannel = new ConcurrentHashMap<>();

    private final ChatInfoService chatInfoService;

    public SocketIOServiceImpl(ChatInfoService chatInfoService) {
        this.chatInfoService = chatInfoService;
    }

    @OnEvent("handleMsg")
    public void handleMsg(SocketIOClient client, JSONObject data, String uid, String fid) {
        SocketIOClient friendClient = roomChannel.get(fid) == null ? listChannel.get(fid) : roomChannel.get(fid);
        ChatInfo chatInfo = JSONObject.toJavaObject(data, ChatInfo.class);
        chatInfo.setUid(uid);
        chatInfo.setFromId(uid);
        chatInfo.setFid(fid);
        chatInfoService.save(chatInfo);
        if (friendClient == null) {

        } else {
            friendClient.sendEvent("dealMsg", new VoidAckCallback() {
                @Override
                protected void onSuccess() {
                    chatInfoService.save(chatInfo);
                }

                @Override
                public void onTimeout() {
                    super.onTimeout();
                    chatInfoService.save(chatInfo);
                }
            }, chatInfo, uid, 1);
        }
    }

    @OnEvent("login")
    public void login(SocketIOClient client, String uid, Integer channel) {
        if (channel == 0) {
            roomChannel.remove(uid);
            listChannel.put(uid, client);
        } else {
            listChannel.remove(uid);
            roomChannel.put(uid, client);
        }
    }

    @PostConstruct
    private void schedule() {
//        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
//            chatInfos.forEach((fid, value) -> {
//                SocketIOClient friendClient = roomChannel.get(fid) == null ? listChannel.get(fid) : roomChannel.get(fid);
//                friendClient.sendEvent("dealMsg", new VoidAckCallback() {
//                    @Override
//                    protected void onSuccess() {
//
//                    }
//
//                    @Override
//                    public void onTimeout() {
//                        chatInfos.put(fid, value);
//                    }
//                }, value, value.getUid(), 1);
//            });
//        }, 1, TimeUnit.SECONDS);
//    }
    }
}
