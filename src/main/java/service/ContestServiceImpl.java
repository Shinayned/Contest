package service;

import com.google.api.services.drive.model.File;
import javax.mail.util.ByteArrayDataSource;
import contest.form.FileForm;
import contest.form.Form;
import contest.form.FormData;
import contest.form.enums.FormType;
import email.EmailService;
import exception.BadRequestException;
import exception.DuplicateException;
import exception.ResourceNotFoundException;
import google.FileInfo;
import model.*;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import repository.ApplicationRepository;
import repository.ContestRepository;

import javax.activation.DataSource;
import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.*;

@Service
public class ContestServiceImpl implements ContestService {
    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private DriveService driveService;

    @Autowired
    private EmailService emailService;

    @Override
    public String getPageUrl(long contestId) throws ResourceNotFoundException {
        Contest contest = getContest(contestId);

        return contest.getPageUrl();
    }

    @Override
    public List<Form> getForms(long contestId) throws ResourceNotFoundException {
        Contest contest = getContest(contestId);

        return contest.getForms();
    }

    @Override
    public List<Form> getAllForms(long contestId) throws ResourceNotFoundException {
        Contest contest = getContest(contestId);

        return contest.getAllForms();
    }

    @Override
    @Transactional
    public void submitApplication(long contestId, String participantEmail, Map<String, String[]> formsData, List<MultipartFile> files)
            throws ResourceNotFoundException, DuplicateException, BadRequestException, DisabledException{
        Contest contest = getContest(contestId);
        if (!contest.isActive())
            throw new DisabledException("Contest " + contest.getName() + " is not active");

        Participant participant = participantService.getParticipantByEmail(participantEmail);

        checkForDuplicateSubmit(participant, contestId);

        try {
            contest.validateData(formsData, files);
        } catch (InvalidParameterException exception) {
            throw new BadRequestException(exception.getMessage());
        }

        Application application = new Application(contest, participant);

        List<FormData> applicationData = new ArrayList<>();
        applicationData.addAll(createFormDataList(contest, formsData));

        if (files != null && !files.isEmpty())
            applicationData.addAll(uploadFiles(application, contest, participant, files));

        application.setData(applicationData);
        applicationRepository.save(application);
    }

    @Override
    public void createContest(Contest contest) throws DuplicateException {
        if (contest.hasFileForms()) {
            File contestFolder = driveService.createFolder(contest.getName());
            String folderId = contestFolder.getId();
            contest.setFilesFolderId(folderId);
        }
        contestRepository.save(contest);
    }

    @Override
    public Contest getContest(long contestId) throws ResourceNotFoundException {
        Contest contest = contestRepository.findById(contestId).get();
        if (contest == null)
            throw new ResourceNotFoundException("Contest №" + contestId + " is not exist.");

        return contest;
    }

    @Override
    public void sendContestApplications(long contestId, String sendToEmail) throws ResourceNotFoundException, IOException{
        Contest contest = getContest(contestId);

        Workbook excel = parseApplicationsToExcel(contest);

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        excel.write(byteOut); // write excel data to a byte array
        byteOut.close();

        DataSource excelData = new ByteArrayDataSource(byteOut.toByteArray(), "application/vnd.ms-excel");
        String excelFileName = contest.getName() + "(" + new DateTime() + ")" + ".xlsx";

        String messageText = "Данні заявок учасників конкурсу \"" + contest.getName() + "\".";

        String contestFolder = contest.getFilesFolderId();
        if (contestFolder != null) {
            String filesUrl = driveService.getLinkFor(contestFolder);
            messageText = messageText.concat("/nФайли учасників: " + filesUrl);
        }

        emailService.sendMessageWithAttachment(sendToEmail, contest.getName(), messageText, excelFileName, excelData);
    }

    @Override
    public List<Contest> getAllContests() {
        Iterable<Contest> iterable = contestRepository.findAll();
        return IteratorUtils.toList(iterable.iterator());
    }

    @Override
    public boolean closeOpenContest(long contestId) {
        Contest contest = contestRepository.findById(contestId).get();

        if(contest == null)
            return false;

        contest.setActive(!contest.isActive());
        contestRepository.save(contest);
        return true;
    }

    @Override
    public boolean setExpirationTime(long contestId, DateTime expirationTime) {
        Contest contest = contestRepository.findById(contestId).get();

        if (contest == null)
            return false;

        contest.setExpirationTime(expirationTime);
        contestRepository.save(contest);
        return true;
    }

    private Workbook parseApplicationsToExcel(Contest contest) {
        List<Application> applications = contest.getApplications();
        List<Form> forms = contest.getForms();
        String contestName = contest.getName();

        Workbook workbook = new XSSFWorkbook ();
        Sheet sheet = workbook.createSheet(contestName);

        int columns = createExcelRawHead(workbook, forms);
        fillExcel(workbook, applications);

        for(int cellIndex = 0; cellIndex < columns; cellIndex++)
            sheet.autoSizeColumn(cellIndex);

        return workbook;
    }

    private int createExcelRawHead(Workbook workbook, List<Form> forms) {
        Sheet sheet = workbook.getSheetAt(0);
        Row rowHead = sheet.createRow(0);

        CellStyle headStyle = workbook.createCellStyle();
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        headStyle.setFont(boldFont);
        headStyle.setAlignment(HorizontalAlignment.CENTER);

        int cellIndex = 0;

        rowHead.createCell(cellIndex++)
                .setCellValue("П.І.Б");
        rowHead.createCell(cellIndex++)
                .setCellValue("Емейл");
        rowHead.createCell(cellIndex++)
                .setCellValue("Дата народження");

        for (Form form : forms) {
            rowHead.createCell(cellIndex++)
                    .setCellValue(form.getName());
        }

        rowHead.forEach( cell -> {
            cell.setCellStyle(headStyle);
        });

        return cellIndex - 1;
    }

    private void fillExcel(Workbook workbook, List<Application> applications) {
        Sheet sheet = workbook.getSheetAt(0);
        int rowIndex = 1;
        int cellIndex = 0;

        for (Application application : applications) {
            Row row = sheet.createRow(rowIndex++);

            Participant participant = application.getParticipant();
            row.createCell(cellIndex++)
                    .setCellValue(participant.getFullName());
            row.createCell(cellIndex++)
                    .setCellValue(participant.getEmail());
            row.createCell(cellIndex++)
                    .setCellValue(participant.getBirthdate().toString("yyyy-MM-dd"));

            List<FormData> applicationData = application.getData();
            for (FormData formData : applicationData){
                if (formData.getType() == FormType.FILE)
                    continue;

                Cell cell = row.createCell(cellIndex++);
                cell.setCellValue(formData.toString());
            }
        }
    }

    private void checkForDuplicateSubmit(Participant participant, long contestId) throws DuplicateException{
        List<Application> applications = participant.getApplications();

        for (Application application : applications) {
            Contest contest = application.getContest();

            if (contest.getId() == contestId) {
                throw new DuplicateException(participant.getEmail() + " has already submitted application for " + contest.getName());
            }
        }
    }

    private List<FormData> createFormDataList(Contest contest, Map<String, String[]> formsData) {
        List<FormData> formDataList = new ArrayList<>();

        formsData.forEach((key, data) -> {
            int formId = Integer.parseInt(key);
            Form form = contest.getForm(formId);

            FormData formData = new FormData(formId, form.getType(), data);
            formDataList.add(formData);
        });

        return formDataList;
    }

    private List<FormData> uploadFiles(Application application, Contest contest, Participant participant, List<MultipartFile> files) {
        String contestFolder = contest.getFilesFolderId();
        File fileFolder = driveService.createFolder(contestFolder, participant.getFullName());
        application.setFilesFolderId(fileFolder.getId());

        List<FileForm> fileForms = contest.getFileForms();
        List<FormData> fileLinks = new ArrayList<>();

        for(FileForm form : fileForms) {
            String name = form.getName();
            String formId = Integer.toString(form.getId());
            String fileId;

            for(MultipartFile file : files) {
                fileId = file.getName();

                if (formId.equals(fileId)) {
                    FileInfo fileInfo = driveService.uploadFile(name, fileFolder.getId(), file);
                    List<String> fileLink = Collections.singletonList(fileInfo.getId());

                    FormData fileData = new FormData(form.getId(), form.getType(),  fileLink);
                    fileLinks.add(fileData);
                }
            }
        }
        return fileLinks;
    }
}