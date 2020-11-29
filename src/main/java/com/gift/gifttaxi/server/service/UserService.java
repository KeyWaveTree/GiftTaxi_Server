package com.gift.gifttaxi.server.service;

import com.gift.gifttaxi.server.model.UserEntity;
import com.gift.gifttaxi.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(UserEntity user) {
        this.userRepository.save(user);
    }

    public UserEntity findUserById(long id) {
        Optional<UserEntity> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    public UserEntity authenticate(String phone, String password) {
        Optional<UserEntity> user = this.userRepository.findByPhone(phone);
        if (user.isPresent()) {
            UserEntity loginUser = user.get();
            if (loginUser.password == password) {
                return loginUser;
            }
        }
        return null;
    }
}