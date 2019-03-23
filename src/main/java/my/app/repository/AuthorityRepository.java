package my.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import my.app.domain.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
