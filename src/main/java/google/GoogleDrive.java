package google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import model.Participant;
import org.hibernate.boot.jaxb.SourceType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Folder;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;

public class GoogleDrive {
    private final String APPLICATION_NAME;
    private final JsonFactory JSON_FACTORY;
    private final java.io.File CREDENTIALS_FOLDER;
    private final String CLIENT_FILE_NAME;
    private final List<String> SCOPES;

    @Value("${google.folder.root.id}")
    private String rootFolderId;

    private Drive service;

    public GoogleDrive(String applicationName,
                       JsonFactory jsonFactory,
                       java.io.File credentialsFolder,
                       String clientFileName,
                       List<String> scopes) throws IOException, GeneralSecurityException {
        this.APPLICATION_NAME = applicationName;
        this.JSON_FACTORY = jsonFactory;
        this.CREDENTIALS_FOLDER = credentialsFolder;
        this.CLIENT_FILE_NAME = clientFileName;
        this.SCOPES = scopes;

        service = getDriveService();
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        java.io.File clientSecretFilePath = new java.io.File(CREDENTIALS_FOLDER, CLIENT_FILE_NAME);

        if (!clientSecretFilePath.exists()) {
            throw new FileNotFoundException("Please copy " + CLIENT_FILE_NAME //
                    + " to folder: " + CREDENTIALS_FOLDER.getAbsolutePath());
        }

        InputStream in = new FileInputStream(clientSecretFilePath);

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                clientSecrets, SCOPES).setDataStoreFactory(new FileDataStoreFactory(CREDENTIALS_FOLDER))
                .setAccessType("offline").build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("systemAccount");
    }

    private Drive getDriveService() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = getCredentials(HTTP_TRANSPORT);
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential) //
                .setApplicationName(APPLICATION_NAME).build();
    }

    public List<FileInfo> uploadFiles(String parentFolderId, List<MultipartFile> files) {
        List<FileInfo> uploadedFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            FileInfo fileInfo = uploadFile(parentFolderId, file);
            uploadedFiles.add(fileInfo);
        }

        return uploadedFiles;
    }

    public FileInfo uploadFile(String name, String parentFolderId, MultipartFile file) {
        File metaData = new File();

        if (name != null)
            metaData.setName(name);
        else
            metaData.setName(file.getOriginalFilename());

        metaData.setParents(Collections.singletonList(parentFolderId));

        try {
            File uploadedFile = service.files().create(metaData, new ByteArrayContent(file.getContentType(), file.getBytes()))
                    .execute();
            FileInfo uploadedFileInfo = new FileInfo(uploadedFile.getName(), uploadedFile.getId(), file.getSize());

            return uploadedFileInfo;
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
        return null;
    }

    public FileInfo uploadFile(String parentFolderId, MultipartFile file) {
        return uploadFile(null, parentFolderId, file);
    }

    public File createFolder(String name) {
        return createFolder(null, name);
    }

    public File createFolder(String parentFolderId, String name) {
        File folder = null;

        List<String> parentFolders = new ArrayList<>();
        parentFolders.add(rootFolderId);

        try {
            folder = new File()
                    .setName(name)
                    .setMimeType("application/vnd.google-apps.folder");
            if (parentFolderId != null)
                folder.setParents(Collections.singletonList(parentFolderId));
            else
                folder.setParents(Collections.singletonList(rootFolderId));

            folder = service.files().create(folder)
                    .execute();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }

        return folder;
    }

    public void changeFileName(String fileId, String newName) {
        try {
            File newMetaData = new File()
                    .setName(newName);

            service.files().update(fileId, newMetaData).execute();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }

    }

    public File downloadFile(String fileId) {
        File file = null;

        try {
            file = service.files().get(fileId)
                    .execute();

            if (file == null) {
                throw new RuntimeException("File with id: " + fileId + " is not found.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }

        return file;
    }

    public void downloadFileToInput(String fileId, OutputStream outputStream) {
        try {
            service.files().get(fileId)
                    .executeMediaAndDownloadTo(outputStream);
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
    }

    public void deleteFile(String fileId) {
        try {
            service.files().delete(fileId).execute();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
    }

    public List<FileInfo> getFolderFiles(String folderId) throws Exception {
        Drive service = getDriveService();

        FileList result = service.files().list()
                .setQ("'" + folderId + "' in parents")
                .execute();

        List<FileInfo> fileList = new ArrayList<>();
        for (File file : result.getFiles()) {
            String name = file.getName();
            String id = file.getId();
            long size;
            if (file.getQuotaBytesUsed() == null) {
                size = 0;
            } else {
                size = file.getQuotaBytesUsed();
            }
            System.out.println(file.getQuotaBytesUsed() + "   " + file.getSize());

            FileInfo fileDto = new FileInfo(name, id, size);
            fileList.add(fileDto);
        }

        return fileList;
    }

    public String createLink(String folderId) {
        String link = "";
        try {
            Permission permission = new Permission();
            permission.setType("anyone");
            permission.setRole("reader");
            service.permissions().create(folderId, permission).execute();

            File file = service.files().get(folderId).setFields("webViewLink").execute();
            link = file.getWebViewLink();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
        return link;
    }

    public void closeAccess(String folderId) {
        try {
            PermissionList permissionList = service.permissions().list(folderId).execute();
            List<Permission> permissions = permissionList.getPermissions();

            for (Permission permission : permissions) {
                service.permissions().delete(folderId, permission.getId()).execute();
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
    }
}