package com.backend.server.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserController {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @PostMapping(value = "/islogin" )
    @ResponseBody
    public Boolean sign(@RequestParam("email") String username,
                        @RequestParam("password") String password){
        String sql = "select password from user_main where Email = "+" \""+username+" \"";
        String real = jdbcTemplate.queryForObject(sql, String.class);
        if(real.equals(password))
            return true;
        else
            return false;
    }

}
