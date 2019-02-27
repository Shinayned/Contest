package contest.form;

import contest.form.enums.FormType;
import org.springframework.web.multipart.MultipartFile;

import java.security.InvalidParameterException;
import java.util.List;

public class FileForm extends Form implements Cloneable {
    private long maxSize;
    private String contentType;

    public FileForm(String name) {
        super(name, FormType.FILE);

        this.maxSize = 100; // MB
    }

    public FileForm(String name, String contentType) {
        this(name);

        this.contentType = contentType;
    }

    public long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void fileValidation(MultipartFile file) throws InvalidParameterException {
        if (file == null) {
            if (this.isObligatory())
                throw new InvalidParameterException(
                        "Form №" + this.getId() + " is required.");

            return;
        }

        if (contentType != null) {
            if (!file.getContentType().equals(contentType))
                throw new InvalidParameterException(
                        "Form №" + this.getId() + " has '" + this.contentType + "' content type.");
        }

        if (file.getSize() > this.maxSize)
            throw new InvalidParameterException(
                    "Form №" + this.getId() + " max file size is " + this.maxSize);
    }

    @Override
    protected void specialValidate(List<String> values) throws InvalidParameterException {
    }

    @Override
    protected Object clone() {
        return super.clone();
    }
}
