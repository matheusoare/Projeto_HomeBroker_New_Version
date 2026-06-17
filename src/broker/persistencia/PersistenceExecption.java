public class PersistenceException extends Exception {

    //a ideia aqui é ter uma classe de exceção que salva o que está acontecendo, além de informar diretamente via terminal

    private String operacao;
    private String problema;
    private Object valor;

    public PersistenceException(String operacao, String problema, Object valor){
        super(String.format("Erro na operação %s: %s. Valor: %s", operacao, problema, valor));
        this.operacao = operacao;
        this.problema = problema;
        this.valor = valor;
    }

    public String getOperacao() {
        return operacao;
    }

    public String getProblema() {
        return problema;
    }

    public Object getValor() {
        return valor;
    }

}