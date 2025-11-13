package questao_02;

import java.util.HashMap;

/**
 * Implementa a interface moderna (Target) e "envolve" (wrap) o sistema legado (Adapter).
 */
public class AdapterBancario implements ProcessadorTransacoes{

    private final SistemaBancarioLegado sistemaLegado;

    public AdapterBancario(SistemaBancarioLegado sistemaLegado){
        this.sistemaLegado = sistemaLegado;
    }

    @Override
    public RespostaAutorizacaoDTO autorizar(String cartao, double valor, String moeda) {
        System.out.println("[ADAPTADOR] Recebida requisição moderna. Traduzindo para o formato legado...");

        //Converter a requisição (Moderna -> Legada)
        HashMap<String, Object> parametrosLegados = new HashMap<>();
        parametrosLegados.put("NUM_CARTAO", cartao);
        parametrosLegados.put("VALOR_TX", valor);

        //Tratar a restrição de código de moeda
        parametrosLegados.put("COD_MOEDA", converterMoedaParaCodigo(moeda));

        // A interface moderna não se importa com "CODIGO_LOJA", mas o legado sim.
        // O adaptador define um valor padrão.
        parametrosLegados.put("CODIGO_LOJA", "LOJA_WEB_01");

        //Chamar o sistema legado
        HashMap<String, Object> respostaLegada = this.sistemaLegado.processarTransacao(parametrosLegados);
        
        //Converter a resposta (Legada -> Moderna) (Problema 3: Bidirecional)
        return converterRespostaLegada(respostaLegada);
    }

    /**
     * Método auxiliar privado para a lógica de tradução.
     */
    private int converterMoedaParaCodigo(String moeda) {
        switch (moeda.toUpperCase()) {
            case "USD":
                return 1;
            case "EUR":
                return 2;
            case "BRL":
                return 3;
            default:
                return 99;
        }
    }

    /**
     * Método auxiliar privado para a lógica de tradução da resposta.
     */
    private RespostaAutorizacaoDTO converterRespostaLegada(HashMap<String, Object> respostaLegada) {
        int statusCode = (int) respostaLegada.get("STATUS_COD");
        String mensagem = (String) respostaLegada.get("MENSAGEM");

        boolean aprovada = (statusCode == 200);

        String idAutorizacao = null;
        if (aprovada && respostaLegada.containsKey("ID_TRANSACAO_LEGADA")) {
            // Converte o tipo de dado obsoleto (long) para o moderno (String)
            idAutorizacao = String.valueOf((long) respostaLegada.get("ID_TRANSACAO_LEGADA"));
        }

        System.out.println("[ADAPTADOR] Resposta legada traduzida para o formato moderno.");
        return new RespostaAutorizacaoDTO(aprovada, mensagem, idAutorizacao);
    }
}