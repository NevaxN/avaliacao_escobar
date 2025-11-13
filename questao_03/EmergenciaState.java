package questao_03;

public class EmergenciaState implements EstadoUsina{
    @Override
    public void verificarCondicoes(UsinaNuclear usina) {
        // Estado terminal. Apenas um reset manual (desligar) pode tirá-la daqui.
        System.out.println("[ESTADO] !!! EMERGÊNCIA !!! SCRAM ATIVADO !!!");
    }

    @Override
    public void desligar(UsinaNuclear usina) {
        // Permite o desligamento de emergência
        System.out.println("[AÇÃO] Desligamento de emergência completo.");
        usina.setEstado(new DesligadaState());
    }

    @Override
    public void entrarEmManutencao(UsinaNuclear usina) {
        System.out.println("[AÇÃO INVÁLIDA] Não é possível entrar em manutenção durante uma EMERGÊNCIA.");
    }
}
