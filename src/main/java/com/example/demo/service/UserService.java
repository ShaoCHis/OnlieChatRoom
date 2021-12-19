package com.example.demo.service;

import com.example.demo.model.Friends;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * author ShaoCHi
 * Date 2021/12/19 7:40 PM
 * Tongji University
 */

@Service
public class UserService {
  @Autowired
  UserRepository userRepository;

  public boolean isPresent(String userId){
    if(userRepository.findById(userId).isPresent())
      return true;
    else
      return false;
  }

  public Result<User> register(User body) {
    userRepository.save(body);
    return Result.wrapSuccessfulResult(body);
  }


  public Result<String> login(User body) {
    User user=userRepository.findById(body.getUserId()).get();
    if(user.getPassword()==body.getPassword()){
      return Result.wrapSuccessfulResult("登陆成功！");
    }
    else
    {
      return Result.wrapErrorResult("账户名或密码错误！");
    }
  }

  public Result<List<User>> getFriends(String id) {
    User user=userRepository.findById(id).get();
    //由于Friends表里面存有两个用户的信息，所以得拿两个多对一的关系进行查询
    Set<Friends> friendsFirstSet=user.getFirstFriendsSet();
    Set<Friends> friendsSecondSet=user.getSecondFriendsSet();
    List<User> userList=new LinkedList<>();
    for(Friends friends:friendsFirstSet){
      userList.add(userRepository.findById(friends.getFirstUser().getUserId()).get());
    }
    for(Friends friends:friendsSecondSet){
      userList.add(userRepository.findById(friends.getSecondUser().getUserId()).get());
    }
    return Result.wrapSuccessfulResult(userList);
  }
}
