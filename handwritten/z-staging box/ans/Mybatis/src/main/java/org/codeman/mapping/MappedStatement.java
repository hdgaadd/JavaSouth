package org.codeman.mapping;

import org.codeman.constants.SqlType;

/**
 * @author hdgaadd
 * created on 2022/02/25
 *
 * mapper文件对应类
 */
public class MappedStatement {

    private String namespace;

    private String sqlId;

    private String sql;

    private String resultType;

    /**
     * sql命令类型
     */
    private SqlType sqlCommandType;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getSqlId() {
        return sqlId;
    }

    public void setSqlId(String sqlId) {
        this.sqlId = sqlId;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public SqlType getSqlCommandType() {
        return sqlCommandType;
    }

    public void setSqlCommandType(SqlType sqlCommandType) {
        this.sqlCommandType = sqlCommandType;
    }

    @Override
    public String toString() {
        return "MappedStatement [namespace=" + namespace + ", sqlId=" + sqlId + ", sql=" + sql + ", resultType="
                + resultType + "]";
    }

}
