package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * author ShaoCHi
 * Date 2021/12/19 6:56 PM
 * Tongji University
 */

@Entity
@Getter
@Setter
public class User {
  @Id
  private String userId;

  private String password;

  private String sex;

  private Integer age;

  private String url;

  @OneToMany(mappedBy = "firstUser",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
  Set<Friends> firstFriendsSet;

  @OneToMany(mappedBy = "secondUser",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
  Set<Friends> secondFriendsSet;

  @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
  Set<TotalMessage> totalMessageSet;

  @OneToMany(mappedBy = "firstUser",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
  Set<FriendMessage> firstFriendMessages;

  @OneToMany(mappedBy = "secondUser",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
  Set<FriendMessage> secondFriendMessages;
}
