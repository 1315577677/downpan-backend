package indi.zx.downpan.service.impl;

import indi.zx.downpan.entity.ChatInfo;
import indi.zx.downpan.repository.ChatInfoRepository;
import indi.zx.downpan.service.ChatInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatInfoServiceImp implements ChatInfoService {
    private final ChatInfoRepository chatInfoRepository;

    public ChatInfoServiceImp(ChatInfoRepository chatInfoRepository) {
        this.chatInfoRepository = chatInfoRepository;
    }

    @Override
    public List<ChatInfo> findChatInfos(String fid, String uid) {
        return chatInfoRepository.findAllByUidAndFid(uid, fid);
    }

    @Override
    public void save(ChatInfo chatInfo) {
        chatInfoRepository.save(chatInfo);
    }
}
