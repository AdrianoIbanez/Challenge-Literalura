package br.alura.cruso.Challenge.Literalura.services.repository;

import br.alura.cruso.Challenge.Literalura.services.models.Autor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Autores extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNome(String nome);

    List<Autor> findByAnoNascimentoLessThanEqualAndAnoFalecimentoGreaterThanEqual(int ano, int ano1);

    @Query("SELECT a AS autor, SUM(l.downloads) AS totalDownloads " +
            "FROM Autor a JOIN a.livros l " +
            "GROUP BY a " +
            "ORDER BY totalDownloads DESC")
    List<Object[]> findTopAutoresByDownloads(Pageable pageable);

}
