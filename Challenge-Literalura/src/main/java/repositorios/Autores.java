package repositorios;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Autores extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNome(String nome);
    List<Autor> findByAnonascimentoLessThanEqualAndAnoFalecimentoGreaterThanEqual(int ano, int ano1);

    @Query("SELECT a, SUM(1.downloads) AS totalDownloads " +
            "FROM Auor a JOIN a.livros 1 " +
            "GROUP BY a " +
            "ORDER BY totalDownloads DESC")
    List<Object[]> findTopAutoresByDownloads(Pageable pageable);
}
