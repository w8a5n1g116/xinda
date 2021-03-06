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

    /**
    * @Description: 根据用户名密码获取用户,验证用户是否存在,登录页使用
    * @Param: [userName, password]
    * @return: java.util.Map<java.lang.String,java.lang.Object>
    * @Author: Stopno
    * @Date: 2020-11-21
    */
    @Select("select top 1 *  from Tab_MES_BaseManage_UserInfo where UserCode= #{userName} and Password= #{password}")
    Map<String,Object> getUserByUserNameAndPassword(String userName,String password);

    /**
    * @Description: 获取生产计划
    * @Param: [userCode, searchString]
    * @return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    * @Author: Stopno
    * @Date: 2020-11-21
    */
    @SelectProvider(method = "getPlanByUserCode",type = BaseMapperProvider.class)
    List<Map<String,Object>> getPlan(String userCode,String searchString);

    /**
    * @Description: 质量合格的完工汇报,用于转下序使用
    * @Param: [scOrderCode, gxCode, userCode]
    * @return: int
    * @Author: Stopno
    * @Date: 2020-11-21
    */
    @Select("exec Pro_MES_ScManage_Gxhb_App "
            +"@scdd = #{scOrderCode,mode=IN,jdbcType=VARCHAR},"
            +"@gxdm = #{gxCode,mode=IN,jdbcType=VARCHAR},"
            +"@hbrCode = #{userCode,mode=IN,jdbcType=VARCHAR}"
            )
    @Options(statementType = StatementType.CALLABLE)
    Integer report(String scOrderCode, String gxCode,String userCode);

    /**
    * @Description: 设置报工完成状态,用于生产计划完工后显示检测计划
    * @Param: [scOrderCode, gxCode]
    * @return: int
    * @Author: Stopno
    * @Date: 2020-11-21
    */
    @Update("update [dbo].[Tab_MES_ScInfo_Xc_Zx] set IsReport = '是' where ScOrder_Code = #{scOrderCode} and Gx_Code = #{gxCode}")
    Integer setReportable(String scOrderCode, String gxCode);

    /**
    * @Description: 获取检测项中的不合格类型
    * @Param: []
    * @return: java.util.List<java.lang.String>
    * @Author: Stopno
    * @Date: 2020-11-21
    */
    @Select("select S_Value FROM [MES].[dbo].[Tab_MES_BaseManage_Sys_Edit]  where S_Key = '质量不合类别'")
    List<String> getUnqualifiedType();

    /**
    * @Description: 根据产品代码获取返修工艺路线
    * @Param: [productId]
    * @return: java.util.List<java.lang.String>
    * @Author: Stopno
    * @Date: 2020-11-21
    */
    @Select("select distinct Fx_Category from  [MES].[dbo].[Tab_MES_BaseManage_FxGxInfo] where  Cp_Category in \n" +
            "(select top 1 Category  from [MES].[dbo].[Tab_MES_BaseManage_ProductInfo]  where Productid = #{productId})")
    List<String> getFXRoute(String productId);

    /**
    * @Description: 获取检测计划
    * @Param: [userCode, productId, orderNo, processName, paramName, queryType]
    * @return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    * @Author: Stopno
    * @Date: 2020-11-21
    */
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

    /**
    * @Description: 设置参数不合格
    * @Param: [selfEval, judgeProgress, submitUserCode, selfSpRe, notType, id]
    * @return: int
    * @Author: Stopno
    * @Date: 2020-11-21
    */
    @Update("update Tab_MES_QualityManage_EvaluteResults set SelfEval = #{selfEval} ,EvalResult='不合格',JudgeProgress = #{judgeProgress} ,SubmitUserCode = #{submitUserCode} ,SelfSpRe = #{selfSpRe} ,NotType = #{notType} ,SubTime = GETDATE(),JudgeLevel = #{fixModel}  where  Id = #{id}")
    Integer setParamUnqualified(String selfEval,String judgeProgress,String submitUserCode,String selfSpRe,String notType,String id,String fixModel);

    /**
    * @Description: 汇报参数合格
    * @Param: [selfEval, submitUserCode, id]
    * @return: int
    * @Author: Stopno
    * @Date: 2020-11-21
    */
    @Update("update Tab_MES_QualityManage_EvaluteResults set SelfEval =#{selfEval}  ,EvalResult='合格',JudgeProgress = '完结' ,SubmitUserCode = #{submitUserCode} ,SubTime = GETDATE()   where  Id = #{id}")
    Integer setParamQualified(String selfEval,String submitUserCode,String id);

    /**
    * @Description: 汇报返修状态
    * @Param: [scOrderCode, fixCount, processOrder, mainFixProcessName, status]
    * @return: int
    * @Author: Stopno
    * @Date: 2020-11-21
    */
    @Select("EXEC [dbo].[Pro_MES_QualityManage_ScOrder_submitRepairQParams] " +
            "@生产订单号 = #{scOrderCode,mode=IN,jdbcType=VARCHAR}," +
            "@返修次数 = #{fixCount,mode=IN,jdbcType=INTEGER}," +
            "@返修工序顺序 = #{processOrder,mode=IN,jdbcType=VARCHAR}," +
            "@主流程返修工序名称 = #{mainFixProcessName,mode=IN,jdbcType=VARCHAR}," +
            "@状态 = #{status,mode=IN,jdbcType=VARCHAR}"
            )
    @Options(statementType = StatementType.CALLABLE)
    Integer reportFix(String scOrderCode,int fixCount,String processOrder,String mainFixProcessName,String status);

    /**
    * @Description: 提交报废
    * @Param: [id, submitUserCode]
    * @return: int
    * @Author: Stopno
    * @Date: 2020-11-21
    */
    @Select("EXEC [dbo].[Pro_MES_QualityManage_product_scraped] " +
            "@id = #{id,mode=IN,jdbcType=VARCHAR}, " +
            "@报废汇报人=#{submitUserCode,mode=IN,jdbcType=VARCHAR}," +
            "@报废原因='质检自评报废'," +
            "@报废类别 = '工序报废'")
    @Options(statementType = StatementType.CALLABLE)
    Integer reportScrap(String id,String submitUserCode);

    /**
    * @Description: 添加审批流程
    * @Param: [id, level]
    * @return: int
    * @Author: Stopno
    * @Date: 2020-11-21
    */
    @Select("EXEC [dbo].[Pro_QualityManage_Judge_Proces] " +
            "@质量参数Id = #{id,mode=IN,jdbcType=VARCHAR}, " +
            "@评审级别 = #{level,mode=IN,jdbcType=VARCHAR}")
    @Options(statementType = StatementType.CALLABLE)
    Integer addjudgeProcess(String id,String level);

    /**
    * @Description: 插入第一次返修的质量参数检查
    * @Param: [id]
    * @return: int
    * @Author: Stopno
    * @Date: 2020-11-21
    */
    @Insert("insert into  [MES].[dbo].[Tab_MES_QualityManage_EvaluteResults] (Id,ScOrder_Code,Customer_Name,Product_Name,Product_Id ,ProductDh,ProductBm,SN ,Gx_Code ,Gx_Name,Param_Name,Standard,ParamType,TestStandard,QualitativeDescribe,MinValue,MaxValue,JudgeLevel,SelfEval,EvalResult,JudgeProgress) " +
            "select  NEWID(), ScOrder_Code,Customer_Name,Product_Name ,Product_Id ,ProductDh,ProductBm ,SN,Gx_Code,Gx_Name,Param_Name+'-返修' ,Standard,ParamType,TestStandard,QualitativeDescribe,MinValue,MaxValue,JudgeLevel,'','',''  " +
            "from [MES].[dbo].[Tab_MES_QualityManage_EvaluteResults] " +
            "where Id = #{id}")
    Integer insertFixFirstEvaluteResults(String id);

    /**
    * @Description: 添加第二次返修的质量参数检查
    * @Param: [scOrderCode, processName]
    * @return: int
    * @Author: Stopno
    * @Date: 2020-11-21
    */
    @Insert("insert into  [MES].[dbo].[Tab_MES_QualityManage_EvaluteResults] (Id,ScOrder_Code,Customer_Name,Product_Name,Product_Id ,ProductDh,ProductBm,SN ,Gx_Code ,Gx_Name,Param_Name,Standard,ParamType,TestStandard,QualitativeDescribe,MinValue,MaxValue,JudgeLevel,SelfEval,EvalResult,JudgeProgress) " +
            "select top 1 NEWID(), ScOrder_Code,Customer_Name,Product_Name ,Product_Id ,ProductDh,ProductBm ,SN,Gx_Code,Gx_Name,Param_Name+'-返修' ,Standard,ParamType,TestStandard,QualitativeDescribe,MinValue,MaxValue,JudgeLevel,'','',''  " +
            "from [MES].[dbo].[Tab_MES_QualityManage_EvaluteResults] " +
            "where ScOrder_Code = #{scOrderCode} and Gx_Name = #{processName} and Param_Name like '%-返修%' and RepairNum is null order by Param_Name desc ")
    Integer insertFixSecondEvaluteResults(String scOrderCode,String processName);

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
