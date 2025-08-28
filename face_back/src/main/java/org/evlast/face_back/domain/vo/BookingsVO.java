package org.evlast.face_back.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingsVO {
    /**
     * 预约记录唯一标识
     */
    private Integer id;

    /**
     * 预约用户ID
     */
    private String userName;

    /**
     * 预约实验室ID
     */
    private String labName;

    /**
     * 预约时间段数组，格式如["09:00-10:00", "14:00-15:00"]
     */
    private Object timeSlots;

    /**
     * 计划到达日期
     */
    private LocalDate arrivalDate;

    /**
     * 状态: pending-待确认, confirmed-已确认, completed-已完成, canceled-已取消
     */
    private Object status;

    /**
     * 预约原因或备注
     */
    private String reason;

    /**
     * 预约创建时间
     */
    private LocalDateTime createdAt;

    @Serial
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userId) {
        this.userName = userId;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
