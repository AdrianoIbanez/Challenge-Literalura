package br.alura.cruso.Challenge.Literalura.services.repository;

import br.alura.cruso.Challenge.Literalura.services.models.Idioma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Idiomas extends JpaRepository<Idioma, Long> {
    Optional<Idioma> findBySiglaIdioma(String siglaIdioma);
}
