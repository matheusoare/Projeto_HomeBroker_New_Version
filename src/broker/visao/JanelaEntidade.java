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
import java.util.Arrays;

public class JanelaEntidade<E extends Entidade> extends JFrame {
    private final Class<E> tipo;
    private final EntidadeDAO<E> dao;
    private final JTable tabela;
    private final JTextField campoId;
    private final DefaultTableModel modeloTabela;
    private final String[] nomesCampos;

    public JanelaEntidade(Class<E> tipo, String titulo) {
        super(titulo);
        this.tipo = tipo;
        this.dao = DAOfactory.getInstancia().getDAO(tipo);
        this.dao.recuperar();
        this.nomesCampos = obterNomesCampos();

        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        String[] colunas = new String[nomesCampos.length + 1];
        colunas[0] = "ID";
        System.arraycopy(nomesCampos, 0, colunas, 1, nomesCampos.length);

        modeloTabela = new DefaultTableModel(colunas, 0) {
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
        JButton botaoNovo = new JButton("Novo");
        JButton botaoEditar = new JButton("Editar");
        JButton botaoApagar = new JButton("Apagar");
        JButton botaoAtualizar = new JButton("Atualizar");

        painelBusca.add(botaoBuscar);
        painelBusca.add(botaoNovo);
        painelBusca.add(botaoEditar);
        painelBusca.add(botaoApagar);
        painelBusca.add(botaoAtualizar);
        painelControle.add(painelBusca, BorderLayout.NORTH);

        add(painelControle, BorderLayout.NORTH);

        botaoBuscar.addActionListener(e -> buscar());
        botaoNovo.addActionListener(e -> salvar());
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

    private String[] obterNomesCampos() {
        try {
            E dummy = tipo.getConstructor().newInstance();
            return dummy.getNomesCampos();
        } catch (Exception e) {
            return new String[]{};
        }
    }

    private void carregarTabela() {
        try {
            E[] lista = dao.carregarTodos();
            modeloTabela.setRowCount(0);
            for (E entidade : lista) {
                Object[] valores = entidade.getValoresCampos();
                Object[] linha = new Object[valores.length + 1];
                linha[0] = entidade.getId();
                System.arraycopy(valores, 0, linha, 1, valores.length);
                modeloTabela.addRow(linha);
            }
        } catch (PersistenceException ex) {
            modeloTabela.setRowCount(0);
        }
    }

    private void buscar() {
        int id = lerId();
        if (id < 0) return;
        try {
            E entidade = dao.carregar(id);
            for (int i = 0; i < modeloTabela.getRowCount(); i++) {
                if (Integer.parseInt(modeloTabela.getValueAt(i, 0).toString()) == id) {
                    tabela.setRowSelectionInterval(i, i);
                    tabela.scrollRectToVisible(tabela.getCellRect(i, 0, true));
                    break;
                }
            }
            JOptionPane.showMessageDialog(this, entidade.toString(), "Registro encontrado", JOptionPane.INFORMATION_MESSAGE);
        } catch (PersistenceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvar() {
        String[] valores = mostrarFormulario("Novo Registro", null, null);
        if (valores == null) return;
        try {
            int id = Integer.parseInt(valores[0].trim());
            E entidade = tipo.getConstructor(int.class).newInstance(id);
            entidade.setCampos(Arrays.copyOfRange(valores, 1, valores.length));
            dao.salvar(entidade);
            carregarTabela();
            JOptionPane.showMessageDialog(this, "Registro salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID deve ser um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (PersistenceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de validação", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao criar registro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editar() {
        int id = lerId();
        if (id < 0) return;
        try {
            E existente = dao.carregar(id);
            String[] valsStr = Arrays.stream(existente.getValoresCampos())
                    .map(v -> v != null ? v.toString() : "")
                    .toArray(String[]::new);
            String[] valores = mostrarFormulario("Editar Registro", valsStr, id);
            if (valores == null) return;
            existente.setCampos(Arrays.copyOfRange(valores, 1, valores.length));
            dao.atualizar(existente);
            carregarTabela();
            JOptionPane.showMessageDialog(this, "Registro atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (PersistenceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de validação", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void apagar() {
        int id = lerId();
        if (id < 0) return;
        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja apagar o registro com ID " + id + "?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            dao.apagar(id);
            carregarTabela();
            JOptionPane.showMessageDialog(this, "Registro apagado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (PersistenceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @param idFixo se não-nulo, o campo ID é pré-preenchido e bloqueado (modo edição)
     */
    private String[] mostrarFormulario(String titulo, String[] valoresIniciais, Integer idFixo) {
        JPanel panel = new JPanel(new GridLayout(nomesCampos.length + 1, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("ID:"));
        JTextField campoIdForm = new JTextField(idFixo != null ? idFixo.toString() : "", 15);
        campoIdForm.setEditable(idFixo == null);
        panel.add(campoIdForm);

        JTextField[] campos = new JTextField[nomesCampos.length];
        for (int i = 0; i < nomesCampos.length; i++) {
            panel.add(new JLabel(nomesCampos[i] + ":"));
            String valorInicial = (valoresIniciais != null && i < valoresIniciais.length) ? valoresIniciais[i] : "";
            campos[i] = new JTextField(valorInicial, 15);
            panel.add(campos[i]);
        }

        int result = JOptionPane.showConfirmDialog(this, panel, titulo,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return null;

        String[] valores = new String[nomesCampos.length + 1];
        valores[0] = campoIdForm.getText().trim();
        for (int i = 0; i < nomesCampos.length; i++) {
            valores[i + 1] = campos[i].getText();
        }
        return valores;
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
}
