package questao_01;

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
