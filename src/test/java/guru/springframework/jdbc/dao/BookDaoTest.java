package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ComponentScan("guru.springframework.jdbc.dao.hibernate")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookDaoTest {
	private static final int PAGE_SIZE = 3;

	@Autowired
	BookDao bookDao;

	@Test
	void findAllSortedByTitle() {
		List<Book> books = bookDao.findAllSortedByTitle(PageRequest.of(0, PAGE_SIZE, Sort.by(Sort.Order.desc("title"))));

		assertThat(books).isNotNull();
		assertThat(books.size()).isEqualTo(PAGE_SIZE);
		assertThat(books.get(0).getTitle().compareTo(books.get(1).getTitle())).isGreaterThanOrEqualTo(0);
	}

	@Test
	void findAllPagedLimited1Page() {
		List<Book> books = bookDao.findAll(PageRequest.of(0, PAGE_SIZE));

		assertThat(books).isNotNull();
		assertThat(books.size()).isEqualTo(PAGE_SIZE);
	}

	@Test
	void findAllPagedLimited2Page() {
		List<Book> books = bookDao.findAll(PageRequest.of(1, PAGE_SIZE));
		assertThat(books).isNotNull();
		assertThat(books.size()).isEqualTo(PAGE_SIZE);
	}

	@Test
	void findAllPagedLimitedOverhead() {
		List<Book> books = bookDao.findAll(PageRequest.of(40, PAGE_SIZE));
		assertThat(books).isNotNull();
		assertThat(books.size()).isEqualTo(0);
	}

	@Test
	void findAllLimited1Page() {
		List<Book> books = bookDao.findAll(0, PAGE_SIZE);

		assertThat(books).isNotNull();
		assertThat(books.size()).isEqualTo(PAGE_SIZE);
	}

	@Test
	void findAllLimited2Page() {
		List<Book> books = bookDao.findAll(3, PAGE_SIZE);
		assertThat(books).isNotNull();
		assertThat(books.size()).isEqualTo(PAGE_SIZE);
	}

	@Test
	void findAllLimitedOverhead() {
		List<Book> books = bookDao.findAll(40, PAGE_SIZE);
		assertThat(books).isNotNull();
		assertThat(books.size()).isEqualTo(0);
	}

	@Test
	void findAll() {
		List<Book> books = bookDao.findAll();

		assertThat(books).isNotNull();
		assertThat(books.size()).isGreaterThan(0);
	}

	@Test
	void testGetById() {
		assertThat(bookDao.getById(1L)).isNotNull();
	}

	@Test
	void testFindByTitle() {
		Book byId = bookDao.getById(1L);

		Book byTitle = bookDao.findBookByTitle(byId.getTitle());
		assertThat(byTitle).isNotNull();
		assertThat(byTitle.getId()).isEqualTo(byId.getId());
	}

	@Test
	void testSave() {
		Book book = new Book();
		book.setTitle("New Testing Amendment");
		book.setIsbn("333-555-101");
		book.setPublisher("Tat Media");
		book.setAuthorId(1L);
		var saved = bookDao.saveNewBook(book);

		assertThat(saved.getId()).isGreaterThan(0);

		bookDao.deleteBookById(saved.getId());
	}

	@Test
	void testUpdate() {
		Book book = new Book();
		book.setTitle("New Testing Amendment");
		book.setIsbn("333-555-101");
		book.setPublisher("Tat Media");
		book.setAuthorId(1L);
		var saved = bookDao.saveNewBook(book);


		saved.setPublisher("Faf Media");
		Book updated = bookDao.updateBook(saved);

		assertThat(updated.getPublisher()).isEqualTo(saved.getPublisher());

		bookDao.deleteBookById(saved.getId());
	}

	@Test
	void testDelete() {
		Book book = new Book();
		book.setTitle("New Testing Amendment");
		book.setIsbn("333-555-101");
		book.setPublisher("Tat Media");
		book.setAuthorId(1L);
		var saved = bookDao.saveNewBook(book);

		bookDao.deleteBookById(saved.getId());

		assertThat(bookDao.getById(saved.getId())).isNull();
	}

	@Test
	void testFindByIsbn() {
		assertThat(bookDao.findBookByIsbn("978-1617297571")).isNotNull();
	}
}
