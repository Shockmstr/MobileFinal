package hieubd.dto;

import java.io.Serializable;

public class PersonalTaskInfoDTO implements Serializable {
    private int id;
    private String name;
    private String source;
    private String description;
    private String handlingContent;
    private String status;
    private int creatorId;
    private Role creatorRole;
    private int workHandlerId;
    private String confirmation;
    private byte[] confirmationImage;

    public PersonalTaskInfoDTO() {

    }

    public PersonalTaskInfoDTO(int id, String name, String source, String description, String handlingContent, String status, int creatorId, Role creatorRole, int workHandlerId, String confirmation, byte[] confirmationImage) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.description = description;
        this.handlingContent = handlingContent;
        this.status = status;
        this.creatorId = creatorId;
        this.creatorRole = creatorRole;
        this.workHandlerId = workHandlerId;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public Role getCreatorRole() {
        return creatorRole;
    }

    public void setCreatorRole(Role creatorRole) {
        this.creatorRole = creatorRole;
    }

    public int getWorkHandlerId() {
        return workHandlerId;
    }

    public void setWorkHandlerId(int workHandlerId) {
        this.workHandlerId = workHandlerId;
    }

    public String getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }

    public byte[] getConfirmationImage() {
        return confirmationImage;
    }

    public void setConfirmationImage(byte[] confirmationImage) {
        this.confirmationImage = confirmationImage;
    }
}
