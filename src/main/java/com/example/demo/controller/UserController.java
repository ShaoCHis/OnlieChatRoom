package com.example.demo.controller;

import com.example.demo.model.TotalMessage;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.utils.Result;
import com.example.demo.view.AllMessage;
import com.example.demo.view.FriendMessage;
import com.example.demo.view.FriendsList;
import com.example.demo.view.MakeFriends;
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
  Result<User> register(@RequestBody User body) {
    if (userService.isPresent(body.getId())) {
      return Result.wrapErrorResult("用户已经存在！");
    }
    return userService.register(body);
  }

  @PostMapping(path = "/session")
  Result<User> login(@RequestBody User body) {
    if (!userService.isPresent(body.getId())) {
      System.out.println(userService.isPresent(body.getId()));
      return Result.wrapErrorResult("用户不存在！");
    }
    return userService.login(body);
  }

  @GetMapping(path = "/getFriends/{id}")
  Result<List<FriendsList>> getFriends(@PathVariable String id) {
    if (!userService.isPresent(id)) {
      return Result.wrapErrorResult("用户不存在！");
    }
    return userService.getFriends(id);
  }

  @PostMapping(path = "/update")
  Result<User> updateUserInformation(@RequestBody User user){
    if(!userService.isPresent(user.getId())){
      return Result.wrapErrorResult("用户不存在！");
    }else if (user.getAge()<0){
      return Result.wrapErrorResult("年龄输入不合法");
    }else if(user.getSex() != "男"&&user.getSex() != "女"){
      return Result.wrapErrorResult("性别输入不合法");
    }
    return userService.updateUserInformation(user);
  }

  @PostMapping(path = "/makeFriends")
  Result<String> makeFriends(@RequestBody MakeFriends body) {
    if (!userService.isPresent(body.getFirstUser())) {
      return Result.wrapErrorResult("用户不存在！");
    } else if (!userService.isPresent(body.getSecondUser())) {
      return Result.wrapErrorResult("用户不存在！");
    } else {
      return userService.makeFriends(body);
    }
  }

  @PostMapping(path = "/deleteFriends")
  Result<String> deleteFriends(@RequestBody MakeFriends body) {
    if (!userService.isPresent(body.getFirstUser())) {
      return Result.wrapErrorResult("用户不存在！");
    } else if (!userService.isPresent(body.getSecondUser())) {
      return Result.wrapErrorResult("用户不存在！");
    } else {
      return userService.deleteFriends(body);
    }
  }

  @PostMapping(path = "/saveAllMessage")
  Result<String> saveAllMessage(@RequestBody AllMessage body) {
    if (!userService.isPresent(body.getUserId())) {
      return Result.wrapErrorResult("用户不存在！");
    }
    return userService.saveAllMessage(body);
  }

  @PostMapping(path = "/saveFriendMessage")
  Result<String> saveFriendMessage(@RequestBody FriendMessage body) {
    if (!userService.isPresent(body.getFirstUser())) {
      return Result.wrapErrorResult("用户不存在！");
    } else if (!userService.isPresent(body.getSecondUser())) {
      return Result.wrapErrorResult("用户不存在！");
    } else {
      return userService.saveFriendMessage(body);
    }
  }

  @GetMapping(path = "/getAllMessage")
  Result<List<AllMessage>> getAllMessage() {
    return userService.getAllMessage();
  }

  @PostMapping(path = "/getFriendMessage")
  Result<List<FriendMessage>> getFriendMessages(@RequestBody MakeFriends body) {
    if (!userService.isPresent(body.getFirstUser())) {
      return Result.wrapErrorResult("用户不存在！");
    } else if (!userService.isPresent(body.getSecondUser())) {
      return Result.wrapErrorResult("用户不存在！");
    } else {
      return userService.getFriendMessages(body);
    }
  }
}
