package indi.zx.downpan.repository;

import indi.zx.downpan.entity.ChatInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface ChatInfoRepository extends CrudRepository<ChatInfo,String> {
   List<ChatInfo> findAllByUidAndFid(String uid, String fid);
   List<ChatInfo> findChatInfosByUidAndFid(String uid, String fid);
  Page<ChatInfo> findAllByUidAndFid(String uid, String fid, Pageable pageable);
}
