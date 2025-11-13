package questao_03;

// DTO (Data Transfer Object) para os dados que disparam transições
public class SensorDataDTO {
    final double temperatura;
    final double pressao;
    final boolean sistemaResfriamentoFalhou;

    public SensorDataDTO(double temperatura, double pressao, boolean sistemaResfriamentoFalhou) {
        this.temperatura = temperatura;
        this.pressao = pressao;
        this.sistemaResfriamentoFalhou = sistemaResfriamentoFalhou;
    }
}
