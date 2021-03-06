package hieubd.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class PersonalWorkManagerDTO implements Serializable {
    private int id;
    private String managerDescription;
    private int managerMark;
    private Timestamp managerCommentBeginTime;

    public PersonalWorkManagerDTO() {
    }

    public PersonalWorkManagerDTO(int id, String managerDescription, int managerMark, Timestamp managerCommentBeginTime) {
        this.id = id;
        this.managerDescription = managerDescription;
        this.managerMark = managerMark;
        this.managerCommentBeginTime = managerCommentBeginTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getManagerDescription() {
        return managerDescription;
    }

    public void setManagerDescription(String managerDescription) {
        this.managerDescription = managerDescription;
    }

    public int getManagerMark() {
        return managerMark;
    }

    public void setManagerMark(int managerMark) {
        this.managerMark = managerMark;
    }

    public Timestamp getManagerCommentBeginTime() {
        return managerCommentBeginTime;
    }

    public void setManagerCommentBeginTime(Timestamp managerCommentBeginTime) {
        this.managerCommentBeginTime = managerCommentBeginTime;
    }
}
