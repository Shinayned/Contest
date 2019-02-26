package model;

import javax.persistence.Embeddable;


// IS NOT USING. DEVELOPING IS FROZEN
@Embeddable
public class ContestPage {
    private String body;

    protected ContestPage() {}

    public ContestPage(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
