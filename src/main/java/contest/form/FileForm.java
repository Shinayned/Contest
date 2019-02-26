package contest.form;

import contest.form.enums.FormType;
import org.springframework.web.multipart.MultipartFile;

import java.security.InvalidParameterException;
import java.util.List;

public class FileForm extends Form implements Cloneable {
    private int maxQuantity;
    private long maxFileSize;
    private long maxTotalSize;

    public FileForm() {
        super(FormType.FILE);
        this.maxTotalSize = 100; // MB
    }

    public FileForm(String title) {
        super(FormType.FILE, title);
        this.maxTotalSize = 100; // MB
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public long getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(int maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public long getMaxTotalSize() {
        return maxTotalSize;
    }

    public void setMaxTotalSize(int maxTotalSize) {
        this.maxTotalSize = maxTotalSize;
    }

    public void filesValidation(List<MultipartFile> files) {
        if (files.size() > maxQuantity)
            throw new InvalidParameterException(
                    "Form №" + this.getId() + " max files quantity is " + this.maxQuantity);

        long maxFileSize = 0;
        long maxTotalSize = 0;

        for(MultipartFile file : files) {
            long fileSize = file.getSize();
            maxTotalSize += fileSize;

            if(maxFileSize < fileSize)
                maxFileSize = fileSize;
        }

        if(maxFileSize > this.maxFileSize)
            throw new InvalidParameterException(
                    "Form №" + this.getId() + " max file size is " + this.maxFileSize);
        if(maxTotalSize > this.maxTotalSize)
            throw new InvalidParameterException(
                    "Form №" + this.getId() + " max size of all files is " + this.maxTotalSize);
    }

    @Override
    protected boolean specialValidate(String[] values) {
        return true;
    }

    @Override
    protected Object clone() {
        return super.clone();
    }
}
