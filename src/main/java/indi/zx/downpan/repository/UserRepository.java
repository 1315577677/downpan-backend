package indi.zx.downpan.repository;

import indi.zx.downpan.entity.UserEntity;
import indi.zx.downpan.service.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-08 16:20
 */
@NoRepositoryBean
public interface UserRepository extends JpaRepository<UserEntity,String> {
    UserEntity findUserEntityByUsername(String username);
    List<UserEntity> findAllByName(String name);
}
