package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookDao {

	List<Book> findAll(Pageable pageable);

	List<Book> findAll(int offset, int pageSize);

	List<Book> findAll();

	Book getById(Long id);

	Book findBookByTitle(String title) throws EntityNotFoundException;

	Book findBookByIsbn(String isbn);

	Book saveNewBook(Book book);

	Book updateBook(Book book);

	void deleteBookById(Long id);
}
