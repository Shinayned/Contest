package model;

import converter.DateTimeConverter;
import enums.FieldType;
import exception.FieldException;
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
    private HashMap<String, ArrayList<String>> data;

    @Convert(converter = DateTimeConverter.class)
    private DateTime fillingDate;
    private boolean isVerified;

    protected Application() {}

    public Application(Contest contest,Participant participant, Map<String, String[]> filledForms) {
        this.contest = contest;
        this.participant = participant;
        this.isVerified = false;
        this.data = fillDataFields(contest.getFields(), filledForms);

        this.fillingDate = new DateTime();
    }

    private static HashMap<String, ArrayList<String>> fillDataFields(List<ContestField> fields, Map<String, String[]> filledForms) throws FieldException {
        HashMap<String, ArrayList<String>> result = new HashMap<>();

        for (ContestField field : fields) {
            String fieldName = field.getFieldName();
            FieldType fieldType = field.getFiledType();
            String[] formData = filledForms.get(fieldName);

            if(formData == null) {
                if (formData == null && field.isObligatory()) {
                    throw new FieldException("Field '" + field.getFieldName() + "' is obligatory.");
                }
            } else {
                switch (fieldType) {
                    case STRING:
                        ArrayList<String> singleParameter = new ArrayList<>();
                        singleParameter.add(formData[0]);

                        result.put(fieldName, singleParameter);
                        break;
                    case ARRAY:
                        result.put(fieldName, new ArrayList<>(Arrays.asList(formData)));
                        break;
                }
            }
        }

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

    public HashMap<String, ArrayList<String>> getData() {
        return new HashMap<>(data);
    }

    public void setData(HashMap<String, ArrayList<String>> data) {
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
