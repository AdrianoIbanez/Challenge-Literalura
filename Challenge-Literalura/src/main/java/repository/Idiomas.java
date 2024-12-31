package repository;

import models.Idioma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Idiomas extends JpaRepository<Idioma, Long> {
    Optional<Idioma> findBySiglaIdioma(String siglaIdioma);
}
