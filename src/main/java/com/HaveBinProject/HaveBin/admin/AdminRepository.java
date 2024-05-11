package com.HaveBinProject.HaveBin.admin;

import com.HaveBinProject.HaveBin.RequestDTO.ReportDTO;
import com.HaveBinProject.HaveBin.RequestDTO.SendReportTrashcanDTO;
import com.HaveBinProject.HaveBin.Trashcan.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    public Long modifyTrashcan(ReportDTO reportDTO){

        Trashcan trashcan = em.find(Trashcan.class, reportDTO.getTrashcanId());
        trashcan.setLatitude(reportDTO.getLatitude());
        trashcan.setLongitude(reportDTO.getLongitude());
        System.out.println("trashcan.getLatitude() = " + trashcan.getLatitude());
        System.out.println("trashcan.getLongitude() = " + trashcan.getLongitude());
        em.persist(trashcan);

        String category = "0";
        TypedQuery<Report_Trashcan> resultQuery = em.createQuery("SELECT rt from Report_Trashcan rt where rt.trashcan.id =: reportTrashcanId and rt.reportCategory =: reportCategory", Report_Trashcan.class);
        resultQuery.setParameter("reportTrashcanId",reportDTO.getTrashcanId());
        resultQuery.setParameter("reportCategory",category);
        List<Report_Trashcan> reportTrashcans = resultQuery.getResultList();



        for (Report_Trashcan reportTrashcan : reportTrashcans){
            System.out.println("trashcan :"+ reportTrashcan);
            em.remove(reportTrashcan);
        }


        TypedQuery<ShowReportTrashcan> resultQuery2 = em.createQuery("SELECT srt from ShowReportTrashcan srt where srt.reportCategory =: reportCategory and srt.trashcanId =: reportTrashcanId", ShowReportTrashcan.class);
        resultQuery2.setParameter("reportTrashcanId",reportDTO.getTrashcanId());
        resultQuery2.setParameter("reportCategory",category);
        List<ShowReportTrashcan> reportTrashcan = resultQuery2.getResultList();
        for(ShowReportTrashcan trashcans : reportTrashcan){
            System.out.println("modifyTrashcan :"+ trashcans);
            trashcans.setModifyStatus(true);
            em.persist(trashcans);
        }


        return Long.parseLong(reportDTO.getReportId());
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
}
