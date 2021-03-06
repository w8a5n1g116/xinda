package net.stopno.xingda.demo.service;

import java.util.List;
import java.util.Map;


public interface IBaseService {
    List<Map<String,Object>> getPlan(String userCode,String searchString);

    List<Map<String,Object>> getParam(String userCode,String productId,String orderNo,String processName,String paramName,String queryType);

    Map<String,Object> getUser(String userName,String passwordMD5);

    Integer report(String scOrderCode,String selfEval, String gxCode,String userCode,String id,String fxbz,String forder);

    Integer reportUnqualified(String scOrderCode,String selfEval,String judgeProgress,String submitUserCode,String selfSpRe,String notType,String id,String fxbz,String forder,String fixModel);

    Integer setReportable(String scOrderCode, String gxCode);

    List<String> getUnqualifiedType();

    List<String> getFXRoute(String productId);
}
