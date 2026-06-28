package broker.persistencia;

import broker.modelos.*;
import java.util.HashMap;
import java.util.Map;

public class DAOfactory {
    private static DAOfactory instancia;
    private final Map<Class<?>, EntidadeDAO<?>> daos = new HashMap<>();

    private DAOfactory() {
        daos.put(Cliente.class, new EntidadeDAO<Cliente>("data/clientes.dat"));
        daos.put(Conta.class, new EntidadeDAO<Conta>("data/contas.dat"));
        daos.put(Ativo.class, new EntidadeDAO<Ativo>("data/ativos.dat"));
        daos.put(Ordem.class, new EntidadeDAO<Ordem>("data/ordens.dat"));
        daos.put(Historico.class, new EntidadeDAO<Historico>("data/historicos.dat"));
        daos.put(Carteira.class, new EntidadeDAO<Carteira>("data/carteiras.dat"));
    }

    public static synchronized DAOfactory getInstancia() {
        if (instancia == null) {
            instancia = new DAOfactory();
        }
        return instancia;
    }

    @SuppressWarnings("unchecked")
    public <E extends Entidade> EntidadeDAO<E> getDAO(Class<E> classe) {
        EntidadeDAO<E> dao = (EntidadeDAO<E>) daos.get(classe);
        if (dao == null) {
            throw new IllegalArgumentException("DAO não configurado para a classe: " + classe.getName());
        }
        return dao;
    }
}