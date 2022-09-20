package com.sxzq.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author liwei
 * @date 2022/9/19 17:01
 */
@RestController
public class IndexController {

    @GetMapping(value = "/index")
    public String index(){
        return "hello " + new Date();
    }
}
