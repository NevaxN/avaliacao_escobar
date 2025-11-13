package questao_01;

/** 
 * Cada algoritmo é encapsulado em sua própria classe.
 * Esse algoritmo sendo o StressTest
*/
public class StressTestingStrategy implements RiskAlgorithm {
    @Override
    public void calculate(FinancialContextDTO context) {
        System.out.printf("[ALGORITMO StressTest]: Executando Stress Test no portfólio de %.2f...\n",
                context.getPortfolioValue());
        System.out.println("... Cenário 'Crise de 2008' aplicado: Perda de 40%.");
    }
}
