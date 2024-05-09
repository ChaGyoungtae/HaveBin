package com.HaveBinProject.HaveBin.admin;

import com.HaveBinProject.HaveBin.RequestDTO.ReportDTO;
import com.HaveBinProject.HaveBin.RequestDTO.SendReportTrashcanDTO;
import com.HaveBinProject.HaveBin.Trashcan.Report_Trashcan;
import com.HaveBinProject.HaveBin.Trashcan.Trashcan;
import com.HaveBinProject.HaveBin.Trashcan.TrashcanRepository;
import com.HaveBinProject.HaveBin.Trashcan.Unknown_Trashcan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
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
        TypedQuery<SendReportTrashcanDTO> resultQuery = em.createQuery("SELECT new com.HaveBinProject.HaveBin.RequestDTO.SendReportTrashcanDTO(rt.id,rt.user.id,rt.trashcan.id,rt.reportCategory,rt.ModifyStatus) from Report_Trashcan rt", SendReportTrashcanDTO.class);
        return resultQuery.getResultList();
    }

    public Long modifyTrashcan(ReportDTO reportDTO){

        Trashcan trashcan = em.find(Trashcan.class, reportDTO.getTrashcanId());
        trashcan.setLatitude(reportDTO.getLatitude());
        trashcan.setLongitude(reportDTO.getLongitude());
        em.persist(trashcan);

        System.out.println("test");
        Report_Trashcan reportTrashcan = em.find(Report_Trashcan.class, reportDTO.getReportId());
        System.out.println(reportTrashcan.getModifyStatus());
        reportTrashcan.setModifyStatus(true);
        em.persist(reportTrashcan);

        return Long.parseLong(reportDTO.getReportId());
    }
}
