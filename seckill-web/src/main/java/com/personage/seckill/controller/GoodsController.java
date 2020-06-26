package com.personage.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.personage.seckill.model.Goods;
import com.personage.seckill.service.GoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping("/")
    public String index(Model model){
        List<Goods> goodsList = goodsService.selectAllGoods();
        model.addAttribute("goodsList", goodsList);
        return "goodsList";
    }

    @RequestMapping("/showGoodsInfo/{goodsId}")
    public String showGoodsInfo(@PathVariable("goodsId") Integer goodsId,Model model){

        Goods goods = goodsService.getGoodsById(goodsId);
        model.addAttribute("goods", goods);
        return "goodsInfo";
    }
}
