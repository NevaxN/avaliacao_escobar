package questao_03;

public class OperacaoNormalState implements EstadoUsina {
    @Override
    public void verificarCondicoes(UsinaNuclear usina) {
        double temp = usina.getDadosSensores().temperatura;
        System.out.printf("[ESTADO] Operação Normal. Temp: %.1f°C\n", temp);

        // Regra: OPERACAO_NORMAL → ALERTA_AMARELO
        if (temp > 300) {
            System.out.println("[ALERTA] Temperatura > 300°C!");
            usina.setEstado(new AlertaAmareloState());
        }
    }

    @Override
    public void desligar(UsinaNuclear usina) {
        System.out.println("[AÇÃO] Iniciando desligamento controlado...");
        usina.setEstado(new DesligadaState());
    }
}
