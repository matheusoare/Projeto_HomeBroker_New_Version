package broker.modelos;

public class Cliente extends Entidade {
    public Cliente() {
    }

    public Cliente(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Cliente{id=" + id + '}';
    }
}
