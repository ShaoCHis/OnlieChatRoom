package com.example.demo.controller;/**
 * Created By ShaoCHi
 * Date 2021/12/19 2:00 PM
 * Tongji University
 */

import com.example.demo.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author ShaoCHi
 * Date 2021/12/19 2:00 PM
 * Tongji University
 */

@RequestMapping(path = "test")
@RestController
public class TestController {

  @GetMapping(path = "test")
  public Result<String> test(){
    Result<String> result=new Result<>();
    result.setMessage("This is a Test!");
    return result;
  }
}
