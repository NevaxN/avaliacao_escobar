package questao_02;

/**
 * DTO (Data Transfer Object) para a resposta moderna.
 * Esconde os tipos de dados legados (long, int) do cliente.
 */
public class RespostaAutorizacaoDTO {
    private final boolean aprovada;
    private final String mensagem;
    private final String idAutorizacao;

    public RespostaAutorizacaoDTO(boolean aprovada, String mensagem, String idAutorizacao) {
        this.aprovada = aprovada;
        this.mensagem = mensagem;
        this.idAutorizacao = idAutorizacao;
    }

    @Override
    public String toString() {
        return "RespostaAutorizacao{" +
                "aprovada=" + aprovada +
                ", mensagem='" + mensagem + '\'' +
                ", idAutorizacao='" + (idAutorizacao != null ? idAutorizacao : "N/A") + '\'' +
                '}';
    }
}
