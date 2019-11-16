package com.itheima.mobile.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * 验证码发送
 *
 * @author wangxin
 * @version 1.0
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 体检预约-发送验证码 send4Order
     */
    @RequestMapping(value = "/send4Order", method = RequestMethod.POST)
    public Result send4Order(String telephone) {
        //1.生成验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        //2.调用短信接口
        try {
            if(false) {
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code.toString());
            }
            System.out.println("手机号码：：：：："+telephone+"::::::验证码::"+code.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //3.将短信验证码 存入redis (后续提交预约的时候要进行校验)
        //key=001_13322223333 value:1234  体检预约
        //key=002_13322223333 value:6677  快速登录
        //key=003_13322223333 value:9999   忘记密码
        jedisPool.getResource().setex(RedisMessageConstant.SENDTYPE_ORDER+"_"+telephone,5*60,code.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
    /**
     * 登录-发送验证码
     * @param telephone
     * @return
     */
    @RequestMapping(value = "/send4Login", method = RequestMethod.POST)
    public Result send4Login(String telephone) {
        //1.生成验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        //2.调用短信接口
        try {
            if(false) {
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code.toString());
            }
            System.out.println("手机号码：：：：："+telephone+"::::::验证码::"+code.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        jedisPool.getResource().setex(RedisMessageConstant.SENDTYPE_LOGIN+"_"+telephone,5*60,code.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
