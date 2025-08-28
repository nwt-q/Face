package org.evlast.face_back.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import lombok.Data;

/**
 * 实验室使用考勤记录表
 * @TableName attendance
 */
@TableName(value ="attendance")
@Data
public class Attendance implements Serializable {
    /**
     * 考勤记录唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Integer user_id;

    /**
     * 实验室ID
     */
    @TableField(value = "lab_id")
    private Integer lab_id;

    /**
     * 签到时间
     */
    @TableField(value = "check_in_time")
    private LocalDateTime check_in_time;

    /**
     * 签退时间
     */
    @TableField(value = "check_out_time")
    private LocalDateTime check_out_time;

    /**
     * 停留时长（单位：分钟）
     */
    @TableField(value = "duration")
    private Integer duration;

    /**
     * 记录创建时间
     */
    @TableField(value = "created_at")
    private LocalDateTime created_at;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getLab_id() {
        return lab_id;
    }

    public void setLab_id(Integer lab_id) {
        this.lab_id = lab_id;
    }

    public LocalDateTime getCheck_in_time() {
        return check_in_time;
    }

    public void setCheck_in_time(LocalDateTime check_in_time) {
        this.check_in_time = check_in_time;
    }

    public LocalDateTime getCheck_out_time() {
        return check_out_time;
    }

    public void setCheck_out_time(LocalDateTime check_out_time) {
        this.check_out_time = check_out_time;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attendance that = (Attendance) o;
        return Objects.equals(id, that.id) && Objects.equals(user_id, that.user_id) && Objects.equals(lab_id, that.lab_id) && Objects.equals(check_in_time, that.check_in_time) && Objects.equals(check_out_time, that.check_out_time) && Objects.equals(duration, that.duration) && Objects.equals(created_at, that.created_at);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user_id, lab_id, check_in_time, check_out_time, duration, created_at);
    }
}