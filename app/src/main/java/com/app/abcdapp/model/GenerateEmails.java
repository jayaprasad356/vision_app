package com.app.abcdapp.model;

public class GenerateEmails {
    String id,student_name,id_number,ecity,institution;

    public GenerateEmails() {

    }

    public GenerateEmails(String id, String student_name, String id_number, String ecity, String institution) {
        this.id = id;
        this.student_name = student_name;
        this.id_number = id_number;
        this.ecity = ecity;
        this.institution = institution;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getEcity() {
        return ecity;
    }

    public void setEcity(String ecity) {
        this.ecity = ecity;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }
}
