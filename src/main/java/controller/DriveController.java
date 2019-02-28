package controller;

import google.FileInfo;
import google.GoogleDrive;
import model.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import service.DriveService;
import service.ParticipantService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Array;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class DriveController {
    @Autowired
    private ParticipantService participantService;

    @Autowired
    private DriveService driveService;


    @RequestMapping("/drive")
    public String onDrive() {
        return "drive";
    }

    @PostMapping("/drive/upload")
    @ResponseBody
    public List<FileInfo> onUpload(@RequestParam("uploadingFiles") MultipartFile[] uploadingFiles, HttpServletRequest request, Principal principal) throws Exception{
        String participantEmail = principal.getName();
        List<FileInfo> uploadedFiles = driveService.uploadFiles(participantEmail, Arrays.asList(uploadingFiles));
        for(FileInfo file : uploadedFiles) {
            System.out.println(file);
        }
        return uploadedFiles;
    }

    @GetMapping("/drive/download")
    public void onDownload(@RequestParam(name = "id") String fileId,
                           Principal principal,
                           HttpServletResponse response) {
        try {
            OutputStream answerStream = response.getOutputStream();
            driveService.downloadFile(fileId, answerStream);
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/drive/fileList")
    @ResponseBody
    public List<FileInfo> onFileList(Principal principal) {
        String participantEmail = principal.getName();
        List<FileInfo> fileList = driveService.getFileList(participantEmail);

        return fileList;
    }

    @GetMapping("/drive/remove")
    @ResponseBody
    public void onRemove(@RequestParam(value = "id") String fileId, Principal principal) {
        String participantEmail = principal.getName();

        driveService.deleteFile(participantEmail, fileId);
    }
}
