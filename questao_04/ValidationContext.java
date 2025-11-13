package questao_04;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ValidationContext {
    private final DocumentoFiscalDTO documento;
    private final List<String> failures = new ArrayList<>();
    private final Deque<Runnable> rollbackActions = new ArrayDeque<>(); // LIFO Stack

    public ValidationContext(DocumentoFiscalDTO documento) {
        this.documento = documento;
    }

    public DocumentoFiscalDTO getDocumento() { return documento; }

    public void addFailure(String message) {
        System.err.println("[FALHA] " + message);
        failures.add(message);
    }

    public int getFailureCount() {
        return failures.size();
    }

    // Circuit Breaker
    public boolean isCircuitOpen() {
        return getFailureCount() >= 3;
    }

    public boolean hasFailed() {
        return !failures.isEmpty();
    }

    // Rollback
    public void addRollbackAction(Runnable action) {
        System.out.println("[ROLLBACK] Adicionando ação de rollback: " + action.toString());
        this.rollbackActions.push(action);
    }

    public void executeRollbacks() {
        System.out.println("\n[SISTEMA] Iniciando rollback de " + rollbackActions.size() + " ações...");
        while (!rollbackActions.isEmpty()) {
            rollbackActions.pop().run(); // Executa e remove (LIFO)
        }
    }
}
