package br.alura.cruso.Challenge.Literalura;

public class Principal {

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
                3 - Listar autores regisrados  
                4 - Listar autores vivos em um determinado ano
                5 - Listar livros em um determinado idioma
                6 - Top 10 livros mais baixados
                7 - Top 5 autores com mais downloads
                                
                0 - Sair
                
                ----------------------------------
                Digite uma opção válida de 1 a 7!
                """;
        System.out.println(menu);

        try {
            opcao = leitura.nextInt();
            leitura.nextLine();
            continue;
        }

        switch (opcao) {
            case 1:

               break;
            case 2;
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 0:
                break;
            default:
                System.out.println("\nOpção inválida! Tente novamente.");
        }
    }
}