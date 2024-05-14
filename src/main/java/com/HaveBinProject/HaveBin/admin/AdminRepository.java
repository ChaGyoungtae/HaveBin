package com.HaveBinProject.HaveBin.admin;

import com.HaveBinProject.HaveBin.RequestDTO.ReportDTO;
import com.HaveBinProject.HaveBin.RequestDTO.ReportTrashcanDTO;
import com.HaveBinProject.HaveBin.RequestDTO.SendReportTrashcanDTO;
import com.HaveBinProject.HaveBin.Trashcan.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Repository
@Transactional
public class AdminRepository {

    @PersistenceContext
    private EntityManager em;


    public List<Unknown_Trashcan> findAllUnknownTrashcan(){
        return em.createQuery("select ut from Unknown_Trashcan ut", Unknown_Trashcan.class).getResultList();
    }

    public Unknown_Trashcan findUnknownTrashcan(Long unknownTrashcanId){
        return em.find(Unknown_Trashcan.class,unknownTrashcanId);
    }

    public Long acceptNewTrashcan(Trashcan trashcan){
        em.persist(trashcan);
        return trashcan.getId();
    }

    public void delete_UnknownTrashcan(Long unknownTrashcanId){
        Unknown_Trashcan unknown_trashcan = em.find(Unknown_Trashcan.class, unknownTrashcanId);
        em.remove(unknown_trashcan);
    }

    //id로 쓰레기통 삭제

    public void deleteTrashcan(Long trashcanId){
        Trashcan trashcan = em.find(Trashcan.class,trashcanId);
        em.remove(trashcan);
    }

    public List<SendReportTrashcanDTO> findAllReportTrashcan(){
        TypedQuery<SendReportTrashcanDTO> resultQuery = em.createQuery("SELECT new com.HaveBinProject.HaveBin.RequestDTO.SendReportTrashcanDTO(rt.id,rt.user.id,rt.trashcan.id,rt.reportCategory,rt.modifyStatus) from Report_Trashcan rt", SendReportTrashcanDTO.class);
        return resultQuery.getResultList();
    }

    public Long modifyTrashcan(Trashcan trashcan){

        em.persist(trashcan);

        return trashcan.getId();
    }



    public void deleteReportTrashcans(Long reportTrashcanId, String reportCategory){
        TypedQuery<Report_Trashcan> resultQuery = em.createQuery("select rt from Report_Trashcan rt where rt.trashcan.id = :reportTrashcanId and rt.reportCategory = :reportCategory", Report_Trashcan.class);
        resultQuery.setParameter("reportTrashcanId",reportTrashcanId);
        resultQuery.setParameter("reportCategory",reportCategory);
        List<Report_Trashcan> reportTrashcans = resultQuery.getResultList();
        System.out.println("reportTrashcan = " + reportTrashcans);
        for (Report_Trashcan reportTrashcan : reportTrashcans){
            em.remove(reportTrashcan);
        }
    }

    public List<ShowReportTrashcan> findShowReportTrashcanByTrashcanIdandReportCategory(Long reportTrashcanId, String reportCategory){
        TypedQuery<ShowReportTrashcan> resultQuery = em.createQuery("select srt from ShowReportTrashcan srt where srt.trashcanId =: reportTrashcanId and srt.reportCategory =: reportCategory", ShowReportTrashcan.class);
        resultQuery.setParameter("reportTrashcanId",reportTrashcanId);
        resultQuery.setParameter("reportCategory",reportCategory);
        return resultQuery.getResultList();
    }

    public void modifyStatus(ShowReportTrashcan showReportTrashcan){
        em.persist(showReportTrashcan);
    }


}
