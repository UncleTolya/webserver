package dbservice.dao;

import datasets.UsersDataSet;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigInteger;

public class UsersDAO {
    private Session session;

    public UsersDAO(Session session) {
        this.session = session;
    }

    public UsersDataSet get(long id) throws HibernateException {
        return (UsersDataSet) session.get(UsersDataSet.class, id);
    }

//    public UsersDataSet get(String login) throws HibernateException {
////        session.
////        return (UsersDataSet) session.get(UsersDataSet.class, login);
//        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
//        CriteriaQuery<UsersDataSet> criteria = criteriaBuilder.createQuery(UsersDataSet.class);
//        Root<UsersDataSet> usersDataSetRoot = criteria.from(UsersDataSet.class);
//        return ((UsersDataSet)criteria.where(criteriaBuilder.equal(usersDataSetRoot.get("name"), name))).getId();
//    }

    public UsersDataSet get(String login) {
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<UsersDataSet> criteria = criteriaBuilder.createQuery(UsersDataSet.class); //создаем критерий
            Root<UsersDataSet> usersDataSetRoot = criteria.from(UsersDataSet.class); //определяем переменную диапазона для FROM
            criteria.select(usersDataSetRoot); //какой тип результата запроса будет (можно поля задавать
            criteria.where(criteriaBuilder.equal(usersDataSetRoot.get("login"), login)); //задаем where
            Query<UsersDataSet> q = session.createQuery(criteria); // собственно сам запрос
            UsersDataSet result = q.getSingleResult(); // получаем результат
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public long insertUser(String login, String password) throws HibernateException {
        return (Long) session.save(new UsersDataSet(login, password));
    }
}
