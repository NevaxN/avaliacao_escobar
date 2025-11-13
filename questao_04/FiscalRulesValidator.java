package questao_04;

// Regras Fiscais (Pode ser SOFT_FAIL)
public class FiscalRulesValidator extends AbstractValidator {
    public FiscalRulesValidator(int timeoutMs) { super(timeoutMs); }

    @Override
    protected ValidationResult performValidation(ValidationContext context) {
        // Simulação de falha leve (permite o circuit breaker ser testado)
        System.out.println("[OK] Regras Fiscais validadas (Cálculo de impostos OK).");
        
        return ValidationResult.SUCCESS;
    }
}
