package br.alura.cruso.Challenge.Literalura.services.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosDoIdioma(@JsonAlias("languages") String siglaIdioma) {
}
