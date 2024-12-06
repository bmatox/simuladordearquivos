package simulador;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Diretorio implements Serializable {
    private String nome;
    private List<Arquivo> arquivos;
    private List<Diretorio> subdiretorios;
    private LocalDateTime dataCriacao;

    public Diretorio(String nome) {
        this.nome = nome;
        this.arquivos = new ArrayList<>();
        this.subdiretorios = new ArrayList<>();
        this.dataCriacao = LocalDateTime.now();
    }

    // Métodos para gerenciar arquivos e diretórios
    public void adicionarArquivo(Arquivo arquivo) {
        arquivos.add(arquivo);
    }

    public void removerArquivo(String nomeArquivo) {
        arquivos.removeIf(arq -> arq.getNome().equals(nomeArquivo));
    }

    public Arquivo getArquivo(String nomeArquivo) {
        for (Arquivo arq : arquivos) {
            if (arq.getNome().equals(nomeArquivo)) {
                return arq;
            }
        }
        return null;
    }

    public void adicionarDiretorio(Diretorio diretorio) {
        subdiretorios.add(diretorio);
    }

    public void removerDiretorio(String nomeDiretorio) {
        subdiretorios.removeIf(dir -> dir.getNome().equals(nomeDiretorio));
    }

    public Diretorio getDiretorio(String nomeDiretorio) {
        for (Diretorio dir : subdiretorios) {
            if (dir.getNome().equals(nomeDiretorio)) {
                return dir;
            }
        }
        return null;
    }

    // Listar conteúdo do diretório
    public List<String> listarConteudo() {
        List<String> conteudo = new ArrayList<>();
        arquivos.forEach(arq -> conteudo.add(arq.getNome()));
        subdiretorios.forEach(dir -> conteudo.add(dir.getNome() + "/"));
        return conteudo;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
