package questao_04;

// Certificado Digital (Deve ser HARD_FAIL)
public class CertificateValidator extends AbstractValidator {
    public CertificateValidator(int timeoutMs) { super(timeoutMs); }

    @Override
    protected ValidationResult performValidation(ValidationContext context) {
        // Simulação
        if (context.getDocumento().getCertificado().equals("REVOGADO")) {
            context.addFailure("Certificado: Consta na lista de revogação (LCR).");
            return ValidationResult.HARD_FAIL; //Para a cadeia
        }
        System.out.println("[OK] Certificado Digital validado.");
        return ValidationResult.SUCCESS;
    }
}
