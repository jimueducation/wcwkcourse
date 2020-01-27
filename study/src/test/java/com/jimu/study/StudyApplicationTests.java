package com.jimu.study;

import com.jimu.study.service.CourseContentService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StudyApplicationTests {

    @Autowired
    private CourseContentService contentService;

    @Test
    private void test(){
        String password = new Md5Hash("111111", "7612060831506800", 1).toHex();
        System.out.println(password);
    }

}
