package br.alura.cruso.Challenge.Literalura.services.main;

import br.alura.cruso.Challenge.Literalura.services.models.*;
import br.alura.cruso.Challenge.Literalura.services.repository.Autores;
import br.alura.cruso.Challenge.Literalura.services.repository.Idiomas;
import br.alura.cruso.Challenge.Literalura.services.repository.Livros;
import br.alura.cruso.Challenge.Literalura.services.services.ConsumoAPI;
import br.alura.cruso.Challenge.Literalura.services.services.ConverteDados;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.*;
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


    public void exibeMenu() {
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
                leitura.nextLine();
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
                    listarlivroPorAutor();
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
                    bancoDeDados();
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
            System.out.println("Autor: " + formatarListaAutores(dados.autores()));
            System.out.println("Idioma: " + formatarListaString(dados.idioma()));
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
        return String.join(", ", lista);
    }

    @Transactional
    private void salvarDados(DadosLivro dados) {
        Autor autor = buscarOuCriarAutor(dados.autores().get(0));
        List<Idioma> idiomas = dados.idioma().stream()
                .map(this::buscarOuCriarIdioma)
                .collect(Collectors.toList());
        Livro livro = new Livro(dados.titulo(), autor, idiomas, dados.downloads());
        repositorioLivros.save(livro);

        if (autor.getLivros() == null) {
            autor.setLivros(new ArrayList<>());
        }
        autor.getLivros().add(livro);
        repositorioAutores.save(autor);
    }

    private Autor buscarOuCriarAutor(DadosDoAutor dadosDoAutor) {
        Optional<Autor> autorRegistrado = repositorioAutores.findByNome(dadosDoAutor.nome());
        if (autorRegistrado.isPresent()) {
            return autorRegistrado.get();
        } else {
            Autor novoAutor = new Autor();
            novoAutor.setNome(dadosDoAutor.nome());
            novoAutor.setAnoNascimento(dadosDoAutor.anoNascimento());
            novoAutor.setAnoFalecimento(dadosDoAutor.anoFalescimento());
            return repositorioAutores.save(novoAutor);
        }
    }

    private Idioma buscarOuCriarIdioma(String siglaIdioma) {
        Optional<Idioma> idiomaOptional = repositorioIdiomas.findBySiglaIdioma(siglaIdioma);
        if (idiomaOptional.isPresent()) {
            return idiomaOptional.get();
        } else {
            Idioma novoIdioma = new Idioma(siglaIdioma);
            return repositorioIdiomas.save(novoIdioma);
        }
    }

    private void buscarLivroAutor () {
        System.out.println("Informe o nome do autor: ");
        var busca = leitura.nextLine();
        var jsonCompleto = consumo.consumoAPI(busca);

        try {
            var json = conversor.toObject(jsonCompleto);

            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> resultados = mapper.convertValue(json.get("results"),
                    new TypeReference<>() {
                    });

            if (resultados.isEmpty()) {
                System.out.println("\nLivro não encontrado.");
                return;
            }

            resultados.sort(Comparator.comparing(result -> result.get("title").toString().toLowerCase()));

            System.out.println("\n**************************");
            System.out.println("     LIVROS ENCONTRADOS"   );
            System.out.println("**************************");
            resultados.forEach(result -> System.out.println(result.get("title")));
            System.out.println("**************************");
            System.out.println("Informe o titulo do livro: ");
            var tituloSelecionado = leitura.nextLine();

            Map<String, Object> livroSelecionado = resultados.stream()
                    .filter(result -> result.get("title").toString().toLowerCase().contains(tituloSelecionado.toLowerCase()))
                    .findFirst()
                    .orElse(null);

            if (livroSelecionado == null) {
                System.out.println("\nNenhum livro encontrado.");
                return;
            }

            DadosLivro dadosLivro = conversor.obterDados(mapper.writeValueAsString(livroSelecionado), DadosLivro.class);
            salvarDados(dadosLivro);
            System.out.println(dadosLivro);
            System.out.println("\nLivro salvo com sucesso.");

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (DataIntegrityViolationException e) {
            System.out.println("\nLivro cadastrado anteriormente!");
        }
    }


    private void listarLivrosRegistrados() {
        List<Livro> livros = repositorioLivros.findAll();

        if (livros.isEmpty()) {
            System.out.println("\nNenhum livro registrado!");
        } else {
            System.out.println("\n----------------------------");
            System.out.println("     LIVROS ENCONTRADOS     ");
            System.out.println("----------------------------");
            livros.stream()
                    .sorted(Comparator.comparing((Livro livro) -> livro.getAutor().getNome())
                            .thenComparing(Livro::getTitulo))
                    .forEach(System.out::println);
        }
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = repositorioAutores.findAll();

        if (autores.isEmpty()) {
            System.out.println("\nNenhum autor registrado!");
        } else {
            System.out.println("\n-----------------------------");
            System.out.println("     AUTORES ENCONTRADOS     ");
            System.out.println("-----------------------------");
            autores.stream()
                    .sorted(Comparator.comparing(Autor::getNome))
                    .forEach(System.out::println);
        }
    }

    private void listarlivroPorAutor() {
        System.out.println("Informe o nome do autor (pode ser um trecho do nome): ");
        String autorEscolhido = leitura.nextLine();

        List<Livro> livros = repositorioLivros.findByAutor_NomeContainingIgnoreCase(autorEscolhido);

        if (livros.isEmpty()) {
            System.out.println("\nNenhum livro encontrado para o autor com o nome informado.");
        } else {
            System.out.println("\n----------------------------");
            System.out.println("     LIVROS ENCONTRADOS     ");
            System.out.println("----------------------------");
            livros.forEach(System.out::println);
        }
    }

    private void listarAutoresVivosPorAno() {
        System.out.println("Informe o ano para verificar quais autores estavam vivos:");
        int ano = Integer.parseInt(leitura.nextLine());

        List<Autor> autoresVivos = repositorioAutores.findByAnoNascimentoLessThanEqualAndAnoFalecimentoGreaterThanEqual(ano, ano);

        if (autoresVivos.isEmpty()) {
            System.out.println("\nNenhum autor encontrado para o ano informado.");
        } else {
            System.out.println("\n-----------------------------------");
            System.out.println("     AUTORES VIVOS NO ANO " + ano);
            System.out.println("-----------------------------------");
            autoresVivos.forEach(System.out::println);
        }
    }

    private void listaLivrosPoridioma() {
        System.out.println("""
            Escolha o idioma para realizar a busca:
            
            1 - Espanhol (es)
            2 - Inglês (en)
            3 - Francês (fr)
            4 - Português (pt)
            
            Insira o número referente ao idioma selecionado (0 para sair):""");

        try {
            int escolhaIdioma = leitura.nextInt();
            leitura.nextLine();

            String idiomaEscolhido;

            switch (escolhaIdioma) {
                case 1:
                    idiomaEscolhido = "es";
                    break;
                case 2:
                    idiomaEscolhido = "en";
                    break;
                case 3:
                    idiomaEscolhido = "fr";
                    break;
                case 4:
                    idiomaEscolhido = "pt";
                    break;
                default:
                    System.out.println("\nEscolha inválida! Retornando ao menu inicial...");
                    return;
            }

            List<Livro> livrosEncontrados = repositorioLivros.findByIdioma_SiglaIdiomaOrderByAutor_NomeAsc(idiomaEscolhido);

            if (livrosEncontrados.isEmpty()) {
                System.out.println("\nNenhum livro encontrado para o idioma selecionado.");
            } else {
                System.out.println("\n----------------------------");
                System.out.println("     LIVROS ENCONTRADOS     ");
                System.out.println("----------------------------");
                for (Livro livro : livrosEncontrados) {
                    System.out.println(livro);
                }
            }

        } catch (InputMismatchException e) {
            System.out.println("\nEscolha inválida! Retornando ao menu inicial...");
            leitura.nextLine();
        }
    }

    private void listarTop10livros() {
        List<Livro> topLivros = repositorioLivros.findTop10ByOrderByDownloadsDesc();

        if (topLivros.isEmpty()) {
            System.out.println("\nNenhum livro registrado!");
        } else {
            System.out.println("\n---------------------------------------");
            System.out.println("      TOP 10 LIVROS MAIS BAIXADOS      ");
            System.out.println("---------------------------------------");
            topLivros.forEach(t ->
                    System.out.println("Downloads: " + t.getDownloads() + " | " + t.getTitulo()));
            System.out.println("---------------------------------------");
        }
    }

    private void listarTop5Autores() {
        PageRequest pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("totalDownloads")));

        List<Object[]> topAutores = repositorioAutores.findTopAutoresByDownloads(pageable);

        if (topAutores.isEmpty()) {
            System.out.println("\nNenhum autor registrado!");
        } else {
            System.out.println("\n--------------------------------------------");
            System.out.println("      TOP 5 AUTORES COM MAIS DOWNLOADS      ");
            System.out.println("--------------------------------------------");
            for (Object[] result : topAutores) {
                Autor autor = (Autor) result[0];
                Long totalDownloads = (Long) result[1];
                System.out.println("Downloads: " + totalDownloads + " | " + autor.getNome());
            }
            System.out.println("--------------------------------------------");
        }
    }

    private void bancoDeDados() {
        List<Livro> livros = repositorioLivros.findAll();

        if (livros.isEmpty()) {
            System.out.println("\nNenhum livro registrado no banco de dados.");
        } else {
            DoubleSummaryStatistics estatisticas = livros.stream()
                    .mapToDouble(Livro::getDownloads)
                    .summaryStatistics();

            System.out.println("\n------------------------------------------");
            System.out.println("      ESTATÍSTICAS DO BANCO DE DADOS      ");
            System.out.println("------------------------------------------");
            System.out.println("Total de livros: " + estatisticas.getCount());
            System.out.println("Soma dos downloads: " + estatisticas.getSum());
            System.out.println("Média dos downloads: " + estatisticas.getAverage());
            System.out.println("Máximo de downloads em um livro: " + estatisticas.getMax());
            System.out.println("Mínimo de downloads em um livro: " + estatisticas.getMin());
            System.out.println("------------------------------------------");
        }
    }

}