package io.cjf.lianxi0509.dto;

public class ChangeUserPasswordDTO {
    private String username;
    private String newPwd;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}
