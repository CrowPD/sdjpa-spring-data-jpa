package guru.springframework.jdbc.dao.template;

import guru.springframework.jdbc.dao.BookDao;
import guru.springframework.jdbc.domain.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Created by jt on 11/25/21.
 */
public class BookDaoJDBCTemplate implements BookDao {
	private final JdbcTemplate jdbcTemplate;

	public BookDaoJDBCTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Book> findAllSortedByTitle(Pageable pageable) {
		String sql = String.format("SELECT * FROM book ORDER BY title %s LIMIT ? OFFSET ?", pageable.getSort().getOrderFor("title").getDirection().name());
		return jdbcTemplate.query(sql, getBookMapper(), pageable.getPageSize(), pageable.getOffset());
	}

	@Override
	public List<Book> findAll(Pageable pageable) {
		return jdbcTemplate.query("SELECT * FROM book LIMIT ? OFFSET ?", getBookMapper(), pageable.getPageSize(), pageable.getOffset());
	}

	@Override
	public List<Book> findAll(int offset, int pageSize) {
		return jdbcTemplate.query("SELECT * FROM book LIMIT ? OFFSET ?", getBookMapper(), pageSize, offset);
	}

	@Override
	public List<Book> findAll() {
		return jdbcTemplate.query("SELECT * FROM book", getBookMapper());
	}

	@Override
	public Book getById(Long id) {
		return jdbcTemplate.queryForObject("SELECT * FROM book where id = ?", getBookMapper(), id);
	}

	@Override
	public Book findBookByTitle(String title) {
		return jdbcTemplate.queryForObject("SELECT * FROM book where title = ?", getBookMapper(), title);
	}

	@Override
	public Book findBookByIsbn(String isbn) {
		return jdbcTemplate.queryForObject("SELECT * FROM book where isbn = ?", getBookMapper(), isbn);
	}

	@Override
	public Book saveNewBook(Book book) {
		jdbcTemplate.update("INSERT INTO book (isbn, publisher, title, author_id) VALUES (?, ?, ?, ?)",
				book.getIsbn(), book.getPublisher(), book.getTitle(), book.getAuthorId());

		Long createdId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

		return this.getById(createdId);
	}

	@Override
	public Book updateBook(Book book) {
		jdbcTemplate.update("UPDATE book set isbn = ?, publisher = ?, title = ?, author_id = ? where id = ?",
				book.getIsbn(), book.getPublisher(), book.getTitle(), book.getAuthorId(), book.getId());

		return this.getById(book.getId());
	}

	@Override
	public void deleteBookById(Long id) {
		jdbcTemplate.update("DELETE from book where id = ?", id);
	}

	private BookMapper getBookMapper() {
		return new BookMapper();
	}
}
