package questao_04;

public interface Validator {
    void setNext(Validator next);
    void validate(ValidationContext context);
}
