package model;

import contest.form.Form;
import contest.form.FormData;
import converter.DateTimeConverter;
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

    @Convert(converter = DateTimeConverter.class)
    private DateTime fillingDate;
    private boolean isVerified;

    protected Application() {}

    public Application(Contest contest, Participant participant, List<FormData> data) {
        this.contest = contest;
        this.participant = participant;
        this.isVerified = false;
        this.data = new ArrayList<>(data);

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

    public void setData(ArrayList<FormData> data) {
        this.data = new ArrayList<>(data);
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
