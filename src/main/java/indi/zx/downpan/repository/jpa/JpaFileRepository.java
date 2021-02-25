package indi.zx.downpan.repository.jpa;

import indi.zx.downpan.entity.FileEntity;
import indi.zx.downpan.repository.FileRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-20 17:05
 */
@Repository
public interface JpaFileRepository extends FileRepository {
}
