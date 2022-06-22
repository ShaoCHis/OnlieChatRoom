package com.example.demo.service;

import com.example.demo.model.Friends;
import com.example.demo.model.TotalMessage;
import com.example.demo.model.User;
import com.example.demo.repository.FriendMessageRepository;
import com.example.demo.repository.FriendsRepository;
import com.example.demo.repository.TotalMessageRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.Result;
import com.example.demo.view.AllMessage;
import com.example.demo.view.FriendMessage;
import com.example.demo.view.FriendsList;
import com.example.demo.view.MakeFriends;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * author ShaoCHi
 * Date 2021/12/19 7:40 PM
 * Tongji University
 */

@Service
public class UserService {
  @Autowired
  UserRepository userRepository;

  @Autowired
  FriendsRepository friendsRepository;

  @Autowired
  TotalMessageRepository totalMessageRepository;

  @Autowired
  FriendMessageRepository friendMessageRepository;

  public boolean isPresent(String userId) {
    if (userRepository.findById(userId).isPresent())
      return true;
    else
      return false;
  }

  public Result<User> register(User body) {
    if (isPresent(body.getId())){
      return Result.wrapErrorResult("用户已经存在！");
    }
    userRepository.save(body);
    return Result.wrapSuccessfulResult(body);
  }


  public Result<User> login(User body) {
    Optional<User> user = userRepository.findById(body.getId());
    if (user.isPresent()){
      if (Objects.equals(user.get().getPassword(), body.getPassword())) {
        return Result.wrapSuccessfulResult(user.get());
      } else {
        return Result.wrapErrorResult("密码错误！");
      }
    }else{
      return Result.wrapErrorResult("用户不存在！");
    }
  }

  public Result<List<FriendsList>> getFriends(String id) {
    Optional<User> optionalUser = userRepository.findById(id);
    if (!optionalUser.isPresent()){
      return Result.wrapErrorResult("用户不存在！");
    }
    User user = userRepository.findById(id).get();
    //由于Friends表里面存有两个用户的信息，所以得拿两个多对一的关系进行查询
    Set<Friends> friendsFirstSet = user.getFirstFriendsSet();
    Set<Friends> friendsSecondSet = user.getSecondFriendsSet();
    //由于使用的两个外码对应不同的值，所以得对其进行遍历然后加入返回信息中
    List<FriendsList> userList = new LinkedList<>();
    //需要对应两个关联关系
    if (friendsFirstSet.size() != 0) {
      for (Friends friends : friendsFirstSet) {
        FriendsList friendsList=new FriendsList();
        friendsList.setUserId(userRepository.findById(friends.getSecondUser().getId()).get().getId());
        friendsList.setUrl(userRepository.findById(friends.getSecondUser().getId()).get().getUrl());
        userList.add(friendsList);
      }
    }
    if(friendsSecondSet.size()!=0) {
      for (Friends friends : friendsSecondSet) {
        FriendsList friendsList=new FriendsList();
        friendsList.setUserId(userRepository.findById(friends.getFirstUser().getId()).get().getId());
        friendsList.setUrl(userRepository.findById(friends.getFirstUser().getId()).get().getUrl());
        userList.add(friendsList);
      }
    }
    return Result.wrapSuccessfulResult(userList);
  }

  public Result<String> makeFriends(MakeFriends body) {
    if (!userRepository.findById(body.getFirstUser()).isPresent()||!userRepository.findById(body.getSecondUser()).isPresent()){
      return Result.wrapErrorResult("用户不存在，请重试！");
    }
    Iterable<Friends> friendsIterable = friendsRepository.findAll();
    for (Friends friends : friendsIterable) {
      if ((Objects.equals(friends.getFirstUser().getId(), body.getFirstUser())
              && Objects.equals(friends.getSecondUser().getId(), body.getSecondUser())) ||
              (Objects.equals(friends.getFirstUser().getId(), body.getSecondUser())
                      && Objects.equals(friends.getSecondUser().getId(), body.getFirstUser()))) {
        return Result.wrapErrorResult("已经是好友，无法再次添加！");
      }
    }
    Friends friends = new Friends();
    friends.setFirstUser(userRepository.findById(body.getFirstUser()).get());
    friends.setSecondUser(userRepository.findById(body.getSecondUser()).get());
    friendsRepository.save(friends);
    return Result.wrapSuccessfulResult("添加好友成功！");
  }


  public Result<String> deleteFriends(MakeFriends body) {
    if (!userRepository.findById(body.getFirstUser()).isPresent()||!userRepository.findById(body.getSecondUser()).isPresent()){
      return Result.wrapErrorResult("用户不存在，请重试！");
    }
    Iterable<Friends> friendsIterable = friendsRepository.findAll();
    for (Friends friends : friendsIterable) {
      if ((Objects.equals(friends.getFirstUser().getId(), body.getFirstUser())
              && Objects.equals(friends.getSecondUser().getId(), body.getSecondUser())) ||
              (Objects.equals(friends.getFirstUser().getId(), body.getSecondUser())
                      && Objects.equals(friends.getSecondUser().getId(), body.getFirstUser()))) {
        friendsRepository.delete(friends);
        return Result.wrapSuccessfulResult("删除好友成功！");
      }
    }
    return Result.wrapErrorResult("用户尚未成为好友！");
  }

  public Result<String> saveAllMessage(AllMessage body) {
    if (!userRepository.findById(body.getUserId()).isPresent()){
      return Result.wrapErrorResult("用户不存在！");
    }else if(body.getMessage().length()==0){
      return Result.wrapErrorResult("用户信息不能为空！");
    }
    TotalMessage totalMessage = new TotalMessage();
    totalMessage.setMessage(body.getMessage());
    totalMessage.setTime(new Date());
    totalMessage.setUser(userRepository.findById(body.getUserId()).get());
    totalMessageRepository.save(totalMessage);
    return Result.wrapSuccessfulResult("存储成功！");
  }

  public Result<String> saveFriendMessage(FriendMessage body) {
    if (!userRepository.findById(body.getFirstUser()).isPresent()||!userRepository.findById(body.getSecondUser()).isPresent()){
      return Result.wrapErrorResult("用户不存在，请重试！");
    }else if(body.getMessage().length()==0){
      return Result.wrapErrorResult("信息不能为空！");
    }
    Iterable<Friends> friendsIterable = friendsRepository.findAll();
    for (Friends friends : friendsIterable) {
      if ((Objects.equals(friends.getFirstUser().getId(), body.getFirstUser())
              && Objects.equals(friends.getSecondUser().getId(), body.getSecondUser())) ||
              (Objects.equals(friends.getFirstUser().getId(), body.getSecondUser())
                      && Objects.equals(friends.getSecondUser().getId(), body.getFirstUser()))) {
        com.example.demo.model.FriendMessage friendMessage = new com.example.demo.model.FriendMessage();
        friendMessage.setMessage(body.getMessage());
        friendMessage.setFirstUser(userRepository.findById(body.getFirstUser()).get());
        friendMessage.setSecondUser(userRepository.findById(body.getSecondUser()).get());
        friendMessage.setTime(new Date());
        friendMessageRepository.save(friendMessage);
        return Result.wrapSuccessfulResult("存储成功！");
      }
    }
    return Result.wrapErrorResult("用户尚未成为好友！");
  }

  public Result<List<AllMessage>> getAllMessage() {
    List<TotalMessage> allMessages = totalMessageRepository.findAll();
    List<AllMessage> allMessageList = new LinkedList<>();
    for (TotalMessage totalMessage : allMessages) {
      AllMessage allMessage = new AllMessage();
      allMessage.setMessage(totalMessage.getMessage());
      allMessage.setUserId(totalMessage.getUser().getId());
      allMessage.setDate(totalMessage.getTime());
      allMessageList.add(allMessage);
    }
    allMessageList.sort(Comparator.comparingLong(AllMessage::getTime));
    return Result.wrapSuccessfulResult(allMessageList);
  }

  public Result<List<FriendMessage>> getFriendMessages(MakeFriends body) {
    if (!userRepository.findById(body.getFirstUser()).isPresent()||!userRepository.findById(body.getSecondUser()).isPresent()){
      return Result.wrapErrorResult("用户不存在，请重试！");
    }
    Iterable<Friends> friendsIterable = friendsRepository.findAll();
    for (Friends friends : friendsIterable) {
      if ((Objects.equals(friends.getFirstUser().getId(), body.getFirstUser())
              && Objects.equals(friends.getSecondUser().getId(), body.getSecondUser())) ||
              (Objects.equals(friends.getFirstUser().getId(), body.getSecondUser())
                      && Objects.equals(friends.getSecondUser().getId(), body.getFirstUser()))) {
        List<com.example.demo.model.FriendMessage> friendMessages = friendMessageRepository.findAll();
        List<FriendMessage> friendMessageList = new LinkedList<>();
        for (com.example.demo.model.FriendMessage friendMessage : friendMessages) {
          //为了区分发送消息方与接收方，得分开判断
          if ((Objects.equals(friendMessage.getFirstUser().getId(), body.getFirstUser())
                  && Objects.equals(friendMessage.getSecondUser().getId(), body.getSecondUser()))
                  || (Objects.equals(friendMessage.getSecondUser().getId(), body.getFirstUser())
                  && Objects.equals(friendMessage.getFirstUser().getId(), body.getSecondUser()))) {
            FriendMessage newFriendMessage = new FriendMessage();
            newFriendMessage.setMessage(friendMessage.getMessage());
            newFriendMessage.setFirstUser(friendMessage.getFirstUser().getId());
            newFriendMessage.setSecondUser(friendMessage.getSecondUser().getId());
            newFriendMessage.setDate(friendMessage.getTime());
            friendMessageList.add(newFriendMessage);
          }
        }
        friendMessageList.sort(Comparator.comparingLong(FriendMessage::getTime));
        return Result.wrapSuccessfulResult(friendMessageList);
      }
    }
    return Result.wrapErrorResult("用户尚未成为好友！");
  }

  public Result<User> updateUserInformation(User user) {
    if(!isPresent(user.getId())){
      return Result.wrapErrorResult("用户不存在！");
    }else if (user.getAge()<0){
      return Result.wrapErrorResult("年龄输入不合法");
    }else if(!Objects.equals(user.getSex(), "男") && !Objects.equals(user.getSex(), "女")){
      return Result.wrapErrorResult("性别输入不合法");
    }
    User nowUser=userRepository.findById(user.getId()).get();
    nowUser.setAge(user.getAge());
    nowUser.setPassword(user.getPassword());
    nowUser.setSex(user.getSex());
    userRepository.save(nowUser);
    return Result.wrapSuccessfulResult(user);
  }
}
