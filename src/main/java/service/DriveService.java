package service;

import contest.form.FormData;
import google.FileInfo;
import model.Contest;
import model.Participant;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

public interface DriveService {
    List<FileInfo> uploadFiles(String participantEmail, MultipartFile[] uploadingFiles);
    void downloadFile(String fileId, OutputStream outputStream);
    List<FileInfo> getFileList(String participantEmail);
    void deleteFile(String participantEmail, String fileId);
    List<FormData> uploadApplicationFiles(Contest contest, Participant participant, List<MultipartFile> uploadingFiles);
}
