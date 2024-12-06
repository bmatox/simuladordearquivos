package simulador;

import java.io.IOException;
import java.util.List;

public class FileSystemSimulator {
    private FileSystem fileSystem;
    private Journal journal;

    public FileSystemSimulator() {
        this.fileSystem = new FileSystem();
        this.journal = new Journal();
    }

    public void fechar() throws IOException {
        fileSystem.fecharArquivoContêiner();
    }

    // Métodos para operações de arquivos e diretórios

    public void criarDiretorio(String caminho) {
        Operacao operacao = new Operacao("criarDiretorio", caminho, "iniciada");
        journal.registrarOperacao(operacao);

        Diretorio dirPai = buscarDiretorioPai(caminho);
        String nomeDiretorio = extrairNome(caminho);

        if (dirPai != null) {
            Diretorio novoDiretorio = new Diretorio(nomeDiretorio);
            dirPai.adicionarDiretorio(novoDiretorio);
            System.out.println("Diretório '" + nomeDiretorio + "' criado com sucesso.");
            fileSystem.salvarSistemaDeArquivos();
        } else {
            System.out.println("Diretório pai não encontrado.");
        }

        operacao.setStatus("concluída");
        journal.atualizarOperacao(operacao);
    }

    public void criarArquivo(String caminhoDiretorio, String nomeArquivo, byte[] conteudo) {
        System.out.println("Iniciando criação do arquivo '" + nomeArquivo + "' em '" + caminhoDiretorio + "'");
        Operacao operacao = new Operacao("criarArquivo", caminhoDiretorio + "/" + nomeArquivo, "iniciada");
        journal.registrarOperacao(operacao);

        Diretorio diretorio = buscarDiretorio(caminhoDiretorio);
        if (diretorio != null) {
            Arquivo arquivo = new Arquivo(nomeArquivo);
            try {
                fileSystem.escreverConteudoArquivo(arquivo, conteudo);
                diretorio.adicionarArquivo(arquivo);
                System.out.println("Arquivo '" + nomeArquivo + "' criado com sucesso em " + caminhoDiretorio);
                fileSystem.salvarSistemaDeArquivos();
            } catch (IOException e) {
                System.err.println("Erro ao criar o arquivo: " + e.getMessage());
            }
        } else {
            System.out.println("Diretório '" + caminhoDiretorio + "' não encontrado.");
        }

        operacao.setStatus("concluída");
        journal.atualizarOperacao(operacao);
    }


    public void lerArquivo(String caminhoArquivo) {
        Arquivo arquivo = buscarArquivo(caminhoArquivo);
        if (arquivo != null) {
            try {
                byte[] conteudo = fileSystem.lerConteudoArquivo(arquivo);
                System.out.println("Conteúdo do arquivo '" + arquivo.getNome() + "':\n");
                System.out.println(new String(conteudo)); // Supondo que o conteúdo seja texto
            } catch (IOException e) {
                System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            }
        } else {
            System.out.println("Arquivo '" + caminhoArquivo + "' não encontrado.");
        }
    }

    public void copiarArquivo(String caminhoOrigem, String caminhoDestino) {
        Operacao operacao = new Operacao("copiarArquivo", "De " + caminhoOrigem + " para " + caminhoDestino, "iniciada");
        journal.registrarOperacao(operacao);

        Arquivo arquivoOrigem = buscarArquivo(caminhoOrigem);
        Diretorio dirDestino = buscarDiretorio(caminhoDestino);

        if (arquivoOrigem != null && dirDestino != null) {
            try {
                // Ler o conteúdo do arquivo de origem
                byte[] conteudo = fileSystem.lerConteudoArquivo(arquivoOrigem);
                // Criar um novo arquivo no diretório de destino com o mesmo conteúdo
                Arquivo novoArquivo = new Arquivo(arquivoOrigem.getNome());
                fileSystem.escreverConteudoArquivo(novoArquivo, conteudo);
                dirDestino.adicionarArquivo(novoArquivo);
                fileSystem.salvarSistemaDeArquivos();
                System.out.println("Arquivo copiado com sucesso.");
            } catch (IOException e) {
                System.err.println("Erro ao copiar o arquivo: " + e.getMessage());
            }
        } else {
            if (arquivoOrigem == null) {
                System.out.println("Arquivo de origem '" + caminhoOrigem + "' não encontrado.");
            }
            if (dirDestino == null) {
                System.out.println("Diretório de destino '" + caminhoDestino + "' não encontrado.");
            }
        }

        operacao.setStatus("concluída");
        journal.atualizarOperacao(operacao);
    }

    public void apagarArquivo(String caminho) {
        Operacao operacao = new Operacao("apagarArquivo", caminho, "iniciada");
        journal.registrarOperacao(operacao);

        Diretorio dirPai = buscarDiretorioPai(caminho);
        String nomeArquivo = extrairNome(caminho);

        if (dirPai != null) {
            Arquivo arquivo = dirPai.getArquivo(nomeArquivo);
            if (arquivo != null) {
                fileSystem.removerConteudoArquivo(arquivo);
                dirPai.removerArquivo(nomeArquivo);
                fileSystem.salvarSistemaDeArquivos();
                System.out.println("Arquivo '" + nomeArquivo + "' apagado com sucesso.");
            } else {
                System.out.println("Arquivo '" + nomeArquivo + "' não encontrado no diretório.");
            }
        } else {
            System.out.println("Diretório pai '" + caminho + "' não encontrado.");
        }

        operacao.setStatus("concluída");
        journal.atualizarOperacao(operacao);
    }

    public void renomearArquivo(String caminho, String novoNome) {
        Operacao operacao = new Operacao("renomearArquivo", caminho + " para " + novoNome, "iniciada");
        journal.registrarOperacao(operacao);

        Arquivo arquivo = buscarArquivo(caminho);
        if (arquivo != null) {
            arquivo.setNome(novoNome);
            fileSystem.salvarSistemaDeArquivos();
            System.out.println("Arquivo renomeado com sucesso.");
        } else {
            System.out.println("Arquivo não encontrado.");
        }

        operacao.setStatus("concluída");
        journal.atualizarOperacao(operacao);
    }

    public void renomearDiretorio(String caminho, String novoNome) {
        Operacao operacao = new Operacao("renomearDiretorio", caminho + " para " + novoNome, "iniciada");
        journal.registrarOperacao(operacao);

        Diretorio diretorio = buscarDiretorio(caminho);
        if (diretorio != null) {
            diretorio.setNome(novoNome);
            fileSystem.salvarSistemaDeArquivos();
            System.out.println("Diretório renomeado com sucesso.");
        } else {
            System.out.println("Diretório não encontrado.");
        }

        operacao.setStatus("concluída");
        journal.atualizarOperacao(operacao);
    }

    public void apagarDiretorio(String caminho) {
        Operacao operacao = new Operacao("apagarDiretorio", caminho, "iniciada");
        journal.registrarOperacao(operacao);

        Diretorio dirPai = buscarDiretorioPai(caminho);
        String nomeDiretorio = extrairNome(caminho);

        if (dirPai != null) {
            dirPai.removerDiretorio(nomeDiretorio);
            fileSystem.salvarSistemaDeArquivos();
            System.out.println("Diretório '" + nomeDiretorio + "' apagado com sucesso.");
        } else {
            System.out.println("Diretório '" + caminho + "' não encontrado.");
        }

        operacao.setStatus("concluída");
        journal.atualizarOperacao(operacao);
    }

    public void listarConteudo(String caminho) {
        Diretorio diretorio = buscarDiretorio(caminho);
        if (diretorio != null) {
            List<String> conteudo = diretorio.listarConteudo();
            System.out.println("Conteúdo de " + caminho + ":");
            conteudo.forEach(System.out::println);
        } else {
            System.out.println("Diretório '" + caminho + "' não encontrado.");
        }
    }

    // Métodos auxiliares para navegação pelo sistema de arquivos

    private Diretorio buscarDiretorio(String caminho) {
        System.out.println("Buscando diretório: " + caminho);
        String[] partes = caminho.split("/");
        if (partes.length < 2 || !partes[1].equals("raiz")) {
            System.out.println("Caminho inválido ou raiz não encontrada.");
            return null;
        }
        Diretorio dirAtual = fileSystem.getDiretorioRaiz();
        for (int i = 2; i < partes.length; i++) {
            if (partes[i].isEmpty()) continue;
            dirAtual = dirAtual.getDiretorio(partes[i]);
            if (dirAtual == null) {
                System.out.println("Diretório '" + partes[i] + "' não encontrado.");
                return null;
            }
        }
        return dirAtual;
    }


    private Diretorio buscarDiretorioPai(String caminho) {
        String[] partes = caminho.split("/");
        if (partes.length < 3 || !partes[1].equals("raiz")) {
            //System.out.println("Caminho inválido ou raiz não encontrada.");
            return null;
        }
        Diretorio dirAtual = fileSystem.getDiretorioRaiz();
        for (int i = 2; i < partes.length - 1; i++) {
            if (partes[i].isEmpty()) continue;
            dirAtual = dirAtual.getDiretorio(partes[i]);
            if (dirAtual == null) {
                return null;
            }
        }
        return dirAtual;
    }

    private Arquivo buscarArquivo(String caminho) {
        String[] partes = caminho.split("/");
        if (partes.length < 3 || !partes[1].equals("raiz")) {
            //System.out.println("Caminho inválido ou raiz não encontrada.");
            return null;
        }
        Diretorio dirAtual = fileSystem.getDiretorioRaiz();
        for (int i = 2; i < partes.length - 1; i++) {
            if (partes[i].isEmpty()) continue;
            dirAtual = dirAtual.getDiretorio(partes[i]);
            if (dirAtual == null) {
                return null;
            }
        }
        return dirAtual.getArquivo(partes[partes.length - 1]);
    }

    private String extrairNome(String caminho) {
        String[] partes = caminho.split("/");
        return partes[partes.length - 1];
    }
}
