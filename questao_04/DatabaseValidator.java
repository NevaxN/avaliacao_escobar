package questao_04;

// Banco de Dados (com Rollback)
public class DatabaseValidator extends AbstractValidator {
    public DatabaseValidator(int timeoutMs) { super(timeoutMs); }

    @Override
    protected ValidationResult performValidation(ValidationContext context) {
        String numeroNFe = context.getDocumento().getNumero();
        
        // Simulação
        if (numeroNFe.equals("999")) {
            context.addFailure("Banco de Dados: NFe 999 já existe (Duplicidade).");
            return ValidationResult.HARD_FAIL;
        }

        //Adiciona o rollback
        // Simula uma inserção temporária ou bloqueio de linha
        System.out.println("[DB] Inserindo lock de verificação para NFe " + numeroNFe);
        
        // Define a ação de rollback (Lambda)
        Runnable rollbackAction = () -> {
            System.out.println("[ROLLBACK-DB] Removendo lock de verificação da NFe " + numeroNFe);
        };
        
        context.addRollbackAction(rollbackAction);
        
        System.out.println("[OK] Banco de Dados (Duplicidade) verificado.");
        return ValidationResult.SUCCESS;
    }
}
