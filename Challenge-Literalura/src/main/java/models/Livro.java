package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

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




}
