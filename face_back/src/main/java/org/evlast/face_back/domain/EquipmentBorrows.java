package org.evlast.face_back.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 设备借还操作记录表
 * @TableName equipment_borrows
 */
@TableName(value ="equipment_borrows")
@Data
public class EquipmentBorrows implements Serializable {
    /**
     * 借还记录唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联的预约记录ID
     */
    @TableField(value = "booking_id")
    private Integer bookingId;

    /**
     * 设备ID
     */
    @TableField(value = "equipment_id")
    private Integer equipmentId;

    /**
     * 操作类型: borrow-借出, return-归还
     */
    @TableField(value = "borrow_type")
    private Object borrowType;

    /**
     * 操作人员ID（管理员操作时记录）
     */
    @TableField(value = "operator_id")
    private Integer operatorId;

    /**
     * 操作备注
     */
    @TableField(value = "notes")
    private String notes;

    /**
     * 操作时间
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

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Object getBorrowType() {
        return borrowType;
    }

    public void setBorrowType(Object borrowType) {
        this.borrowType = borrowType;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}