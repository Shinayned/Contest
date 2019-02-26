package model;

import contest.form.Form;
import converter.DateTimeConverter;
import contest.form.Forms;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Lob
    private Forms forms;

    // ************ IS NOT USING. DEVELOPING IS FROZEN ************
    //private ContestPage pageData;

    private String page;

    @Convert(converter = DateTimeConverter.class)
    private DateTime creatingTime;

    private String filesFolderId;

    protected Contest(){}

    public Contest(String name, List<Form> forms, String pageName) {
        this.name = name;
        this.forms = new Forms(forms);
        this.page = pageName;

        this.isActive = false;
        this.creatingTime = new DateTime();
    }

    public long getId() {
        return id;
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
        return new ArrayList<>(applications);
    }

    public void setApplications(List<Application> applications) {
        this.applications = new ArrayList<>(applications);
    }

    public List<Form> getForms() {
        return forms.getAllForms();
    }

    public void setForms(List<Form> forms) {
        this.forms.setForms(forms);
    }

    public void addForms(List<Form> forms) {
        this.forms.addForms(forms);
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getFilesFolderId() {
        return filesFolderId;
    }

    public void setFilesFolderId(String filesFolderId) {
        this.filesFolderId = filesFolderId;
    }
}
