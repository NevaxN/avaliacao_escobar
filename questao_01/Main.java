package questao_01;
/**
 * JUSTIFICATIVA DA DECISÃO DE DESIGN (Conforme solicitado na memória):
 * * Padrão de Design Escolhido: Strategy Pattern
 * * Por que foi escolhido?
 * 1.  **Intercambialidade (Requisito 1 & 3):** O problema exige que algoritmos (Value at Risk, Expected Shortfall, etc.) sejam "intercambiáveis em tempo de execução". O padrão Strategy é ideal para isso, pois encapsula cada algoritmo em seu próprio objeto (Strategy).
 * 2.  **Desacoplamento (Restrição 1):** O cliente (o "RiskProcessor" no nosso caso) precisa apenas conhecer a interface comum (`RiskAlgorithm`). Ele não precisa saber os detalhes de implementação de `ValueAtRiskStrategy` ou `StressTestingStrategy`. Isso permite trocar de "estratégia" dinamicamente.
 * 3.  **Contexto Compartilhado (Requisito 2):** O padrão permite passar um objeto de contexto (que chamamos de `FinancialContext`) para a interface do algoritmo. Isso garante que todos os algoritmos, por mais diferentes que sejam, recebam o mesmo conjunto de dados financeiros para processar.
 *
 */
public class Main {
    public static void main(String[] args) {
        FinancialContextDTO context = new FinancialContextDTO(1000000.0, 0.2, 10);

        RiskProcessor processor = new RiskProcessor();

        processor.setAlgorithm(new ValueRiskStrategy());
        processor.processRisk(context);

        System.out.println("\n");

        processor.setAlgorithm(new StressTestingStrategy());
        processor.processRisk(context);

        System.out.println("\n");

        processor.setAlgorithm(new ExpectedShortfallStrategy());
        processor.processRisk(context);
    }
}
