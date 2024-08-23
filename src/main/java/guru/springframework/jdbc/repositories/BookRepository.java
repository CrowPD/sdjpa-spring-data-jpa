package guru.springframework.jdbc.repositories;

import guru.springframework.jdbc.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
	Optional<Book> findByTitle(String title);

	/* skipped examples on returning a future, a stream and working with nulls.
	 The last one requires package-info.java annotation.
	 The first one requires @Async annotation on method
	 */

	@Query("SELECT b FROM Book b WHERE b.isbn = :isbnParam")
	Book findMeAnIsbn(@Param("isbnParam") String isbn);
}
