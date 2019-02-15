package service;

import com.google.api.services.drive.model.File;
import google.FileInfo;
import google.GoogleDrive;
import model.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
public class GoogleDriveService implements DriveService {
    @Autowired
    private ParticipantService participantService;

    @Autowired
    private GoogleDrive googleDrive;

    @Override
    public List<FileInfo> uploadFiles(String participantEmail, MultipartFile[] uploadingFiles) {
        Participant participant = participantService.getParticipantByEmail(participantEmail);

        String participantFolder = getParticipantFolderId(participant);
        List<FileInfo> uploadedFiles = googleDrive.uploadFiles(participantFolder, uploadingFiles);

        if (!uploadedFiles.isEmpty()) {
            for (FileInfo uploadedFile : uploadedFiles) {
                participant.addUploadedFile(uploadedFile);
            }

            participantService.updateAccount(participant);
        }

        return uploadedFiles;
    }

    private String getParticipantFolderId(Participant participant) {
        String folderId;

        if (participant.getFilesFolderId() == null) {
            folderId = googleDrive.createFolder(participant.getFullName());

            participant.setFilesFolderId(folderId);
            participantService.updateAccount(participant);
        } else {
            File folder = googleDrive.downloadFile(participant.getFilesFolderId());
            folderId = folder.getId();
        }

        return folderId;
    }

    @Override
    public void downloadFile(String fileId, OutputStream outputStream) {
        googleDrive.downloadFileToInput(fileId, outputStream);
    }

    @Override
    public List<FileInfo> getFileList(String participantEmail) {
        Participant participant = participantService.getParticipantByEmail(participantEmail);

        return participant.getUploadedFiles();
    }

    @Override
    public void deleteFile(String fileId) {
        googleDrive.deleteFile(fileId);
    }
}
