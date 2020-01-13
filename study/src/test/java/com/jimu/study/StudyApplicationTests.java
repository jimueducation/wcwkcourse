package com.jimu.study;

import com.jimu.study.service.CourseContentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StudyApplicationTests {

    @Autowired
    private CourseContentService contentService;

    @Test
    private void test(){

    }

}
