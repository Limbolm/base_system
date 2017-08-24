package com.smpl.base.annotations;

import java.lang.annotation.*;

/**
 * 需要给两个值<br>
 * tableName = "表名"<br>
 * id = "表的主键"(如果是多个主建,默认为第一个)<br>
 * @author lijianning 2015-09-02
 * @Email: mmm333zzz520@163.com
 * @version 3.1v
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TableSeg {
    /**
     * 表名
     * @return
     */
    public String tableName();

    /**
     * 表的主键,如果是多个主建,默认为第一个
     * @return
     */
    public String id();

}
