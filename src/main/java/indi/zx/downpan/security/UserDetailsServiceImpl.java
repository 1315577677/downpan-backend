package indi.zx.downpan.security;

import indi.zx.downpan.entity.UserEntity;
import indi.zx.downpan.repository.jpa.JpaUserRepository;
import indi.zx.downpan.security.model.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-08 16:20
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private JpaUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
         UserEntity user = userRepository.findUserEntityByUsername(s);
        return new JwtUser(user);
    }

}
