package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * author ShaoCHi
 * Date 2021/12/19 7:37 PM
 * Tongji University
 */

@RestController
@RequestMapping(path = "/api/users")
public class UserController {
  @Autowired
  UserService userService;

  @PostMapping(path = "/register")
  Result<User> register(@RequestBody User body){
    if(userService.isPresent(body.getUserId())){
      return Result.wrapErrorResult("用户已经存在！");
    }
    return userService.register(body);
  }

  @PostMapping(path = "/session")
  Result<String> login(@RequestBody User body){
    if(!userService.isPresent(body.getUserId())){
      return Result.wrapErrorResult("用户不存在！");
    }
    return userService.login(body);
  }

  @PostMapping(path = "/getFriends/{id}")
  Result<List<User>> getFriends(@PathVariable String id){
    if(!userService.isPresent(id)){
      return Result.wrapErrorResult("用户不存在！");
    }
    return userService.getFriends(id);
  }
}
