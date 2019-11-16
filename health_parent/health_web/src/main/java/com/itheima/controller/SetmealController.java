package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import com.itheima.service.CheckGroupService;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;

/**
 * 套餐控制层
 *
 * @author wangxin
 * @version 1.0
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;
    @Autowired
    private JedisPool jedisPool;

    /**
     * 图片上传七牛云
     * 回显图片（将文件名返回即可）
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Result upload(@RequestParam("imgFile")MultipartFile multipartFile) {
        try {
            String originalFilename = multipartFile.getOriginalFilename();//原始文件名
            String suffx = originalFilename.substring(originalFilename.lastIndexOf("."));//后缀
            //随机产生一个不重复的文件名
            String newFileName = UUID.randomUUID().toString()+suffx;//新文件名
            //上传文件
            QiniuUtils.upload2Qiniu(multipartFile.getBytes(),newFileName);

            //将上传图片的记录到redis
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,newFileName);
            //回显图片 将图片名称 设置到result对象中
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,newFileName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }


    /**
     * 新增套餐
     * @param setmeal
     * @param groupIds
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody Setmeal setmeal, Integer[] groupIds) {
        //调用service服务
        try {
            setmealService.add(setmeal,groupIds);
            //新增套餐成功后将记录写入redis
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 套餐分页
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping(value = "/findPage", method = RequestMethod.POST)
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        //调用service服务
        try {
            PageResult pageResult = setmealService.findPage(queryPageBean.getPageSize(), queryPageBean.getCurrentPage(), queryPageBean.getQueryString());
            return pageResult;//直接返回分页数据
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
