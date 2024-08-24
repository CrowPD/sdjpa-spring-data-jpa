package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorMapper implements RowMapper<Author> {
	@Override
	public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
		var result = new Author();
		result.setId(rs.getLong("id"));
		result.setFirstName(rs.getString("first_name"));
		result.setLastName(rs.getString("last_name"));
		return result;
	}
}
