package com.example.demo.controller;

import com.example.demo.redisson.RedissonDistributedLocker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestContorller {
    @Autowired
    private  RedissonDistributedLocker locker;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @RequestMapping("/lock")
    public void test1(){
        String key =  "stock";
        try{
            locker.lock("key",3);
            //业务逻辑
            String stock = redisTemplate.opsForValue().get(key);
            System.out.println("当前的库存为"+stock);
            if(Integer.parseInt(stock)>=1){
                Integer i = Integer.parseInt(stock)-1;
                redisTemplate.opsForValue().set(key,String.valueOf(i));
            }else {
                System.out.println("库存不足！");
            }
        }catch (Exception e){
                e.printStackTrace();
        }finally {
            //最后执行解锁
            locker.unlock("key");
        }

    }

    @RequestMapping("/index")
    public String main(){
        return "index.jsp";
    }


}
