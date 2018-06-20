package com.cgi.dentistapp.dao;

import com.cgi.dentistapp.dao.entity.VisitEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VisitDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void create(VisitEntity visit) {
        entityManager.persist(visit);
    }

    public List<VisitEntity> getAllVisits() {
        return entityManager.createQuery("SELECT e FROM VisitEntity e").getResultList();
    }

    public List<VisitEntity> getVisitsForDoctor(Long doctorID){
        return entityManager.createQuery("SELECT e FROM VisitEntity e WHERE doctor_id = " + doctorID).getResultList();}


    // Requests full VisitDetails from two database tables from all available entries
    public List<VisitDetails> getFullVisitDetails(){
        String query = "SELECT Visit.id as visit_id," +
                "Visit.doctor_id as doctor_id," +
                "Doctor.type as doctor_type," +
                "Doctor.name as doctor_name," +
                "Visit.visit_time as visit_time" +
                " FROM Visit JOIN Doctor ON Visit.doctor_id = Doctor.id";
        return collectDetails(entityManager.createNativeQuery(query).getResultList());
    }

    // Requests full VisitDetails from two database tables for a specific doctor based on the provided doctor id
    public List<VisitDetails> getFullVisitDetailsForDoctor(Long doctorId){
        String query = "SELECT Visit.id as visit_id," +
                "Visit.doctor_id as doctor_id," +
                "Doctor.type as doctor_type," +
                "Doctor.name as doctor_name," +
                "Visit.visit_time as visit_time" +
                " FROM Visit JOIN Doctor ON Visit.doctor_id = Doctor.id " +
                "WHERE Doctor.id = " + doctorId;
        return collectDetails(entityManager.createNativeQuery(query).getResultList());
    }

    // Generates a list of of VisitDetails based on the provided list of Object[]
    private List<VisitDetails> collectDetails(List<Object[]> results){
        List<VisitDetails> details = new ArrayList<>();
        for (Object[] r : results) {
            Date date = (Date) r[4];
            details.add(new VisitDetails(
                    (BigInteger) r[0],
                    (BigInteger) r[1],
                    (String) r[2],
                    (String) r[3],
                    date.toLocalDate()
            ));
        }
        return details;
    }

    // Deletes a specific visit based on visit id
    public void deleteVisitEntry(Long visitId){
        String query = "DELETE FROM Visit WHERE id = " + visitId + ";";
        entityManager.createNativeQuery(query).executeUpdate();
    }

    public VisitEntity getVisit(Long visitId){
        return (VisitEntity) entityManager.createQuery("SELECT e FROM VisitEntity e WHERE id = " + visitId).getSingleResult();
    }


    // Updates the time of a specific visit based on visit id
    public void updateVisitEntry(Long visitId, LocalDate newVisitTime){
        String query = "UPDATE Visit SET visit_time = '" + java.sql.Date.valueOf(newVisitTime) + "' WHERE id = " + visitId;
        entityManager.createNativeQuery(query).executeUpdate();
    }


    // Check whether a specific doctor is available at a specific time
    public boolean isTimeAvailable(Long doctorId, LocalDate newVisitTime){
        String query = "SELECT * FROM Visit WHERE" +
                " doctor_id = " + doctorId + " AND" +
                " visit_time = '" + java.sql.Date.valueOf(newVisitTime) + "'";
        List<Object[]> results = entityManager.createNativeQuery(query).getResultList();
        if(results.size()== 0) return true;
        else return false;
    }


}
