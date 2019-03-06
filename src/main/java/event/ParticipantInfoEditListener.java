package event;

import dto.ParticipantDto;
import model.Application;
import model.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import service.DriveService;

import java.util.Collection;
import java.util.List;

@Component
public class ParticipantInfoEditListener implements ApplicationListener<OnParticipantInfoEditEvent> {
    @Autowired
    private DriveService driveService;

    @Override
    public void onApplicationEvent(OnParticipantInfoEditEvent onParticipantInfoEditEvent) {
        Participant participant = onParticipantInfoEditEvent.getParticipant();
        ParticipantDto newInfo = onParticipantInfoEditEvent.getParticipantDto();

        boolean changedEmail = !participant.getEmail().equals(newInfo.getEmail());
        boolean changedName = !participant.getFullName().equals(newInfo.getFullName());

        if (changedEmail)
            onEmailChange(participant, newInfo.getEmail());

        if(changedName)
            onNameChange(participant, newInfo.getFullName());
    }

    private void onEmailChange(Participant participant, String newEmail) {
        String password = participant.getPassword();

        Collection<SimpleGrantedAuthority> nowAuthorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder
                .getContext().getAuthentication().getAuthorities();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                newEmail,
                password,
                nowAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void onNameChange(Participant participant, String newName) {
        boolean hasUploadedFiles = participant.getFilesFolderId() != null;
        if (hasUploadedFiles)
            driveService.changeFileName(participant.getFilesFolderId(), newName);

        List<Application> applications = participant.getApplications();
        for(Application application : applications) {
            boolean hasFolder = application.getFilesFolderId() != null;
            if (hasFolder)
                driveService.changeFileName(application.getFilesFolderId(), newName);
        }
    }
}
