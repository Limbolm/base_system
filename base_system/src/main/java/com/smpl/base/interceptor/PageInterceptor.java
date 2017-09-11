package com.smpl.base.interceptor;

import com.smpl.base.Exception.BusinessException;
import com.smpl.base.Utils.EhcacheUtils;
import com.smpl.base.Utils.ReflectHelper;
import com.smpl.base.Utils.StringUtils;
import com.smpl.base.entity.PageInfo;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMapping.Builder;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * 自定义物理分页
 * 需要分页的sql 传入的mapperId 必须含有 QueryPage
 *
 */
@Intercepts({@Signature(type = StatementHandler.class,method = "prepare",args = {Connection.class, Integer.class})})
public class PageInterceptor implements Interceptor {

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
            //基于base-mapper 将进行sql 拼接和参数封装
            createSqlData(mappedStatement,boundSql,getParameterObject);
        }else {
            //不是select语句.
            if (!"SELECT".equals(mappedStatement.getSqlCommandType().name())) {
                return invocation.proceed();
            }
            Object parameterObject = boundSql.getParameterObject();
            PageInfo pageParams = getPageParamsForParamObj(parameterObject);
            if (pageParams == null) { //无法获取分页参数，不进行分页。
                return invocation.proceed();
            }

            //获取配置中是否启用分页功能.
            if (!(mappedStatement.getId().indexOf(SQL_KEY)>0)) {  //不使用分页插件.
                return invocation.proceed();
            }
            //获取相关配置的参数.
            Integer pageNum = pageParams.getPageNumber() == null? defaultPage : pageParams.getPageNumber();
            Integer pageSize = pageParams.getPageSize() == null? defaultPageSize : pageParams.getPageSize();
            Boolean checkFlag = defaultCheckFlag;
            Boolean cleanOrderBy = defaultCleanOrderBy;
            //计算总条数
            int total = this.getTotal(invocation, metaStatementHandler, boundSql, cleanOrderBy, dbType);
            //回填总条数到分页参数
            pageParams.setTotalCount(total);
            //计算总页数.
            int totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
            //回填总页数到分页参数.
            pageParams.setTotalPageNumber(totalPage);
            //检查当前页码的有效性.
            this.checkPage(checkFlag, pageNum, totalPage);
            //修改sql
            return this.preparedSQL(invocation, metaStatementHandler, boundSql, pageNum, pageSize, dbType);

        }




        return invocation.proceed();
    }

    @Override
    public Object plugin(Object statementHandler) {
        return Plugin.wrap(statementHandler, this);
    }

    @Override
    public void setProperties(Properties props) {
        String strDefaultPage = props.getProperty("default.page", "1");
        String strDefaultPageSize = props.getProperty("default.pageSize", "50");
        String strDefaultUseFlag = props.getProperty("default.useFlag", "false");
        String strDefaultCheckFlag = props.getProperty("default.checkFlag", "false");
        String StringDefaultCleanOrderBy = props.getProperty("default.cleanOrderBy", "false");

        this.defaultPage = Integer.parseInt(strDefaultPage);
        this.defaultPageSize = Integer.parseInt(strDefaultPageSize);
        this.defaultUseFlag = Boolean.parseBoolean(strDefaultUseFlag);
        this.defaultCheckFlag = Boolean.parseBoolean(strDefaultCheckFlag);
        this.defaultCleanOrderBy = Boolean.parseBoolean(StringDefaultCleanOrderBy);

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
    private void createSqlData( MappedStatement mappedStatement,BoundSql boundSql,Object parameterObject) throws Exception {

        Map map = (Map) parameterObject;
        String cloumNameOfString = "";
        String tableName = "";
        String sql = "";
        List<ParameterMapping> paraList = new ArrayList<ParameterMapping>();
        Configuration configuration = mappedStatement.getConfiguration();
        if (null != map) {

            //TODO: 测试数据
            /*tableName=map.get("tabaleName").toString();*/
            tableName = "sys.userInfo";
            //根据表名获取字段名称String
            //TODO: 前期为 从本地缓存中 获取，后期迁移至 redis
            /*Map<String,Object> tableMap= (Map<String, Object>) EhcacheUtils.get(tableName);*/
            /*cloumNameOfString=tableMap.get("cloum").toString();*/
            cloumNameOfString = "userCode,name,password";
        }

        //判断 执行语句的类型 CRUD
        if ("INSERT".equals(mappedStatement.getSqlCommandType().name())) {
            StringBuilder cloum = new StringBuilder();
            StringBuilder cloumValue = new StringBuilder();
            String[] cloumNames = cloumNameOfString.split(",");

            //循环遍历传入 对象  获取 列和对应值
            for (String cloumName : cloumNames) {
                Object o = map.get(cloumName);
                if (o != null) {
                    cloum.append(cloumName + ",");
                    cloumValue.append("?,");
                    //传入参数类型获取 转换  相当于 $ #
                    Builder mappring = new ParameterMapping.Builder(configuration, cloumName, o.getClass());
                    ParameterMapping parameter = mappring.build();
                    paraList.add(parameter);

                }
            }
            sql = "insert into " + tableName.toString() + " (" + cloum.substring(0, cloum.length() - 1) + ")  values (" + cloumValue.substring(0, cloumValue.length() - 1) + ")";

        } else if ("UPDATE".equals(mappedStatement.getSqlCommandType().name())) {
            StringBuilder setColum = new StringBuilder();
            StringBuilder setWhere = new StringBuilder(" where id = ?");
            String[] cloumNames = cloumNameOfString.split(",");
            for (String cloumName : cloumNames) {
                Object o = map.get(cloumName);
                if (null != o) {
                    setColum.append(cloumName + "=?,");
                    Builder mappring = new ParameterMapping.Builder(configuration, cloumName, o.getClass());
                    ParameterMapping param = mappring.build();
                    paraList.add(param);
                }

            }
            if (!StringUtils.isEmpty(map.get("id").toString())) {
                Builder mappring = new ParameterMapping.Builder(configuration, "id", map.get("id").getClass());
                ParameterMapping param = mappring.build();
                paraList.add(param);
            }
            sql = "UPDATE " + tableName + " set " + setColum.toString().substring(0, setColum.length() - 1) + setWhere.toString();
        } else if ("DELETE".equals(mappedStatement.getSqlCommandType().name())) {
            if (map.containsKey("ids")) {
                sql = "DELETE from " + tableName + " where  id in (" + map.get("ids") + ")";
            } else if (!map.containsKey("ids") && map.containsKey("id")) {
                sql = "DELETE from " + tableName + " where  id in (" + map.get("id") + ")";
            }else if (mappedStatement.getId().indexOf("deleteByAttribute")>-1){
              StringBuffer delSql=new StringBuffer("DELETE from "+tableName+" where ");
              StringBuffer coumSql=new StringBuffer();
                String[] cloumNames = cloumNameOfString.split(",");
                for (String cloumName:cloumNames){
                    Object o=map.get(cloumName);
                    if(o!=null){
                        coumSql.append(" "+cloumName+" = ?");
                        Builder mappring=new ParameterMapping.Builder(configuration,cloumName,o.getClass());
                        ParameterMapping param = mappring.build();
                        paraList.add(param);
                    }
                }
                sql=delSql.toString()+coumSql.toString();
            }
        } else if ("SELECT".equals(mappedStatement.getSqlCommandType().name())) {
            //根据 mappedStatement.id 判断查询方式  findByID  findbyname findByatuibe 以上方法默认不需分页
            if (mappedStatement.getId().indexOf("selectById") > -1) {
                if (map.containsKey("id")) {
                    sql = "select * from   " + tableName + " where id in (" + map.get("id").toString() + ")";
                }
            } else if (mappedStatement.getId().indexOf("selectByAttribute") > -1) {
                StringBuilder setSelect = new StringBuilder("select * from " + tableName + " where 1=1  ");
                String[] cloumNames = cloumNameOfString.split(",");
                for (String cloumName : cloumNames) {
                    Object o = map.get(cloumName);
                    if (null != o) {
                        setSelect.append("and " + cloumName + "=? ");
                        Builder mappring = new ParameterMapping.Builder(configuration, cloumName, o.getClass());
                        ParameterMapping param = mappring.build();
                        paraList.add(param);
                    }
                    sql = setSelect.toString();
                }

                //根据分页参数 判断是否分页
                Object parameterObj = boundSql.getParameterObject();
                PageInfo pageParams = this.getPageParamsForParamObj(parameterObj);
                //分页参数为空 则不需要分页
                if (pageParams == null) {
                    return;
                }


            }

            if (paraList.size() > 0) {
                ReflectHelper.setValueByFieldName(boundSql, "parameterMappings", paraList);
            }

            ReflectHelper.setValueByFieldName(boundSql, "sql", sql);

        }
    }


    /***
     * 分离出分页参数.
     * @param parameterObject --执行参数
     * @return 分页参数
     * @throws Exception
     */
    public PageInfo getPageParamsForParamObj(Object parameterObject) throws Exception {
        PageInfo pageParams = null;
        if (parameterObject == null) {
            return null;
        }
        //处理map参数和@Param注解参数，都是MAP
        if (parameterObject instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> paramMap = (Map<String, Object>) parameterObject;
            Set<String> keySet = paramMap.keySet();
            Iterator<String> iterator = keySet.iterator();
            while(iterator.hasNext()) {
                String key = iterator.next();
                Object value = paramMap.get(key);
                if (value instanceof PageInfo) {
                    return (PageInfo)value;
                }
            }
        } else if (parameterObject instanceof PageInfo) { //参数POJO继承了PageParams
            return (PageInfo) parameterObject;
        } else { //从POJO尝试读取分页参数.
            Field[] fields = parameterObject.getClass().getDeclaredFields();
            //尝试从POJO中获得类型为PageParams的属性
            for (Field field : fields) {
                if (field.getType() == PageInfo.class) {
                    PropertyDescriptor pd = new PropertyDescriptor (field.getName(), parameterObject.getClass());
                    Method method = pd.getReadMethod();
                    return (PageInfo) method.invoke(parameterObject);
                }
            }
        }
        return pageParams;
    }

    /**
     * 获取总条数.
     *
     * @param ivt Invocation
     * @param metaStatementHandler statementHandler
     * @param boundSql sql
     * @param cleanOrderBy 是否清除order by语句
     * * @param dbType
     * @return sql查询总数.
     * @throws Throwable 异常.
     */
    private int getTotal(Invocation ivt, MetaObject metaStatementHandler, BoundSql boundSql, Boolean cleanOrderBy, String dbType) throws Throwable {
        //获取当前的mappedStatement
        MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
        //配置对象
        Configuration cfg = mappedStatement.getConfiguration();
        //当前需要执行的SQL
        String sql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
        //去掉最后的order by语句
        if (cleanOrderBy) {
            sql = this.cleanOrderByForSql(sql);
        }
        String countSql = this.getTotalSQL(sql, dbType);
        //获取拦截方法参数，根据插件签名，知道是Connection对象.
        Connection connection = (Connection) ivt.getArgs()[0];
        PreparedStatement ps = null;
        int total = 0;
        try {
            //预编译统计总数SQL
            ps = connection.prepareStatement(countSql);
            //构建统计总数SQL
            BoundSql countBoundSql = new BoundSql(cfg, countSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
            //构建MyBatis的ParameterHandler用来设置总数Sql的参数。
            ParameterHandler handler = new DefaultParameterHandler(mappedStatement, boundSql.getParameterObject(), countBoundSql);
            //设置总数SQL参数
            handler.setParameters(ps);
            //执行查询.
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                total = rs.getInt("total");
            }
        } finally {
            //这里不能关闭Connection否则后续的SQL就没法继续了。
            if (ps != null) {
                ps.close();
            }
        }
        return total;
    }

    private String cleanOrderByForSql(String sql) {
        StringBuilder sb = new StringBuilder(sql);
        String newSql = sql.toLowerCase();
        //如果没有order语句,直接返回
        if (newSql.indexOf("order") == -1) {
            return sql;
        }
        int idx = newSql.lastIndexOf("order");
        return sb.substring(0, idx).toString();
    }

    /**
     * TODO
     * 计算总数的SQL,
     * 这里需要根据数据库的类型改写SQL，目前支持MySQL和Oracle
     * @param currSql —— 当前执行的SQL
     * @return 改写后的SQL
     * @throws
     */
    private String getTotalSQL(String currSql, String dbType) throws BusinessException {
        if (DB_TYPE_MYSQL.equals(dbType)) {
            return  "select count(*) as total from (" + currSql + ") $_paging";
        } else if (DB_TYPE_ORACLE.equals(dbType)) {
            return "select count(*) as total from (" + currSql +")";
        } else {
            throw new BusinessException("当前插件未支持此类型数据库");
        }
    }

    /**
     * 检查当前页码的有效性.
     * @param checkFlag
     * @param pageNum
     * @param pageTotal
     * @throws Throwable
     */
    private void checkPage(Boolean checkFlag, Integer pageNum, Integer pageTotal) throws Throwable  {
        if (checkFlag) {
            //检查页码page是否合法.
            if (pageNum > pageTotal) {
                throw new Exception("查询失败，查询页码【" + pageNum + "】大于总页数【" + pageTotal + "】！！");
            }
        }
    }

    /**
     * 预编译改写后的SQL，并设置分页参数
     * @param invocation
     * @param metaStatementHandler
     * @param boundSql
     * @param pageNum
     * @param pageSize
     * @param dbType
     * @throws IllegalAccessException
     * @throws BusinessException
     */
    private Object preparedSQL(Invocation invocation, MetaObject metaStatementHandler, BoundSql boundSql, int pageNum, int pageSize, String dbType) throws Exception {
        //获取当前需要执行的SQL
        String sql = boundSql.getSql();
        String newSql = this.getPageDataSQL(sql, dbType);
        //修改当前需要执行的SQL
        metaStatementHandler.setValue("delegate.boundSql.sql", newSql);
        //执行编译，这里相当于StatementHandler执行了prepared()方法，这个时候，就剩下2个分页参数没有设置。
        Object statementObj = invocation.proceed();
        //设置两个分页参数。
        this.preparePageDataParams((PreparedStatement)statementObj, pageNum, pageSize, dbType);
        return statementObj;
    }

    /**
     * TODO 需要使用其他数据库需要改写
     * 分页获取参数的SQL
     * 这里需要根据数据库的类型改写SQL，目前支持MySQL和Oracle
     * @param currSql —— 当前执行的SQL
     * @return 改写后的SQL
     * @throws BusinessException
     */
    private String getPageDataSQL(String currSql, String dbType) throws BusinessException {
        if (DB_TYPE_MYSQL.equals(dbType)) {
            return "select * from (" + currSql + ") $_paging_table limit ?, ?";
        } else if (DB_TYPE_ORACLE.equals(dbType)) {
            return " select * from (select cur_sql_result.*, rownum rn from (" + currSql + ") cur_sql_result  where rownum <= ?) where rn > ?";
        } else {
            throw new BusinessException("当前插件未支持此类型数据库");
        }
    }

    /**
     * TODO 需要使用其他数据库需要改写
     * 使用PreparedStatement预编译两个分页参数，如果数据库的规则不一样，需要改写设置的参数规则。目前支持MySQL和Oracle
     * @throws BusinessException
     *
     */
    private void preparePageDataParams(PreparedStatement ps, int pageNum, int pageSize, String dbType) throws Exception {
        //prepared()方法编译SQL，由于MyBatis上下文没有我们分页参数的信息，所以这里需要设置这两个参数.
        //获取需要设置的参数个数，由于我们的参数是最后的两个，所以很容易得到其位置
        int idx = ps.getParameterMetaData().getParameterCount();
        if (DB_TYPE_MYSQL.equals(dbType)) {
            //最后两个是我们的分页参数.
            ps.setInt(idx -1, (pageNum - 1) * pageSize);//开始行
            ps.setInt(idx, pageSize); //限制条数
        } else if (DB_TYPE_ORACLE.equals(dbType)) {
            ps.setInt(idx -1, pageNum * pageSize);//结束行
            ps.setInt(idx, (pageNum - 1) * pageSize); //开始行
        } else {
            throw new BusinessException("当前插件未支持此类型数据库");
        }

    }

}
