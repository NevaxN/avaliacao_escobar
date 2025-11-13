package questao_01;

/** 
 * Cada algoritmo é encapsulado em sua própria classe.
 * Esse algoritmo sendo o VaR
*/
public class ValueRiskStrategy implements RiskAlgorithm{
    @Override
    public void calculate(FinancialContextDTO context){
        double var = context.getPortfolioValue() * context.getVolatility() * 1.645;
        System.out.printf("[ALGORITMO VaR]: Value at Risk (95%%) para %d dias: %.2f\n",
                context.getTimeHorizonDays(), var);
    }
}
