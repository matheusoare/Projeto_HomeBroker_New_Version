package broker.persistencia;

import broker.modelos.Entidade;
import java.io.*;
import java.util.*;

public class EntidadeDAO<E extends Entidade> {
    private final String nomearquivo;
    private Set<E> entidades = new HashSet<>();

    public EntidadeDAO(String nomearquivo) {
        this.nomearquivo = nomearquivo;
        recuperar();
    }

    public void salvar(E e) throws PersistenceException {
        if (e == null) {
            throw new PersistenceException("Salvar", "Entidade nula", e);
        }
        if (entidades.contains(e)) {
            throw new PersistenceException("Salvar", "Entidade com este ID já existe", e);
        }
        entidades.add(e);
        persistir();
    }

    public void atualizar(E e) throws PersistenceException {
        if (e == null) {
            throw new PersistenceException("Atualizar", "Entidade nula", e);
        }
        if (!entidades.contains(e)) {
            throw new PersistenceException("Atualizar", "Entidade não encontrada para atualização", e);
        }
        entidades.remove(e);
        entidades.add(e);
        persistir();
    }

    public E apagar(int id) throws PersistenceException {
        for (E entidade : entidades) {
            if (entidade.getId() == id) {
                entidades.remove(entidade);
                persistir();
                return entidade;
            }
        }
        throw new PersistenceException("Apagar", "Entidade com o ID informado não foi encontrada", id);
    }

    public E carregar(int id) throws PersistenceException {
        for (E entidade : entidades) {
            if (entidade.getId() == id) {
                return entidade;
            }
        }
        throw new PersistenceException("Carregar", "Entidade com o ID informado não foi encontrada", id);
    }

    @SuppressWarnings("unchecked")
    public E[] carregarTodos() throws PersistenceException {
        if (entidades.isEmpty()) {
            throw new PersistenceException("CarregarTodos", "Nenhuma entidade cadastrada", null);
        }

        List<E> ordenadas = new ArrayList<>(entidades);
        Collections.sort(ordenadas);

        E[] array = (E[]) java.lang.reflect.Array.newInstance(
                ordenadas.get(0).getClass(),
                ordenadas.size()
        );
        return ordenadas.toArray(array);
    }

    public void persistir() throws PersistenceException {
        File arquivo = new File(nomearquivo);

        if (arquivo.getParentFile() != null) {
            arquivo.getParentFile().mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(entidades);
        } catch (IOException e) {
            throw new PersistenceException("Persistir", "Erro ao salvar os dados", e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void recuperar() {
        File arquivo = new File(nomearquivo);
        if (!arquivo.exists()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            this.entidades = (Set<E>) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Erro crítico ao recuperar dados do arquivo: " + ex.getMessage());
        }
    }
}