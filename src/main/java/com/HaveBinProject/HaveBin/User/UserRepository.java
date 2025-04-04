package com.HaveBinProject.HaveBin.User;

import com.HaveBinProject.HaveBin.RequestDTO.UserDataDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(User user){
        em.persist(user);
        return user.getId();
    }

    public User find(Long id){
        return em.find(User.class, id);
    }

    public UserDataDTO findUserbyEmail (String email) {
        TypedQuery<User> resultQuery = em.createQuery("SELECT m from User m where m.email = :email", User.class);
        resultQuery.setParameter("email", email);

        User user = resultQuery.getResultList().get(0);

        UserDataDTO userDataDTO = new UserDataDTO(user);

        return userDataDTO;
    }

    public List<User> findAll() {
        return em.createQuery("SELECT  m from User m", User.class)
                .getResultList();
    }

    public User findOne(Long id){
        return em.find(User.class, id);
    }

    public List<User> findByEmail(String email){
        TypedQuery<User> resultQuery = em.createQuery("SELECT  m from User m where m.email = :email", User.class);
        resultQuery.setParameter("email", email);
        return resultQuery.getResultList();
    }

    public List<User> findByNickname(String nickname){
        TypedQuery<User> resultQuery = em.createQuery("SELECT  m from User m where m.nickname = :nickname", User.class);
        resultQuery.setParameter("nickname", nickname);
        return resultQuery.getResultList();
    }

    public Long findIdByNickname(String nickname){
        TypedQuery<User> resultQuery = em.createQuery("SELECT m from User m where m.nickname = :nickname", User.class);
        resultQuery.setParameter("nickname", nickname);
        return resultQuery.getResultList().get(0).getId();
    }

    public Long findIdByEmail(String email){
        TypedQuery<Long> resultQuery = em.createQuery("SELECT m.id from User m where m.email = :email", Long.class);
        resultQuery.setParameter("email", email);
        return resultQuery.getResultList().get(0);
    }

    public void delete(Long id){
        User user = em.find(User.class, id);
        em.remove(user);
    }
}
