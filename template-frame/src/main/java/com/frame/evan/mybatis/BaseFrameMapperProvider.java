package com.frame.evan.mybatis;

import com.frame.evan.utils.BaseStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program base-frame 
 * @description:
 * @author: wang
 * @create: 2021/03/02 15:57 
 */
@Slf4j
public class BaseFrameMapperProvider extends MapperTemplate {
    public BaseFrameMapperProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String insertOrUpdateToOne(MappedStatement ms){
        String tpl="{1}=values({1})";
        StringBuilder sql = new StringBuilder();
        Class<?> entityClass = getEntityClass(ms);
        // 获取全部列
        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);
        // 过滤掉非插入数据库列
        columns = columns.stream().filter(EntityColumn::isInsertable).collect(Collectors.toSet());
        // 拼接表名称
        sql.append(SqlHelper.insertIntoTable(entityClass,tableName(entityClass)));
        // 拼接SQL字段
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        sql.append(columns.stream().map(c->{
            String columnName = c.getColumn()+",";
            return c.isId() ? columnName : SqlHelper.getIfIsNull(c, columnName, isNotEmpty());
        }).collect(Collectors.joining()));
        sql.append("</trim>");
        // 拼接SQL数值
        sql.append("<trim prefix=\"VALUES(\" suffix=\")\" suffixOverrides=\",\">");
        sql.append(columns.stream().map(c->{
            String value = c.getColumnHolder()+",";
            return c.isId()?value:SqlHelper.getIfIsNull(c,value,isNotEmpty());
        }).collect(Collectors.joining()));
        sql.append("</trim>");
        // mysql特有:已经存在记录时执行更新操作
        sql.append("<trim prefix=\"ON DUPLICATE KEY UPDATE\" suffixOverrides=\",\">");
        sql.append(columns.stream().filter(EntityColumn::isUpdatable).filter(c->!c.isId()).map(c->SqlHelper.getIfIsNull(c, BaseStringUtils.formatIndex(tpl,c.getColumn()),isNotEmpty())).collect(
                Collectors.joining(",")));
        sql.append("</trim>");
        log.info("sql结果:\n"+sql.toString()+"\n");
        return sql.toString();
    }

    public String insertOrUpdate(MappedStatement ms){
        String tpl = "{1} = IFNULL(VALUES({1}), {1})";
        StringBuilder sql = new StringBuilder();
        Class<?> entityClass = getEntityClass(ms);
        // 获取全部列
        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);
        // 过滤掉非插入数据库列
        columns = columns.stream().filter(EntityColumn::isInsertable).collect(Collectors.toSet());
        sql.append(" INSERT INTO ");
        sql.append(SqlHelper.getDynamicTableName(entityClass,tableName(entityClass)));
        sql.append(columns.stream().filter(EntityColumn::isInsertable).map(c->c.getColumn()).collect(Collectors.joining(",","(",")")));
        sql.append(" VALUES ");
        sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
        sql.append(columns.stream().filter(EntityColumn::isInsertable).map(c->c.getColumnHolder("record")).collect(Collectors.joining(",","(",")")));
        sql.append("</foreach>");
        //[SQL] 更新
        sql.append(" ON DUPLICATE KEY UPDATE ");
        sql.append(columns.stream().filter(column -> !column.isId()).map(column -> BaseStringUtils.formatIndex(tpl,column.getColumn()))
                .collect(Collectors.joining(",")));
        log.info("sql结果:\n"+sql.toString()+"\n");
        return sql.toString();
    }

    public String selectListByNotNullColumn(MappedStatement ms){
        StringBuilder sql = new StringBuilder();
        Class<?> entityClass = getEntityClass(ms);
        sql.append(SqlHelper.selectAllColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass,tableName(entityClass)));
        sql.append(SqlHelper.whereAllIfColumns(entityClass,false));
        sql.append(SqlHelper.orderByDefault(entityClass));
        log.info("sql结果:\n"+sql.toString()+"\n");
        return sql.toString();
    }
}
