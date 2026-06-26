package broker.modelos;

public class Ordem extends Entidade {
    private static final long serialVersionUID = 2L;
    private int idConta;
    private int idAtivo;
    private String tipo;
    private int quantidade;
    private double precoLimite;

    public Ordem() {}

    public Ordem(int id) {
        this.id = id;
    }

    @Override
    public String[] getNomesCampos() {
        return new String[]{"ID Conta", "ID Ativo", "Tipo (COMPRA/VENDA)", "Quantidade", "Preço Limite"};
    }

    @Override
    public Object[] getValoresCampos() {
        return new Object[]{idConta, idAtivo, tipo, quantidade, precoLimite};
    }

    @Override
    public void setCampos(String[] valores) throws IllegalArgumentException {
        if (valores.length < 5) throw new IllegalArgumentException("Preencha todos os campos da ordem");
        try {
            this.idConta = Integer.parseInt(valores[0].trim());
            this.idAtivo = Integer.parseInt(valores[1].trim());
            this.tipo = valores[2].trim().toUpperCase();
            this.quantidade = Integer.parseInt(valores[3].trim());
            this.precoLimite = Double.parseDouble(valores[4].trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valores numéricos inválidos");
        }
    }

    public int getIdConta() { return idConta; }
    public int getIdAtivo() { return idAtivo; }
    public String getTipo() { return tipo; }
    public int getQuantidade() { return quantidade; }
    public double getPrecoLimite() { return precoLimite; }

    @Override
    public String toString() {
        return "Ordem{id=" + id + ", idConta=" + idConta + ", idAtivo=" + idAtivo
            + ", tipo=" + tipo + ", quantidade=" + quantidade + ", precoLimite=" + precoLimite + "}";
    }
}
