package event;

import dto.ParticipantDto;
import model.Participant;
import org.springframework.context.ApplicationEvent;

public class OnParticipantInfoEditEvent extends ApplicationEvent {
    private Participant participant;
    private ParticipantDto participantDto;


    public OnParticipantInfoEditEvent(Participant participant, ParticipantDto participantDto) {
        super(participant);
        this.participant = participant;
        this.participantDto = participantDto;
    }

    public Participant getParticipant() {
        return participant;
    }

    public ParticipantDto getParticipantDto() {
        return participantDto;
    }
}
