package broker.modelos;

public class Carteira extends Entidade {
    public Carteira() {
    }

    public Carteira(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Carteira{id=" + id + '}';
    }
}
