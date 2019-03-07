package model;

import contest.form.FileForm;
import contest.form.Form;
import contest.form.FormData;
import converter.DateTimeConverter;
import contest.form.Forms;
import org.joda.time.DateTime;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.*;

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

    @OneToMany(mappedBy = "contest", fetch = FetchType.EAGER)
    private List<Application> applications;

    @Lob
    private Forms forms;

    private String title;
    private String description;
    private String imgUrl;
    private String pageUrl;

    @Convert(converter = DateTimeConverter.class)
    private DateTime creatingTime;

    private String filesFolderId;

    protected Contest(){}

    public Contest(String name, List<Form> forms, String title, String description, String imgUrl, String pageUrl) {
        this.name = name;
        this.forms = new Forms(forms);
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.pageUrl = pageUrl;

        this.isActive = false;
        this.creatingTime = new DateTime();
    }

    public void validateData(Map<String, String[]> formsData, List<MultipartFile> files) {
        forms.validateForms(formsData);
        forms.validateFileForms(files);
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
            if(expirationTime != null && expirationTime.isBefore(DateTime.now())) {
                this.expirationTime = null;
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
            throw new RuntimeException("Expiration time must be to future.");
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

    public Form getForm(int id) {
        return forms.getForm(id);
    }

    public List<Form> getForms() {
        return forms.getForms();
    }

    public List<FileForm> getFileForms() {
        return forms.getFileForms();
    }

    public List<Form> getAllForms() {
        return forms.getAllForms();
    }

    public boolean hasFileForms() {
        return forms.hasFileForms();
    }

    public void setForms(List<Form> forms) {
        this.forms.setForms(forms);
    }

    public void addForms(List<Form> forms) {
        this.forms.addForms(forms);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getFilesFolderId() {
        return filesFolderId;
    }

    public void setFilesFolderId(String filesFolderId) {
        this.filesFolderId = filesFolderId;
    }
}
