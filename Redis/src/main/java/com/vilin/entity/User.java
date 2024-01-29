package com.vilin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName t_user
 */
@TableName(value = "t_user")
@Data
public class User implements Serializable {

  /**
   *
   */
  @TableId(type = IdType.AUTO)
  private Long id;

  /**
   *
   */
  private String username;

  @TableField(exist = false)
  private static final long serialVersionUID = 1L;
}