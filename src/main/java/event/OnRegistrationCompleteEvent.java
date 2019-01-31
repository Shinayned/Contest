package event;

import model.Participant;
import org.springframework.context.ApplicationEvent;

public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private Participant participant;
    private String appUrl;

    public OnRegistrationCompleteEvent(Participant participant, String appUrl) {
        super(participant);

        this.participant = participant;
        this.appUrl = appUrl;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }
}