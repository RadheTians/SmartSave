package com.save.smartsave.ui.notifications;

public class SaveMoneyModelClass {

    private String heading,body,amount;

    public SaveMoneyModelClass(String heading, String body, String amount) {
        this.heading = heading;
        this.body = body;
        this.amount = amount;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
