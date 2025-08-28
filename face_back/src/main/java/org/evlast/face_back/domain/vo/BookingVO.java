package org.evlast.face_back.domain.vo;


import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BookingVO {
    private Integer id;
    private String labName;
    private List<String> selectedSlots;  // 存储时间槽JSON字符串
    private String reason;
    private Boolean isBorrowReturn;
    private List<Integer> selectedEquipment;
    private String borrowReturn;  // "borrow"或"return"
    private LocalDate arrivalDate;   // ISO 8601格式日期时间

    // 可添加辅助方法
    public boolean isBorrow() {
        return "borrow".equalsIgnoreCase(borrowReturn);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public List<String> getSelectedSlots() {
        return selectedSlots;
    }

    public void setSelectedSlots(List<String> selectedSlots) {
        this.selectedSlots = selectedSlots;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean getBorrowReturn() {
        return isBorrowReturn;
    }

    public void setBorrowReturn(String borrowReturn) {
        this.borrowReturn = borrowReturn;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public void setBorrowReturn(Boolean borrowReturn) {
        isBorrowReturn = borrowReturn;
    }

    public List<Integer> getSelectedEquipment() {
        return selectedEquipment;
    }

    public void setSelectedEquipment(List<Integer> selectedEquipment) {
        this.selectedEquipment = selectedEquipment;
    }
}