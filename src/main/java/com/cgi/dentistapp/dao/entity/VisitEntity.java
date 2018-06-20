package com.cgi.dentistapp.dao.entity;

import javax.persistence.*;
import java.sql.Date;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "visit")
public class VisitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(name = "visit_time", columnDefinition = "DATE")
    private Date visitTime;

    public VisitEntity() {
    }

    public VisitEntity(Long doctorId, Date visitTime) {
        this.doctorId = doctorId;
        this.visitTime = visitTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Date getVisitTime() {return visitTime;}

    public void setVisitTime(Date visitTime) {
        this.visitTime = visitTime;
    }

    public String getFormattedVisitTime(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return dateTimeFormatter.format(visitTime.toLocalDate());
    }
}
