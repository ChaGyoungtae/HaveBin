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

    public Long saveOne(Trashcan trashcan){
        em.persist(trashcan);
        return(trashcan.getId());
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
        resultQuery.setParameter("maxLat", responseDTO.getMaxLat());
        resultQuery.setParameter("maxLon", responseDTO.getMaxLon());
        resultQuery.setParameter("minLat", responseDTO.getMinLat());
        resultQuery.setParameter("minLon", responseDTO.getMinLon());
        return resultQuery.getResultList();
    }
}
