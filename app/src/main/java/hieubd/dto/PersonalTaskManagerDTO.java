package hieubd.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class PersonalTaskManagerDTO implements Serializable {
    private int id;
    private String managerComment;
    private int managerMark;
    private Timestamp managerCommentBeginTime;

    public PersonalTaskManagerDTO() {
    }

    public PersonalTaskManagerDTO(int id, String managerComment, int managerMark, Timestamp managerCommentBeginTime) {
        this.id = id;
        this.managerComment = managerComment;
        this.managerMark = managerMark;
        this.managerCommentBeginTime = managerCommentBeginTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getManagerComment() {
        return managerComment;
    }

    public void setManagerComment(String managerComment) {
        this.managerComment = managerComment;
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
