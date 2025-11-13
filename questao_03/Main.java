package questao_03;
/**
 * JUSTIFICATIVA DA DECISÃO DE DESIGN (Conforme solicitado na memória):
 * * Padrão de Design Escolhido: State Pattern
 * * Por que foi escolhido?
 * 1.  **Encapsulamento de Comportamento (Problema 1):** O problema descreve um objeto (UsinaNuclear) que muda drasticamente seu comportamento com base em seu estado interno. O State Pattern é ideal para isso, pois encapsula a lógica de cada estado (DESLIGADA, OPERACAO_NORMAL, etc.) em sua própria classe.
 * 2.  **Prevenção de Transições Inválidas (Problema 3 e 4):** Ao invés de um grande 'switch' ou 'if/else' na classe 'UsinaNuclear' (o "Contexto"), a lógica de transição é delegada ao *objeto de estado atual*. A classe 'OperacaoNormalState' só pode transicionar para 'AlertaAmareloState' ou 'DesligadaState'. Ela nem *sabe* da existência do 'EmergenciaState'. Isso torna impossível a transição 'OPERACAO_NORMAL -> EMERGENCIA', satisfazendo a regra de que 'EMERGENCIA' só vem de 'ALERTA_VERMELHO'.
 * 3.  **Gerenciamento de Overrides (Problema 5):** O modo 'ManutencaoState' é implementado como outro estado. Quando o contexto é forçado a entrar nesse estado, a lógica de 'verificarSensores' do 'ManutencaoState' (que não faz nada ou apenas verifica a conclusão da manutenção) "sobreescreve" a lógica dos estados operacionais, pois é ela que será executada.
 *
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        UsinaNuclear usina = new UsinaNuclear();
        
        // Simular loop de monitoramento
        Runnable monitor = () -> {
            while (true) {
                usina.verificarSensores();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        };
        Thread monitorThread = new Thread(monitor);
        monitorThread.setDaemon(true); 
        monitorThread.start();

        // Ligar
        Thread.sleep(2000);
        usina.ligar();
        Thread.sleep(2000);

        // Aumentar temperatura -> ALERTA_AMARELO
        System.out.println("\n--- SIMULANDO AQUECIMENTO (301°C) ---");
        usina.atualizarSensores(new SensorDataDTO(301.0, 1.5, false));
        Thread.sleep(2000);

        // Normalizar temperatura -> OPERACAO_NORMAL (Teste bidirecional)
        System.out.println("\n--- SIMULANDO RESFRIAMENTO (290°C) ---");
        usina.atualizarSensores(new SensorDataDTO(290.0, 1.4, false));
        Thread.sleep(2000);

        // Aumentar temperatura > 400°C, mas por menos de 30s
        System.out.println("\n--- SIMULANDO AQUECIMENTO FORTE (410°C) ---");
        usina.atualizarSensores(new SensorDataDTO(410.0, 2.0, false));
        Thread.sleep(5000);

        // Aumentar temperatura > 400°C por mais de 30s
        System.out.println("\n--- SIMULANDO ALERTA VERMELHO (Pulando 30s) ---");
        AlertaAmareloState estadoAntigo = new AlertaAmareloState();
        usina.setEstado((EstadoUsina) java.lang.reflect.Proxy.newProxyInstance(
            EstadoUsina.class.getClassLoader(),
            new Class<?>[] { EstadoUsina.class },
            (proxy, method, args_) -> {
                if (method.getName().equals("verificarCondicoes")) {
                    usina.setEstado(new AlertaVermelhoState());
                    return null;
                }
                return method.invoke(estadoAntigo, args_);
            }
        ));
        usina.atualizarSensores(new SensorDataDTO(410.0, 2.0, false));
        Thread.sleep(2000);

        // Falhar o sistema de resfriamento -> EMERGENCIA
        System.out.println("\n--- SIMULANDO FALHA DE RESFRIAMENTO ---");
        usina.atualizarSensores(new SensorDataDTO(450.0, 2.5, true));
        Thread.sleep(3000);
        
        // Tentar entrar em manutenção -> Inválido em Emergência
        usina.entrarEmManutencao();
        Thread.sleep(1000);

        // Desligar a usina (Reset de Emergência)
        System.out.println("\n--- SIMULANDO DESLIGAMENTO DE EMERGÊNCIA ---");
        usina.desligar();
        Thread.sleep(2000);

        // Testar o modo Manutenção
        System.out.println("\n--- SIMULANDO MODO MANUTENÇÃO ---");
        usina.entrarEmManutencao();
        Thread.sleep(2000);
        // Sensores são ignorados
        usina.atualizarSensores(new SensorDataDTO(500.0, 3.0, true));
        System.out.println("[SIMULAÇÃO] Sensores em nível de emergência, mas devem ser ignorados.");
        Thread.sleep(3000);
        
        usina.sairDaManutencao();
        Thread.sleep(1000);
        System.out.println("\n--- FIM DA SIMULAÇÃO ---");
    }
}
