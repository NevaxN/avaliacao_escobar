package questao_01;

/**
* Essa Interface define a operação que todos os algoritmos de risco devem implementar.
*/
public interface RiskAlgorithm {
    void calculate(FinancialContextDTO context);
}
