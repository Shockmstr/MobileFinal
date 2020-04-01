package hieubd.dto;

import java.io.Serializable;

public class PersonalTaskInfoDTO implements Serializable {
    private int id;
    private String name;
    private String description;
    private String handlingContent;
    private String status;
    private String creator;
    private Role creatorRole;
    private String taskHandler;
    private String confirmation;
    private String confirmationImage;

    public PersonalTaskInfoDTO() {

    }

    public PersonalTaskInfoDTO(String name, String description, String handlingContent, String status, String creator, String taskHandler, String confirmation, String confirmationImage) {
        this.name = name;
        this.description = description;
        this.handlingContent = handlingContent;
        this.status = status;
        this.creator = creator;
        this.taskHandler = taskHandler;
        this.confirmation = confirmation;
        this.confirmationImage = confirmationImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHandlingContent() {
        return handlingContent;
    }

    public void setHandlingContent(String handlingContent) {
        this.handlingContent = handlingContent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getTaskHandler() {
        return taskHandler;
    }

    public void setTaskHandler(String taskHandler) {
        this.taskHandler = taskHandler;
    }

    public String getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }

    public String getConfirmationImage() {
        return confirmationImage;
    }

    public void setConfirmationImage(String confirmationImage) {
        this.confirmationImage = confirmationImage;
    }

    public Role getCreatorRole() {
        return creatorRole;
    }

    public void setCreatorRole(Role creatorRole) {
        this.creatorRole = creatorRole;
    }
}
