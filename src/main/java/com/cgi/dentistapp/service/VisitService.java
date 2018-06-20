package com.cgi.dentistapp.service;

import com.cgi.dentistapp.dao.VisitDao;
import com.cgi.dentistapp.dao.VisitDetails;
import com.cgi.dentistapp.dao.entity.VisitEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class VisitService {

    @Autowired
    private VisitDao visitDao;

    public void addVisit(Long doctorId, LocalDate visitTime){
        VisitEntity visit = new VisitEntity(doctorId, java.sql.Date.valueOf(visitTime));
        visitDao.create(visit);
    }

    public List<VisitEntity> listVisits(){return visitDao.getAllVisits();}

    public List<VisitEntity> getVisitsForDoctor(Long doctorId){return visitDao.getVisitsForDoctor(doctorId);}

    public List<VisitDetails> getFullVisitDetails(){return visitDao.getFullVisitDetails();}

    public List<VisitDetails> getFullVisitDetailsForDoctor(Long doctorId){return visitDao.getFullVisitDetailsForDoctor(doctorId);}

    public void deleteVisitEntry(Long visitId){visitDao.deleteVisitEntry(visitId);}

    public VisitEntity getVisit(Long visitId){return  visitDao.getVisit(visitId);}

    public void updateVisitEntry(Long visitId, LocalDate newVisitTime){visitDao.updateVisitEntry(visitId, newVisitTime);}

    public boolean isTimeAvailable(Long doctorId, LocalDate newVisitTime){return visitDao.isTimeAvailable(doctorId, newVisitTime);}
}
