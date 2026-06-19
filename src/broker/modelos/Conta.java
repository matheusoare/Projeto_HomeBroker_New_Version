package broker.modelos;

public class Conta extends Entidade {
    public Conta() {
    }

    public Conta(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Conta{id=" + id + '}';
    }
}
