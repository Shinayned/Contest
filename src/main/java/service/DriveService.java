package service;

import google.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

public interface DriveService {
    List<FileInfo> uploadFiles(String participantEmail, MultipartFile[] uploadingFiles);
    void downloadFile(String fileId, OutputStream outputStream);
    List<FileInfo> getFileList(String participantEmail);
    void deleteFile(String fileId);
}
