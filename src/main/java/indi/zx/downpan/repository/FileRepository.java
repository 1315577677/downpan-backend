package indi.zx.downpan.repository;

import indi.zx.downpan.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Base64;
import java.util.List;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-20 16:59
 */
@NoRepositoryBean
public interface FileRepository extends CrudRepository<FileEntity,String> , JpaSpecificationExecutor<FileEntity> {
    List<FileEntity> findFileEntitysByCreateUserAndParent(String s, String username);

    List<FileEntity> findFileEntitysByMD5(String md5);


    List<FileEntity> findFileEntitysByCreateUser(String username);

    @Modifying
    @Query(value = "update file set is_delete = false where create_user = ?1",nativeQuery = true)
    void updateFileStatusByCreateUser(String createUser);

    List<FileEntity> findFileEntityByParentAndName(String parent, String name);
}
