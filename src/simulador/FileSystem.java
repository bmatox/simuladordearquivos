package simulador;

import java.io.*;
import java.time.LocalDateTime;

public class FileSystem {
    private Diretorio diretorioRaiz;
    private final String caminhoContêiner = "sistemaDeArquivos.dat";
    private RandomAccessFile arquivoContêiner;
    private GerenciadorDeEspaco gerenciadorDeEspaco;

    public FileSystem() {
        this.gerenciadorDeEspaco = new GerenciadorDeEspaco();
        carregarSistemaDeArquivos();
    }

    private void carregarSistemaDeArquivos() {
        try {
            File arquivo = new File(caminhoContêiner);
            if (arquivo.exists() && arquivo.length() > 8) {
                arquivoContêiner = new RandomAccessFile(arquivo, "rw");

                // Lê o tamanho do sistema de arquivos serializado
                arquivoContêiner.seek(0);
                long tamanhoSistema = arquivoContêiner.readLong();

                // Calcula a posição onde o sistema de arquivos começa
                long posicaoSistema = arquivoContêiner.length() - tamanhoSistema;

                // Se a posição for menor que 8, ajustamos para 8
                if (posicaoSistema < 8) {
                    posicaoSistema = 8;
                }

                // Lê os bytes do sistema de arquivos serializado
                arquivoContêiner.seek(posicaoSistema);
                byte[] sistemaDeArquivosBytes = new byte[(int) tamanhoSistema];
                arquivoContêiner.readFully(sistemaDeArquivosBytes);

                // Deserializa o sistema de arquivos
                ByteArrayInputStream bis = new ByteArrayInputStream(sistemaDeArquivosBytes);
                ObjectInputStream ois = new ObjectInputStream(bis);
                diretorioRaiz = (Diretorio) ois.readObject();
                ois.close();

            } else {
                arquivoContêiner = new RandomAccessFile(arquivo, "rw");
                diretorioRaiz = new Diretorio("raiz");
                salvarSistemaDeArquivos();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            diretorioRaiz = new Diretorio("raiz"); // Inicializa o diretório raiz em caso de erro
        }
    }


    public void salvarSistemaDeArquivos() {
        try {
            // Serializa o sistema de arquivos
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(diretorioRaiz);
            oos.flush();

            byte[] sistemaDeArquivosBytes = bos.toByteArray();
            long tamanhoSistema = sistemaDeArquivosBytes.length;

            // Escreve o tamanho do sistema de arquivos no início do arquivo
            arquivoContêiner.seek(0);
            arquivoContêiner.writeLong(tamanhoSistema);

            // Calcula a posição onde o sistema de arquivos começa
            long posicaoSistema = arquivoContêiner.length() - tamanhoSistema;

            // Se o arquivo está vazio ou posicaoSistema for menor que 8 (após o long inicial), ajustamos a posição
            if (posicaoSistema < 8) {
                posicaoSistema = 8; // Escrevemos o sistema de arquivos logo após o tamanho
            }

            // Escreve o sistema de arquivos na posição calculada
            arquivoContêiner.seek(posicaoSistema);
            arquivoContêiner.write(sistemaDeArquivosBytes);

            // Não truncar o arquivo - remove chamada ao setLength
            // Isso evita perder os dados dos arquivos

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void escreverConteudoArquivo(Arquivo arquivo, byte[] conteudo) throws IOException {
        // Obtém o tamanho do sistema de arquivos
        long tamanhoSistema = obterTamanhoSistemaDeArquivos();

        // Calcula a posição onde o sistema de arquivos começa
        long posicaoSistema = arquivoContêiner.length() - tamanhoSistema;

        // Se a posição for menor que 8, ajustamos para 8
        if (posicaoSistema < 8) {
            posicaoSistema = 8;
        }

        // Calcula a posição para escrever o conteúdo do arquivo
        Long posicao = gerenciadorDeEspaco.alocarEspaco(conteudo.length);
        if (posicao == null || posicao >= posicaoSistema) {
            // Escreve o conteúdo antes do sistema de arquivos
            posicao = posicaoSistema;
            posicaoSistema += conteudo.length; // Move a posição do sistema de arquivos
        }

        // Escreve o conteúdo do arquivo
        arquivoContêiner.seek(posicao);
        arquivoContêiner.write(conteudo);

        // Atualiza as informações do arquivo
        arquivo.setPosicaoInicio(posicao);
        arquivo.setTamanho(conteudo.length);
        arquivo.setDataModificacao(LocalDateTime.now());

        // Salva o sistema de arquivos atualizado
        salvarSistemaDeArquivos();
    }


    public byte[] lerConteudoArquivo(Arquivo arquivo) throws IOException {
        if (arquivo.getPosicaoInicio() < 0 || arquivo.getTamanho() <= 0) {
            return new byte[0];
        }

        arquivoContêiner.seek(arquivo.getPosicaoInicio());
        byte[] conteudo = new byte[(int) arquivo.getTamanho()];
        arquivoContêiner.readFully(conteudo);
        return conteudo;
    }

    public void removerConteudoArquivo(Arquivo arquivo) {
        // Marca o espaço ocupado como livre
        gerenciadorDeEspaco.adicionarEspacoLivre(arquivo.getPosicaoInicio(), arquivo.getTamanho());
        arquivo.setPosicaoInicio(-1);
        arquivo.setTamanho(0);
        arquivo.setDataModificacao(LocalDateTime.now());

        salvarSistemaDeArquivos();
    }

    // Método auxiliar para obter o tamanho do sistema de arquivos
    private long obterTamanhoSistemaDeArquivos() {
        try {
            arquivoContêiner.seek(0);
            return arquivoContêiner.readLong();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Diretorio getDiretorioRaiz() {
        return diretorioRaiz;
    }

    public void fecharArquivoContêiner() throws IOException {
        arquivoContêiner.close();
    }
}
