package questao_03;

public class UsinaNuclear {
    private EstadoUsina estadoAtual;
    private SensorDataDTO dadosSensores;

    public UsinaNuclear() {
        // A usina começa desligada
        this.estadoAtual = new DesligadaState();
        this.dadosSensores = new SensorDataDTO(25.0, 1.0, false);
        System.out.println("[CONTEXTO] Usina criada. Estado inicial: DESLIGADA");
    }

    // Método principal do loop de controle
    public void verificarSensores() {
        this.estadoAtual.verificarCondicoes(this);
    }

    // Método para o mundo exterior atualizar os dados
    public void atualizarSensores(SensorDataDTO novosDados) {
        this.dadosSensores = novosDados;
    }
    
    // Métodos de controle
    public void ligar() {
        this.estadoAtual.ligar(this);
    }
    
    public void desligar() {
        this.estadoAtual.desligar(this);
    }

    public void entrarEmManutencao() {
        this.estadoAtual.entrarEmManutencao(this);
    }

    public void sairDaManutencao() {
        this.estadoAtual.sairDaManutencao(this);
    }

    public SensorDataDTO getDadosSensores() {
        return dadosSensores;
    }

    public void setEstado(EstadoUsina novoEstado) {
        System.out.printf("[TRANSIÇÃO] Mudando de %s para %s\n",
                this.estadoAtual.getClass().getSimpleName(),
                novoEstado.getClass().getSimpleName());
        this.estadoAtual = novoEstado;
    }
}
