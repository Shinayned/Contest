package model;

import converter.DateTimeConverter;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "Contests")
public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private boolean isActive;

    @Convert(converter = DateTimeConverter.class)
    private DateTime expirationTime;

    @OneToMany(mappedBy = "contest")
    private List<Application> applications;

    @ElementCollection
    @MapKeyColumn(name="name")
    @Column(name="type")
    @CollectionTable(name="contest_fields", joinColumns=@JoinColumn(name="id"))
    private Map<String, String> fields;

    public Contest(String name, Map<String, String> fields) {
        this.name = name;
        this.fields = new HashMap<>(fields);
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

        DateTime currentTime = new DateTime();
        return isActive = currentTime.isBefore(expirationTime);
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public DateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(DateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public Map<String, String> getFields() {
        return new HashMap<>(fields);
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }
}
