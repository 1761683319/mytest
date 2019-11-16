package com.itheima.mobile.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 移动端 -手机号快速登录
 *
 * @author wangxin
 * @version 1.0
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;

    /**
     * 手机号码快速登录
     */
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public Result login(@RequestBody Map map, HttpServletResponse response) {
        // 1.根据手机号码获取验证码 发送验证码短信
        // 调用短信接口 将验证码存入redis
        // 2.输入验证码登录，后台校验验证码
        //         redis取出和用户输入的对比
        //{"validateCode":"11111","telephone":"13311122233"}
        String validateCode = (String) map.get("validateCode");
        String telephone = (String) map.get("telephone");
        if(StringUtils.isEmpty(telephone) || StringUtils.isEmpty(validateCode)){
            return new Result(false,MessageConstant.PARAM_FAIL);
        }
        String redisCode = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone);
        if(StringUtils.isEmpty(redisCode) || !validateCode.equals(redisCode)){
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }
        // 3.根据手机号码查询member会员表是否存在
        Member member = memberService.findByTelephone(telephone);
        // 4.存在，通过cookie将用户信息写入。
        if(member == null){
            // 5.不存在，则往member表插入记录。
            //自动注册后也要 将用户信息写入cookie
            Member dbMember = new Member();
            dbMember.setPhoneNumber(telephone);
            dbMember.setRegTime(new Date());
            memberService.add(dbMember);
        }
        Cookie cookie = new Cookie("login_member_telephone",telephone);
        cookie.setPath("/");//路径 所有路径下的页面都可以使用此cookie
        cookie.setMaxAge(30*60*60*24);///设置30天有效
        response.addCookie(cookie);
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
}
