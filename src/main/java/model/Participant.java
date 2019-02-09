package model;

import converter.DateTimeConverter;
import dto.ParticipantDto;
import org.joda.time.DateTime;

import javax.persistence.*;
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

    protected Participant() {
        this.enabled = false;
    }

    public Participant(ParticipantDto participantDto) {
        this.email = participantDto.getEmail();
        this.password = participantDto.getPassword();
        this.fullName = participantDto.getFullName();
        this.birthdate = participantDto.getBirthdate();
        this.enabled = false;
    }

    public Participant(String email, String password, String fullName, DateTime birthdate) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.birthdate = birthdate;
        this.enabled = false;
    }

    public Participant(String email, String password) {
        this.email = email;
        this.password = password;
        this.enabled = false;
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
}
