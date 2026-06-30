package broker.modelos;

public class Ativo extends Entidade {
    private static final long serialVersionUID = 2L;
    private String codigo;
    private String nome;
    private double valor;

    public Ativo() {}

    public Ativo(int id) {
        this.id = id;
    }

    @Override
    public String[] getNomesCampos() {
        return new String[]{"Código", "Nome", "Valor"};
    }

    @Override
    public Object[] getValoresCampos() {
        return new Object[]{codigo, nome, valor};
    }

    @Override
    public void setCampos(String[] valores) throws IllegalArgumentException {
        if (valores.length < 3) throw new IllegalArgumentException("Informe Código, Nome e Valor");
        String cod = valores[0].trim().toUpperCase();
        if (cod.isEmpty()) throw new IllegalArgumentException("Código não pode ser vazio");
        String nomeVal = valores[1].trim();
        if (nomeVal.isEmpty()) throw new IllegalArgumentException("Nome não pode ser vazio");
        try {
            double v = Double.parseDouble(valores[2].trim().replace(",", "."));
            if (v < 0) throw new IllegalArgumentException("Valor não pode ser negativo");
            this.valor = v;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor deve ser numérico");
        }
        this.codigo = cod;
        this.nome = nomeVal;
    }

    public String getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public double getValor() { return valor; }

    @Override
    public String toString() {
        return "Ativo{id=" + id + ", codigo=" + codigo + ", nome=" + nome + ", valor=" + valor + "}";
    }
}
