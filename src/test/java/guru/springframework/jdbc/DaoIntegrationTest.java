package guru.springframework.jdbc;

import guru.springframework.jdbc.dao.AuthorDao;
import guru.springframework.jdbc.dao.AuthorDaoImpl;
import guru.springframework.jdbc.dao.BookDao;
import guru.springframework.jdbc.domain.Author;
import guru.springframework.jdbc.domain.Book;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by jt on 8/28/21.
 */
@ActiveProfiles("local")
@DataJpaTest
@ComponentScan(basePackages = {"guru.springframework.jdbc.dao"})
@Import({AuthorDaoImpl.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DaoIntegrationTest {
	@Autowired
	AuthorDao authorDao;

	@Autowired
	BookDao bookDao;

	@Test
	void testDeleteBook() {
		Book book = new Book();
		book.setIsbn("1234");
		book.setPublisher("Self");
		book.setTitle("my book");
		Book saved = bookDao.saveNewBook(book);

		bookDao.deleteBookById(saved.getId());

		Book deleted = bookDao.getById(saved.getId());

		assertThat(deleted).isNull();
	}

	@Test
	void updateBookTest() {
		Book book = new Book();
		book.setIsbn("1234");
		book.setPublisher("Self");
		book.setTitle("my book");
		book.setAuthorId(3L);

		Book saved = bookDao.saveNewBook(book);

		saved.setTitle("New Book");
		bookDao.updateBook(saved);

		Book fetched = bookDao.getById(saved.getId());

		assertThat(fetched.getTitle()).isEqualTo("New Book");
	}

	@Test
	void testSaveBook() {
		Book book = new Book();
		book.setIsbn("1234");
		book.setPublisher("Self");
		book.setTitle("my book");

		book.setAuthorId(3L);
		Book saved = bookDao.saveNewBook(book);

		assertThat(saved).isNotNull();
	}

	@Test
	void testGetBookByName() {
		Book book = bookDao.findBookByTitle("Clean Code");

		assertThat(book).isNotNull();
	}

	@Test
	void testGetBookByNameThrowsOnMissing() {
		assertThrows(EntityNotFoundException.class, () -> {
			bookDao.findBookByTitle("foo");
		});
	}

	@Test
	void testFindBookByIsbn() {
		Book book = bookDao.findBookByIsbn("978-1617294945");

		assertNotNull(book);
	}

	@Test
	void testGetBook() {
		Book book = bookDao.getById(3L);

		assertThat(book.getId()).isNotNull();
	}

	@Test
	void testDeleteAuthor() {
		Author author = new Author();
		author.setFirstName("john");
		author.setLastName("t");

		Author saved = authorDao.saveNewAuthor(author);

		authorDao.deleteAuthorById(saved.getId());

		assertThat(authorDao.getById(saved.getId())).isNull();

	}

	@Test
	void testUpdateAuthor() {
		Author author = new Author();
		author.setFirstName("john");
		author.setLastName("t");

		Author saved = authorDao.saveNewAuthor(author);

		saved.setLastName("Thompson");
		Author updated = authorDao.updateAuthor(saved);

		assertThat(updated.getLastName()).isEqualTo("Thompson");
	}

	@Test
	void testSaveAuthor() {
		Author author = new Author();
		author.setFirstName("John");
		author.setLastName("Thompson");
		Author saved = authorDao.saveNewAuthor(author);

		assertThat(saved).isNotNull();
	}

	@Test
	void testGetAuthorByName() {
		Author author = authorDao.findAuthorByName("Craig", "Walls");

		assertThat(author).isNotNull();
	}

	@Test
	void testGetAuthorByNameNotFound() {
		assertThrows(EntityNotFoundException.class, () -> authorDao.findAuthorByName("foo", "bar"));
	}

	@Test
	void testGetAuthor() {

		Author author = authorDao.getById(1L);

		assertThat(author).isNotNull();

	}
}
