package com.HaveBinProject.HaveBin.Trashcan;

import com.HaveBinProject.HaveBin.User.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
        return em.createQuery("SELECT  m from User m", Trashcan.class)
                .getResultList();
    }

}
