package simulador;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Journal {
    private final String caminhoJournal = "journal.log";
    private List<Operacao> operacoesPendentes;

    public Journal() {
        operacoesPendentes = new ArrayList<>();
        recuperarOperacoes();
    }

    public void registrarOperacao(Operacao operacao) {
        operacoesPendentes.add(operacao);
        escreverOperacaoNoLog(operacao);
    }

    public void atualizarOperacao(Operacao operacao) {
        operacao.setStatus("concluída");
        escreverOperacaoNoLog(operacao);
    }

    private void escreverOperacaoNoLog(Operacao operacao) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoJournal, true))) {
            bw.write(operacao.formatarParaLog());
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao escrever no journal: " + e.getMessage());
        }
    }

    private void recuperarOperacoes() {
        File arquivoJournal = new File(caminhoJournal);
        if (arquivoJournal.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(arquivoJournal))) {
                String linha;
                while ((linha = br.readLine()) != null) {
                    Operacao op = Operacao.parseFromLog(linha);
                    if (op != null && !"concluída".equals(op.getStatus())) {
                        System.out.println("Recuperando operação pendente: " + op.getTipo() + " - " + op.getDetalhes());
                        // Aqui você pode reexecutar ou reverter a operação, se necessário
                        operacoesPendentes.add(op);
                    }
                }
            } catch (IOException e) {
                System.err.println("Erro ao ler o journal: " + e.getMessage());
            }
        }
    }

    // Método opcional para limpar o journal após as operações serem concluídas
    public void limparJournal() {
        File arquivoJournal = new File(caminhoJournal);
        if (arquivoJournal.exists()) {
            arquivoJournal.delete();
        }
        operacoesPendentes.clear();
    }
}
