package com.zhangdy.mongo.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;
    private String userName;
    private int age;
    private String nickname;

}
