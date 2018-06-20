package com.cgi.dentistapp.dao;

import com.cgi.dentistapp.dao.entity.DoctorEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class DoctorDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void create(DoctorEntity doctor){entityManager.persist(doctor);}

    public List<DoctorEntity> getAllDoctors(){
        return entityManager.createQuery("SELECT e from DoctorEntity e").getResultList();
    }

    // Check whether a doctor with the provided name exists
    public boolean doctorExists(String name){
        return entityManager.createQuery("SELECT e from DoctorEntity e WHERE name ='" + name + "'").getResultList().size() == 1;
    }

    // Get a list of all the doctors with the type "Dentist"
    public List<DoctorEntity> getDentistList(){
        return entityManager.createQuery("SELECT e from DoctorEntity e WHERE type ='Dentist'").getResultList();
    }

    // Get a list of all the doctors with the type "GP" (general practitioner)
    public List<DoctorEntity> getGPList(){
        return entityManager.createQuery("SELECT e from DoctorEntity e WHERE type ='GP'").getResultList();
    }

    // Get a specific doctor via his doctor id
    public List<DoctorEntity> getDoctor(Long doctorId){
        return entityManager.createQuery("SELECT e from DoctorEntity e WHERE doctor_id = " + doctorId).getResultList();
    }

    // Get the ID of a specific doctor via his name
    public Long getDoctorId(String name){
        DoctorEntity doc = (DoctorEntity) entityManager.createQuery("SELECT e from DoctorEntity e WHERE name = '" + name + "'").getSingleResult();
        return doc.getId();
    }

    // Find all the doctors that have the provided string within their name
    public List<DoctorEntity> searchForDoctor(String name){
        return entityManager.createQuery("SELECT e from DoctorEntity e WHERE LOWER(name) LIKE '%" + name + "%'").getResultList();
    }
}
