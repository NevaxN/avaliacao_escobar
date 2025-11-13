package questao_04;

// Schema XML (Deve ser HARD_FAIL)
public class XmlSchemaValidator extends AbstractValidator{
    public XmlSchemaValidator(int timeoutMs) { super(timeoutMs); }

    @Override
    protected ValidationResult performValidation(ValidationContext context) {
        // Simulação
        if (!context.getDocumento().getXmlContent().startsWith("<nfe>")) {
            context.addFailure("XML Schema: Tag raiz <nfe> ausente.");
            return ValidationResult.HARD_FAIL; //Para a cadeia
        }
        System.out.println("[OK] XML Schema validado.");
        return ValidationResult.SUCCESS;
    }
}
