package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "livros")
@JsonIgnoreProperties(ignoreUnknown = true)

public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Autor autor;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "livros_idiomas",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "idioma_id" ))
    private List<Idioma> idioma;

    private Integer downloads;

    public Livro() {}

    public Livro(String titulo, Autor autor, List<idioma> idioma, Integer downloads) {
        this.titulo = titulo;
        this.idioma = idioma;
        this.autor = autor;
        this.downloads = downloads;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Idioma> getIdiomas {
        return idioma;
    }

    public void setIdioma(List<Idioma> idiomas) {
        this.idioma = idiomas;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }

    @Override
    public String toString() {
        return "\n********** LIVRO **********" + "\n" +
               "Titulo: " + titulo + "\n" +
               "Autor: " + formatarAutor(autor) + "\n" +
               "Idioma: " + formataridiomas(idioma) + "\n" +
               "Downloads: " + downloads + "\n" +
               "*****************************";
    }

    private  String formatarAutor(Autor autor) {
        if (autor == null) {
            return "Autor desconhecido!";
        }
        return  autor.getNome();
}
private String formataridiomas(List<Idioma> idiomas) {
    if (idiomas == null || idiomas.isEmpty()) {
        return "Idioma desconhecido!";
}
return idiomas.stream()
    .map(Idioma::getSiglaIdioma)
    .collect(Collectors.joining(", "));
}
}
