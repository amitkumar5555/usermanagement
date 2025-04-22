package com.user.management.service.impl;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.user.management.dto.BaseResponseDto;
import com.user.management.model.User;
import com.user.management.repository.UserRepository;
import com.user.management.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @SuppressWarnings("unused")
    @Override
    public Object createUser(User user) {
        BaseResponseDto dto;
        User savedUser = userRepository.save(user);
        if (Objects.nonNull(savedUser)) {
            dto = BaseResponseDto.builder().status(HttpStatus.CREATED.value()).result(savedUser).build();
        } else {
            dto = BaseResponseDto.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error while creating user").build();
        }
        return dto;
    }

    @Override
    public Object getUserDetails(String userId) {
        BaseResponseDto dto;
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent() && Objects.nonNull(user.get())) {
            dto = BaseResponseDto.builder().status(HttpStatus.OK.value()).result(user.get()).build();
        } else {
            dto = BaseResponseDto.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("user not found on id : " + userId).build();
        }
        return dto;

    }

    @SuppressWarnings("unused")
    @Override
    public Object updateUser(User user) {
        BaseResponseDto dto;
        Optional<User> userById = userRepository.findById(user.getId());
        if(userById.isPresent()){
            User updatedUser = userRepository.save(user);
            if (Objects.nonNull(updatedUser)) {
                dto = BaseResponseDto.builder().status(HttpStatus.CREATED.value()).result(updatedUser).build();
            } else {
                dto = BaseResponseDto.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("Error while updating user").build();
            }
        }else{
            dto = BaseResponseDto.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .message("user not found on given id : "+user.getId()).build();
        }
       
        return dto;
    }

    @Override
    public Object deleteUser(String userId) {
        BaseResponseDto dto;
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent() && Objects.nonNull(user.get())) {
            userRepository.deleteById(userId);
            dto = BaseResponseDto.builder().status(HttpStatus.OK.value()).result("user deleted successfull").build();
        } else {
            dto = BaseResponseDto.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("user not found on id : " + userId).build();
        }
        return dto;
    }

}
