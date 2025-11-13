package questao_03;

public class ManutencaoState implements EstadoUsina {
    @Override
    public void verificarCondicoes(UsinaNuclear usina) {
        // Este método ignora os sensores de temperatura/pressão.
        System.out.println("[ESTADO] Em Manutenção. Sensores de operação ignorados.");
    }

    @Override
    public void sairDaManutencao(UsinaNuclear usina) {
        System.out.println("[AÇÃO] Saindo da manutenção. Usina retornará ao estado DESLIGADA.");
        usina.setEstado(new DesligadaState());
    }

    @Override
    public void ligar(UsinaNuclear usina) {
        System.out.println("[AÇÃO INVÁLIDA] Não é possível LIGAR enquanto estiver em manutenção.");
    }
}
