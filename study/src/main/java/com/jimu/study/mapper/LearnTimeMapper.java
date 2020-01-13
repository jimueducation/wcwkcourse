package com.jimu.study.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jimu.study.model.LearnTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * @author hxt
 */
@Mapper
public interface LearnTimeMapper extends BaseMapper<LearnTime> {

    /**
     * 修改每日视频时长
     * @param time 时间，单位：分钟
     * @param usersId 用户ID
     * @return boolean
     */
    @Update("update learn_time set video_time_day = video_time_day + #{time} where users_id = #{usersId}")
    Boolean updateVideoDay(Integer time, Integer usersId);

    /**
     * 修改每日图文时长
     * @param time 时间，单位：分钟
     * @param usersId 用户ID
     * @return boolean
     */
    @Update("update learn_time set pic_time_day = pic_time_day + #{time} where users_id = #{usersId}")
    Boolean updatePicDay(Integer time, Integer usersId);

    /**
     * 修改每日音频时长
     * @param time 时间，单位：分钟
     * @param usersId 用户ID
     * @return boolean
     */
    @Update("update learn_time set audio_time_day = audio_time_day + #{time} where users_id = #{usersId}")
    Boolean updateAudioDay(Integer time, Integer usersId);


    /**
     * 更新每日时长到总长，并把每日时长归零
     * @return boolean
     */
    @Update("update learn_time set video_time = video_time + video_time_day, video_time_day = 0, " +
            "pic_time = pic_time + pic_time_day, pic_time_day = 0, " +
            "audio_time = audio_time + audio_time_day, audio_time_day = 0")
    Boolean updateEveryDay();
}
