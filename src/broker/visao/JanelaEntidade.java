package broker.visao;

import broker.modelos.Entidade;
import broker.persistencia.DAOfactory;
import broker.persistencia.EntidadeDAO;
import broker.persistencia.PersistenceException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.util.Arrays;

public class JanelaEntidade<E extends Entidade> extends JFrame {
    private final Class<E> tipo;
    private final EntidadeDAO<E> dao;
    private final JTable tabela;
    private final JTextField campoId;
    private final DefaultTableModel modeloTabela;

    public JanelaEntidade(Class<E> tipo, String titulo) {
        super(titulo);
        this.tipo = tipo;
        this.dao = DAOfactory.getInstancia().getDAO(tipo);
        this.dao.recuperar();

        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        modeloTabela = new DefaultTableModel(new Object[]{"ID"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.setAutoCreateRowSorter(true);
        tabela.getTableHeader().setReorderingAllowed(true);
        JScrollPane scroll = new JScrollPane(tabela);
        add(scroll, BorderLayout.CENTER);

        JPanel painelControle = new JPanel(new BorderLayout(8, 8));
        painelControle.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBusca.add(new JLabel("ID:"));
        campoId = new JTextField(10);
        painelBusca.add(campoId);

        JButton botaoBuscar = new JButton("Buscar");
        JButton botaoSalvar = new JButton("Salvar");
        JButton botaoEditar = new JButton("Editar");
        JButton botaoApagar = new JButton("Apagar");
        JButton botaoAtualizar = new JButton("Atualizar");

        painelBusca.add(botaoBuscar);
        painelBusca.add(botaoSalvar);
        painelBusca.add(botaoEditar);
        painelBusca.add(botaoApagar);
        painelBusca.add(botaoAtualizar);
        painelControle.add(painelBusca, BorderLayout.NORTH);

        add(painelControle, BorderLayout.NORTH);

        botaoBuscar.addActionListener(e -> buscar());
        botaoSalvar.addActionListener(e -> salvar());
        botaoEditar.addActionListener(e -> editar());
        botaoApagar.addActionListener(e -> apagar());
        botaoAtualizar.addActionListener(e -> carregarTabela());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                try {
                    dao.persistir();
                } catch (PersistenceException ex) {
                    JOptionPane.showMessageDialog(
                            JanelaEntidade.this,
                            ex.getMessage(),
                            "Erro ao persistir",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        carregarTabela();
    }

    private void carregarTabela() {
        try {
            E[] lista = dao.carregarTodos();
            modeloTabela.setRowCount(0);
            Arrays.stream(lista).forEach(entidade -> modeloTabela.addRow(new Object[]{entidade.getId()}));
        } catch (PersistenceException ex) {
            modeloTabela.setRowCount(0);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Informação", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscar() {
        int id = lerId();
        if (id < 0) {
            return;
        }
        try {
            E entidade = dao.carregar(id);
            campoId.setText(String.valueOf(entidade.getId()));
            JOptionPane.showMessageDialog(this, "Registro encontrado: " + entidade.getId(), "Busca", JOptionPane.INFORMATION_MESSAGE);
        } catch (PersistenceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvar() {
        int id = lerId();
        if (id < 0) {
            return;
        }
        try {
            E entidade = criarEntidade(id);
            dao.salvar(entidade);
            carregarTabela();
            JOptionPane.showMessageDialog(this, "Registro salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (PersistenceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao criar entidade: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editar() {
        int id = lerId();
        if (id < 0) {
            return;
        }
        try {
            E entidade = criarEntidade(id);
            dao.atualizar(entidade);
            carregarTabela();
            JOptionPane.showMessageDialog(this, "Registro atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (PersistenceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao editar entidade: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void apagar() {
        int id = lerId();
        if (id < 0) {
            return;
        }
        try {
            E entidade = dao.apagar(id);
            carregarTabela();
            JOptionPane.showMessageDialog(this, "Registro apagado: " + entidade.getId(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (PersistenceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int lerId() {
        String texto = campoId.getText().trim();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe um ID.", "Erro", JOptionPane.WARNING_MESSAGE);
            return -1;
        }
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O ID deve ser um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    private E criarEntidade(int id) throws Exception {
        Constructor<E> construtor = tipo.getConstructor(int.class);
        return construtor.newInstance(id);
    }
}
