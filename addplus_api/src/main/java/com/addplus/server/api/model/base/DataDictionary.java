package com.addplus.server.api.model.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 类名: DataDictionary
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2018/9/21 下午4:04
 * @description 类描述:数据字典实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("data_dictionary")
public class DataDictionary extends BaseModel {

    /**
     * key名称
     */
    @TableField("redis_key")
    private String redisKey;
    /**
     * 内容值(json格式)
     */
    @TableField("redis_value")
    private String redisValue;

}
