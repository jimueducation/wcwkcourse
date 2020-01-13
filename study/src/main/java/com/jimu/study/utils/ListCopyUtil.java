package com.jimu.study.utils;

import com.jimu.study.model.Course;
import com.jimu.study.model.Orders;
import com.jimu.study.model.vo.CourseList;
import com.jimu.study.model.vo.OrdersVO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hxt
 */
public class ListCopyUtil {

    public static List<CourseList> copyCourseListToVo(List<Course> courses){
        List<CourseList> list2 = new ArrayList<>();
        for(Course course: courses){
            CourseList courseList = new CourseList();
            BeanUtils.copyProperties(course, courseList);
            list2.add(courseList);
        }
        return list2;
    }

    public static List<OrdersVO> copyOrderListToVo(List<Orders> orders){
        List<OrdersVO> ordersVos = new ArrayList<>();
        for(Orders order: orders){
            OrdersVO ordersVO = new OrdersVO();
            BeanUtils.copyProperties(order, ordersVO);
            ordersVos.add(ordersVO);
        }
        return ordersVos;
    }
}
