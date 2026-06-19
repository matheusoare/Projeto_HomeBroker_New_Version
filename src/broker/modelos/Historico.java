package broker.modelos;

public class Historico extends Entidade {
    public Historico() {
    }

    public Historico(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Historico{id=" + id + '}';
    }
}
