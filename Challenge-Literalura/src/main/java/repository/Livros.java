package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Livros extends JpaRepository<Livro, Long> {
    List<Livro> findByIdioma_SiglaIdiomaOrderByAutor_NomeAsc(String idiomaEscolhido);
    List<Livro> findTop10ByOrderByDownloadsDesc();
    List<Livro> FindByAutor_NomeContainigIgnoreCase(String autorEscolhido);
}
