package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackages = {"guru.springframework.jdbc.dao"})
class BookDaoJDBCTemplateTest {
	private static final int PAGE_SIZE = 3;

	@Autowired
	JdbcTemplate jdbcTemplate;

	BookDao bookDao;

	@BeforeEach
	void setUp() {
		bookDao = new BookDaoJDBCTemplate(jdbcTemplate);
	}

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
	void getById() {
		Book book = bookDao.getById(3L);

		assertThat(book.getId()).isNotNull();
	}

	@Test
	void findBookByTitle() {
		Book book = bookDao.findBookByTitle("Clean Code");

		assertThat(book).isNotNull();
	}

	@Test
	void saveNewBook() {
		Book book = new Book();
		book.setIsbn("1234");
		book.setPublisher("Self");
		book.setTitle("my book");
		book.setAuthorId(1L);

		Book saved = bookDao.saveNewBook(book);

		assertThat(saved).isNotNull();
	}

	@Test
	void updateBook() {
		Book book = new Book();
		book.setIsbn("1234");
		book.setPublisher("Self");
		book.setTitle("my book");
		book.setAuthorId(1L);
		Book saved = bookDao.saveNewBook(book);

		saved.setTitle("New Book");
		bookDao.updateBook(saved);

		Book fetched = bookDao.getById(saved.getId());

		assertThat(fetched.getTitle()).isEqualTo("New Book");
	}

	@Test
	void deleteBookById() {

		Book book = new Book();
		book.setIsbn("1234");
		book.setPublisher("Self");
		book.setTitle("my book");
		Book saved = bookDao.saveNewBook(book);

		bookDao.deleteBookById(saved.getId());

		assertThrows(EmptyResultDataAccessException.class, () -> bookDao.getById(saved.getId()));
	}
}