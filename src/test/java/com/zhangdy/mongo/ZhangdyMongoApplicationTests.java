package com.zhangdy.mongo;

import com.zhangdy.mongo.entity.User;
import com.zhangdy.mongo.entity.UserPassword;
import com.zhangdy.mongo.repository.MongoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ZhangdyMongoApplication.class)
@Slf4j
class ZhangdyMongoApplicationTests {


    @Autowired
    MongoRepository<User> userMongoRepository;
    @Autowired
    MongoRepository<User> userMongoRepository1;
    @Autowired
    MongoRepository<UserPassword> passwordMongoRepository;

    @Test
    public void test(){

        User user = User.builder()
                .id(1L)
                .age(18)
                .nickname("123")
                .userName("zhangdy")
                .build();

        userMongoRepository.save(user);

        UserPassword password = UserPassword.builder()
                .id(2L)
                .userId(1L)
                .password("123")
                .type(0)
                .build();

        passwordMongoRepository.save(password);

        UserPassword userPassword = passwordMongoRepository.getById(2L);
        log.info("{}", userPassword);

    }


}
