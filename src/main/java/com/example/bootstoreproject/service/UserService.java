package com.example.bootstoreproject.service;

import com.example.bootstoreproject.dao.RoleDao;
import com.example.bootstoreproject.dao.UserDao;
import com.example.bootstoreproject.entity.Role;
import com.example.bootstoreproject.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    public final UserDao userDao;
    public final RoleDao roleDao;
    private final PasswordEncoder  passwordEncoder;

    public UserService(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void signUp(User user){
        Role role = new Role();
        role.setName("ROLE_PURCHASE_USER");
        Role userRole = roleDao.findRoleByName("ROLE_PURCHASE_USER").orElse(role);
        user.addRole(userRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
    }
}
