package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;
import guru.springframework.jdbc.repositories.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BookDaoImpl implements BookDao {
	private final BookRepository bookRepository;

	public BookDaoImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Override
	public Book getById(Long id) {
		return bookRepository.findById(id).orElse(null);
	}

	@Override
	public Book findBookByTitle(String title) throws EntityNotFoundException {
		return bookRepository.findByTitle(title).orElseThrow(EntityNotFoundException::new);
	}

	@Override
	public Book saveNewBook(Book book) {
		return bookRepository.save(book);
	}

	@Transactional
	@Override
	public Book updateBook(Book book) {
		Optional<Book> dbBookOpt = bookRepository.findById(book.getId());
		if (dbBookOpt.isEmpty()) {
			throw new EntityNotFoundException("Book to update not found");
		}
		Book dbBook = dbBookOpt.get();
		dbBook.setIsbn(book.getIsbn());
		dbBook.setPublisher(book.getPublisher());
		dbBook.setTitle(book.getTitle());
		dbBook.setAuthorId(book.getAuthorId());
		return bookRepository.save(dbBook);
	}

	@Override
	public void deleteBookById(Long id) {
		bookRepository.deleteById(id);
	}
}
