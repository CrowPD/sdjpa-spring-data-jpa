package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackages = {"guru.springframework.jdbc.dao"})
class AuthorDaoJDBCTemplateTest {
	private static final int PAGE_SIZE = 5;

	@Autowired
	JdbcTemplate jdbcTemplate;

	AuthorDao authorDao;

	@BeforeEach
	void setUp() {
		authorDao = new AuthorDaoJDBCTemplate(jdbcTemplate);
	}

	@Test
	void findAllSmithsSortedByFirstName() {
		List<Author> authors = authorDao.findByLastName(PageRequest.of(0, PAGE_SIZE, Sort.by(Sort.Order.desc("first_name"))), "Smith");
		assertThat(authors).isNotNull();
		assertThat(authors.size()).isEqualTo(PAGE_SIZE);
		assertThat(authors.get(0).getFirstName().compareTo(authors.get(1).getFirstName())).isGreaterThanOrEqualTo(0);
	}

	@Test
	void findAllSmiths2ndPage() {
		List<Author> authors = authorDao.findByLastName(PageRequest.of(2, PAGE_SIZE, Sort.by(Sort.Order.desc("first_name"))), "Smith");
		assertThat(authors).isNotNull();
		assertThat(authors.size()).isEqualTo(PAGE_SIZE);
	}

	@Test
	void findAllSmithsOverflow() {
		List<Author> authors = authorDao.findByLastName(PageRequest.of(20, PAGE_SIZE, Sort.by(Sort.Order.desc("first_name"))), "Smith");
		assertThat(authors).isNotNull();
		assertThat(authors.size()).isEqualTo(0);
	}

}