package com.example.demo.view;/**
 * Created By ShaoCHi
 * Date 2021/12/19 10:49 PM
 * Tongji University
 */

import lombok.Data;

import java.util.Date;

/**
 * author ShaoCHi
 * Date 2021/12/19 10:49 PM
 * Tongji University
 */
@Data
public class AllMessage {
  private String message;

  private String userId;

  //date是便于后面获取消息时进行类的服用，请求时不需要传递
  private Date date;

  public Long getTime(){
    return this.date.getTime();
  }
}
