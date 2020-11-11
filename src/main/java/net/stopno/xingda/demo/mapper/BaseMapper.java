package net.stopno.xingda.demo.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.StatementType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface BaseMapper {

    @Select("select top 1 *  from Tab_MES_BaseManage_UserInfo where UserCode= #{userName} and Password= #{password}")
    Map<String,Object> getUserByUserNameAndPassword(String userName,String password);

    @SelectProvider(method = "getPlanByUserCode",type = BaseMapperProvider.class)
    List<Map<String,Object>> getPlan(String userCode);

    @Select("exec Pro_MES_QualityManage_SelfAssessment_Lists "
            +"@用户号 = #{userCode,mode=IN,jdbcType=VARCHAR},"
            +"@产品名称 = #{productId,mode=IN,jdbcType=VARCHAR},"
            +"@生产订单号 = #{orderNo,mode=IN,jdbcType=VARCHAR},"
            +"@工序名称 = #{processName,mode=IN,jdbcType=VARCHAR},"
            +"@参数名称 = #{paramName,mode=IN,jdbcType=VARCHAR},"
            +"@查询类别 = #{queryType,mode=IN,jdbcType=VARCHAR}"
            )
    @Options(statementType = StatementType.CALLABLE)
    List<Map<String,Object>> getParam(String userCode,String productId,String orderNo,String processName,String paramName,String queryType);

    class BaseMapperProvider{
        public String getPlanByUserCode(String userCode){
            String sql = "";
            if(userCode.equals("system")){
                sql = "select  *  from [MES].[dbo].[View_Mes_ScInfo_Gxhb_List] where 1=1  and IsReport is NULL ";
            }else{
                sql = "select  *  from [MES].[dbo].[View_Mes_ScInfo_Gxhb_List] where  Gx_Name in (select distinct gx_Name from Tab_MES_BaseManage_Gx_User  where UserCode='" + userCode + "' ) and IsReport is NULL";
            }
            return sql;
        }
    }
}
