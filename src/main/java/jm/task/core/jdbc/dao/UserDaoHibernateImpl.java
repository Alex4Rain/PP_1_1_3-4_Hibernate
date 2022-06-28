package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import javax.persistence.PersistenceException;
import java.util.List;


public class UserDaoHibernateImpl implements UserDao {
    private SessionFactory sessionFactory;

    public UserDaoHibernateImpl() {
        sessionFactory = Util.getSessionFactory();

    }
    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.createNativeQuery("CREATE TABLE users (id bigint auto_increment primary key, name varchar(255) not null, lastName varchar(255) not null, age TINYINT not null)").executeUpdate();
            session.getTransaction().commit();
        } catch (PersistenceException e) {
            System.out.println("Table with this name already exist");
        } catch (Exception e1) {
            if (sessionFactory.getCurrentSession().getTransaction().getStatus() == TransactionStatus.ACTIVE || sessionFactory.getCurrentSession().getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK) {
                sessionFactory.getCurrentSession().getTransaction().rollback();
            }
            e1.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.createSQLQuery("DROP TABLE users").executeUpdate();
            session.getTransaction().commit();
        } catch (PersistenceException e) {
            System.out.println("Table with this name don't exist");
        } catch (Exception e1) {
            if (sessionFactory.getCurrentSession().getTransaction().getStatus() == TransactionStatus.ACTIVE || sessionFactory.getCurrentSession().getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK) {
                sessionFactory.getCurrentSession().getTransaction().rollback();
            }
            e1.printStackTrace();
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.save(new User(name, lastName, age));
            session.getTransaction().commit();
        } catch (Exception e) {
            if (sessionFactory.getCurrentSession().getTransaction().getStatus() == TransactionStatus.ACTIVE || sessionFactory.getCurrentSession().getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK) {
                sessionFactory.getCurrentSession().getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            User user = null;
            for (User userTemp : this.getAllUsers()) {
                if (userTemp.getId() == id) {
                    user = userTemp;
                    break;
                }
            }
            if (user != null) {
                session.getTransaction().begin();
                session.delete(user);
                session.getTransaction().commit();
                System.out.println("User with ID = " + id + " was deleted");
            } else {
                System.out.println("User with ID = " + id + " wasn't found in DB");
            }

        } catch (Exception e) {
            if (sessionFactory.getCurrentSession().getTransaction().getStatus() == TransactionStatus.ACTIVE || sessionFactory.getCurrentSession().getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK) {
                sessionFactory.getCurrentSession().getTransaction().rollback();
            }
            e.printStackTrace();
        }

    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User", User.class).list();
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (sessionFactory.getCurrentSession().getTransaction().getStatus() == TransactionStatus.ACTIVE || sessionFactory.getCurrentSession().getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK) {
                sessionFactory.getCurrentSession().getTransaction().rollback();
            }
            e.printStackTrace();
        }

    }
}
