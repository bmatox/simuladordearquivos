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
            if (arquivo.exists()) {
                arquivoContêiner = new RandomAccessFile(arquivo, "rw");
                arquivoContêiner.seek(0);

                // Lê o sistema de arquivos serializado a partir do início do arquivo
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo));
                diretorioRaiz = (Diretorio) ois.readObject();
                ois.close();
            } else {
                arquivoContêiner = new RandomAccessFile(arquivo, "rw");
                diretorioRaiz = new Diretorio("raiz");
                salvarSistemaDeArquivos();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
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

            // Escreve no início do arquivo contêiner
            arquivoContêiner.seek(0);
            arquivoContêiner.writeLong(tamanhoSistema);
            arquivoContêiner.write(sistemaDeArquivosBytes);
            arquivoContêiner.setLength(tamanhoSistema + 8); // 8 bytes para o tamanho

            // Opcionalmente, atualize informações sobre espaços livres
            // Exemplo simplificado aqui
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Operações com arquivos

    public void escreverConteudoArquivo(Arquivo arquivo, byte[] conteudo) throws IOException {
        Long posicao = gerenciadorDeEspaco.alocarEspaco(conteudo.length);
        if (posicao == null) {
            // Não há espaço livre suficiente, escrever no final do arquivo
            posicao = arquivoContêiner.length();
        }

        arquivo.setPosicaoInicio(posicao);
        arquivo.setTamanho(conteudo.length);
        arquivo.setDataModificacao(LocalDateTime.now());

        arquivoContêiner.seek(posicao);
        arquivoContêiner.write(conteudo);

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

    // Métodos para acessar o diretório raiz
    public Diretorio getDiretorioRaiz() {
        return diretorioRaiz;
    }

    public void fecharArquivoContêiner() throws IOException {
        arquivoContêiner.close();
    }
}
