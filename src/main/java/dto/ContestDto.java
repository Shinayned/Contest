package dto;

import model.Contest;
import org.joda.time.DateTime;

public class ContestDto {
    private long id;
    private String name;
    private String description;
    private int amountOfApplications;
    private DateTime expirationTime;

    public ContestDto(Contest contest) {
        this.id = contest.getId();
        this.name = contest.getName();
        this.description = contest.getDescription();
        this.amountOfApplications = contest.getApplications().size();
        this.expirationTime = contest.getExpirationTime();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public int getAmountOfApplications() {
        return amountOfApplications;
    }

    public void setAmountOfApplications(int amountOfApplications) {
        this.amountOfApplications = amountOfApplications;
    }

    public DateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(DateTime expirationTime) {
        this.expirationTime = expirationTime;
    }
}
