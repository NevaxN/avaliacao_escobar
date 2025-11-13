package questao_04;

// Suporte a validações condicionais (HARD_FAIL para a cadeia)
public enum ValidationResult {
    SUCCESS,
    SOFT_FAIL, // Registra falha, mas continua a cadeia
    HARD_FAIL  // Registra falha e interrompe a cadeia
}
