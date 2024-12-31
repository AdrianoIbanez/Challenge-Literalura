package models;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public record DadosLivro(@JsonAlias("id") Integer id,
                         @JsonAlias("title") String titulo,
                         @JsonAlias("authors") List<DadosDoAutor> autores,
                         @JsonAlias("languages") List<String> idioma,
                         @JsonAlias("download_count") Integer downloads) {

    @Override
    public String toString() {
        return "\n********** LIVRO **********" + "\n" +
               "Titulo: " + titulo + "\n" +
               "Autor: " + autores + "\n" +
               "Idioma: " + idioma + "\n" +
               "Downloads: " + downloads +"\n" +
               "*****************************";
    }
}
