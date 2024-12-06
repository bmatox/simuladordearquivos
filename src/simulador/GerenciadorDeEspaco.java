package simulador;

import java.util.*;

public class GerenciadorDeEspaco {
    private NavigableMap<Long, Long> espacosLivres; // Mapeia posição inicial -> tamanho

    public GerenciadorDeEspaco() {
        espacosLivres = new TreeMap<>();
    }

    // Adiciona um espaço livre
    public void adicionarEspacoLivre(long posicao, long tamanho) {
        espacosLivres.put(posicao, tamanho);
    }

    // Encontra um espaço onde caiba o tamanho necessário
    public Long alocarEspaco(long tamanhoNecessario) {
        for (Map.Entry<Long, Long> entrada : espacosLivres.entrySet()) {
            if (entrada.getValue() >= tamanhoNecessario) {
                long posicao = entrada.getKey();
                long tamanhoDisponivel = entrada.getValue();

                // Ajusta ou remove o espaço livre utilizado
                if (tamanhoDisponivel > tamanhoNecessario) {
                    espacosLivres.put(posicao + tamanhoNecessario, tamanhoDisponivel - tamanhoNecessario);
                }
                espacosLivres.remove(posicao);
                return posicao;
            }
        }
        // Se não encontrar espaço, retorna null
        return null;
    }

    // Obter o primeiro espaço livre após uma posição específica
    public long obterFimDoArquivoContido(long tamanhoArquivo) {
        if (espacosLivres.isEmpty()) {
            return tamanhoArquivo;
        } else {
            return Math.max(tamanhoArquivo, espacosLivres.lastKey() + espacosLivres.lastEntry().getValue());
        }
    }
}
