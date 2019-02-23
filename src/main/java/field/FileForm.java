package field;

import enums.FormType;

public class FileForm extends Form {
    private int maxQuantity;
    private int maxFileSize;

    public FileForm(String name, String title) {
        super(name, FormType.FILE, title);
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public int getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(int maxFileSize) {
        this.maxFileSize = maxFileSize;
    }
}
