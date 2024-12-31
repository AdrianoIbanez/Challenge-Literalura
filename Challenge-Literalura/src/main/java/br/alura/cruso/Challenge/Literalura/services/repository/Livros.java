package br.alura.cruso.Challenge.Literalura.services.repository;

import br.alura.cruso.Challenge.Literalura.services.models.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Livros extends JpaRepository<Livro, Long> {
    List<Livro> findByIdioma_SiglaIdiomaOrderByAutor_NomeAsc(String idiomaEscolhido);
    List<Livro> findTop10ByOrderByDownloadsDesc();
    List<Livro> FindByAutor_NomeContainigIgnoreCase(String autorEscolhido);
}
