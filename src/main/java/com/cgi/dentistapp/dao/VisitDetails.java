package com.cgi.dentistapp.dao;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Helper class used to be able to easily cross reference Doctors and Visits
*/
public class VisitDetails {
    private BigInteger visitId;
    private BigInteger doctorId;
    private String doctorType;
    private String doctorName;
    private LocalDate visitTime;

    public VisitDetails(BigInteger visitId, BigInteger doctorId, String doctorType, String doctorName, LocalDate visitTime) {
        this.visitId = visitId;
        this.doctorId = doctorId;
        this.doctorType = doctorType;
        this.doctorName = doctorName;
        this.visitTime = visitTime;
    }

    public BigInteger getVisitId() {
        return visitId;
    }

    public void setVisitId(BigInteger visitId) {
        this.visitId = visitId;
    }

    public BigInteger getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(BigInteger doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorType() {
        return doctorType;
    }

    public void setDoctorType(String doctorType) {
        this.doctorType = doctorType;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getVisitTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return dateTimeFormatter.format(visitTime);
    }

    public void setVisitTime(LocalDate visitTime) {
        this.visitTime = visitTime;
    }


}

