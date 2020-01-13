package com.jimu.study.model.vo;

import com.jimu.study.model.Teacher;
import lombok.Data;

import java.util.List;

/**
 * @author hxt
 */
@Data
public class TeacherVO extends Teacher {

    private Integer studyNum;

    private Long courseNum;

    private List<CourseList> courseLists;
}
