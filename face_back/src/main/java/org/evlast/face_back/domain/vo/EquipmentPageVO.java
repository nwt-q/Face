package org.evlast.face_back.domain.vo;


import java.time.LocalDateTime;


public class EquipmentPageVO {
    /**
     * 设备唯一标识
     */
    private Integer id;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 所属实验室ID
     */
    private String labName;

    /**
     * 状态: available-可用, borrowed-已借出, maintenance-维护中
     */
    private Object status;

    /**
     * 设备累计借出次数
     */
    private Integer borrowCount;

    /**
     * 最后借用人ID（关联users表）
     */
    private String lastBorrowerName;

    /**
     * 设备录入时间
     *
     */
    private LocalDateTime createdAt;


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

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
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

    public void setBorrowCount(Integer borrowCount) {
        this.borrowCount = borrowCount;
    }

    public String getLastBorrowerName() {
        return lastBorrowerName;
    }

    public void setLastBorrowerName(String lastBorrowerName) {
        this.lastBorrowerName = lastBorrowerName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
