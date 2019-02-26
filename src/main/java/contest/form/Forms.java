package contest.form;

import contest.form.enums.FormType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Forms implements Serializable{
    private ArrayList<Form> sortedForms;
    private int idCounter = 1;

    public Forms() {
        sortedForms = new ArrayList<>();
    }

    public Forms(List<Form> forms) {
        setForms(forms);
    }

    public void addForm(Form form) {
        Form formClone = (Form) form.clone();
        int newId = idCounter++;

        formClone.setId(newId);
        sortedForms.add(formClone);

        form.setId(newId);
    }

    public void addForms(List<Form> forms) {
        forms.forEach(form -> addForm(form));
    }

    public void setForms(List<Form> forms) {
        sortedForms = new ArrayList<>();
        forms.forEach(form -> addForm(form));
    }

    public Form getForm(int id){
        for(Form form : sortedForms) {
            if (form.getId() == id) {
                return (Form) form.clone();
            }
        }
        return null;
    }

    public List<Form> getAllForms() {
        ArrayList<Form> formList = new ArrayList<>();
        sortedForms.forEach(form -> {
            formList.add((Form) form.clone());
        });

        return formList;
    }

    public boolean changeForm(Form form) {
        for(int index = 0; index < sortedForms.size(); index++) {
            Form formFromList = sortedForms.get(index);
            int formId = formFromList.getId();
            FormType formType = formFromList.getType();

            if(formId == form.getId() && formType == form.getType()) {
                sortedForms.set(index, form);
                return true;
            }
        }

        return false;
    }

    public boolean moveTo(int newPosition, Form form) {
        int currentPosition = sortedForms.indexOf(form);
        if( currentPosition == -1) return false;

        if(newPosition < 0) newPosition = 0;
        if(newPosition >= sortedForms.size()) newPosition = sortedForms.size() - 1;

        form = sortedForms.get(currentPosition);
        sortedForms.add(newPosition, form);

        if(newPosition > currentPosition) {
            sortedForms.remove(currentPosition);
        } else {
            sortedForms.remove(currentPosition + 1);
        }

        return true;
    }

    public boolean remove(int id) {
        for(int index = 0; index < sortedForms.size(); index++) {
            Form form = sortedForms.get(index);

            if(form.getId() == id) {
                sortedForms.remove(index);
                return true;
            }
        }
        return false;
    }

    public boolean remove(Form form) {
        return sortedForms.remove(form);
    }
}
