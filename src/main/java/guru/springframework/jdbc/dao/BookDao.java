package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;
import jakarta.persistence.EntityNotFoundException;

public interface BookDao {

	Book getById(Long id);

	Book findBookByTitle(String title) throws EntityNotFoundException;

	Book saveNewBook(Book book);

	Book updateBook(Book book);

	void deleteBookById(Long id);
}
