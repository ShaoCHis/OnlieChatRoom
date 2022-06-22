package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.utils.Result;
import com.example.demo.view.MakeFriends;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FriendServiceTest extends DemoApplicationTests{
  private static Logger logger = Logger.getLogger(com.example.demo.Test.class);
  private static String testerName="LCF";
  @Autowired
  private UserService userService;

  @Test
  public void UT_005_001() throws Exception {
    String id = "";
    if (userService.getFriends(id).isSuccess()){
      logger.error("UT_005_001测试不通过\t测试人员："+testerName);
      throw new Exception();
    }else {
      // 输出日志
      logger.info("UT_005_001测试通过\t测试人员："+testerName);
    }
  }

  @Test
  public void UT_005_002() throws Exception {
    String id = "dhakdhak";
    if (userService.getFriends(id).isSuccess()){
      logger.error("UT_005_002测试不通过\t测试人员："+testerName);
      throw new Exception();
    }else {
      // 输出日志
      logger.info("UT_005_002测试通过\t测试人员："+testerName);
    }
  }

  @Test
  public void UT_005_003() throws Exception {
    String id = "LCF";
    if (userService.getFriends(id).isSuccess()){
      // 输出日志
      logger.info("UT_005_003测试通过\t测试人员："+testerName);
    }else {
      logger.error("UT_005_003测试不通过\t测试人员："+testerName);
      throw new Exception();
    }
  }

  @Test
  public void UT_006_001() throws Exception {
    MakeFriends makeFriends = new MakeFriends();
    makeFriends.setFirstUser("");
    makeFriends.setSecondUser("");
    if (userService.makeFriends(makeFriends).isSuccess()){
      logger.error("UT_006_001测试不通过\t测试人员："+testerName);
      throw new Exception();
    }else {
      // 输出日志
      logger.info("UT_006_001测试通过\t测试人员："+testerName);
    }
  }

  @Test
  public void UT_006_002() throws Exception {
    MakeFriends makeFriends = new MakeFriends();
    makeFriends.setFirstUser("ymh");
    makeFriends.setSecondUser("");
    if (userService.makeFriends(makeFriends).isSuccess()){
      logger.error("UT_006_002测试不通过\t测试人员："+testerName);
      throw new Exception();
    }else {
      // 输出日志
      logger.info("UT_006_002测试通过\t测试人员："+testerName);
    }
  }

  @Test
  public void UT_006_003() throws Exception {
    MakeFriends makeFriends = new MakeFriends();
    makeFriends.setFirstUser("ShaoCHi");
    makeFriends.setSecondUser("LCF");
    if (userService.makeFriends(makeFriends).isSuccess()){
      logger.error("UT_006_003测试不通过\t测试人员："+testerName);
      throw new Exception();
    }else {
      // 输出日志
      logger.info("UT_006_003测试通过\t测试人员："+testerName);
    }
  }

  @Test
  public void UT_006_004() throws Exception {
    MakeFriends makeFriends = new MakeFriends();
    makeFriends.setFirstUser("ShaoCHi");
    makeFriends.setSecondUser("st");
    if (userService.makeFriends(makeFriends).isSuccess()){
      // 输出日志
      logger.info("UT_006_004测试通过\t测试人员："+testerName);
    }else {
      logger.error("UT_006_004测试不通过\t测试人员："+testerName);
      throw new Exception();
    }
  }

  @Test
  public void UT_007_001() throws Exception {
    MakeFriends makeFriends = new MakeFriends();
    makeFriends.setFirstUser("");
    makeFriends.setSecondUser("");
    if (userService.deleteFriends(makeFriends).isSuccess()){
      logger.error("UT_007_001测试不通过\t测试人员："+testerName);
      throw new Exception();
    }else {
      // 输出日志
      logger.info("UT_007_001测试通过\t测试人员："+testerName);
    }
  }

  @Test
  public void UT_007_002() throws Exception {
    MakeFriends makeFriends = new MakeFriends();
    makeFriends.setFirstUser("ymh");
    makeFriends.setSecondUser("lcf");
    if (userService.deleteFriends(makeFriends).isSuccess()){
      logger.error("UT_007_002测试不通过\t测试人员："+testerName);
      throw new Exception();
    }else {
      // 输出日志
      logger.info("UT_007_002测试通过\t测试人员："+testerName);
    }
  }

  @Test
  public void UT_007_003() throws Exception {
    MakeFriends makeFriends = new MakeFriends();
    makeFriends.setFirstUser("ShaoCHi");
    makeFriends.setSecondUser("123456");
    if (userService.deleteFriends(makeFriends).isSuccess()){
      logger.error("UT_007_003测试不通过\t测试人员："+testerName);
      throw new Exception();
    }else {
      // 输出日志
      logger.info("UT_007_003测试通过\t测试人员："+testerName);
    }
  }

  @Test
  public void UT_007_004() throws Exception {
    MakeFriends makeFriends = new MakeFriends();
    makeFriends.setFirstUser("ShaoCHi");
    makeFriends.setSecondUser("st");
    if (userService.deleteFriends(makeFriends).isSuccess()){
      // 输出日志
      logger.info("UT_007_004测试通过\t测试人员："+testerName);
    }else {
      logger.error("UT_007_004测试不通过\t测试人员："+testerName);
      throw new Exception();
    }
  }
}
