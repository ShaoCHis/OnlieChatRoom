package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.utils.Result;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.log4j.Logger;

public class UserServiceTest extends DemoApplicationTests {

  private static Logger logger = Logger.getLogger(com.example.demo.Test.class);
  private static String testerName="ST";
  @Autowired
  private UserService userService;

  @Test
  public void UT_001_001(){
    try {
      Assert.assertFalse(userService.isPresent(""));
      // 输出日志
      logger.info("UT_001_001测试通过\t测试人员："+testerName);
    }
    catch (AssertionError failure){
      // 输出日志
      logger.error("UT_001_001测试不通过\t测试人员："+testerName);
      throw failure;
    }
  }

  @Test
  public void UT_001_002(){
    try {
      Assert.assertFalse(userService.isPresent("1123"));
      // 输出日志
      logger.info("UT_001_002测试通过\t测试人员："+testerName);
    }
    catch (AssertionError failure){
      // 输出日志
      logger.error("UT_001_002测试不通过\t测试人员："+testerName);
      throw failure;
    }
  }

  @Test
  public void UT_001_003(){
    try {
      Assert.assertTrue(userService.isPresent("LCF"));
      // 输出日志
      logger.info("UT_001_003测试通过\t测试人员："+testerName);
    }
    catch (AssertionError failure){
      // 输出日志
      logger.error("UT_001_003测试不通过\t测试人员："+testerName);
      throw failure;
    }
  }

  @Test
  public void UT_002_001() throws Exception {
    User user = new User();
    user.setId("LCF");
    Result<User> result = userService.register(user);
    if (result.isSuccess()){
      logger.error("UT_002_002测试不通过\t测试人员："+testerName);
      throw new Exception();
    }else {
      // 输出日志
      logger.info("UT_002_002测试通过\t测试人员："+testerName);
    }
  }

  @Test
  public void UT_002_002() throws Exception {
    User user = new User();
    user.setId("1234567");
    user.setPassword("1234567");
    user.setSex("男");
    user.setAge(18);
    Result<User> result = userService.register(user);
    if (result.isSuccess()){
      logger.info("UT_002_002测试通过\t测试人员："+testerName);
    }else {
      // 输出日志
      logger.error("UT_002_002测试不通过\t测试人员："+testerName);
      throw new Exception();
    }
  }

  @Test
  public void UT_002_003() throws Exception {
    User user = new User();
    user.setId("LCF");
    Result<User> result = userService.register(user);
    if (result.isSuccess()){
      logger.error("UT_002_003测试不通过\t测试人员："+testerName);
      throw new Exception();
    }else {
      // 输出日志
      logger.info("UT_002_003测试通过\t测试人员："+testerName);
    }
  }

  @Test
  public void UT_003_001() throws Exception {
    User user = new User();
    user.setId("");
    user.setPassword("");
    Result<User> result = userService.login(user);
    if (result.isSuccess()){
      logger.error("UT_003_001测试不通过\t测试人员："+testerName);
      throw new Exception();
    }else {
      // 输出日志
      logger.info("UT_003_001测试通过\t测试人员："+testerName);
    }
  }

  @Test
  public void UT_003_002() throws Exception {
    User user = new User();
    user.setId("dadadadda");
    user.setPassword("");
    Result<User> result = userService.login(user);
    System.out.println(result.isSuccess());
    if (result.isSuccess()){
      logger.error("UT_003_002测试不通过\t测试人员："+testerName);
      throw new Exception();
    }else {
      // 输出日志
      logger.info("UT_003_002测试通过\t测试人员："+testerName);
    }
  }

  @Test
  public void UT_003_003() throws Exception {
    User user = new User();
    user.setId("LCF");
    user.setPassword("11");
    Result<User> result = userService.login(user);
    if (result.isSuccess()){
      logger.info("UT_003_003测试通过\t测试人员："+testerName);
    }else {
      // 输出日志
      logger.error("UT_003_003测试不通过\t测试人员："+testerName);
      throw new Exception();
    }
  }

  @Test
  public void UT_003_004() throws Exception {
    User user = new User();
    user.setId("LCF");
    user.setPassword("12321");
    Result<User> result = userService.login(user);
    if (result.isSuccess()){
      logger.error("UT_003_003测试不通过\t测试人员："+testerName);
      throw new Exception();
    }else {
      // 输出日志
      logger.info("UT_003_003测试通过\t测试人员："+testerName);
    }
  }

  @Test
  public void UT_004_001() throws Exception {
    User user = new User();
    user.setId("");
    user.setPassword("");
    Result<User> result = userService.updateUserInformation(user);
    if (result.isSuccess()){
      logger.error("UT_004_001测试不通过\t测试人员："+testerName);
      throw new Exception();
    }else {
      // 输出日志
      logger.info("UT_004_001测试通过\t测试人员："+testerName);
    }
  }

  @Test
  public void UT_004_002() throws Exception {
    User user = new User();
    user.setId("LCF");
    user.setPassword("11");
    user.setAge(-1);
    user.setSex("男");
    Result<User> result = userService.updateUserInformation(user);
    if (result.isSuccess()){
      logger.error("UT_004_002测试不通过\t测试人员："+testerName);
      throw new Exception();
    }else {
      // 输出日志
      logger.info("UT_004_002测试通过\t测试人员："+testerName);
    }
  }

  @Test
  public void UT_004_003() throws Exception {
    User user = new User();
    user.setId("LCF");
    user.setPassword("11");
    user.setAge(18);
    user.setSex("男");
    Result<User> result = userService.updateUserInformation(user);
    if (result.isSuccess()){
      logger.info("UT_004_003测试通过\t测试人员："+testerName);
    }else {
      // 输出日志
      logger.error("UT_004_003测试不通过\t测试人员："+testerName);
      throw new Exception();
    }
  }
  @Test
  public void UT_004_004() throws Exception {
    User user = new User();
    user.setId("LCF");
    user.setPassword("11");
    user.setAge(20);
    user.setSex("a");
    Result<User> result = userService.updateUserInformation(user);
    if (result.isSuccess()){
      logger.error("UT_004_004测试不通过\t测试人员："+testerName);
      throw new Exception();
    }else {
      // 输出日志
      logger.info("UT_004_004测试通过\t测试人员："+testerName);
    }
  }
}
