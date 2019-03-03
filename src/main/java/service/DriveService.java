package service;

import com.google.api.services.drive.model.File;
import contest.form.FormData;
import google.FileInfo;
import model.Contest;
import model.Participant;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

public interface DriveService {
    List<FileInfo> uploadFiles(String participantEmail, List<MultipartFile> uploadingFiles);
    void downloadFile(String fileId, OutputStream outputStream);
    List<FileInfo> getFileList(String participantEmail);
    void deleteFile(String participantEmail, String fileId);
    FileInfo uploadFile(String name, String folderId, MultipartFile file);
    File createFolder(String name);
    File createFolder(String parentFolderId, String name);
    String getLinkFor(String fileId);
    void closeAccessTo(String fileId);
}
