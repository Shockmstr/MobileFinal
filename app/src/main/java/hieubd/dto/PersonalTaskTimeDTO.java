package hieubd.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class PersonalTaskTimeDTO implements Serializable, Comparable<PersonalTaskTimeDTO> {
    private int id;
    private Timestamp timeBegin;
    private Timestamp timeFinish;
    private Timestamp timeCreated;

    public PersonalTaskTimeDTO(int id, Timestamp timeBegin, Timestamp timeFinish, Timestamp timeCreated) {
        this.id = id;
        this.timeBegin = timeBegin;
        this.timeFinish = timeFinish;
        this.timeCreated = timeCreated;
    }

    public PersonalTaskTimeDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTimeBegin() {
        return timeBegin;
    }

    public void setTimeBegin(Timestamp timeBegin) {
        this.timeBegin = timeBegin;
    }

    public Timestamp getTimeFinish() {
        return timeFinish;
    }

    public void setTimeFinish(Timestamp timeFinish) {
        this.timeFinish = timeFinish;
    }

    public Timestamp getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Timestamp timeCreated) {
        this.timeCreated = timeCreated;
    }

    @Override
    public int compareTo(PersonalTaskTimeDTO timeDTO) {
        if (this.getTimeFinish().after(timeDTO.timeFinish)){
            return -1;
        }else if (this.getTimeFinish().before(timeDTO.timeFinish)){
            return 1;
        }else
        return 0;
    }
}
