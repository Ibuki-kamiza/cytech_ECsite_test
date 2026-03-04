package jp.co.sss.cytech_ECsite_test.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jp.co.sss.cytech_ECsite_test.entity.User;
import jp.co.sss.cytech_ECsite_test.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("ユーザーが見つかりません: " + email);
        }
        String role = user.getRole() != null ? user.getRole() : "USER";
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPasswords(),
            List.of(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }
}
