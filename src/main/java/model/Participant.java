package model;

import converter.DateTimeConverter;
import google.FileInfo;
import dto.ParticipantDto;
import org.hibernate.annotations.Fetch;
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

    private String phoneNumber;
    private String organization;
    private String workAddress;
    private String scienceWorks;
    private String position;

    @OneToMany(mappedBy = "participant", fetch = FetchType.EAGER)
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

        this.phoneNumber = phoneNumber;
        this.organization = participantDto.getOrganization();
        this.workAddress = participantDto.getWorkAddress();
        this.scienceWorks = participantDto.getScienceWorks();
        this.position = participantDto.getPosition();
    }

    public Participant(String email, String password, String fullName, DateTime birthdate) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.birthdate = birthdate;

        this.enabled = false;
        this.uploadedFiles = new ArrayList<>();
    }

    public long getId() {
        return id;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getScienceWorks() {
        return scienceWorks;
    }

    public void setScienceWorks(String scienceWorks) {
        this.scienceWorks = scienceWorks;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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

    public void removeUploadedFile(String fileId) {
        for(FileInfo file : uploadedFiles) {
            if(file.getId().equals(fileId)) {
                uploadedFiles.remove(file);
                return;
            }
        }
    }

    public void setUploadedFiles(ArrayList<FileInfo> uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void updateInfo(ParticipantDto participantDto) {
        this.fullName = participantDto.getFullName();
        this.birthdate = participantDto.getBirthdate();
        this.phoneNumber = participantDto.getPhone();
        this.email = participantDto.getEmail();
        this.organization = participantDto.getOrganization();
        this.position = participantDto.getPosition();
        this.workAddress = participantDto.getWorkAddress();
        this.scienceWorks = participantDto.getScienceWorks();
    }
}
