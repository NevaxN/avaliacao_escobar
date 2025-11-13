package questao_04;

public class Main {

    // Método auxiliar para construir a cadeia
    private static Validator setupChain(Validator... validators) {
        for (int i = 0; i < validators.length - 1; i++) {
            validators[i].setNext(validators[i + 1]);
        }
        return validators[0]; // Retorna o início da cadeia
    }

    public static void main(String[] args) {
        
        System.out.println("--- CENÁRIO 1: Falha na SEFAZ (Testando Rollback) ---");
        runCenario1();

        System.out.println("\n\n--- CENÁRIO 2: Falha Crítica no Certificado (Testando Chaining Condicional) ---");
        runCenario2();
        
        System.out.println("\n\n--- CENÁRIO 3: Falha por Timeout (Testando Restrição 3) ---");
        runCenario3();
    }

    private static void runCenario1() {
        // Tudo passa, exceto a SEFAZ. Deve acionar o rollback do DB.
        
        // Configuração dos validadores (timeouts em ms)
        Validator chain = setupChain(
                new XmlSchemaValidator(500),
                new CertificateValidator(1000),
                new FiscalRulesValidator(300),
                new DatabaseValidator(800),
                new SefazValidator(1000, true, 200) // Timeout de 1s, falha SEFAZ, latência de 200ms
        );

        DocumentoFiscalDTO doc = new DocumentoFiscalDTO("123", "<nfe>...</nfe>", "VALIDO");
        ValidationContext context = new ValidationContext(doc);

        // Inicia a validação
        chain.validate(context);

        // Analisa o resultado
        if (context.hasFailed()) {
            System.out.println("\n[SISTEMA] Validação do Cenário 1 FALHOU.");
            // Executa o rollback
            context.executeRollbacks();
        } else {
            System.out.println("\n[SISTEMA] Validação do Cenário 1 SUCESSO.");
        }
    }

    private static void runCenario2() {
        // Falha no Certificado. A cadeia deve parar (Restrição 1).
        
        Validator chain = setupChain(
                new XmlSchemaValidator(500),
                new CertificateValidator(1000), // Vai falhar
                new FiscalRulesValidator(300), // Não deve executar
                new DatabaseValidator(800), // Não deve executar
                new SefazValidator(1000, false, 200) // Não deve executar
        );

        DocumentoFiscalDTO doc = new DocumentoFiscalDTO("456", "<nfe>...</nfe>", "REVOGADO"); // Certificado inválido
        ValidationContext context = new ValidationContext(doc);

        chain.validate(context);

        if (context.hasFailed()) {
            System.out.println("\n[SISTEMA] Validação do Cenário 2 FALHOU (como esperado).");
            // O rollback não deve ser chamado ou estará vazio, pois o DBValidator não executou
            context.executeRollbacks();
        }
    }

    private static void runCenario3() {
        // Sefaz demora demais (Timeout).
        
        Validator chain = setupChain(
                new XmlSchemaValidator(500),
                new CertificateValidator(1000),
                new FiscalRulesValidator(300),
                new DatabaseValidator(800),
                new SefazValidator(1000, false, 1200) // Timeout de 1s, latência de 1.2s
        );

        DocumentoFiscalDTO doc = new DocumentoFiscalDTO("789", "<nfe>...</nfe>", "VALIDO");
        ValidationContext context = new ValidationContext(doc);

        chain.validate(context);

        if (context.hasFailed()) {
            System.out.println("\n[SISTEMA] Validação do Cenário 3 FALHOU (Timeout).");
            // Deve acionar o rollback do DB
            context.executeRollbacks();
        }
    }
}