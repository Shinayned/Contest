package model;

import converter.DateTimeConverter;
import google.FileInfo;
import dto.ParticipantDto;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Participants")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String email;
    private String password;

    private String fullName;

    @Convert(converter = DateTimeConverter.class)
    private DateTime birthdate;

    @OneToMany(mappedBy = "participant")
    private List<Application> applications;

    private boolean enabled;
    private String filesFolderId;

    @Lob
    private ArrayList<FileInfo> uploadedFiles;

    protected Participant() {
    }

    public Participant(ParticipantDto participantDto) {
        this(participantDto.getEmail(),
                participantDto.getPassword(),
                participantDto.getFullName(),
                participantDto.getBirthdate());
    }

    public Participant(String email, String password, String fullName, DateTime birthdate) {
        this(email, password);

        this.fullName = fullName;
        this.birthdate = birthdate;
    }

    public Participant(String email, String password) {
        this.email = email;
        this.password = password;

        this.enabled = false;
        this.uploadedFiles = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public DateTime getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(DateTime birthdate) {
        this.birthdate = birthdate;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFilesFolderId() {
        return filesFolderId;
    }

    public void setFilesFolderId(String filesFolderId) {
        this.filesFolderId = filesFolderId;
    }

    public ArrayList<FileInfo> getUploadedFiles() {
        return new ArrayList<>(uploadedFiles);
    }

    public void addUploadedFile(FileInfo fileInfo) {
        uploadedFiles.add(fileInfo);
    }

    public void setUploadedFiles(ArrayList<FileInfo> uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }

    public List<Application> getApplications() {
        return applications;
    }
}
