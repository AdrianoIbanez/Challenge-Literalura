package br.alura.cruso.Challenge.Literalura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Principal;

@SpringBootApplication
public class ChallengeLiteraluraApplication implements CommandLineRunner {

	@Autowired
	private LivroRepository livroRepositorio;

	@Autowired
	private AutorRepository autorRepositorio;

	@Autowired
	private IdiomaRepository idiomaRepositorio;

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
