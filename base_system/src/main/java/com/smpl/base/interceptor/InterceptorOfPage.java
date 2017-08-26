package com.smpl.base.interceptor;

import com.smpl.base.Exception.BusinessException;
import com.smpl.base.Utils.EhcacheUtils;
import com.smpl.base.Utils.ReflectHelper;
import com.smpl.base.Utils.StringUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMapping.Builder;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 自定义物理分页
 * 需要分页的sql 传入的mapperId 必须含有 QueryPage
 *
 */
@Intercepts({@Signature(type = StatementHandler.class,method = "prepare",args = {Connection.class, Integer.class})})
public class InterceptorOfPage implements Interceptor {

    /**
     * 插件默认参数，可配置
     */
    private Integer defaultPage; //默认页码
    private Integer defaultPageSize;//默认每页条数
    private Boolean defaultUseFlag; //默认是否启用插件
    private Boolean defaultCheckFlag; //默认是否检测页码参数
    private Boolean defaultCleanOrderBy; //默认是否清除最后一个order by 后的语句

    //数据库类型
    private static final String DB_TYPE_MYSQL = "mysql";
    private static final String DB_TYPE_ORACLE = "oracle";
    private static final String SQL_KEY="QueryPage"; //判断 是否进行分页key


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler stmtHandler = (StatementHandler) getUnProxyObject(invocation.getTarget());
        MetaObject metaStatementHandler = SystemMetaObject.forObject(stmtHandler);
        BoundSql boundSql= (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
        String sql = boundSql.getSql();//获取 xml sql
        MappedStatement mappedStatement =  (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
        String dbType = this.getDataSourceType(mappedStatement);


        //获取传入参数
        Object getParameterObject= boundSql.getParameterObject();
        if (getParameterObject==null){
            //  参数为空直接执行语句
            return invocation.proceed();
        }

        //判断 是基于Base-mapper 还是自定义 mapper 查询
        if(mappedStatement.getId().indexOf("Base-mapper")>-1){

        }



        return null;
    }

    @Override
    public Object plugin(Object o) {
        return null;
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 从代理对象中分离出真实对象.
     *
     * @param target --Invocation
     * @return 非代理StatementHandler对象
     */
    private Object getUnProxyObject(Object target) {
        MetaObject metaStatementHandler = SystemMetaObject.forObject(target);
        // 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过循环可以分离出最原始的的目标类)
        Object object = null;
        while (metaStatementHandler.hasGetter("h")) {
            object = metaStatementHandler.getValue("h");
        }
        if (object == null) {
            return target;
        }
        return object;
    }

    /**
     *
     * TODO 需要使用其他数据库需要改写
     * 目前支持MySQL和Oracle
     * @param mappedStatement
     * @return
     * @throws Exception
     */
    private String getDataSourceType(MappedStatement mappedStatement) throws Exception {
        Connection conn = null;
        String dbConnectionStr = null;
        try {
            conn = mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();
            dbConnectionStr  = conn.toString();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        if (null == dbConnectionStr || dbConnectionStr.trim().equals(""))  {
            throw new BusinessException("当前插件未能获得数据库连接信息。");
        }
        dbConnectionStr = dbConnectionStr.toLowerCase();
        if (dbConnectionStr.contains(DB_TYPE_MYSQL)) {
            return DB_TYPE_MYSQL;
        } else if (dbConnectionStr.contains(DB_TYPE_ORACLE)) {
            return DB_TYPE_ORACLE;
        } else {
            throw new BusinessException("当前插件未支持此类型数据库");
        }
    }

    //构造sql 查询 语句 及参数
    private void createSqlData( MappedStatement mappedStatement,BoundSql boundSql,Object parameterObject) throws NoSuchFieldException, IllegalAccessException {

        Map map= (Map) parameterObject;
        String cloumNameOfString="";
        String tableName="";
        String sql="";
        List<ParameterMapping> paraList =new ArrayList<ParameterMapping>();
        Configuration configuration = mappedStatement.getConfiguration();
        if (null!=map){
            tableName=map.get("tabaleName").toString();
            //根据表名获取字段名称String
            //TODO: 前期为 从本地缓存中 获取，后期迁移至 redis
            Map<String,Object> tableMap= (Map<String, Object>) EhcacheUtils.get(tableName);
            cloumNameOfString=tableMap.get("cloum").toString();
        }

        //判断 执行语句的类型 CRUD
        if ("INSERT".equals(mappedStatement.getSqlCommandType())){
            StringBuilder  cloum=new StringBuilder();
            StringBuilder cloumValue=new StringBuilder();
            String[] cloumNames=cloumNameOfString.split(",");

            //循环遍历传入 对象  获取 列和对应值
            for (String cloumName:cloumNames){
               Object o= map.get(cloumName);
                if (o!=null){
                    cloum.append(cloumName+ ",") ;
                    cloumValue.append("?,");
                    //传入参数类型获取 转换  相当于 $ #
                    Builder mappring= new ParameterMapping.Builder(configuration,cloumName,o.getClass());
                    ParameterMapping parameter=mappring.build();
                    paraList.add(parameter);

                }
            }
            sql = "insert into " + tableName.toString() + " (" + cloum.substring(0,cloum.length()-2) + ")  values (" +cloumValue.substring(0,cloumValue.length()-2) + ")";

        }else if ("UPDATE".equals(mappedStatement.getSqlCommandType())){
            StringBuilder setColum=new StringBuilder();
            StringBuilder setWhere=new StringBuilder(" where id = ?");
            String[] cloumNames=cloumNameOfString.split(",");
            for (String cloumName: cloumNames) {
                Object o=map.get(cloumName);
                if (null!=o){
                    setColum.append(cloumName+"=?,");
                    Builder mappring=new ParameterMapping.Builder(configuration,cloumName,o.getClass());
                    ParameterMapping param=mappring.build();
                    paraList.add(param);
                }

            }
            if (!StringUtils.isEmpty(map.get("id").toString())){
                Builder mappring=new ParameterMapping.Builder(configuration,"id",map.get("id").getClass());
                ParameterMapping param=mappring.build();
                paraList.add(param);
            }
            sql="UPDATE "+tableName+"set"+setColum.toString().substring(0,setColum.length()-2)+setWhere.toString();
        }else if ("DELETE".equals(mappedStatement.getSqlCommandType())){
            if (!StringUtils.isEmpty(map.get("ids").toString())){
                sql="DELETE from "+tableName+" where  id in ("+map.get("ids")+")";
            }else if (StringUtils.isEmpty(map.get("ids").toString()) && !StringUtils.isEmpty(map.get("id").toString())){
                sql="DELETE from "+tableName+" where  id in ("+map.get("id")+")";
            }
            //TODO:后期添加 更具字段删除
        }else if ("SELECT".equals(mappedStatement.getSqlCommandType())){
            //判断是否分页
            if (mappedStatement.getId().indexOf(SQL_KEY)>-1){

            }
        }

        if (paraList.size()>0){
            ReflectHelper.setValueByFieldName(boundSql,"parameterMappings",paraList);
        }

        ReflectHelper.setValueByFieldName(boundSql,"sql",sql);

    }



}
