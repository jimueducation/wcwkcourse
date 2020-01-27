package com.jimu.study.quartz;

import com.jimu.study.model.vo.CourseList;
import com.jimu.study.service.CourseService;
import com.jimu.study.service.LearnTimeService;
import com.jimu.study.utils.ApplicationContextUtil;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hxt
 */
@Component
@EnableScheduling
@DependsOn("applicationContextUtil")
public class ScheduledTask {

    private LearnTimeService learnTimeService = (LearnTimeService) ApplicationContextUtil.getBean("learnTimeService");

    private CourseService courseService = (CourseService) ApplicationContextUtil.getBean("courseService");

    public static List<CourseList> newestCourse = new ArrayList<>();

    public ScheduledTask(){
        updateNewest();
    }

    @Scheduled(cron = "0 0 0 * * ? ")
    public void updateLearnTime(){
        learnTimeService.updateEveryDay();
        updateNewest();
    }

    private void updateNewest(){
        courseService.updateNewest();
        courseService.updateHotest();
        newestCourse = courseService.newestCourse();
    }

}
