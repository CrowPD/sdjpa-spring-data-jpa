package guru.springframework.jdbc.dao.hibernate;

import guru.springframework.jdbc.dao.BookDao;
import guru.springframework.jdbc.domain.Book;
import jakarta.persistence.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HibernateBookDaoImpl implements BookDao {
	private final EntityManagerFactory emf;

	public HibernateBookDaoImpl(EntityManagerFactory emf) {
		this.emf = emf;
	}

	@Override
	public List<Book> findAllSortedByTitle(Pageable pageable) {
		return List.of();
	}

	@Override
	public List<Book> findAll(Pageable pageable) {
		try (EntityManager em = emf.createEntityManager()) {
			TypedQuery<Book> q = em.createQuery(
							"SELECT b FROM Book b ",
							Book.class
					).setFirstResult(Math.toIntExact(pageable.getOffset()))
					.setMaxResults(pageable.getPageSize());
			return q.getResultList();
		}
	}

	@Override
	public List<Book> findAll(int offset, int pageSize) {
		try (EntityManager em = emf.createEntityManager()) {
			TypedQuery<Book> q = em.createQuery(
							"SELECT b FROM Book b ",
							Book.class
					).setFirstResult(offset)
					.setMaxResults(pageSize);
			return q.getResultList();
		}
	}

	@Override
	public List<Book> findAll() {
		try (EntityManager em = emf.createEntityManager()) {
			TypedQuery<Book> q = em.createQuery(
					"SELECT b FROM Book b",
					Book.class
			);
			return q.getResultList();
		}
	}

	@Override
	public Book getById(Long id) {
		try (EntityManager em = getEntityManager()) {
			return em.find(Book.class, id);
		}
	}

	@Override
	public Book findBookByTitle(String title) {
		try (EntityManager em = getEntityManager()) {
			TypedQuery<Book> q = em.createQuery(
					"SELECT b FROM Book b WHERE b.title = :title",
					Book.class
			);
			q.setParameter("title", title);
			return q.getSingleResult();
		}
	}

	@Override
	public Book saveNewBook(Book book) {
		try (EntityManager em = getEntityManager()) {
			EntityTransaction transaction = em.getTransaction();
			transaction.begin();
			em.persist(book);
			em.flush();
			transaction.commit();
		}
		return getById(book.getId());
	}

	@Override
	public Book updateBook(Book book) {
		try (EntityManager em = getEntityManager()) {
			EntityTransaction transaction = em.getTransaction();
			transaction.begin();
			em.merge(book);
			em.flush();
			transaction.commit();
		}
		return getById(book.getId());
	}

	@Override
	public void deleteBookById(Long id) {
		try (EntityManager em = getEntityManager()) {
			EntityTransaction transaction = em.getTransaction();
			transaction.begin();
			em.remove(em.find(Book.class, id));
			em.flush();
			transaction.commit();
		}
	}

	@Override
	public Book findBookByIsbn(String isbn) {
		try (EntityManager em = getEntityManager()) {
			TypedQuery<Book> q = em.createQuery("SELECT b FROM Book b WHERE b.isbn = :isbn", Book.class);
			q.setParameter("isbn", isbn);
			return q.getSingleResult();
		}
	}

	private EntityManager getEntityManager() {
		return emf.createEntityManager();
	}
}
