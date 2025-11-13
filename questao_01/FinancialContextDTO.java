package questao_01;

/**
 * Este DTO (Data Transfer Object) simples armazena os parâmetros financeiros
 * que serão compartilhados entre todos os algoritmos.
 */
public class FinancialContextDTO {
    private double portfolioValue;
    private double volatility;
    private int timeHorizonDays;

    public FinancialContextDTO(double portfolioValue, double volatility, int timeHorizonDays){
        this.portfolioValue = portfolioValue;
        this.volatility = volatility;
        this.timeHorizonDays = timeHorizonDays;        
    }

    public double getPortfolioValue(){
        return portfolioValue;
    }

    public double getVolatility(){
        return volatility;
    }

    public int getTimeHorizonDays(){
        return timeHorizonDays;
    }
}
