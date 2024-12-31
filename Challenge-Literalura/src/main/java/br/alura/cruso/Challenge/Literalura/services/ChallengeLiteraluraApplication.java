package br.alura.cruso.Challenge.Literalura.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import br.alura.cruso.Challenge.Literalura.services.repository.Autores;
import br.alura.cruso.Challenge.Literalura.services.repository.Idiomas;
import br.alura.cruso.Challenge.Literalura.services.repository.Livros;

import java.security.Principal;

@SpringBootApplication
public class ChallengeLiteraluraApplication implements CommandLineRunner {

	@Autowired
	private Livros livroRepositorio;

	@Autowired
	private Autores autorRepositorio;

	@Autowired
	private Idiomas idiomaRepositorio;

	public static void main(String[] args) {
		SpringApplication.run(ChallengeLiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(livroRepositorio, autorRepositorio, idiomaRepositorio); {
		principal.exibirMenu();
		}
	}
}
