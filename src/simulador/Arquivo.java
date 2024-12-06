package simulador;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Arquivo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private long posicaoInicio; // Posição inicial do conteúdo no arquivo contêiner
    private long tamanho;       // Tamanho do conteúdo
    private LocalDateTime dataCriacao;
    private LocalDateTime dataModificacao;

    public Arquivo(String nome) {
        this.nome = nome;
        this.posicaoInicio = -1; // Indica que o conteúdo ainda não foi escrito
        this.tamanho = 0;
        this.dataCriacao = LocalDateTime.now();
        this.dataModificacao = LocalDateTime.now();
    }

    // Getters e Setters

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public long getPosicaoInicio() { return posicaoInicio; }
    public void setPosicaoInicio(long posicaoInicio) { this.posicaoInicio = posicaoInicio; }

    public long getTamanho() { return tamanho; }
    public void setTamanho(long tamanho) { this.tamanho = tamanho; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public LocalDateTime getDataModificacao() { return dataModificacao; }
    public void setDataModificacao(LocalDateTime dataModificacao) { this.dataModificacao = dataModificacao; }
}
