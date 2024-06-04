package com.techelevator.StudentBrowseSystem.services;

import com.techelevator.StudentBrowseSystem.dao.UsersDao;
import com.techelevator.StudentBrowseSystem.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This class is used to load users for the auth system
 */
@Component
public class StudentBrowseUserDetailsService implements UserDetailsService {
    /**
     * The DAO used to access user data
     */
    private PasswordEncoder passwordEncoder;
    private UsersDao usersDao;

    /**
     * Create a new instance of this class
     */
    public StudentBrowseUserDetailsService(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    /**
     * Load a user by their username
     *
     * @param username The username to load
     * @return The user details as a JwtUser
     * @throws UsernameNotFoundException If the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = usersDao.getUserByUsername(username);

        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        JwtUser jwtUser = new JwtUser();

        jwtUser.setUsername(user.getUsername());
        jwtUser.setPassword(user.getPassword());

        List<String> roles = usersDao.getUserRoles(user.getUsername());
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        jwtUser.setAuthorities(authorities);
        jwtUser.setAccountNonExpired(true);
        jwtUser.setAccountNonLocked(true);
        jwtUser.setApiAccessAllowed(true);
        jwtUser.setCredentialsNonExpired(true);
        jwtUser.setEnabled(true);

        return jwtUser;
    }

//    @Autowired
//    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//    }
}
