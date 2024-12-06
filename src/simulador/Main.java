package simulador;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FileSystemSimulator simulador = new FileSystemSimulator();
        Scanner scanner = new Scanner(System.in);
        boolean executando = true;

        while (executando) {
            System.out.println("\nEscolha uma operação:");
            System.out.println("1 - Criar Diretório");
            System.out.println("2 - Criar Arquivo com Conteúdo");
            System.out.println("3 - Ler Conteúdo de Arquivo");
            System.out.println("4 - Copiar Arquivo");
            System.out.println("5 - Apagar Arquivo");
            System.out.println("6 - Renomear Arquivo");
            System.out.println("7 - Renomear Diretório");
            System.out.println("8 - Apagar Diretório");
            System.out.println("9 - Listar Conteúdo de um Diretório");
            System.out.println("10 - Sair");

            System.out.print("Opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (opcao) {
                case 1:
                    System.out.print("Caminho do novo diretório: ");
                    String caminhoNovoDiretorio = scanner.nextLine();
                    simulador.criarDiretorio(caminhoNovoDiretorio);
                    break;
                case 2:
                    System.out.print("Caminho do diretório onde o arquivo será criado: ");
                    String caminhoDiretorioArquivo = scanner.nextLine();
                    System.out.print("Nome do novo arquivo: ");
                    String nomeArquivo = scanner.nextLine();
                    System.out.print("Conteúdo do arquivo: ");
                    String conteudoArquivo = scanner.nextLine();
                    simulador.criarArquivo(caminhoDiretorioArquivo, nomeArquivo, conteudoArquivo.getBytes());
                    break;
                case 3:
                    System.out.print("Caminho do arquivo a ser lido: ");
                    String caminhoArquivoLer = scanner.nextLine();
                    simulador.lerArquivo(caminhoArquivoLer);
                    break;
                case 4:
                    System.out.print("Caminho do arquivo de origem: ");
                    String caminhoOrigem = scanner.nextLine();
                    System.out.print("Caminho do diretório de destino: ");
                    String caminhoDestino = scanner.nextLine();
                    simulador.copiarArquivo(caminhoOrigem, caminhoDestino);
                    break;
                case 5:
                    System.out.print("Caminho do arquivo a ser apagado: ");
                    String caminhoApagarArquivo = scanner.nextLine();
                    simulador.apagarArquivo(caminhoApagarArquivo);
                    break;
                case 6:
                    System.out.print("Caminho do arquivo a ser renomeado: ");
                    String caminhoRenomearArquivo = scanner.nextLine();
                    System.out.print("Novo nome: ");
                    String novoNomeArquivo = scanner.nextLine();
                    simulador.renomearArquivo(caminhoRenomearArquivo, novoNomeArquivo);
                    break;
                case 7:
                    System.out.print("Caminho do diretório a ser renomeado: ");
                    String caminhoRenomearDiretorio = scanner.nextLine();
                    System.out.print("Novo nome: ");
                    String novoNomeDiretorio = scanner.nextLine();
                    simulador.renomearDiretorio(caminhoRenomearDiretorio, novoNomeDiretorio);
                    break;
                case 8:
                    System.out.print("Caminho do diretório a ser apagado: ");
                    String caminhoApagarDiretorio = scanner.nextLine();
                    simulador.apagarDiretorio(caminhoApagarDiretorio);
                    break;
                case 9:
                    System.out.print("Caminho do diretório: ");
                    String caminhoListar = scanner.nextLine();
                    simulador.listarConteudo(caminhoListar);
                    break;
                case 10:
                    System.out.println("Saindo do simulador...");
                    executando = false;
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
        scanner.close();
        try {
            simulador.fechar();
        } catch (IOException e) {
            System.err.println("Erro ao fechar o simulador: " + e.getMessage());
        }
    }
}
