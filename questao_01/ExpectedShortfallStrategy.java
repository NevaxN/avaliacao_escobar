package questao_01;

/** 
 * Cada algoritmo é encapsulado em sua própria classe.
 * Esse algoritmo sendo o ES
*/
public class ExpectedShortfallStrategy implements RiskAlgorithm {
    @Override
    public void calculate(FinancialContextDTO context) {
        double es = context.getPortfolioValue() * context.getVolatility() * 2.06;
        System.out.printf("[ALGORITMO ES]: Expected Shortfall (97.5%%) com volatilidade %.2f: %.2f\n",
                context.getVolatility(), es);
    }
}
