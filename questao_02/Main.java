package questao_02;

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
