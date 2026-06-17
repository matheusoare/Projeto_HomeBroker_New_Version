package broker.persistencia;

import broker.modelos.Entidade;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class EntidadeDAO<E extends Entidade> {
    private Set<E> entidades = new HashSet<>(); 
    public String nomearquivo;

    public EntidadeDAO(String nomearquivo) {
        this.nomearquivo = nomearquivo;
        carregar(); // Carrega os dados serializados assim que o DAO é criado
    }

    public void salvar(E e) throws PersistenceException {
        // Se já existe uma entidade com esse ID, gera erro de duplicidade
        if (entidades.contains(e)) {
            throw new PersistenceException("Salvar", "Entidade com este ID já existe", e);
        }
        entidades.add(e);
        persistir(); 
    }

    public void atualizar(E e) throws PersistenceException {
        if (entidades.contains(e)) {
            entidades.remove(e); 
            entidades.add(e); 
            persistir(); 
        } else {
            throw new PersistenceException("Atualizar", "Entidade não encontrada para atualização", e);
        }
    }

    public void excluir(E e) throws PersistenceException {
        if (entidades.contains(e)) {
            entidades.remove(e);
            persistir(); 
        } else {
            throw new PersistenceException("Excluir", "Entidade não encontrada para exclusão", e);
        }
    }

    public E carregar(int id) throws PersistenceException {
        for (E entidade : entidades) {
            if (entidade.getId() == id) {
                return entidade;
            }
        }
        throw new PersistenceException("Carregar", "Entidade com o ID informado não foi encontrada", id);
    }

    public List<E> carregarTodos() {
        // Retorna a lista ordenada com base no compareTo (por ID)
        return entidades.stream().sorted().collect(Collectors.toList());
    }

    public void persistir() {
        File arquivo = new File(nomearquivo);

        if (arquivo.getParentFile() != null) {
            arquivo.getParentFile().mkdirs(); 
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(entidades);
        } catch (IOException e) {
            System.out.println("Erro ao persistir os dados: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void carregar() {
        File arquivo = new File(nomearquivo);
        if (!arquivo.exists()) {
            return; 
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            this.entidades = (Set<E>) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Erro crítico ao carregar dados do arquivo: " + ex.getMessage());
        }
    }
}