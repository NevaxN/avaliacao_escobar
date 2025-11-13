package questao_01;

/**
 * Esta classe mantém uma referência à estratégia atual e permite trocá-la.
 * É o "cliente"
 */
public class RiskProcessor {
    private RiskAlgorithm currentAlgorithm;

    // Método para trocar a estratégia em tempo de execução
    public void setAlgorithm(RiskAlgorithm algorithm){
        System.out.println("--- [SISTEMA]: Trocando algoritmo de risco ---");
        this.currentAlgorithm = algorithm;
    }

    // Método que executa a estratégia atual usando o contexto
    public void processRisk(FinancialContextDTO context){
        if(currentAlgorithm == null){
            System.err.println("Erro: Nenhum algoritmo de risco foi definido.");
            return;
        }

        this.currentAlgorithm.calculate(context);
    }
}
