package org.evlast.face_back.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 实验设备详细信息表
 * @TableName equipment
 */
@TableName(value ="equipment")
@Data
public class Equipment implements Serializable {
    /**
     * 设备唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 设备名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 所属实验室ID
     */
    @TableField(value = "lab_id")
    private Integer labId;

    /**
     * 状态: available-可用, borrowed-已借出, maintenance-维护中
     */
    @TableField(value = "status")
    private Object status;

    /**
     * 设备累计借出次数
     */
    @TableField(value = "borrow_count")
    private Integer borrowCount;

    /**
     * 最后借用人ID（关联users表）
     */
    @TableField(value = "last_borrower")
    private Integer lastBorrower;

    /**
     * 设备录入时间
     */
    @TableField(value = "created_at")
    private LocalDateTime createdAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLabId() {
        return labId;
    }

    public void setLabId(Integer lab_id) {
        this.labId = lab_id;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public Integer getBorrowCount() {
        return borrowCount;
    }

    public void setBorrowCount(Integer borrow_count) {
        this.borrowCount = borrow_count;
    }

    public Integer getLastBorrower() {
        return lastBorrower;
    }

    public void setLastBorrower(Integer last_borrower) {
        this.lastBorrower = last_borrower;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime created_at) {
        this.createdAt = created_at;
    }
}