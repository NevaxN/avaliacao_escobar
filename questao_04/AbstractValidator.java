package questao_04;

public abstract class AbstractValidator implements Validator {
    protected Validator next;
    private final int timeoutMs; // Timeout individual

    public AbstractValidator(int timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

    @Override
    public void setNext(Validator next) {
        this.next = next;
    }

    @Override
    public final void validate(ValidationContext context) {
        // Circuit Breaker
        if (context.isCircuitOpen()) {
            System.out.printf("[CHAIN] Circuit Breaker (3 falhas) ativado. Pulando %s.\n", getClass().getSimpleName());
            return;
        }

        System.out.println("--- Executando: " + getClass().getSimpleName());
        long startTime = System.currentTimeMillis();
        ValidationResult result = ValidationResult.HARD_FAIL; // Default to fail-safe

        try {
            // Executa a lógica de validação da subclasse
            result = performValidation(context);

            long duration = System.currentTimeMillis() - startTime;
            
            // Verificação de Timeout
            if (duration > timeoutMs) {
                context.addFailure(getClass().getSimpleName() + ": TIMEOUT (levou " + duration + "ms, max " + timeoutMs + "ms)");
                result = ValidationResult.HARD_FAIL;
            }

        } catch (Exception e) {
            context.addFailure(getClass().getSimpleName() + ": Exceção inesperada! " + e.getMessage());
            result = ValidationResult.HARD_FAIL;
        }

        // Chaining Condicional
        // A cadeia só continua se o resultado NÃO for HARD_FAIL
        if (next != null && result != ValidationResult.HARD_FAIL) {
            next.validate(context);
        }
    }

    /**
     * Subclasses implementam esta lógica.
     * @return O resultado da validação.
     */
    protected abstract ValidationResult performValidation(ValidationContext context);
}
