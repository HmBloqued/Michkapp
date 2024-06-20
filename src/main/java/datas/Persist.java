package datas;

import jakarta.persistence.*;

public class Persist {
    public void create( Object object) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(this);
            transaction.commit();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            entityManager.getTransaction().rollback();
        }
        finally {
            entityManager.close();
            entityManagerFactory.close();
        }
    }

    public void update( Object object) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.merge(this);
            transaction.commit();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            entityManager.getTransaction().rollback();
        }
        finally {
            entityManager.close();
            entityManagerFactory.close();
        }
    }

    public void delete( Object object) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.remove(entityManager.contains(this) ? this : entityManager.merge(this));
            transaction.commit();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            entityManager.getTransaction().rollback();
        }
        finally {
            entityManager.close();
            entityManagerFactory.close();
        }
    }

    public Object read( Object object, int id) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            object = entityManager.find(this.getClass(), id);
            transaction.commit();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            entityManager.getTransaction().rollback();
        }
        finally {
            entityManager.close();
            entityManagerFactory.close();
        }
        return object;
    }
}
