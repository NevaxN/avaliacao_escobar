package questao_03;

public class AlertaVermelhoState implements EstadoUsina {
    @Override
    public void verificarCondicoes(UsinaNuclear usina) {
        boolean falhaResfriamento = usina.getDadosSensores().sistemaResfriamentoFalhou;
        double temp = usina.getDadosSensores().temperatura;
        System.out.printf("[ESTADO] ALERTA VERMELHO. Resfriamento falhou: %b\n", falhaResfriamento);

        // Regra: ALERTA_VERMELHO → EMERGENCIA
        if (falhaResfriamento) {
            System.out.println("[EMERGÊNCIA] FALHA NO SISTEMA DE RESFRIAMENTO!");
            usina.setEstado(new EmergenciaState());
        }
        // Regra: Bidirecional (ALERTA_VERMELHO -> ALERTA_AMARELO)
        else if (temp <= 400) {
            System.out.println("[INFO] Risco (Temp > 400°C) contido. Voltando para Alerta Amarelo.");
            usina.setEstado(new AlertaAmareloState());
        }
    }
}
