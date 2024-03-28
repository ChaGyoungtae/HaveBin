package com.HaveBinProject.HaveBin.admin;

import com.HaveBinProject.HaveBin.Trashcan.Trashcan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class AdminRepository {

    @PersistenceContext
    private EntityManager em;

    public void saveAll(Trashcan trashcan){
        em.persist(trashcan);
    }
}
