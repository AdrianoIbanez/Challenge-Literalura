package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface idiomas extends JpaRepository<Idioma, Long> {
    Optional<idioma> findBySiglaIdioma(String siglaIdioma);
}
