package broker.modelos;

public class Historico extends Entidade {
    private static final long serialVersionUID = 2L;
    private int idOrdem;
    private String data;
    private String status;

    public Historico() {}

    public Historico(int id) {
        this.id = id;
    }

    @Override
    public String[] getNomesCampos() {
        return new String[]{"ID Ordem", "Data", "Status"};
    }

    @Override
    public Object[] getValoresCampos() {
        return new Object[]{idOrdem, data, status};
    }

    @Override
    public void setCampos(String[] valores) throws IllegalArgumentException {
        if (valores.length < 3) throw new IllegalArgumentException("Preencha todos os campos do histórico");
        try {
            this.idOrdem = Integer.parseInt(valores[0].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID Ordem deve ser numérico");
        }
        this.data = valores[1].trim();
        this.status = valores[2].trim();
    }

    public int getIdOrdem() { return idOrdem; }
    public String getData() { return data; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return "Historico{id=" + id + ", idOrdem=" + idOrdem + ", data=" + data + ", status=" + status + "}";
    }
}
