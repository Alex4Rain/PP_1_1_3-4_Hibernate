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
    private Transaction transaction;

    public UserDaoHibernateImpl() {
        sessionFactory = Util.getSessionFactory();

    }
    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createNativeQuery("CREATE TABLE users (id bigint auto_increment primary key, name varchar(255) not null, lastName varchar(255) not null, age TINYINT not null)").executeUpdate();
            transaction.commit();
        } catch (PersistenceException e) {
            System.out.println("Table with this name already exist");
        } catch (Exception e1) {
            if (transaction.getStatus() == TransactionStatus.ACTIVE || transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK) {
                transaction.rollback();
            }
            e1.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE users").executeUpdate();
            transaction.commit();
        } catch (PersistenceException e) {
            System.out.println("Table with this name don't exist");
        } catch (Exception e1) {
            if (transaction.getStatus() == TransactionStatus.ACTIVE || transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK) {
                transaction.rollback();
            }
            e1.printStackTrace();
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
        } catch (Exception e) {
            if (transaction.getStatus() == TransactionStatus.ACTIVE || transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK) {
                transaction.rollback();
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
                transaction = session.beginTransaction();
                session.delete(user);
                transaction.commit();
                System.out.println("User with ID = " + id + " was deleted");
            } else {
                System.out.println("User with ID = " + id + " wasn't found in DB");
            }

        } catch (Exception e) {
            if (transaction.getStatus() == TransactionStatus.ACTIVE || transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK) {
                transaction.rollback();
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
            transaction = session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction.getStatus() == TransactionStatus.ACTIVE || transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

    }
}
