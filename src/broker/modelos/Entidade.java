package broker.modelos;

import java.io.Serializable;

public abstract class Entidade implements Comparable<Entidade>, Serializable {
    protected int id;
    private static final long serialVersionUID = 1L;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { 
            return true;
        }
        if (!(obj instanceof Entidade)) {
            return false;
        }

        Entidade entidade = (Entidade) obj;
        return this.id == entidade.id;
    }

    @Override
    public int compareTo(Entidade obj) {
        return Integer.compare(this.id, obj.id);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.id);
    }
}