package contest.form;

import contest.form.enums.FormType;
import exception.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.*;

public class Forms implements Serializable {
    private List<Form> sortedForms;

    private List<FileForm> fileForms;
    private List<Form> forms;
    private int idCounter;

    public Forms() {
        sortedForms = new ArrayList<>();
        fileForms = new ArrayList<>();
        forms = new ArrayList<>();
        idCounter = 1;
    }

    public Forms(List<Form> forms) {
        setForms(forms);
    }

    public void addForm(Form form) {
        int newId = idCounter++;
        form.setId(newId);

        Form formClone = (Form) form.clone();
        sortedForms.add(formClone);

        if (form instanceof FileForm) {
            fileForms.add((FileForm) form);
        } else {
            forms.add(form);
        }
    }

    public void addForms(List<Form> forms) {
        forms.forEach(form -> addForm(form));
    }

    public void setForms(List<Form> forms) {
        sortedForms = new ArrayList<>();
        fileForms = new ArrayList<>();
        forms = new ArrayList<>();

        addForms(forms);
    }

    public Form getForm(int id) {
        for (Form form : sortedForms) {
            if (form.getId() == id) {
                return (Form) form.clone();
            }
        }
        return null;
    }

    public List<Form> getForms() {
        ArrayList<Form> formList = new ArrayList<>();
        forms.forEach(form -> {
            formList.add((Form) form.clone());
        });

        return formList;
    }

    public List<FileForm> getFileForms() {
        ArrayList<FileForm> formList = new ArrayList<>();
        fileForms.forEach(form -> {
            formList.add((FileForm) form.clone());
        });

        return formList;
    }

    public List<Form> getAllForms() {
        ArrayList<Form> formList = new ArrayList<>();
        sortedForms.forEach(form -> {
            formList.add((Form) form.clone());
        });

        return formList;
    }

    public boolean changeForm(Form form) {
        for (int index = 0; index < sortedForms.size(); index++) {
            Form formFromList = sortedForms.get(index);
            int formId = formFromList.getId();
            FormType formType = formFromList.getType();

            if (formId == form.getId() && formType == form.getType()) {
                sortedForms.set(index, form);
                return true;
            }
        }

        return false;
    }

    public boolean moveTo(int newPosition, Form form) {
        int currentPosition = sortedForms.indexOf(form);
        if (currentPosition == -1) return false;

        if (newPosition < 0) newPosition = 0;
        if (newPosition >= sortedForms.size()) newPosition = sortedForms.size() - 1;

        form = sortedForms.get(currentPosition);
        sortedForms.add(newPosition, form);

        if (newPosition > currentPosition) {
            sortedForms.remove(currentPosition);
        } else {
            sortedForms.remove(currentPosition + 1);
        }

        return true;
    }

    public boolean remove(int id) {
        for (int index = 0; index < sortedForms.size(); index++) {
            Form form = sortedForms.get(index);

            if (form.getId() == id) {
                sortedForms.remove(index);
                return true;
            }
        }
        return false;
    }

    public boolean remove(Form form) {
        return sortedForms.remove(form);
    }

    public void validateForms(Map<String, String[]> formsData) throws InvalidParameterException{
        formsData = new HashMap<>(formsData);

        for (Form form : forms) {
            int formId = form.getId();
            List<String> values = Arrays.asList(formsData.get(formId));
            form.validate(values);

            formsData.remove(formId);
        }

        if (!formsData.isEmpty())
            throw new InvalidParameterException("Request has excess parameters");
    }

    public void validateFileForms(List<MultipartFile> files) throws InvalidParameterException{
        files = new ArrayList<>(files);

        for (FileForm form : fileForms) {
            MultipartFile requiredFile = null;
            boolean isAlreadyFound = false;

            for (int index = 0; index < files.size(); index++) {
                 MultipartFile file = files.get(index);

                String fileId = file.getName();
                String formId = Integer.toString(form.getId());

                if (fileId.equals(formId)) {
                    if (isAlreadyFound)
                        throw new InvalidParameterException("File form accept only one file.");

                    requiredFile = file;
                    files.remove(index);
                    isAlreadyFound = true;
                }
            }

            form.fileValidation(requiredFile);
        }

        if (!files.isEmpty())
            throw new InvalidParameterException("Request has excess files");
    }
}