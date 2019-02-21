package model;

import converter.DateTimeConverter;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Contests")
public class Contest {
    @Id
    private String name;
    private boolean isActive;

    @Convert(converter = DateTimeConverter.class)
    private DateTime expirationTime;

    @OneToMany(mappedBy = "contest")
    private List<Application> applications;

    @ElementCollection
    private List<ContestField> fields;

    private ContestPage pageData;

    protected Contest(){}

    public Contest(String name, List<ContestField> fields, ContestPage pageData) {
        this.name = name;
        this.fields = new ArrayList<>(fields);
        this.pageData = pageData;

        this.isActive = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        if(expirationTime == null) {
            return isActive;
        }

        return isActive = DateTime.now().isBefore(expirationTime);
    }


    public void setActive(boolean active) {
        if(active) {
            if(expirationTime.isBefore(DateTime.now())) {
                throw new RuntimeException("Expiration time is out.");
            }
            this.isActive = active;
        } else {
            this.expirationTime = null;
            this.isActive = false;
        }
    }

    public DateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(DateTime expirationTime) {
        if(expirationTime.isBefore(DateTime.now())) {
            throw new RuntimeException("Expiration time is out.");
        } else {
            this.expirationTime = expirationTime;
        }
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public List<ContestField> getFields() {
        return new ArrayList<>(fields);
    }

    public void setFields(List<ContestField> fields) {
        this.fields = fields;
    }

    public ContestPage getPageData() {
        return pageData;
    }

    public void setPageData(ContestPage pageData) {
        this.pageData = pageData;
    }


}
