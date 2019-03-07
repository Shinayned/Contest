package model;

import converter.DateTimeAttributeConverter;
import enums.TokenType;
import org.joda.time.DateTime;
import javax.persistence.Id;

import javax.persistence.*;

@Entity
@Table(name = "Tokens")
public class Token {
    private static final int EXPIRATION = 24;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String token;
    private TokenType type;

    @OneToOne(targetEntity = Participant.class)
    @JoinColumn(nullable = false, name = "participant_id")
    private Participant participant;

    @Convert(converter = DateTimeAttributeConverter.class)
    private DateTime expiryDate;

    protected Token() {
    }

    public Token(Participant participant, TokenType type, String token) {
        this.participant = participant;
        this.type = type;
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

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
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

    public boolean isExpired() {
        return expiryDate.isBefore(DateTime.now());
    }

    public void setExpiryDate(DateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
}
