package questao_02;

/**
 * A interface "alvo" que o nosso novo sistema (cliente) usará.
 */
public interface ProcessadorTransacoes {
    RespostaAutorizacaoDTO autorizar(String cartao, double valor, String moeda);
}
