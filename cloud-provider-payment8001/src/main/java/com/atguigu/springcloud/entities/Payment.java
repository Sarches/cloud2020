package com.atguigu.springcloud.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor  //全参构造器
@NoArgsConstructor  //无参构造器
public class Payment implements Serializable {

    private Long id;
    private String serial;
}
