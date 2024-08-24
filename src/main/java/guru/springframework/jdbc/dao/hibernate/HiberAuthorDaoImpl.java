package guru.springframework.jdbc.dao.hibernate;

import guru.springframework.jdbc.dao.AuthorDao;
import guru.springframework.jdbc.domain.Author;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HiberAuthorDaoImpl implements AuthorDao {
	private final EntityManagerFactory emf;

	public HiberAuthorDaoImpl(EntityManagerFactory emf) {
		this.emf = emf;
	}

	@Override
	public Author getById(Long id) {
		try (EntityManager em = getEntityManager()) {
			return em.find(Author.class, id);
		}
	}

	@Override
	public Author findAuthorByName(String firstName, String lastName) {
		try (EntityManager em = getEntityManager()) {
			TypedQuery<Author> q = em.createQuery(
					"SELECT a FROM Author a WHERE a.firstName = :firstName AND a.lastName = :lastName",
					Author.class
			);
			q.setParameter("firstName", firstName);
			q.setParameter("lastName", lastName);
			return q.getSingleResult();
		}
	}

	@Override
	public Author saveNewAuthor(Author author) {
		try (EntityManager em = getEntityManager()) {
			em.getTransaction().begin();
			em.persist(author);
			em.flush();
			em.getTransaction().commit();
		}

		return getById(author.getId());
	}

	@Override
	public Author updateAuthor(Author author) {
		try (EntityManager em = getEntityManager()) {
			em.getTransaction().begin();
			em.merge(author);
			em.flush();
			em.getTransaction().commit();
		}
		return getById(author.getId());
	}

	@Override
	public void deleteAuthorById(Long id) {
		try (EntityManager em = getEntityManager()) {
			em.getTransaction().begin();
			em.remove(em.find(Author.class, id));
			em.flush();
			em.getTransaction().commit();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Author> findByLastName(Pageable pageable, String lastName) {
		try (EntityManager em = getEntityManager()) {
			StringBuilder hql = new StringBuilder("SELECT a FROM Author a WHERE a.lastName LIKE :lastName");

			if (pageable.getSort().isSorted()) {
				hql.append(" ORDER BY");
				for (Sort.Order order : pageable.getSort().stream().toList()) {
					hql.append(" a.").append(order.getProperty()).append(" ").append(order.getDirection().name());
				}
			}

			Query q = em.createQuery(hql.toString());
			q.setParameter("lastName", String.format("%%%s%%", lastName));
			q.setFirstResult(Math.toIntExact(pageable.getOffset()))
					.setMaxResults(pageable.getPageSize());
			return q.getResultList();
		}
	}

	private EntityManager getEntityManager() {
		return emf.createEntityManager();
	}
}
