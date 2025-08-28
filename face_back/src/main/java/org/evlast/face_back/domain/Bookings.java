package org.evlast.face_back.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import lombok.Data;

/**
 * 实验室预约记录表
 * @TableName bookings
 */
@TableName(value ="bookings")
@Data
public class Bookings implements Serializable {
    /**
     * 预约记录唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 预约用户ID
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 预约实验室ID
     */
    @TableField(value = "lab_id")
    private Integer labId;

    /**
     * 预约时间段数组，格式如["09:00-10:00", "14:00-15:00"]
     */
    @TableField(value = "time_slots")
    private Object timeSlots;

    /**
     * 计划到达日期
     */
    @TableField(value = "arrival_date")
    private LocalDate arrivalDate;

    /**
     * 状态: pending-待确认, confirmed-已确认, completed-已完成, canceled-已取消
     */
    @TableField(value = "status")
    private Object status;

    /**
     * 预约原因或备注
     */
    @TableField(value = "reason")
    private String reason;

    /**
     * 预约创建时间
     */
    @TableField(value = "created_at")
    private LocalDateTime createdAt;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getLabId() {
        return labId;
    }

    public void setLabId(Integer labId) {
        this.labId = labId;
    }

    public Object getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(Object timeSlots) {
        this.timeSlots = timeSlots;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime created_at) {
        this.createdAt = created_at;
    }
}