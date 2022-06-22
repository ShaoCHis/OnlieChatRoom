package com.example.demo;

import com.example.demo.service.UserService;
import com.example.demo.view.AllMessage;
import com.example.demo.view.FriendMessage;
import com.example.demo.view.MakeFriends;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MessageServiceTest extends DemoApplicationTests{
  private static Logger logger = Logger.getLogger(com.example.demo.Test.class);
  private static String testerName="LCF";
  @Autowired
  private UserService userService;

  @Test
  public void UT_008_001() throws Exception {
    AllMessage allMessage = new AllMessage();
    allMessage.setUserId("LCF");
    allMessage.setMessage("");
    if (userService.saveAllMessage(allMessage).isSuccess()){
      logger.error("UT_008_001测试不通过\t测试人员："+testerName);
      throw new Exception();
    }else {
      // 输出日志
      logger.info("UT_008_001测试通过\t测试人员："+testerName);
    }
  }

  @Test
  public void UT_008_002() throws Exception {
    AllMessage allMessage = new AllMessage();
    allMessage.setUserId("adasdasdas");
    allMessage.setMessage("hello");
    if (userService.saveAllMessage(allMessage).isSuccess()){
      logger.error("UT_008_002测试不通过\t测试人员："+testerName);
      throw new Exception();
    }else {
      // 输出日志
      logger.info("UT_008_002测试通过\t测试人员："+testerName);
    }
  }

  @Test
  public void UT_008_003() throws Exception {
    AllMessage allMessage = new AllMessage();
    allMessage.setUserId("LCF");
    allMessage.setMessage("hello");
    if (userService.saveAllMessage(allMessage).isSuccess()){
      // 输出日志
      logger.info("UT_008_003测试通过\t测试人员："+testerName);
    }else {
      logger.error("UT_008_003测试不通过\t测试人员："+testerName);
      throw new Exception();
    }
  }

  @Test
  public void UT_009_001() throws Exception {
    FriendMessage friendMessage = new FriendMessage();
    friendMessage.setFirstUser("");
    friendMessage.setSecondUser("LCF");
    friendMessage.setMessage("");
    if (userService.saveFriendMessage(friendMessage).isSuccess()){
      logger.error("UT_009_001测试不通过\t测试人员："+testerName);
      throw new Exception();
    }else {
      // 输出日志
      logger.info("UT_009_001测试通过\t测试人员："+testerName);
    }
  }

  @Test
  public void UT_009_002() throws Exception {
    FriendMessage friendMessage = new FriendMessage();
    friendMessage.setFirstUser("ymh");
    friendMessage.setSecondUser("st");
    friendMessage.setMessage("hello");
    if (userService.saveFriendMessage(friendMessage).isSuccess()){
      logger.error("UT_009_002测试不通过\t测试人员："+testerName);
      throw new Exception();
    }else {
      // 输出日志
      logger.info("UT_009_002测试通过\t测试人员："+testerName);
    }
  }

  @Test
  public void UT_009_003() throws Exception {
    FriendMessage friendMessage = new FriendMessage();
    friendMessage.setFirstUser("123456");
    friendMessage.setSecondUser("ShaoCHi");
    friendMessage.setMessage("hello");
    if (userService.saveFriendMessage(friendMessage).isSuccess()){
      logger.error("UT_009_003测试不通过\t测试人员："+testerName);
      throw new Exception();
    }else {
      // 输出日志
      logger.info("UT_009_003测试通过\t测试人员："+testerName);
    }
  }

  @Test
  public void UT_009_004() throws Exception {
    FriendMessage friendMessage = new FriendMessage();
    friendMessage.setFirstUser("ShaoCHi");
    friendMessage.setSecondUser("LCF");
    friendMessage.setMessage("hello");
    if (userService.saveFriendMessage(friendMessage).isSuccess()){
      // 输出日志
      logger.info("UT_009_004测试通过\t测试人员："+testerName);
    }else {
      logger.error("UT_009_004测试不通过\t测试人员："+testerName);
      throw new Exception();
    }
  }

  @Test
  public void UT_010_001() throws Exception {
    if (userService.getAllMessage().isSuccess()){
      // 输出日志
      logger.info("UT_010_001测试通过\t测试人员："+testerName);
    }else {
      logger.error("UT_010_001测试不通过\t测试人员："+testerName);
      throw new Exception();
    }
  }

  @Test
  public void UT_011_001() throws Exception {
    MakeFriends makeFriends = new MakeFriends();
    makeFriends.setFirstUser("");
    makeFriends.setSecondUser("LCF");
    if (userService.getFriendMessages(makeFriends).isSuccess()){
      logger.error("UT_011_001测试不通过\t测试人员："+testerName);
      throw new Exception();
    }else {
      // 输出日志
      logger.info("UT_011_001测试通过\t测试人员："+testerName);
    }
  }

  @Test
  public void UT_011_002() throws Exception {
    MakeFriends makeFriends = new MakeFriends();
    makeFriends.setFirstUser("ymh");
    makeFriends.setSecondUser("LCF");
    if (userService.getFriendMessages(makeFriends).isSuccess()){
      logger.error("UT_011_001测试不通过\t测试人员："+testerName);
      throw new Exception();
    }else {
      // 输出日志
      logger.info("UT_011_001测试通过\t测试人员："+testerName);
    }
  }

  @Test
  public void UT_011_003() throws Exception {
    MakeFriends makeFriends = new MakeFriends();
    makeFriends.setFirstUser("123456");
    makeFriends.setSecondUser("LCF");
    if (userService.getFriendMessages(makeFriends).isSuccess()){
      logger.error("UT_011_003测试不通过\t测试人员："+testerName);
      throw new Exception();
    }else {
      // 输出日志
      logger.info("UT_011_003测试通过\t测试人员："+testerName);
    }
  }

  @Test
  public void UT_011_004() throws Exception {
    MakeFriends makeFriends = new MakeFriends();
    makeFriends.setFirstUser("ShaoCHi");
    makeFriends.setSecondUser("LCF");
    if (userService.getFriendMessages(makeFriends).isSuccess()){
      // 输出日志
      logger.info("UT_011_004测试通过\t测试人员："+testerName);
    }else {
      logger.error("UT_011_004测试不通过\t测试人员："+testerName);
      throw new Exception();
    }
  }
}
