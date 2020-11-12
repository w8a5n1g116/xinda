package net.stopno.xingda.demo.controller;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import net.stopno.xingda.demo.service.IBaseService;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "")
public class BaseController  {
    @Autowired
    private IBaseService iBaseService;

    @Autowired
    private HttpSession session;

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/planList")
    public String planList(){
        return "planList";
    }

    @RequestMapping("/paramList")
    public String paramList(){
        return "paramList";
    }

    @RequestMapping("/checkParam")
    public ModelAndView checkParam(String scOrderCode,String userCode,String productName,String processName,String paramName,String productId){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("checkParam");
        String queryType = "条件筛选";
        List<Map<String,Object>> params = iBaseService.getParam(userCode,productName,scOrderCode,processName,paramName,queryType);
        Map<String,Object> param = params.get(0);
        if(param.size() != 0){
            mv.addObject("paramModel",param);
        }
        List<String> unqualifiedTypeList = iBaseService.getUnqualifiedType();
        mv.addObject("unqualifiedTypeList",unqualifiedTypeList);
        List<String> fxRouteList = iBaseService.getFXRoute(productId);
        mv.addObject("fxRouteList",fxRouteList);

        return mv;
    }



    @RequestMapping(value = "/loginPost",method = RequestMethod.POST)
    @ResponseBody
    public String lgoinPost(String userName, String password){
        String passwordMD5 = SecureUtil.md5(password);
        Map<String,Object> user = iBaseService.getUser(userName,passwordMD5);
        session.setAttribute("user",user);
        return JSON.toJSONString(user);
    }

    @RequestMapping(value = "/getPlan")
    @ResponseBody
    public String getPlan(String userCode,String searchString){
        List<Map<String,Object>> plan = iBaseService.getPlan(userCode,searchString);
        return JSON.toJSONString(plan);
    }

    @RequestMapping(value = "/getParam")
    @ResponseBody
    public String getParam(String userCode, String productId, String orderNo, String processName, String paramName){
        String queryType = "条件筛选";
        if(productId == null) {  productId = ""; }
        if(orderNo == null) {  orderNo = "SCH"; }
        if(processName == null) {  processName = ""; }
        if(paramName == null) {  paramName = ""; }
        List<Map<String,Object>> param = iBaseService.getParam(userCode,productId,orderNo,processName,paramName,queryType);
        return JSON.toJSONString(param);
    }

    @RequestMapping(value = "/setReportable")
    @ResponseBody
    public String setReportable(String scOrderCode, String gxCode){

            int ret = iBaseService.setReportable(scOrderCode, gxCode);
            if(ret != 0){
                return JSON.toJSONString(true);
            }else{
                return JSON.toJSONString(false);
            }
    }

    @RequestMapping(value = "/report")
    @ResponseBody
    public String report(String scOrderCode, String gxCode,String userCode){
        try{
            int ret = iBaseService.report(scOrderCode, gxCode, userCode);
            return JSON.toJSONString(true);
        }catch (Exception e){
            return JSON.toJSONString(false);
        }
    }


}
