package indi.zx.downpan.service;

import indi.zx.downpan.entity.ChatInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatInfoService {
    List<ChatInfo> findChatInfos(String fid, String uid);

    void save(ChatInfo chatInfo);
}
