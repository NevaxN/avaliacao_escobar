package questao_04;

public class SefazValidator extends AbstractValidator {
    private final boolean simularFalha;
    private final int latenciaSimulada;

    public SefazValidator(int timeoutMs, boolean simularFalha, int latenciaSimulada) {
        super(timeoutMs);
        this.simularFalha = simularFalha;
        this.latenciaSimulada = latenciaSimulada;
    }

    @Override
    protected ValidationResult performValidation(ValidationContext context) {
        try {
            // Simula latência de rede
            Thread.sleep(latenciaSimulada);
        } catch (InterruptedException e) {
            // Ignorado na simulação
        }

        if (simularFalha) {
            context.addFailure("SEFAZ: Lote rejeitado (Erro 215: Falha na conexão).");
            return ValidationResult.HARD_FAIL; // Falha na SEFAZ é crítica
        }

        System.out.println("[OK] SEFAZ (Consulta Online) validada.");
        return ValidationResult.SUCCESS;
    }
}
