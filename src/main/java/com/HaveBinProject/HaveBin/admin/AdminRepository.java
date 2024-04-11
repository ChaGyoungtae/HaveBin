package com.HaveBinProject.HaveBin.admin;

import com.HaveBinProject.HaveBin.Trashcan.Report_Trashcan;
import com.HaveBinProject.HaveBin.Trashcan.Trashcan;
import com.HaveBinProject.HaveBin.Trashcan.Unknown_Trashcan;
import com.HaveBinProject.HaveBin.User.User;
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

    public List<Report_Trashcan> findAllReportTrashcan(){
        TypedQuery<Report_Trashcan> resultQuery = em.createQuery("SELECT  rt from Report_Trashcan rt", Report_Trashcan.class);
        return resultQuery.getResultList();
    }

}
