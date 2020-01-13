package com.jimu.study.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jimu.study.model.Announcement;

/**
 * @author hxt
 */
public interface AnnouncementService extends IService<Announcement> {

    /**发布公告*/
    Integer insertAnno(Announcement announcement);

    /**分页搜索公告列表*/
    IPage<Announcement> findAnnoList(IPage<Announcement> page);

    /**返回具体的公告详情*/
    Announcement findOneAnno(Integer annoId);

    /**增加阅读数*/
    String addReadNum(Integer annoId);
}
