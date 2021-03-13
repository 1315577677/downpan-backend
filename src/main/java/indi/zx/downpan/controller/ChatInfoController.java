package indi.zx.downpan.controller;

import com.alibaba.fastjson.JSONObject;
import indi.zx.downpan.common.response.Response;
import indi.zx.downpan.entity.ChatInfo;
import indi.zx.downpan.service.ChatInfoService;
import indi.zx.downpan.support.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-09 11:16
 */
@RestController
@RequestMapping("/chat")
public class ChatInfoController {
    @Autowired
    ChatInfoService chatInfoService;
    @PostMapping("/getChatInfos")
    public Response getChatInfos(@RequestBody JSONObject param) {
        String fid = param.getString("fid");
        String uid = param.getString("uid");
        List<ChatInfo> chatInfos = chatInfoService.findChatInfos(fid, uid);
        List<ChatInfo> chatInfos1 = chatInfoService.findChatInfos(uid, fid);
        chatInfos1.addAll(chatInfos);
        return ResponseUtil.success(chatInfos1.stream().sorted(Comparator.comparing(ChatInfo::getCreateTime)).collect(Collectors.toList()));
    }
}
