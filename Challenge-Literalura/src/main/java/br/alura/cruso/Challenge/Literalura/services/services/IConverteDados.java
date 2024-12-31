package br.alura.cruso.Challenge.Literalura.services.services;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
