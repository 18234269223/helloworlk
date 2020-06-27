package com.personage.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.personage.seckill.commons.Constants;
import com.personage.seckill.commons.ReturnObject;
import com.personage.seckill.model.Goods;
import com.personage.seckill.service.GoodsService;
import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * ClassName:GoodsController
 * Package:com.personage.seckill.controller
 * Description:
 *
 * @date:2020/6/26 14:52
 * @author:xxx
 */
@Controller
public class GoodsController {

    @Reference
    private GoodsService goodsService;


    //获取秒杀的产品
    @RequestMapping("/")
    public String index(Model model){
        List<Goods> goodsList = goodsService.selectAllGoods();
        model.addAttribute("goodsList", goodsList);
        return "goodsList";
    }

    //产品详情
    @RequestMapping("/showGoodsInfo/{goodsId}")
    public String showGoodsInfo(@PathVariable("goodsId") Integer goodsId,Model model){

        Goods goods = goodsService.getGoodsById(goodsId);
        model.addAttribute("goods", goods);
        return "goodsInfo";
    }

    //获取秒杀开始的时间
    @RequestMapping("/getSystemTime")
    public @ResponseBody Object getSystemTime(){
        ReturnObject returnObject = new ReturnObject();
        returnObject.setCode(Constants.OK);
        returnObject.setMessage("获取时间成功");
        returnObject.setResult(System.currentTimeMillis());
        return returnObject;
    }

    //执行秒杀按钮操作逻辑
    @RequestMapping("/getRandomName/{goodsId}")
    public @ResponseBody Object getRandomName(@PathVariable("goodsId") Integer goodsId){
        ReturnObject returnObject = new ReturnObject();
        Goods goods = goodsService.getGoodsById(goodsId);
        //防止黑客手动拼接请求
        if (goods==null){
            returnObject.setCode(Constants.ERROR);
            returnObject.setMessage("对不起，没有这个产品");
            returnObject.setResult("");
            return returnObject;
        }
        //判断秒杀时间
        Long newData = System.currentTimeMillis();
        if (newData<goods.getStarttime().getTime()){
            returnObject.setCode(Constants.ERROR);
            returnObject.setMessage("对不起，秒杀时间未开始");
            returnObject.setResult("");
            return returnObject;
        }
        if (newData>goods.getEndtime().getTime()){
            returnObject.setCode(Constants.ERROR);
            returnObject.setMessage("对不起，秒杀活动已结束");
            returnObject.setResult("");
            return returnObject;
        }
        //获取商品随机名
        if (goods.getRandomname()==null||"".equals(goods.getRandomname())){
            returnObject.setCode(Constants.ERROR);
            returnObject.setMessage("对不起，信息有误");
            returnObject.setResult("");
            return returnObject;
        }

        returnObject.setCode(Constants.OK);
        returnObject.setMessage("获取随机名成功");
        returnObject.setResult(goods.getRandomname());
        return returnObject;
    }

    @RequestMapping("/secKill/{goodsId}/{randomName}")
    public @ResponseBody Object secKill(@PathVariable("goodsId") Integer goodsId,@PathVariable("randomName") long randomName){
        Integer userId = 1;
        ReturnObject returnObject=goodsService.seckill(goodsId,randomName,userId);
        return returnObject;
    }

}
