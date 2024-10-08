package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;
import guru.springframework.jdbc.repositories.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component("repoBookDao")
public class BookDaoImpl implements BookDao {
	private final BookRepository bookRepository;

	public BookDaoImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Override
	public List<Book> findAllSortedByTitle(Pageable pageable) {
		return bookRepository.findAll(pageable).getContent();
	}

	@Override
	public List<Book> findAll(Pageable pageable) {
		return bookRepository.findAll(pageable).getContent();
	}

	@Override
	public List<Book> findAll(int offset, int pageSize) {
		PageRequest pageRequest = PageRequest.ofSize(pageSize);

		int pageNum = 0;
		if (offset > 0) {
			pageNum = offset / pageSize;
		}
		return findAll(pageRequest.withPage(pageNum));
	}

	@Override
	public List<Book> findAll() {
		return bookRepository.findAll();
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
	public Book findBookByIsbn(String isbn) {
		return bookRepository.findMeAnIsbn(isbn);
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
