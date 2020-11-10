package net.stopno.xingda.demo.controller;

import com.alibaba.fastjson.JSON;
import net.stopno.xingda.demo.service.IBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "")
public class BaseController  {
    @Autowired
    private IBaseService iBaseService;

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping(value = "/getPlan")
    @ResponseBody
    public String getPlan(String userCode){
        List<Map<String,Object>> plan = iBaseService.getPlan(userCode);
        return JSON.toJSONString(plan);
    }

    @RequestMapping(value = "/getParam")
    @ResponseBody
    public String getParam(String userCode, String productId, String orderNo, String processName, String paramName){
        String queryType = "条件筛选";
        if(productId == null)   productId = "";
        if(orderNo == null)   orderNo = "";
        if(processName == null)   processName = "";
        if(paramName == null)   paramName = "";
        List<Map<String,Object>> param = iBaseService.getParam(userCode,productId,orderNo,processName,paramName,queryType);
        return JSON.toJSONString(param);
    }
}
