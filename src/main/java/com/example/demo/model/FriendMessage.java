package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

/**
 * author ShaoCHi
 * Date 2021/12/19 7:20 PM
 * Tongji University
 */

@Entity
@Getter
@Setter
public class FriendMessage {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer Id;

  private Date time;

  private String message;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "first_user")
  private User firstUser;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="second_user")
  private User secondUser;
}
