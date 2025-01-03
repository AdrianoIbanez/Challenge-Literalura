package br.alura.cruso.Challenge.Literalura.services.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosDoAutor(@JsonAlias("name") String nome,
                           @JsonAlias("birth_year") Integer anoNascimento,
                           @JsonAlias("death_year") Integer anoFalescimento) {

    @Override
    public  String toString() {
        return nome + "(Nascimento: " + anoNascimento + ", Falecimento: " + anoFalescimento + ")";
    }
}