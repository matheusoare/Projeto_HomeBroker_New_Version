package broker.modelos;

public class Cliente extends Entidade {
    private static final long serialVersionUID = 2L;
    private String nome;
    private String cpf;

    public Cliente() {}

    public Cliente(int id) {
        this.id = id;
    }

    @Override
    public String[] getNomesCampos() {
        return new String[]{"Nome", "CPF"};
    }

    @Override
    public Object[] getValoresCampos() {
        return new Object[]{nome, cpf};
    }

    @Override
    public void setCampos(String[] valores) throws IllegalArgumentException {
        if (valores.length < 2) throw new IllegalArgumentException("Informe Nome e CPF");
        this.nome = valores[0].trim();
        this.cpf = valores[1].trim();
    }

    public String getNome() { return nome; }
    public String getCpf() { return cpf; }

    @Override
    public String toString() {
        return "Cliente{id=" + id + ", nome=" + nome + ", cpf=" + cpf + "}";
    }
}
