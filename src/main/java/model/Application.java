package model;

import contest.form.FormData;
import converter.DateTimeAttributeConverter;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "Applications")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "contest_id")
    private Contest contest;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;

    @Lob
    private ArrayList<FormData> data;

    @Convert(converter = DateTimeAttributeConverter.class)
    private DateTime fillingDate;

    private String filesFolderId;
    private boolean isVerified;

    protected Application() {}

    public Application(Contest contest, Participant participant) {
        this(contest, participant, null);
    }

    public Application(Contest contest, Participant participant, List<FormData> data) {
        this.contest = contest;
        this.participant = participant;
        this.isVerified = false;
        this.data = data == null ? new ArrayList<>() : new ArrayList<>(data);

        this.fillingDate = new DateTime();
    }

    public Contest getContest() {
        return contest;
    }

    public Participant getParticipant() {
        return participant;
    }

    public List<FormData> getData() {
        return new ArrayList<>(data);
    }

    public void setData(List<FormData> data) {
        this.data = new ArrayList<>(data);
    }

    public String getFilesFolderId() {
        return filesFolderId;
    }

    public void setFilesFolderId(String filesFolderId) {
        this.filesFolderId = filesFolderId;
    }

    public DateTime getFillingDate() {
        return fillingDate;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
}
