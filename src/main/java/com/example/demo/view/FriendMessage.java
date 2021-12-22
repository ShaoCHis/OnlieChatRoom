package com.example.demo.view;/**
 * Created By ShaoCHi
 * Date 2021/12/19 10:55 PM
 * Tongji University
 */

import lombok.Data;

import java.util.Date;

/**
 * author ShaoCHi
 * Date 2021/12/19 10:55 PM
 * Tongji University
 */
@Data
public class FriendMessage {
  private String message;

  private String firstUser;

  private String secondUser;

  //date是便于后面获取消息时进行类的服用，请求时不需要传递
  private Date date;

  public Long getTime(){
    return this.date.getTime();
  }
}
