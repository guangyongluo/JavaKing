package com.vilin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_customer
 */
@TableName(value ="t_customer")
@Data
public class Customer implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String cname;

    /**
     * 
     */
    private Integer age;

    /**
     * 
     */
    private String phone;

    /**
     * 
     */
    private Integer sex;

    /**
     * 
     */
    private Date birth;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}