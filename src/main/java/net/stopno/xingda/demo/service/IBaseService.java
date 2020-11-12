package net.stopno.xingda.demo.service;

import java.util.List;
import java.util.Map;


public interface IBaseService {
    List<Map<String,Object>> getPlan(String userCode,String searchString);

    List<Map<String,Object>> getParam(String userCode,String productId,String orderNo,String processName,String paramName,String queryType);

    Map<String,Object> getUser(String userName,String passwordMD5);

    int report(String scOrderCode, String gxCode,String userCode);

    int setReportable(String scOrderCode, String gxCode);

    List<String> getUnqualifiedType();

    List<String> getFXRoute(String productId);
}
