package broker.modelos;

public class Conta extends Entidade {
    private static final long serialVersionUID = 2L;
    private int idCliente;
    private double saldo;

    public Conta() {}

    public Conta(int id) {
        this.id = id;
    }

    @Override
    public String[] getNomesCampos() {
        return new String[]{"ID Cliente", "Saldo"};
    }

    @Override
    public Object[] getValoresCampos() {
        return new Object[]{idCliente, saldo};
    }

    @Override
    public void setCampos(String[] valores) throws IllegalArgumentException {
        if (valores.length < 2) throw new IllegalArgumentException("Informe ID Cliente e Saldo");
        try {
            this.idCliente = Integer.parseInt(valores[0].trim());
            this.saldo = Double.parseDouble(valores[1].trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID Cliente e Saldo devem ser numéricos");
        }
    }

    public int getIdCliente() { return idCliente; }
    public double getSaldo() { return saldo; }

    @Override
    public String toString() {
        return "Conta{id=" + id + ", idCliente=" + idCliente + ", saldo=" + saldo + "}";
    }
}
