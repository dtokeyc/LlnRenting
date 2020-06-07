package com.backend.server.controller;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ItemConroller {
    String img="http://gepfdu.natappfree.cc/img/";
    @Resource
    private JdbcTemplate jdbcTemplate;
    @PostMapping(value = "/publish" )
    public Boolean ispublish(@RequestBody Map params)
    {
        List<Map> list = (List<Map>) params.get("photos");
        String photo="";
        for(Map tp:list)
        {
            photo+=tp.get("url")+";";
        }
        String sql ="insert into item (title,id_post, kind,price,photos,description,available ,time) value " +
                "( \""+params.get("title")+"\","+params.get("id_post")+","+params.get("kind")+","+params.get("price")+",\""+photo+"\""+",\""+params.get("discribe")+"\","+1+",\""+params.get("time")+"\""+")";
        //System.out.println(sql);
        try{
            jdbcTemplate.update(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @PostMapping(value = "/item_detail" )
    public String item_detail(@RequestBody Map params)
    {
        //+" \""+username+" \""
        String sql ="select * from item where ID ="+params.get("item_id");
        JSONObject jo = null;
        //System.out.println();
        try {
            jo = new JSONObject(jdbcTemplate.queryForList(sql).get(0));
            String photo = (String) jo.get("photos");
            String [] a = photo.split(";");
            JSONArray aj = new JSONArray();
            for(int i=0;i<a.length;i++)
            {
                JSONObject tp= new JSONObject();
                tp.put("url",img+a[i]);
                aj.put(tp);
            }
            jo.put("photos",aj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jo.toString();
    }

    @PostMapping(value = "/list" )
    public String item_list(@RequestBody Map params)
    {
        String sql;
        String fff= (String) params.get("type");
        if(!fff.equals("0"))
            sql="select title,photos,price,time from item where kind ="+params.get("type");
        else
            sql="select title,photos,price,time from item ";
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
        JSONObject jo= new JSONObject();
        JSONArray aj = new JSONArray();
        for(Map tp:data){
            JSONObject fk= new JSONObject();
            try {
                String s[] =((String)tp.get("photos")).split(";");
                fk.put("name",tp.get("title"));
                fk.put("image_url",img+s[0]);
                fk.put("price",tp.get("price"));
                fk.put("time",tp.get("time"));
                aj.put(fk);
            } catch (JSONException e) {
                e.printStackTrace();
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


    @PostMapping(value = "/list_id" )
    public String item_list_id(@RequestBody Map params)
    {
        String sql;
        sql="select title,photos,price,time from item where id_post ="+params.get("id");
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
        JSONObject jo= new JSONObject();
        JSONArray aj = new JSONArray();
        for(Map tp:data){
            JSONObject fk= new JSONObject();
            try {
                String s[] =((String)tp.get("photos")).split(";");
                fk.put("name",tp.get("title"));
                fk.put("image_url",img+s[0]);
                fk.put("price",tp.get("price"));
                fk.put("time",tp.get("time"));
                aj.put(fk);
            } catch (JSONException e) {
                e.printStackTrace();
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

    @PostMapping(value = "/list_find" )
    public String item_list_find(@RequestBody Map params)
    {
        String sql;
        sql="select title,photos,price,time from item where title like \"%"+params.get("key")+"%\"";
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
        JSONObject jo= new JSONObject();
        JSONArray aj = new JSONArray();
        for(Map tp:data){
            JSONObject fk= new JSONObject();
            try {
                String s[] =((String)tp.get("photos")).split(";");
                fk.put("name",tp.get("title"));
                fk.put("image_url",img+s[0]);
                fk.put("price",tp.get("price"));
                fk.put("time",tp.get("time"));
                aj.put(fk);
            } catch (JSONException e) {
                e.printStackTrace();
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

    @PostMapping(value = "/collect_id" )
    public String item_list_findconllection(@RequestBody Map params)
    {
        String sql;
        sql="select title,photos,price,time from item where ID in (select item_id from collection where user_id = "+params.get("id")+")";
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
        JSONObject jo= new JSONObject();
        JSONArray aj = new JSONArray();
        for(Map tp:data){
            JSONObject fk= new JSONObject();
            try {
                String s[] =((String)tp.get("photos")).split(";");
                fk.put("name",tp.get("title"));
                fk.put("image_url",img+s[0]);
                fk.put("price",tp.get("price"));
                fk.put("time",tp.get("time"));
                aj.put(fk);
            } catch (JSONException e) {
                e.printStackTrace();
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

    @PostMapping(value = "/collect" )
    public boolean item_con(@RequestBody Map params)
    {
        String sql="insert into collection (user_id,item_id) value ("+params.get("user_id")+","+params.get("item_id")+")";
        try{
            jdbcTemplate.update(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
