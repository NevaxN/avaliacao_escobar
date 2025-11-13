package questao_03;

public interface EstadoUsina {
    // Método principal de verificação de condições
    void verificarCondicoes(UsinaNuclear usina);

    // Métodos de transição explícita (padrão)
    // Por padrão, a maioria das ações é inválida em um estado
    default void ligar(UsinaNuclear usina) {
        System.out.println("[AÇÃO INVÁLIDA] Não é possível LIGAR neste estado.");
    }

    default void desligar(UsinaNuclear usina) {
        System.out.println("[AÇÃO INVÁLIDA] Não é possível DESLIGAR neste estado.");
    }

    default void entrarEmManutencao(UsinaNuclear usina) {
        // A manutenção pode ser iniciada de quase qualquer estado
        usina.setEstado(new ManutencaoState());
    }

    default void sairDaManutencao(UsinaNuclear usina) {
        System.out.println("[AÇÃO INVÁLIDA] Não é possível SAIR DA MANUTENÇÃO neste estado.");
    }
}
