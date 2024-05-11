package com.HaveBinProject.HaveBin.Trashcan;

import com.HaveBinProject.HaveBin.RequestDTO.MyReportTrashcanDTO;
import com.HaveBinProject.HaveBin.RequestDTO.PosResponse;
import com.HaveBinProject.HaveBin.RequestDTO.SendReportTrashcanDTO;
import com.HaveBinProject.HaveBin.ResponseDTO.TrashcanData;
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

//    public List<Trashcan> findAll() {
//        return em.createQuery("SELECT  t from Trashcan t", Trashcan.class)
//                .getResultList();
//    }

    //쓰레기통 신고
    public Long saveReportTrashcan(Report_Trashcan reportTrashcan){
        em.persist(reportTrashcan);
        return reportTrashcan.getId();
    }

    //조회용
    public Long saveShowReportTrashcan(ShowReportTrashcan showReportTrashcan){
        em.persist(showReportTrashcan);
        return showReportTrashcan.getId();
    }

    //1개의 신고당한 쓰레기통 조회
    public Report_Trashcan findReportTrashcan(Long reportTrashcanId){
        return em.find(Report_Trashcan.class, reportTrashcanId);
    }

    public List<TrashcanData> findTrashcanData() {
        String jpql = "SELECT new com.HaveBinProject.HaveBin.ResponseDTO.TrashcanData(t.id, t.address, t.categories, t.date, t.detailAddress, t.latitude, t.longitude, t.roadviewImgpath, t.state, u.nickname) " +
                "FROM Trashcan t " +
                "JOIN User u ON t.userId = u.id";

        TypedQuery<TrashcanData> resultQuery = em.createQuery(jpql, TrashcanData.class);
        return resultQuery.getResultList();
    }


    //근처 쓰레기통 찾기
    public List<TrashcanData> findNearTrashcanData(PosResponse posResponse) {
        String jpql = "SELECT new com.HaveBinProject.HaveBin.ResponseDTO.TrashcanData(t.id, t.address, t.categories, t.date, t.detailAddress, t.latitude, t.longitude, t.roadviewImgpath, t.state, u.nickname) " +
                "FROM Trashcan t " +
                "JOIN User u ON t.userId = u.id " +
                "WHERE t.latitude >= :minLat and t.latitude <= :maxLat and t.longitude >= :minLon and t.longitude <= : maxLon";

        TypedQuery<TrashcanData> resultQuery = em.createQuery(jpql, TrashcanData.class);

        resultQuery.setParameter("maxLat", posResponse.getNeLat());
        resultQuery.setParameter("maxLon", posResponse.getNeLon());
        resultQuery.setParameter("minLat", posResponse.getSwLat());
        resultQuery.setParameter("minLon", posResponse.getSwLon());

        return resultQuery.getResultList();
    }


    public List<String> findReportTrashcanByIdAndReportCategoryAndTrashcanId(String reportCategory, String userEmail, Long trashcanId) {

        System.out.println("reportCategory = " + reportCategory);
        System.out.println("userEmail = " + userEmail);

        TypedQuery<String> resultQuery = em.createQuery("select rt.user.email from Report_Trashcan rt where rt.user.email = :userEmail and rt.reportCategory = :reportCategory and rt.trashcan.id = :trashcanId", String.class);
        resultQuery.setParameter("userEmail", userEmail);
        resultQuery.setParameter("reportCategory", reportCategory);
        resultQuery.setParameter("trashcanId", trashcanId);

        return resultQuery.getResultList();
    }
    // 해당 쓰레기통을 신고한 사람의 수 조회(신고 횟수)
    public int findReportCount(Long trashcanId){
        String jpql = "SELECT COUNT(rt) FROM Report_Trashcan rt WHERE rt.trashcan.id = :trashcanId";

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        query.setParameter("trashcanId", trashcanId);

        Long count = query.getSingleResult();

        return count != null ? count.intValue() : 0;
    }

    //사용자가 신고내역 삭제
    public void deleteReportTrashcan(Long reportTrashcanId, String reportCategory, String email){
        TypedQuery<Report_Trashcan> resultQuery = em.createQuery("select rt from Report_Trashcan rt where rt.trashcan.id = :reportTrashcanId and rt.reportCategory = :reportCategory and rt.user.email = :email", Report_Trashcan.class);
        resultQuery.setParameter("reportTrashcanId",reportTrashcanId);
        resultQuery.setParameter("reportCategory",reportCategory);
        resultQuery.setParameter("email", email);
        Report_Trashcan reportTrashcan = resultQuery.getSingleResult();
        System.out.println("reportTrashcan = " + reportTrashcan);
        em.remove(reportTrashcan);
    }

    public void deleteShowReportTrashcan(Long reportTrashcanId, String reportCategory, String email){
        TypedQuery<ShowReportTrashcan> resultQuery = em.createQuery("select rt from ShowReportTrashcan rt where rt.trashcanId = :reportTrashcanId and rt.reportCategory = :reportCategory and rt.email = :email", ShowReportTrashcan.class);
        resultQuery.setParameter("reportTrashcanId",reportTrashcanId);
        resultQuery.setParameter("reportCategory",reportCategory);
        resultQuery.setParameter("email", email);
        ShowReportTrashcan reportTrashcan = resultQuery.getSingleResult();
        System.out.println("reportTrashcan = " + reportTrashcan);
        em.remove(reportTrashcan);
    }

    public List<SendReportTrashcanDTO> findReportTrashcansByEmail(String email){
        TypedQuery<SendReportTrashcanDTO> resultQuery = em.createQuery("SELECT new com.HaveBinProject.HaveBin.RequestDTO.SendReportTrashcanDTO(rt.id,rt.user.id,rt.trashcan.id,rt.reportCategory,rt.modifyStatus) from Report_Trashcan rt where rt.user.email = :email", SendReportTrashcanDTO.class);
        resultQuery.setParameter("email",email);
        return resultQuery.getResultList();
    }

    public List<Unknown_Trashcan> ValidateDuplicateUnknownTrashcan(PosResponse posResponse){
        String jpql = "SELECT ut.unknown_trashcan_id from Unknown_Trashcan ut WHERE ut.latitude >= :minLat and ut.latitude <= :maxLat and ut.longitude >= :minLon and ut.longitude <= : maxLon";
        TypedQuery<Unknown_Trashcan> resultQuery = em.createQuery(jpql, Unknown_Trashcan.class);

        resultQuery.setParameter("maxLat", posResponse.getNeLat());
        resultQuery.setParameter("maxLon", posResponse.getNeLon());
        resultQuery.setParameter("minLat", posResponse.getSwLat());
        resultQuery.setParameter("minLon", posResponse.getSwLon());

        return resultQuery.getResultList();
    }

    public List<ShowReportTrashcan> myReportTrashcans(String email){
        String jpql = "SELECT rt " +
                "from ShowReportTrashcan rt " +
                "where rt.email = :email";
        TypedQuery<ShowReportTrashcan> resultQuery = em.createQuery(jpql, ShowReportTrashcan.class);
        resultQuery.setParameter("email",email);
        return resultQuery.getResultList();
    }
}
