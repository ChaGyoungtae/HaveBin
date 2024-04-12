package com.HaveBinProject.HaveBin.Trashcan;

import com.HaveBinProject.HaveBin.DTO.ResponseDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrashcanRepository {

    @PersistenceContext
    private EntityManager em;

    public Long saveOne_unknown(Unknown_Trashcan unknown_trashcan){
        em.persist(unknown_trashcan);
        return(unknown_trashcan.getUnknown_trashcan_id());
    }

    public Long saveOne(Trashcan trashcan){
        em.persist(trashcan);
        return trashcan.getId();
    }

    public Trashcan find(Long trashcanId){
        return em.find(Trashcan.class, trashcanId);
    }

    public List<Trashcan> findAll() {
        return em.createQuery("SELECT  t from Trashcan t", Trashcan.class)
                .getResultList();
    }

    //근처 쓰레기통 찾기
    public List<Trashcan> findNear(ResponseDTO responseDTO){
        TypedQuery<Trashcan> resultQuery = em.createQuery("SELECT t from Trashcan t where t.latitude >= :minLat and t.latitude <= :maxLat and t.longitude >= :minLon and t.longitude <= : maxLon", Trashcan.class);
        resultQuery.setParameter("maxLat", responseDTO.getNeLat());
        resultQuery.setParameter("maxLon", responseDTO.getNeLon());
        resultQuery.setParameter("minLat", responseDTO.getSwLat());
        resultQuery.setParameter("minLon", responseDTO.getSwLon());
        return resultQuery.getResultList();
    }

    //쓰레기통 신고
    public Long saveReportTrashcan(Report_Trashcan reportTrashcan){
        em.persist(reportTrashcan);
        return reportTrashcan.getId();
    }

    //1개의 신고당한 쓰레기통 조회
    public Report_Trashcan findReportTrashcan(Long reportTrashcanId){
        return em.find(Report_Trashcan.class, reportTrashcanId);
    }

    //해당 쓰레기통을 신고한 사람의 수 조회(신고 횟수)
    public int findReportCount(Long trashcanId){
        String jpql = "SELECT COUNT(rt) FROM Report_Trashcan rt WHERE rt.trashcan.id = :trashcanId";

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        query.setParameter("trashcanId", trashcanId);

        Long count = query.getSingleResult();

        return count != null ? count.intValue() : 0;
    }

}
