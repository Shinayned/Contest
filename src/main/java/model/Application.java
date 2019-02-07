package model;

import converter.DateTimeConverter;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

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

    @ElementCollection
    @MapKeyColumn(name="field")
    @Column(name="value")
    @CollectionTable(name="application_data", joinColumns=@JoinColumn(name="id"))
    private Map<String, String> data;

    @Convert(converter = DateTimeConverter.class)
    private DateTime fillingDate;
    private boolean isVerified;

    public Application(Contest contest,Participant participant, Map<String, String> data) {
        this.contest = contest;
        this.participant = participant;
        isVerified = false;
        data = fillDataFields(contest.getFields(), data);

        fillingDate = new DateTime();
    }

    private static Map<String, String> fillDataFields(Map<String, String> fields, Map<String, String> data) {
        Map<String, String> result = new HashMap<>();

        fields.forEach((fieldName, fieldType) -> {
            String fieldData = data.getOrDefault(fieldName, "");
            result.put(fieldName, fieldData);
        });

        return result;
    }

    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Map<String, String> getData() {
        return new HashMap<>(data);
    }

    public void setData(Map<String, String> data) {
        this.data = data;
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
