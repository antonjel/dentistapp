package com.cgi.dentistapp.service;

import com.cgi.dentistapp.dao.DoctorDao;
import com.cgi.dentistapp.dao.entity.DoctorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DoctorService {

    @Autowired
    private DoctorDao doctorDao;

    public void addDoctor(String type, String name){
        DoctorEntity doctor = new DoctorEntity(type, name);
        doctorDao.create(doctor);
    }

    public List<DoctorEntity> listDoctors(){return doctorDao.getAllDoctors();}

    public boolean doctorExists(String name){return doctorDao.doctorExists(name);}

    public List<DoctorEntity> getDentistList(){return doctorDao.getDentistList();}

    public List<DoctorEntity> getGPList(){return doctorDao.getGPList();}

    public List<DoctorEntity> getDoctor(Long doctorId){return doctorDao.getDoctor(doctorId);}

    public Long getDoctorId(String name){return doctorDao.getDoctorId(name);}

    public List<DoctorEntity> searchForDoctor(String name){return  doctorDao.searchForDoctor(name);}
}
