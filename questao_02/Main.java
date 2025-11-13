package questao_02;
/**
 * JUSTIFICATIVA DA DECISÃO DE DESIGN (Conforme solicitado na memória):
 * * Padrão de Design Escolhido: Adapter Pattern
 * * Por que foi escolhido?
 * 1.  **Incompatibilidade de Interfaces (Problema Central):** O problema descreve um cenário clássico do Adapter: temos uma interface "cliente" (moderna) `ProcessadorTransacoes` e uma interface "adaptada" (legada) `SistemaBancarioLegado` que são incompatíveis. O Adapter serve como uma "ponte" ou "tradutor" entre elas.
 * 2.  **Encapsulamento da Complexidade (Problema 2 e 4):** A interface legada usa estruturas complexas (HashMap) e exige campos que a moderna não tem (ex: `CODIGO_LOJA`). O Adapter encapsula toda essa lógica de "tradução" (mapeamento de `String` para `int`, adição de campos default), mantendo o cliente (que usa `ProcessadorTransacoes`) limpo e ignorante sobre a complexidade do legado.
 * 3.  **Bidirecionalidade (Problema 3):** O padrão Adapter não se limita a "traduzir a ida". Ele também é responsável por "traduzir a volta". Na nossa implementação, o método `autorizar` chama o legado e, em seguida, traduz a `HashMap` de resposta legada de volta para um objeto `RespostaAutorizacao` moderno.
 *
 */
public class Main {
    public static void main(String[] args) {
        // O cliente só conhece a interface moderna e sua resposta.
        // Ele não sabe nada sobre HashMaps, códigos de moeda ou IDs legados (long).

        SistemaBancarioLegado sistemaLegado = new SistemaBancarioLegado();
        ProcessadorTransacoes processador = new AdapterBancario(sistemaLegado); // O cliente recebe o adaptador

        // Cenário de Sucesso
        System.out.println("\n--- [CLIENTE] Testando transação BRL (Baixo Valor) ---");
        RespostaAutorizacaoDTO resp1 = processador.autorizar("1111-2222", 450.00, "BRL");
        System.out.println("[CLIENTE] Resposta recebida: " + resp1);

        // Cenário de Rejeição (Regra de negócio do legado)
        System.out.println("\n--- [CLIENTE] Testando transação BRL (Alto Valor) ---");
        RespostaAutorizacaoDTO resp2 = processador.autorizar("3333-4444", 8000.00, "BRL");
        System.out.println("[CLIENTE] Resposta recebida: " + resp2);
        
        // Cenário de Sucesso (Outra moeda)
        System.out.println("\n--- [CLIENTE] Testando transação USD (Alto Valor) ---");
        RespostaAutorizacaoDTO resp3 = processador.autorizar("5555-6666", 8000.00, "USD");
        System.out.println("[CLIENTE] Resposta recebida: " + resp3);
    }
}
