package com.backend.server.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.rmi.ServerException;
import java.util.Map;

@RestController
public class UserController {
    @Resource
    private JdbcTemplate jdbcTemplate;

//    @PostMapping(value = "/islogin" )
//    @ResponseBody
//    public Boolean sign(@RequestParam("email") String username,
//                        @RequestParam("password") String password){
//        String sql = "select password from user_main where Email = "+" \""+username+" \"";
//        String real = jdbcTemplate.queryForObject(sql, String.class);
//        if(real.equals(password))
//            return true;
//        else
//            return false;
//    }


    @PostMapping(value = "/islogin" ,produces="application/json;charset=UTF-8")
    public String sign(@RequestBody Map params) throws ServerException {
        String sql = "select password from user_main where Email = "+" \""+params.get("username")+" \"";
        String real="-";
        JSONObject jo= new JSONObject();
        try{
            real = jdbcTemplate.queryForObject(sql, String.class);
        }catch (Exception E) {
            ServerException s=new ServerException("这个我们没法处理！");

            throw new ServerException("这个我们没法处理！");
        }
        if(real.equals(params.get("password"))) {
            try {
                jo.put("result","success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            throw new ServerException("密码似乎有点不对哦！");
        }

        return jo.toString();
    }

//    @PostMapping(value = "/issignup")
//    public Boolean signup(@RequestParam("email") String username,
//                          @RequestParam("password") String password,
//                          @RequestParam("sno") String sno){
//            String sql ="insert into user_main (Email, password,sno) value " +
//                    "( \""+username+"\","+"\""+password+"\","+sno+")";
//            try{
//                jdbcTemplate.update(sql);
//                return true;
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//    }

    @PostMapping(value = "/issignup",produces="application/json;charset=UTF-8")
    public String signup(@RequestBody Map params) throws ServerException {
            String username = (String) params.get("username");
            String password = (String) params.get("password");
            String sno = (String) params.get("sno");
            String sql ="insert into user_main (Email, password,sno) value " +
                    "( \""+username+"\","+"\""+password+"\","+sno+")";
            JSONObject jo= new JSONObject();
            try{
                jdbcTemplate.update(sql);
                jo.put("result","success");
            } catch (Exception E) {
                throw new ServerException("不太行~，如果邮箱没有重复的话，应该是学号被盗用了，请私信我们。");
            }
            return jo.toString();
    }

}
