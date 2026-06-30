package broker.modelos;

public class Carteira extends Entidade {
    private static final long serialVersionUID = 2L;
    private int idCliente;
    private int idAtivo;
    private int quantidade;

    public Carteira() {}

    public Carteira(int id) {
        this.id = id;
    }

    @Override
    public String[] getNomesCampos() {
        return new String[]{"ID Cliente", "ID Ativo", "Quantidade"};
    }

    @Override
    public Object[] getValoresCampos() {
        return new Object[]{idCliente, idAtivo, quantidade};
    }

    @Override
    public void setCampos(String[] valores) throws IllegalArgumentException {
        if (valores.length < 3) throw new IllegalArgumentException("Preencha todos os campos da carteira");
        try {
            this.idCliente = Integer.parseInt(valores[0].trim());
            this.idAtivo = Integer.parseInt(valores[1].trim());
            this.quantidade = Integer.parseInt(valores[2].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valores devem ser numéricos");
        }
    }

    public int getIdCliente() { return idCliente; }
    public int getIdAtivo() { return idAtivo; }
    public int getQuantidade() { return quantidade; }

    @Override
    public String toString() {
        return "Carteira{id=" + id + ", idCliente=" + idCliente + ", idAtivo=" + idAtivo + ", quantidade=" + quantidade + "}";
    }
}
