package questao_04;

// 1. DTO (Data Transfer Object) - O documento a ser validado
public class DocumentoFiscalDTO {
    private final String numero;
    private final String xmlContent;
    private final String certificado;

    public DocumentoFiscalDTO(String numero, String xmlContent, String certificado) {
        this.numero = numero;
        this.xmlContent = xmlContent;
        this.certificado = certificado;
    }

    public String getNumero() { return numero; }
    public String getXmlContent() { return xmlContent; }
    public String getCertificado() { return certificado; }
}
