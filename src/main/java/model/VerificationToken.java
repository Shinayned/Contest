package model;

import converter.DateTimeConverter;
import org.joda.time.DateTime;
import javax.persistence.Id;

import javax.persistence.*;

@Entity
@Table(name = "VerificationTokens")
public class VerificationToken {
    private static final int EXPIRATION = 24;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String token;

    @OneToOne(targetEntity = Participant.class)
    @JoinColumn(nullable = false, name = "participant_id")
    private Participant participant;

    @Convert(converter = DateTimeConverter.class)
    private DateTime expiryDate;

    protected VerificationToken() {
    }

    public VerificationToken(Participant participant, String token) {
        this.participant = participant;
        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    public void updateToken(String token) {
        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    private DateTime calculateExpiryDate(int expiryHours) {
        DateTime expiryDate = new DateTime().plusHours(expiryHours);
        return expiryDate;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public DateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(DateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
}
