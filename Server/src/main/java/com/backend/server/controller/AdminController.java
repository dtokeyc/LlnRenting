package com.backend.server.controller;

import org.apache.catalina.Server;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.rmi.ServerException;
import java.util.List;
import java.util.Map;

@RestController
public class AdminController {
    String img="http://gepfdu.natappfree.cc/img/";
    @Resource
    private JdbcTemplate jdbcTemplate;

    @PostMapping(value = "/admin_add" )
    public String addAdmin(@RequestBody Map params) throws ServerException {
        String sql ="insert into admin (username,password,status) value " +
                "( \""+params.get("name")+"\","+"\""+params.get("password")+"\","+"0"+")";
        JSONObject jo= new JSONObject();
        try{
            jdbcTemplate.update(sql);
            jo.put("success","true");
        } catch (Exception e) {
            throw new ServerException("已经存在这名管理员了哦！");
        }
        return jo.toString();
    }

    @PostMapping(value = "/admin_change" )
    public String changeAdmin(@RequestBody Map params) throws ServerException {
        String sql = "select password from admin where username = "+" \""+params.get("name")+" \"";
        String real="-";
        JSONObject jo= new JSONObject();
        try{
            real = jdbcTemplate.queryForObject(sql, String.class);
            if(real.equals(params.get("password"))){
                sql = "update admin set password="+"\""+params.get("newpassword")+"\""+"where username = "+" \""+params.get("name")+" \"";
                jdbcTemplate.update(sql);
                jo.put("success","true");
            }
            else{
                throw new ServerException("密码似乎有点不对哦！");
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw new ServerException("这个我们没法处理！");
        }

        return jo.toString();
    }

    @PostMapping(value = "/admin_del" )
    public String delAdmin(@RequestBody Map params) throws ServerException {
        String sql = "select password from admin where username = "+" \""+params.get("name")+" \"";
        String real="-";
        JSONObject jo= new JSONObject();
        try{
            real = jdbcTemplate.queryForObject(sql, String.class);
            if(real.equals(params.get("password"))){
                sql = "update admin set status="+"\"1\""+"where username = "+" \""+params.get("name")+" \"";
                jdbcTemplate.update(sql);
                jo.put("success","true");
            }
            else{
                throw new ServerException("密码似乎有点不对哦！");
            }
        }catch (Exception e) {
            throw new ServerException("这个我们没法处理！");
        }

        return jo.toString();
    }

    @PostMapping(value = "/admin_login" )
    public String loginAdmin(@RequestBody Map params) throws ServerException {
        String sql = "select password from admin where username = "+" \""+params.get("name")+" \"";
        String real="-";
        JSONObject jo= new JSONObject();

        try{
            real = jdbcTemplate.queryForObject(sql, String.class);
            if(real.equals(params.get("password"))){
                jo.put("success","true");
            }
            else{
                throw new ServerException("密码似乎有点不对哦！");
            }
        }catch (Exception e) {
            throw new ServerException("这个我们没法处理！");
        }

        return jo.toString();
    }

    @PostMapping(value = "/admin_info" )
    public String seeAdmin(@RequestBody Map params) throws ServerException {
        String sql = "select password from admin where username = "+" \""+params.get("name")+" \"";
        String real="-";
        JSONObject jo= new JSONObject();

        try{
            real = jdbcTemplate.queryForObject(sql, String.class);
            if(real.equals(null)){
                throw new ServerException("不存在这名用户哦！");
            }
            else{
                jo.put("name",params.get("name"));
                jo.put("password",real);
            }
        }catch (Exception e) {
            throw new ServerException("这个我们没法处理！");
        }

        return jo.toString();
    }

    @PostMapping(value = "/admin_check_item_list" )
    public String itemlistAdmin(@RequestBody Map params) throws ServerException {
        String sql="select title,photos,price,time from item where available = 1";//寻找未审核的物品
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
        JSONObject jo= new JSONObject();
        JSONArray aj = new JSONArray();
        for(Map tp:data){
            JSONObject fk= new JSONObject();
            try {
                String s[] =((String)tp.get("photos")).split(";");
                fk.put("item_id",tp.get("ID"));
                fk.put("name",tp.get("title"));
                fk.put("image_url",img+s[0]);
                fk.put("price",tp.get("price"));
                fk.put("time",tp.get("time"));
                fk.put("description",tp.get("description"));
                aj.put(fk);
            } catch (JSONException e) {
                e.printStackTrace();
                throw new ServerException("这个我们没法处理！");
            }
        }
        try {
            jo.put("size",data.size());
            jo.put("list",aj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo.toString();
    }

    @PostMapping(value = "/admin_check_item" )
    public String checkitemAdmin(@RequestBody Map params) throws ServerException {
        String sql = "update item set available = 0 where ID = "+" \""+params.get("item_id")+" \"";
        JSONObject jo= new JSONObject();

        try{
            int row = jdbcTemplate.update(sql);
            if(row==0){
                throw new ServerException("不存在这个物品哦！");
            }
            else{
                jo.put("success","true");
            }
        }catch (Exception e) {
            throw new ServerException("这个我们没法处理！");
        }

        return jo.toString();
    }

    @PostMapping(value = "/admin_check_shopping_log" )
    public String logAdmin(@RequestBody Map params) throws ServerException {
        String sql;
        String shaixuan= (String) params.get("kind");
        if(shaixuan.equals("user")){
            sql ="select * from deal where id_borrow = "+"\""+params.get("user_id")+"\"";//租方ID筛选
        }
        else if(shaixuan.equals("item")){
            sql="select * from deal where id_item = "+"\""+params.get("item_id")+"\"";//物品ID筛选
        }
        else if(shaixuan.equals("time")){
            sql="select * from deal where time_back = "+"\""+params.get("time")+"\"";//归还时间筛选
        }
        else {
            sql="select * from deal";//全部
        }

        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
        JSONObject jo= new JSONObject();
        JSONArray aj = new JSONArray();
        for(Map tp:data){
            JSONObject fk= new JSONObject();
            try {
                fk.put("ID",tp.get("ID"));
                fk.put("time_reserve",tp.get("time_reserve"));
                fk.put("time_back",tp.get("time_back"));
                fk.put("id_borrow",tp.get("id_borrow"));
                fk.put("id_use",tp.get("id_use"));
                fk.put("id_item",tp.get("id_item"));
                fk.put("appraisal_item",tp.get("appraisal_item"));
                fk.put("appraisal_borrow",tp.get("appraisal_borrow"));
                fk.put("appraisal_use",tp.get("appraisal_use"));
                fk.put("cost",tp.get("cost"));
                fk.put("status",tp.get("status"));
                aj.put(fk);
            } catch (JSONException e) {
                e.printStackTrace();
                throw new ServerException("这个我们没法处理！");
            }
        }
        try {
            jo.put("size",data.size());
            jo.put("list",aj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo.toString();
    }

    @PostMapping(value = "/admin_change_price" )//修改推荐价格
    public String changepriceAdmin(@RequestBody Map params) throws ServerException {
        String sql = "update item_suggest_price set suggest_price = "+"\""+params.get("price")+"\""+" where kind = "+" \""+params.get("kind")+" \"";
        JSONObject jo= new JSONObject();
        try{
            int row = jdbcTemplate.update(sql);
            if(row==0){
                throw new ServerException("不存在这个类别哦！");
            }
            else{
                jo.put("success","true");
            }
        }catch (Exception e) {
            throw new ServerException("这个我们没法处理！");
        }

        return jo.toString();
    }

    @PostMapping(value = "/admin_change_user_status" )
    public String changestatusmAdmin(@RequestBody Map params) throws ServerException {
        String sql = "update user_main set status = "+"\""+params.get("status")+"\""+" where ID = "+" \""+params.get("user_id")+" \"";
        JSONObject jo= new JSONObject();

        try{
            int row = jdbcTemplate.update(sql);
            if(row==0){
                throw new ServerException("不存在这个用户哦！");
            }
            else{
                jo.put("success","true");
            }
        }catch (Exception e) {
            throw new ServerException("这个我们没法处理！");
        }

        return jo.toString();
    }






}
