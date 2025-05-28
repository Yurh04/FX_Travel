package org.fxtravel.fxspringboot.mapper;

import org.apache.ibatis.annotations.*;
import org.fxtravel.fxspringboot.pojo.entities.Notification;

import java.util.List;

@Mapper
public interface NotificationMapper {

    // 插入新通知
    @Insert("INSERT INTO notification (id, user_id, event_type, content, channel, status, create_time, update_time) " +
            "VALUES (#{id}, #{userId}, #{eventType}, #{content}, #{channel}, #{status}, #{createTime}, #{updateTime})")
    void insert(Notification notification);

    // 更新通知状态
    @Update("UPDATE notification SET status = #{status}, update_time = #{updateTime} WHERE id = #{id}")
    void update(Notification notification);

    // 根据用户ID查询通知
    @Select("SELECT * FROM notification WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Notification> findByUserId(Integer userId);

    // 根据用户ID和状态查询通知
    @Select("<script>" +
            "SELECT * FROM notification WHERE user_id = #{userId}" +
            "<if test='status != null and status != \"\"'> AND status = #{status}</if>" +
            " ORDER BY create_time DESC" +
            "</script>")
    List<Notification> findByUserIdAndStatus(
            @Param("userId") Integer userId,
            @Param("status") String status);

    // 标记通知为已读
    @Update("UPDATE notification SET is_read = true WHERE id = #{notificationId}")
    void markAsRead(String notificationId);

    // 获取未读通知数量
    @Select("SELECT COUNT(*) FROM notification WHERE user_id = #{userId} AND is_read = false")
    Integer countUnreadNotifications(Integer userId);

    // 根据状态查询通知(用于失败重试)
    @Select("SELECT * FROM notification WHERE status = #{status} ORDER BY create_time ASC")
    List<Notification> findByStatus(String status);

    // 增加重试次数
    @Update("UPDATE notification SET retry_count = retry_count + 1 WHERE id = #{notificationId}")
    void incrementRetryCount(String notificationId);
}