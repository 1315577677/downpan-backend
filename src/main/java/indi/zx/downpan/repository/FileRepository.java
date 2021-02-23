package indi.zx.downpan.repository;

import indi.zx.downpan.entity.FileEntity;
import indi.zx.downpan.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.awt.print.Pageable;
import java.util.List;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-20 16:59
 */
@NoRepositoryBean
public interface FileRepository extends CrudRepository<FileEntity,String> {


}
