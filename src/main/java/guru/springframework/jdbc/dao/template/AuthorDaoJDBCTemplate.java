package guru.springframework.jdbc.dao.template;

import guru.springframework.jdbc.dao.AuthorDao;
import guru.springframework.jdbc.domain.Author;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class AuthorDaoJDBCTemplate implements AuthorDao {

	private final JdbcTemplate jdbcTemplate;

	public AuthorDaoJDBCTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Author> findByLastName(Pageable pageable, String lastName) {
		String sql = String.format(
				"SELECT * FROM author WHERE last_name = ? ORDER BY first_name %s LIMIT ? OFFSET ?",
				pageable.getSort().getOrderFor("first_name").getDirection().name()
		);
		return jdbcTemplate.query(sql, getRowMapper(), lastName, pageable.getPageSize(), pageable.getOffset());
	}

	private RowMapper<Author> getRowMapper() {
		return new AuthorMapper();
	}

	@Override
	public Author getById(Long id) {
		return null;
	}

	@Override
	public Author findAuthorByName(String firstName, String lastName) throws EntityNotFoundException {
		return null;
	}

	@Override
	public Author saveNewAuthor(Author author) {
		return null;
	}

	@Override
	public Author updateAuthor(Author author) {
		return null;
	}

	@Override
	public void deleteAuthorById(Long id) {

	}
}
