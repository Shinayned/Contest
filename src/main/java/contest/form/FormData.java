package contest.form;

import contest.form.enums.FormType;

import javax.xml.crypto.Data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormData implements Serializable {
    private int formId;
    private FormType type;
    private List<String> data;

    public FormData(int formId, FormType type, List<String> data) {
        this.formId = formId;
        this.type = type;
        this.data = new ArrayList<>(data);
    }

    public FormData(int formId, FormType type, String[] data){
        this(formId, type, Arrays.asList(data));
    }

    public int getFormId() {
        return formId;
    }

    public List<String> getData() {
        return new ArrayList<>(data);
    }

    public FormType getType() {
        return type;
    }

    @Override
    public String toString() {
        if(data.size() == 1) {
            return data.get(0);
        } else {
            StringBuilder dataAsString = new StringBuilder();

            for(int index = 0; index < data.size(); index++) {
                dataAsString.append(data.get(index));
                if(index != data.size() - 1) {
                    dataAsString.append(" | ");
                }
            }
            return dataAsString.toString();
        }
    }
}