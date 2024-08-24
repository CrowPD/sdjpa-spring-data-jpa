package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@DataJpaTest
@ComponentScan("guru.springframework.jdbc.dao.hibernate")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthorDaoTest {
	private final int PAGE_SIZE = 5;
	@Autowired
	AuthorDao authorDao;

	@Test
	void findAllSmithsSortedByFirstName() {
		List<Author> authors = authorDao.findByLastName(PageRequest.of(0, PAGE_SIZE, Sort.by(Sort.Order.desc("firstName"))), "Smith");
		AssertionsForClassTypes.assertThat(authors).isNotNull();
		AssertionsForClassTypes.assertThat(authors.size()).isEqualTo(PAGE_SIZE);
		AssertionsForClassTypes.assertThat(authors.get(0).getFirstName().compareTo(authors.get(1).getFirstName())).isGreaterThanOrEqualTo(0);
	}

	@Test
	void findAllSmiths2ndPage() {
		List<Author> authors = authorDao.findByLastName(PageRequest.of(2, PAGE_SIZE, Sort.by(Sort.Order.desc("firstName"))), "Smith");
		AssertionsForClassTypes.assertThat(authors).isNotNull();
		AssertionsForClassTypes.assertThat(authors.size()).isEqualTo(PAGE_SIZE);
	}

	@Test
	void findAllSmithsOverflow() {
		List<Author> authors = authorDao.findByLastName(PageRequest.of(20, PAGE_SIZE, Sort.by(Sort.Order.desc("firstName"))), "Smith");
		AssertionsForClassTypes.assertThat(authors).isNotNull();
		AssertionsForClassTypes.assertThat(authors.size()).isEqualTo(0);
	}

	@Test
	void testGetById() {
		Author author = authorDao.getById(1L);

		assertThat(author).isNotNull();
	}

	@Test
	void testGetByName() {
		Author foundByName = authorDao.findAuthorByName("Eric", "Evans");
		assertThat(foundByName).isNotNull();

		Author foundById = authorDao.getById(foundByName.getId());
		assertThat(foundById).isNotNull();
		assertThat(foundByName.getFirstName()).isEqualTo(foundById.getFirstName());
		assertThat(foundByName.getLastName()).isEqualTo(foundById.getLastName());
	}

	@Test
	void testSave() {
		Author author = new Author();
		author.setFirstName("Andy");
		author.setLastName("Newman");

		Author saved = authorDao.saveNewAuthor(author);

		assertThat(saved.getFirstName()).isEqualTo(author.getFirstName());
		assertThat(saved.getId()).isNotNull();

		authorDao.deleteAuthorById(saved.getId());
	}

	@Test
	void testUpdate() {
		Author author = new Author();
		author.setFirstName("Andy");
		author.setLastName("Newman");

		Author saved = authorDao.saveNewAuthor(author);

		saved.setLastName("Oldboy");
		var updated = authorDao.updateAuthor(saved);

		assertThat(updated.getLastName()).isEqualTo(saved.getLastName());

		authorDao.deleteAuthorById(updated.getId());
	}

	@Test
	void testDelete() {
		Author author = new Author();
		author.setFirstName("Andy");
		author.setLastName("Newman");

		Author saved = authorDao.saveNewAuthor(author);

		authorDao.deleteAuthorById(saved.getId());
		assertThat(authorDao.getById(saved.getId())).isNull();
	}

	@Test
	void testFindByLastName() {
		List<Author> list = authorDao.findByLastName(
				PageRequest.of(0, 1),
				"Smith"
		);

		assertThat(list).isNotNull();
		assertThat(list.size()).isEqualTo(1);
	}
}
