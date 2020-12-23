package com.zhangdy.mongo.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserPassword {
    private Long id;
    private Long userId;
    private int type;
    private String password;

}
