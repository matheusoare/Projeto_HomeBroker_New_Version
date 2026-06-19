package broker.modelos;

public class Ordem extends Entidade {
    public Ordem() {
    }

    public Ordem(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Ordem{id=" + id + '}';
    }
}
