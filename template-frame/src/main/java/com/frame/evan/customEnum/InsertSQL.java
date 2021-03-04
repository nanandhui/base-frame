package com.frame.evan.customEnum;

public enum InsertSQL {
    /**
    * 正常插入行数据，插入失败报错
    */
    INSERT,

    /**
     * 插入数据存在时更新行数据
     */
    UPDATE,

    /**
     * 插入数据存在时替换行数据
     */
    REPLACE,

    /**
     * 插入数据错误时忽略错误行，继续执行其他插入行数据插入
     */
    IGNORE
}

