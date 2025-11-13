package questao_02;

import java.util.HashMap;

/**
 * Esta classe é antiga e não pode ser modificada.
 */
public class SistemaBancarioLegado {
    /**
     * O método legado que recebe um mapa complexo e retorna outro mapa.
     */
    public HashMap<String, Object> processarTransacao(HashMap<String, Object> parametros){
        System.out.println("[LEGADO] Processando transação com parâmetros: " + parametros.toString());

        double valor = (double) parametros.get("VALOR_TX");
        int codMoeda = (int) parametros.get("COD_MOEDA");
        String codLoja = (String) parametros.get("CODIGO_LOJA");

        HashMap<String, Object> respostaLegada = new HashMap<>();

        if (codLoja == null || codLoja.isEmpty()) {
            respostaLegada.put("STATUS_COD", 500);
            respostaLegada.put("MENSAGEM", "CODIGO_LOJA E OBRIGATORIO");
            return respostaLegada;
        }

        if (valor > 5000 && codMoeda == 3) {
            respostaLegada.put("STATUS_COD", 403);
            respostaLegada.put("MENSAGEM", "REJEITADO_LIMITE_BRL");
        } else {
            respostaLegada.put("STATUS_COD", 200);
            respostaLegada.put("MENSAGEM", "APROVADO");
            respostaLegada.put("ID_TRANSACAO_LEGADA", 987654L);
        }

        System.out.println("[LEGADO] Resposta: " + respostaLegada.toString());
        return respostaLegada;
    }
}
