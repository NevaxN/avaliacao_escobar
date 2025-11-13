package questao_04;

/**
 * JUSTIFICATIVA DA DECISÃO DE DESIGN (Conforme solicitado na memória):
 * * Padrão de Design Escolhido: Chain of Responsibility (CoR)
 * * Por que foi escolhido?
 * 1.  **Processamento em Cadeia (Problema 1):** O problema descreve uma "cadeia" de "validadores especializados". O CoR é o padrão clássico para conectar objetos de processamento (Handlers, ou no nosso caso, Validadores) em uma sequência, onde um documento (NF-e) passa por cada elo da cadeia.
 * 2.  **Lógica de Contexto e Estado (Req 2, 3, 4):** Um CoR simples apenas passa a solicitação. Para este problema, precisamos de mais controle (Circuit Breaker, Rollback, Condicionais). Para resolver isso, usamos uma variação do CoR onde:
 * a)  Um **Objeto de Contexto** (`ValidationContext`) é passado *através* da cadeia.
 * b)  Este contexto armazena o estado global: a lista de falhas, o contador para o circuit breaker (Req 3) e uma pilha de ações de rollback (Req 4).
 * c)  Os validadores não apenas decidem se *continuam* a cadeia, mas também *modificam* esse contexto.
 * 3.  **Controle Fino de Fluxo (Req 2, Restrição 1):**
 * - Para suportar "pule Y se X falhar", o `AbstractValidator` verifica o resultado de cada validador.
 * - Introduzimos o `ValidationResult` (SUCCESS, SOFT_FAIL, HARD_FAIL). Um `HARD_FAIL` (ex: Schema ou Certificado inválido) interrompe a cadeia *imediatamente*, satisfazendo a Restrição 1. Um `SOFT_FAIL` (ex: regra fiscal menor) registra a falha, mas permite que a cadeia continue, possibilitando que o Circuit Breaker (Req 3) seja atingido.
 * 4.  **Rollback (Req 4, Restrição 2):**
 * - O `DatabaseValidator` usa o `ValidationContext` para "empilhar" (`push`) uma ação de rollback (uma Lambda/Runnable).
 * - Se a cadeia falhar *depois* dele (ex: no `SefazValidator`), o cliente principal (o `main`) detecta a falha e invoca `context.executeRollbacks()`. A pilha LIFO garante que os rollbacks sejam executados na ordem inversa das operações, "desfazendo" o que o `DatabaseValidator` fez.
 * 5.  **Timeouts (Restrição 3):**
 * - O `AbstractValidator` (a classe base de todos os validadores) encapsula a lógica de medir o tempo de execução do método `performValidation` da subclasse. Se exceder o `timeoutMs` configurado, ele força uma falha, centralizando essa lógica.
 *
 */
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