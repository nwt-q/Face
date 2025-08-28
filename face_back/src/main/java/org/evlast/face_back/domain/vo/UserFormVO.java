package org.evlast.face_back.domain.vo;

import lombok.Data;
import org.evlast.face_back.domain.Users;

@Data
public class UserFormVO {
    private Integer id;
    private String username;
    private String password;
    private String status;
    // 可根据实际需求添加其他字段
    //邮箱 电话号码
    private String email;
    private String phone;

    public Users toUser() {
        Users user = new Users();
        user.setId(this.id);
        user.setUsername(this.username);
        user.setPassword(this.password);
        user.setStatus(this.status);
        user.setEmail(this.email);
        user.setPhone(this.phone);
        return user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}