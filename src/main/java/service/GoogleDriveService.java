package service;

import com.google.api.services.drive.model.File;
import contest.form.FileForm;
import contest.form.FormData;
import google.FileInfo;
import google.GoogleDrive;
import model.Contest;
import model.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleDriveService implements DriveService {
    @Autowired
    private ParticipantService participantService;

    @Autowired
    private GoogleDrive googleDrive;

    @Override
    public List<FileInfo> uploadFiles(String participantEmail, List<MultipartFile> uploadingFiles) {
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
            File folder = googleDrive.createFolder(participant.getFullName());
            folderId = folder.getId();

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
    public void changeFileName(String fileId, String newName) {
        googleDrive.changeFileName(fileId, newName);
    }

    @Override
    public List<FileInfo> getFileList(String participantEmail) {
        Participant participant = participantService.getParticipantByEmail(participantEmail);

        return participant.getUploadedFiles();
    }

    @Override
    public void deleteFile(String participantEmail, String fileId) {
        googleDrive.deleteFile(fileId);
        Participant participant = participantService.getParticipantByEmail(participantEmail);
        participant.removeUploadedFile(fileId);
        participantService.updateAccount(participant);
    }

    @Override
    public FileInfo uploadFile(String name, String folderId, MultipartFile file) {
        return googleDrive.uploadFile(name, folderId, file);
    }

    @Override
    public File createFolder(String name) {
        return googleDrive.createFolder(name);
    }

    @Override
    public File createFolder(String parentFolderId, String name) {
        return googleDrive.createFolder(parentFolderId, name);
    }

    @Override
    public String getLinkFor(String fileId) {
        return googleDrive.createLink(fileId);
    }

    @Override
    public void closeAccessTo(String fileId) {
        googleDrive.closeAccess(fileId);
    }
}
