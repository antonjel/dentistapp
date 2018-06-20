package com.cgi.dentistapp.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class VisitDTO {

    Long doctorId;

    @NotNull
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    LocalDate visitTime;

    public VisitDTO() {
    }

    public VisitDTO(Long doctorId, LocalDate visitTime) {
        this.doctorId = doctorId;
        this.visitTime = visitTime;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDate getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(LocalDate visitTime) {
        this.visitTime = visitTime;
    }


}
