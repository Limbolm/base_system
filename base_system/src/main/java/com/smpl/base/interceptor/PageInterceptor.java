package com.smpl.base.interceptor;

import com.smpl.base.entity.PageInfo;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Properties;

/**
 * 自定义物理分页
 */
@Intercepts({@Signature(type = StatementHandler.class,method = "prepare",args = {Connection.class})})
public class PageInterceptor implements Interceptor{
    private String dialect = ""; //数据库方言
    private String pageSqlId = ""; //mapper.xml中需要拦截的ID(正则匹配)
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler= (StatementHandler) invocation.getTarget();
        MetaObject metaObject=MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY,
                SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY,new DefaultReflectorFactory());

        MappedStatement mappedStatement= (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        String sqlId=mappedStatement.getId();

        if (sqlId.matches(pageSqlId)){
            BoundSql boundSql=statementHandler.getBoundSql();

            String sql= boundSql.getSql();

            Map<?,?> parmsMap= (Map<?,?>) boundSql.getParameterObject();

            PageInfo pageInfo= (PageInfo) parmsMap.get("pageInfo");

            String countSql = "select count(*) from (" + sql + ") alias";

            Connection connection= (Connection) invocation.getArgs()[0];

            PreparedStatement preparedStatement= connection.prepareStatement(countSql);
            ParameterHandler parameterHandler = (ParameterHandler) metaObject.getValue("delegate.parameterHandler");
            parameterHandler.setParameters(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                pageInfo.setTotalCount(resultSet.getInt(1));

            }

            String pageSql = sql + " limit " + pageInfo.getDbIndex() + "," + pageInfo.getPageSize();

            metaObject.setValue("delegate.boundSql.sql", pageSql);



        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {
        pageSqlId = (String) properties.get("sqlIdByPageRegex");
        System.out.println(pageSqlId);

    }
}
