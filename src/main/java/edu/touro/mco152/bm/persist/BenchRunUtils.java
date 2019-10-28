package edu.touro.mco152.bm.persist;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * A simple utilities class to be used alongside BenchRun Objects
 */
public class BenchRunUtils {
    // utility methods for collection

    public static List<DiskRun> findAll() {
        EntityManager em = EM.getEntityManager();
        return em.createNamedQuery("DiskRun.findAll", DiskRun.class).getResultList();
    }

    public static int deleteAll() {
        EntityManager em = EM.getEntityManager();
        em.getTransaction().begin();
        int deletedCount = em.createQuery("DELETE FROM DiskRun").executeUpdate();
        em.getTransaction().commit();
        return deletedCount;
    }
}
