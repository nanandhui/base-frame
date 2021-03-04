package com.frame.evan.mybatis;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.boot.test.context.SpringBootTest;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@SpringBootTest
@RegisterMapper
public interface BaseFrameMapper<T> extends Mapper<T> {
    /**
    * 插入或者更新数据
     * @param t 待插入对象
     * @return 影响行数
    */
    @InsertProvider(type = BaseFrameMapperProvider.class,method = "dynamicSQL")
    Integer insertOrUpdateToOne(T t);

    /**
     * 插入或者更新数据
     * @param list 待插入对象集合
     * @return 影响行数
     */
    @InsertProvider(type = BaseFrameMapperProvider.class,method = "dynamicSQL")
    Integer insertOrUpdate(@Param("list") List<T> list);

    /**
     * 插入或者忽略数据
     * @param list 待插入数据
     * @return 影响行数
     */
    //Integer insertOrIgnore(@Param("list") List<T> list);

    /**
     * 插入或者替换数据
     * @param list 待插入数据
     * @return 影响行数
     */
    //Integer insertOrReplace(@Param("list") List<T> list);


    /**
     * 通过过滤条件查询数据
     * @param t 过滤对象
     * @return 影响行数
     */
    @SelectProvider(type = BaseFrameMapperProvider.class,method = "dynamicSQL")
    List<T> selectListByNotNullColumn(T t);

}
