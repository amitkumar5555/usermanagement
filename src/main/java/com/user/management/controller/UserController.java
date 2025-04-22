package com.user.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.management.model.User;
import com.user.management.service.UserService;

@RestController
@RequestMapping("/user/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Validated User user){
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping("/getUserDetails/{id}")
    private ResponseEntity<?> getUserDetails(@PathVariable String id){
        return ResponseEntity.ok(userService.getUserDetails(id));
    }

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody @Validated User user){
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @DeleteMapping("/deleteUser/{id}")
    private ResponseEntity<?> deleteUser(@PathVariable String id){
        return ResponseEntity.ok(userService.deleteUser(id));
    }

}
