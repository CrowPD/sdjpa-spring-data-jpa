package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by jt on 8/22/21.
 */
public interface AuthorDao {
	List<Author> findByLastName(Pageable pageable, String lastName);

	Author getById(Long id);

	Author findAuthorByName(String firstName, String lastName) throws EntityNotFoundException;

	Author saveNewAuthor(Author author);

	Author updateAuthor(Author author);

	void deleteAuthorById(Long id);
}
