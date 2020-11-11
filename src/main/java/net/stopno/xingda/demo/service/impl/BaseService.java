package net.stopno.xingda.demo.service.impl;

import net.stopno.xingda.demo.mapper.BaseMapper;
import net.stopno.xingda.demo.service.IBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BaseService implements IBaseService {
    @Autowired
    private BaseMapper baseMapper;

    @Override
    public List<Map<String,Object>> getPlan(String userCode){
        return baseMapper.getPlan(userCode);
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
}
