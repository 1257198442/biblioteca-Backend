package com.example.demo.domain.service;

import com.example.demo.adapters.mongodb.daos.UserRepository;
import com.example.demo.domain.models.Admin;
import com.example.demo.domain.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Qualifier("jiaming.SHI")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String mobile) {
        User user = userRepository.readByTelephone(mobile)
                .orElseThrow(() -> new UsernameNotFoundException("mobile not found. " + mobile)).toUser();
        return this.userBuilder(user.getTelephone(), user.getPassword(), new Admin[]{Admin.AUTHENTICATED}, user.getActive());
    }

    private org.springframework.security.core.userdetails.User userBuilder(String mobile, String password, Admin[] roles,
                                                                           boolean active) {
        List< GrantedAuthority > authorities = new ArrayList<>();
        for (Admin role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.withPrefix()));
        }
        return new org.springframework.security.core.userdetails.User(mobile, password, active, true,
                true, true, authorities);
    }
}
