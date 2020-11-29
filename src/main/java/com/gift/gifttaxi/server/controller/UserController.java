package com.gift.gifttaxi.server.controller;

import com.gift.gifttaxi.server.dto.LoginDto;
import com.gift.gifttaxi.server.dto.LoginResultDto;
import com.gift.gifttaxi.server.dto.UserDto;
import com.gift.gifttaxi.server.model.UserEntity;
import com.gift.gifttaxi.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@Controller("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(UserDto dto) {
        UserEntity user = new UserEntity();
        user.name = dto.name;
        user.phone = dto.phone;
        user.password = dto.password;

        this.userService.addUser(user);
    }

    @PostMapping("/login")
    public LoginResultDto login(LoginDto dto) {
        UserEntity result = this.userService.authenticate(dto.phone, dto.password);
        if (result == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        LoginResultDto resultDto = new LoginResultDto();
        resultDto.userId = result.id;
        return resultDto;
    }
}
