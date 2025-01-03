package br.alura.cruso.Challenge.Literalura.services.main;

import br.alura.cruso.Challenge.Literalura.services.models.DadosDoAutor;
import br.alura.cruso.Challenge.Literalura.services.models.DadosLivro;
import br.alura.cruso.Challenge.Literalura.services.repository.Autores;
import br.alura.cruso.Challenge.Literalura.services.repository.Idiomas;
import br.alura.cruso.Challenge.Literalura.services.repository.Livros;
import br.alura.cruso.Challenge.Literalura.services.services.ConsumoAPI;
import br.alura.cruso.Challenge.Literalura.services.services.ConverteDados;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private final Scanner leitura = new Scanner(System.in);
    private final ConsumoAPI consumo = new ConsumoAPI();
    private final Livros repositorioLivros;
    private final Autores repositorioAutores;
    private final Idiomas repositorioIdiomas;
    private final ConverteDados conversor = new ConverteDados();

    public Principal(Livros repositorioLivros, Autores repositorioAutores, Idiomas repositorioIdiomas) {
        this.repositorioLivros = repositorioLivros;
        this.repositorioAutores = repositorioAutores;
        this.repositorioIdiomas = repositorioIdiomas;
    }

    public void exibirMenu() {
        int opcao = -1;
        while (opcao != 0) {
            String menu = """
                    \n-------------------------------
                    ------Biblioteca Literalura------
                    ---------------------------------
                    1 - Buscar Livros pelo titulo
                    2 - Buscar livro por autor
                    3 - Listar livros registrados 
                    4 - Listar autores regisrados
                    5 - Listar livros por autor  
                    6 - Listar autores vivos em um determinado ano
                    7 - Listar livros em um determinado idioma
                    8 - Top 10 livros mais baixados
                    9 - Top 5 autores com mais downloads
                    10 - Estatistica do banco de dados
                    
                    0 - Sair
                    
                    ----------------------------------
                    Digite a opção desejada!
                    """;
            System.out.println(menu);

            try {
                opcao = leitura.nextInt();
                leitura.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("\nOpção invalida, tente novamente!");
                leitura.nextline();
                continue;
            }

            switch (opcao) {
                case 1:
                    buscarLivroTitulo();
                    break;
                case 2:
                    buscarLivroAutor();
                    break;
                case 3:
                    listarLivrosRegistrados();
                    break;
                case 4:
                    listarAutoresRegistrados();
                    break;
                case 5:
                    listarlivrosPorAutor();
                    break;
                case 6:
                    listarAutoresVivosPorAno();
                    break;
                case 7:
                    listaLivrosPoridioma();
                    break;
                case 8:
                    listarTop10livros();
                    break;
                case 9:
                    listarTop5Autores();
                    break;
                case 10:
                    BancoDeDados();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("\nOpção inválida! Tente novamente.");
            }
        }
}
    private void buscarLivroTitulo() {

        System.out.println("Qual o título do livro?: ");
        var busca = leitura.nextLine();
        var jsomCompleto = consumo.consumoAPI(busca);

        try {
            var json = conversor.toObject(jsomCompleto);
            DadosLivro dados = conversor.obterDados(String.valueOf(json.get("results").get(0)),DadosLivro.class);

            System.out.println("\n********** LIVRO **********");
            System.out.println("Titulo: " + dados.titulo());
            System.out.println("Autor: " + formatarListaAutores(dados.autores());
            System.out.println("Idioma: " + formatarListaString(dados.idioma));
            System.out.println("Downloads: " + dados.downloads());
            System.out.println("*****************************");

            salvarDados(dados);
            System.out.println("\nLivro salvo com sucesso!\n");

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            System.out.println("\n Livro não encontrado!");
        } catch (DataIntegrityViolationException e) {
            System.out.println("\n Livro cadastrado anteriormente!");
        }
    }

    private String formatarListaAutores(List<DadosDoAutor> autores) {
        return autores.stream()
                .map(autor -> autor.nome() + "(Nascimento: " + autor.anoNascimento() +
                        ",Falecimento: " + (autor.anoFalescimento() != null ? autor.anoFalescimento() : "N/A") + ")")
                .collect(Collectors.joining("; "));
    }

    private String formatarListaString(List<String> lista) {
        return String.join(", " lista);
    }

    @Transactional



