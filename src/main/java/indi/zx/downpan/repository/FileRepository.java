package indi.zx.downpan.repository;

import indi.zx.downpan.entity.FileEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-20 16:59
 */
@NoRepositoryBean
public interface FileRepository extends CrudRepository<FileEntity,String> {
    List<FileEntity> findFileEntitysByCreateUserAndParent(String username);

    FileEntity findFileEntityByMD5();
}
