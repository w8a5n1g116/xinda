package net.stopno.xingda.demo.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import net.stopno.xingda.demo.mapper.BaseMapper;
import net.stopno.xingda.demo.service.IBaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BaseService implements IBaseService {
    @Autowired
    private BaseMapper baseMapper;

    @Override
    public List<Map<String,Object>> getPlan(String userCode,String searchString){
        return baseMapper.getPlan(userCode,searchString);
    }

    @Override
    public List<Map<String, Object>> getParam(String userCode, String productId, String orderNo, String processName, String paramName, String queryType) {
        return baseMapper.getParam(userCode,productId,orderNo,processName,paramName,queryType);
    }

    @Override
    public Map<String,Object> getUser(String userName,String passwordMD5) {
        Map<String,Object> user = baseMapper.getUserByUserNameAndPassword(userName,passwordMD5);
        return user;

    }

    @Override
    public Integer report(String scOrderCode,String selfEval, String gxCode, String userCode,String id,String fxbz,String forder) {
        baseMapper.setParamQualified(selfEval,userCode,id);
        if(StrUtil.isNotEmpty(fxbz)){
            String[] strArray = fxbz.split("_");
            String mainProcessName = strArray[0];
            Integer fixCount = Convert.toInt(strArray[1].substring(0,1));
            baseMapper.reportFix(scOrderCode,fixCount,forder,mainProcessName,"合格");
        }
        return baseMapper.report(scOrderCode, gxCode, userCode);
    }

    @Override
    public Integer reportUnqualified(String scOrderCode,String selfEval,String judgeProgress,String submitUserCode,String selfSpRe,String notType,String id,String fxbz,String forder,String fixModel) {
        baseMapper.setParamUnqualified(selfEval,judgeProgress,submitUserCode,selfSpRe,notType,id,fixModel);
        baseMapper.addjudgeProcess(id,"1");
//        if(selfSpRe.equals("返修")){
//            if(StrUtil.isNotEmpty(fxbz)){
//                String[] strArray = fxbz.split("_");
//                String mainProcessName = strArray[0];
//                Integer fixCount = Convert.toInt(strArray[1].substring(0,1));
//                baseMapper.reportFix(scOrderCode,fixCount,forder,mainProcessName,"返修");
//                return baseMapper.insertFixSecondEvaluteResults(scOrderCode,mainProcessName);
//            }else{
//                return baseMapper.insertFixFirstEvaluteResults(id);
//            }
//        }else if(selfSpRe.equals("报废")){
//            return baseMapper.reportScrap(id,submitUserCode);
//        }else{
//            if(StrUtil.isNotEmpty(fxbz)){
//                String[] strArray = fxbz.split("_");
//                String mainProcessName = strArray[0];
//                Integer fixCount = Convert.toInt(strArray[1].substring(0,1));
//                return baseMapper.reportFix(scOrderCode,fixCount,forder,mainProcessName,"合格");
//            }else{
//                return 0;
//            }
//        }
        return 0;

    }

    @Override
    public Integer setReportable(String scOrderCode, String gxCode) {
        return baseMapper.setReportable(scOrderCode, gxCode);
    }

    @Override
    public List<String> getUnqualifiedType() {
        return baseMapper.getUnqualifiedType();
    }

    @Override
    public List<String> getFXRoute(String productId) {
        return baseMapper.getFXRoute(productId);
    }
}
