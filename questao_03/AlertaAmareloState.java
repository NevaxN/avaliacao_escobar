package questao_03;

import java.time.Duration;
import java.time.Instant;

public class AlertaAmareloState implements EstadoUsina{
    private final Instant timestampEntrada = Instant.now();

    @Override
    public void verificarCondicoes(UsinaNuclear usina) {
        double temp = usina.getDadosSensores().temperatura;
        System.out.printf("[ESTADO] Alerta Amarelo. Temp: %.1f°C\n", temp);

        // Regra: ALERTA_AMARELO → ALERTA_VERMELHO
        if (temp > 400) {
            Duration duracaoNoEstado = Duration.between(timestampEntrada, Instant.now());
            System.out.printf("... temperatura > 400°C por %d segundos\n", duracaoNoEstado.getSeconds());

            if (duracaoNoEstado.getSeconds() > 30) {
                System.out.println("[ALERTA] Temperatura acima de 400°C por mais de 30s!");
                usina.setEstado(new AlertaVermelhoState());
            }
        }
        // Regra: Bidirecional (ALERTA_AMARELO -> OPERACAO_NORMAL)
        else if (temp <= 300) {
            System.out.println("[INFO] Temperatura estabilizada.");
            usina.setEstado(new OperacaoNormalState());
        }
    }
}
