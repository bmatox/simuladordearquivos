package simulador;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Operacao {
    private String tipo;
    private String detalhes;
    private LocalDateTime timestamp;
    private String status;

    // Formato para serialização das datas
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public Operacao(String tipo, String detalhes, String status) {
        this.tipo = tipo;
        this.detalhes = detalhes;
        this.timestamp = LocalDateTime.now();
        this.status = status;
    }

    // Getters e Setters

    public String getTipo() {
        return tipo;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Métodos para formatar a operação para o log e ler do log

    public String formatarParaLog() {
        return String.format("%s|%s|%s|%s", timestamp.format(formatter), tipo, detalhes, status);
    }

    public static Operacao parseFromLog(String linha) {
        String[] partes = linha.split("\\|");
        if (partes.length != 4) return null;
        LocalDateTime timestamp = LocalDateTime.parse(partes[0], formatter);
        Operacao op = new Operacao(partes[1], partes[2], partes[3]);
        op.timestamp = timestamp;
        return op;
    }
}
