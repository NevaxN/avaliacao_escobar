package questao_03;

public class DesligadaState implements EstadoUsina {
    @Override
    public void verificarCondicoes(UsinaNuclear usina) {
        // Desligada, não verifica sensores de risco
        System.out.println("[ESTADO] Desligada. Aguardando comando para ligar.");
    }

    @Override
    public void ligar(UsinaNuclear usina) {
        System.out.println("[AÇÃO] Iniciando sequência de ligação...");
        usina.setEstado(new OperacaoNormalState());
    }

    @Override
    public void entrarEmManutencao(UsinaNuclear usina) {
        System.out.println("[AÇÃO] Entrando em manutenção a partir do estado DESLIGADA.");
        usina.setEstado(new ManutencaoState());
    }
}