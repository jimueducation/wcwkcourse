package com.jimu.study.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jimu.study.mapper.AnnouncementMapper;
import com.jimu.study.model.Announcement;
import com.jimu.study.service.AnnouncementService;
import org.springframework.stereotype.Service;

/**
 * @author hxt
 */
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {

    @Override
    public Integer insertAnno(Announcement announcement) {
        return baseMapper.insert(announcement);
    }

    @Override
    public IPage<Announcement> findAnnoList(IPage<Announcement> page) {
        return baseMapper.selectPage(page, new QueryWrapper<>());
    }

    @Override
    public Announcement findOneAnno(Integer annoId) {
        return baseMapper.selectById(annoId);
    }

    @Override
    public String addReadNum(Integer annoId) {
        Announcement anno = baseMapper.selectById(annoId);
        anno.setReadNum(anno.getReadNum() + 1);
        baseMapper.updateById(anno);
        return "成功";
    }
}
