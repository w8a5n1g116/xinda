package net.stopno.xingda.demo.service;

import java.util.List;
import java.util.Map;


public interface IBaseService {
    List<Map<String,Object>> getPlan(String userCode);

    List<Map<String,Object>> getParam(String userCode,String productId,String orderNo,String processName,String paramName,String queryType);

    Map<String,Object> getUser(String userName,String passwordMD5);
}
