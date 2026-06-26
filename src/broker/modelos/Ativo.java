package broker.modelos;

public class Ativo extends Entidade {
    private static final long serialVersionUID = 2L;
    private String codigo;
    private String nome;

    public Ativo() {}

    public Ativo(int id) {
        this.id = id;
    }

    @Override
    public String[] getNomesCampos() {
        return new String[]{"Código", "Nome"};
    }

    @Override
    public Object[] getValoresCampos() {
        return new Object[]{codigo, nome};
    }

    @Override
    public void setCampos(String[] valores) throws IllegalArgumentException {
        if (valores.length < 2) throw new IllegalArgumentException("Informe Código e Nome");
        this.codigo = valores[0].trim().toUpperCase();
        this.nome = valores[1].trim();
    }

    public String getCodigo() { return codigo; }
    public String getNome() { return nome; }

    @Override
    public String toString() {
        return "Ativo{id=" + id + ", codigo=" + codigo + ", nome=" + nome + "}";
    }
}
