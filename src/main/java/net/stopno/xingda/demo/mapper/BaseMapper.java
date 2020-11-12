package net.stopno.xingda.demo.mapper;

import org.apache.commons.lang3.StringUtils;
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
    List<Map<String,Object>> getPlan(String userCode,String searchString);

    @Select("exec Pro_MES_ScManage_Gxhb_App "
            +"@scdd = #{scOrderCode,mode=IN,jdbcType=VARCHAR},"
            +"@gxdm = #{gxCode,mode=IN,jdbcType=VARCHAR},"
            +"@hbrCode = #{userCode,mode=IN,jdbcType=VARCHAR}"
            )
    @Options(statementType = StatementType.CALLABLE)
    int report(String scOrderCode, String gxCode,String userCode);

    @Update("update [dbo].[Tab_MES_ScInfo_Xc_Zx] set IsReport = '是' where ScOrder_Code = #{scOrderCode} and Gx_Code = #{gxCode}")
    int setReportable(String scOrderCode, String gxCode);

    @Select("select S_Value FROM [MES].[dbo].[Tab_MES_BaseManage_Sys_Edit]  where S_Key = '质量不合类别'")
    List<String> getUnqualifiedType();

    @Select("select distinct Fx_Category from  [MES].[dbo].[Tab_MES_BaseManage_FxGxInfo] where  Cp_Category in \n" +
            "(select top 1 Category  from [MES].[dbo].[Tab_MES_BaseManage_ProductInfo]  where Productid = #{productId})")
    List<String> getFXRoute(String productId);

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
        public String getPlanByUserCode(String userCode,String searchString){
            String sql = "";
            if(userCode.equals("system")){
                sql = "select  *  from [MES].[dbo].[View_Mes_ScInfo_Gxhb_List] where 1=1  and IsReport is NULL ";
            }else{
                sql = "select  *  from [MES].[dbo].[View_Mes_ScInfo_Gxhb_List] where  Gx_Name in (select distinct gx_Name from Tab_MES_BaseManage_Gx_User  where UserCode='" + userCode + "' ) and IsReport is NULL ";
            }
            if(StringUtils.isNotEmpty(searchString)){
                sql += " and (JH like '%" + searchString + "%' or ScOrder_Code like '%" + searchString + "%' or Productid like '%" + searchString + "%' or ProductName like '%" + searchString + "%')";
            }
            sql += " order by JH desc";
            return sql;
        }
    }
}
