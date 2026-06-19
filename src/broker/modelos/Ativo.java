package broker.modelos;

public class Ativo extends Entidade {
    public Ativo() {
    }

    public Ativo(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Ativo{id=" + id + '}';
    }
}
