package broker.visao;

import broker.modelos.*;
import broker.persistencia.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Set;

public class JanelaConta extends JFrame {

    private final EntidadeDAO<Conta>     daoContas;
    private final EntidadeDAO<Ordem>     daoOrdens;
    private final EntidadeDAO<Historico> daoHistorico;
    private final EntidadeDAO<Carteira>  daoCarteira;

    private final DefaultTableModel modeloContas    = modelo("ID", "ID Cliente", "Saldo");
    private final DefaultTableModel modeloOrdens    = modelo("ID", "ID Conta", "ID Ativo", "Tipo", "Qtd", "Preço Limite");
    private final DefaultTableModel modeloHistorico = modelo("ID", "ID Ordem", "Data", "Status");
    private final DefaultTableModel modeloCarteira  = modelo("ID", "ID Cliente", "ID Ativo", "Quantidade");

    private final JTable tabelaContas = tabela(modeloContas);
    private final JTextField campoId  = new JTextField(6);

    public JanelaConta() {
        super("Contas — Home Broker");
        setSize(1100, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DAOfactory f = DAOfactory.getInstancia();
        daoContas    = f.getDAO(Conta.class);
        daoOrdens    = f.getDAO(Ordem.class);
        daoHistorico = f.getDAO(Historico.class);
        daoCarteira  = f.getDAO(Carteira.class);

        daoContas.recuperar();
        daoOrdens.recuperar();
        daoHistorico.recuperar();
        daoCarteira.recuperar();

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Ordens",    new JScrollPane(tabela(modeloOrdens)));
        abas.addTab("Histórico", new JScrollPane(tabela(modeloHistorico)));
        abas.addTab("Carteira",  new JScrollPane(tabela(modeloCarteira)));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                painelContas(), abas);
        split.setDividerLocation(340);
        add(split);

        tabelaContas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) atualizarAbas();
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                try { daoContas.persistir(); } catch (PersistenceException ex) { erro(ex.getMessage()); }
            }
        });

        carregarContas();
    }

    private JPanel painelContas() {
        JPanel painel = new JPanel(new BorderLayout(4, 4));
        painel.setBorder(BorderFactory.createTitledBorder("Contas"));
        painel.add(new JScrollPane(tabelaContas), BorderLayout.CENTER);

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barra.add(new JLabel("ID:"));
        barra.add(campoId);
        JButton buscar = new JButton("Buscar");
        JButton novo   = new JButton("Novo");
        JButton editar = new JButton("Editar");
        JButton apagar = new JButton("Apagar");
        barra.add(buscar);
        barra.add(novo);
        barra.add(editar);
        barra.add(apagar);
        painel.add(barra, BorderLayout.SOUTH);

        buscar.addActionListener(e -> buscarConta());
        novo.addActionListener(e -> novaConta());
        editar.addActionListener(e -> editarConta());
        apagar.addActionListener(e -> apagarConta());
        return painel;
    }

    private void carregarContas() {
        modeloContas.setRowCount(0);
        try {
            for (Conta c : daoContas.carregarTodos())
                modeloContas.addRow(new Object[]{c.getId(), c.getIdCliente(), c.getSaldo()});
        } catch (PersistenceException ignored) {}
    }

    private void atualizarAbas() {
        int row = tabelaContas.getSelectedRow();
        if (row < 0) return;
        int id = (int) modeloContas.getValueAt(tabelaContas.convertRowIndexToModel(row), 0);

        Conta conta;
        try { conta = daoContas.carregar(id); }
        catch (PersistenceException e) { return; }

        // Ordens desta conta
        modeloOrdens.setRowCount(0);
        Set<Integer> idsOrdens = new HashSet<>();
        try {
            for (Ordem o : daoOrdens.carregarTodos()) {
                if (o.getIdConta() == conta.getId()) {
                    idsOrdens.add(o.getId());
                    modeloOrdens.addRow(new Object[]{
                        o.getId(), o.getIdConta(), o.getIdAtivo(),
                        o.getTipo(), o.getQuantidade(), o.getPrecoLimite()
                    });
                }
            }
        } catch (PersistenceException ignored) {}

        // Histórico das ordens desta conta
        modeloHistorico.setRowCount(0);
        try {
            for (Historico h : daoHistorico.carregarTodos()) {
                if (idsOrdens.contains(h.getIdOrdem()))
                    modeloHistorico.addRow(new Object[]{h.getId(), h.getIdOrdem(), h.getData(), h.getStatus()});
            }
        } catch (PersistenceException ignored) {}

        // Carteira do cliente desta conta
        modeloCarteira.setRowCount(0);
        try {
            for (Carteira c : daoCarteira.carregarTodos()) {
                if (c.getIdCliente() == conta.getIdCliente())
                    modeloCarteira.addRow(new Object[]{c.getId(), c.getIdCliente(), c.getIdAtivo(), c.getQuantidade()});
            }
        } catch (PersistenceException ignored) {}
    }

    private void buscarConta() {
        int id = lerId();
        if (id < 0) return;
        try {
            Conta c = daoContas.carregar(id);
            for (int i = 0; i < modeloContas.getRowCount(); i++) {
                if ((int) modeloContas.getValueAt(tabelaContas.convertRowIndexToModel(i), 0) == id) {
                    tabelaContas.setRowSelectionInterval(i, i);
                    tabelaContas.scrollRectToVisible(tabelaContas.getCellRect(i, 0, true));
                    break;
                }
            }
            JOptionPane.showMessageDialog(this, c.toString(), "Conta encontrada", JOptionPane.INFORMATION_MESSAGE);
        } catch (PersistenceException ex) {
            erro(ex.getMessage());
        }
    }

    private void novaConta() {
        JTextField fIdCliente = new JTextField(10);
        JTextField fSaldo     = new JTextField(10);
        JTextField fId        = new JTextField(10);
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("ID:")); panel.add(fId);
        panel.add(new JLabel("ID Cliente:")); panel.add(fIdCliente);
        panel.add(new JLabel("Saldo:"));     panel.add(fSaldo);
        if (JOptionPane.showConfirmDialog(this, panel, "Nova Conta", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            int id = Integer.parseInt(fId.getText().trim());
            Conta c = new Conta(id);
            c.setCampos(new String[]{fIdCliente.getText(), fSaldo.getText()});
            daoContas.salvar(c);
            try { daoContas.persistir(); } catch (PersistenceException ignored) {}
            carregarContas();
        } catch (NumberFormatException ex) {
            erro("ID deve ser numérico");
        } catch (PersistenceException | IllegalArgumentException ex) {
            erro(ex.getMessage());
        }
    }

    private void editarConta() {
        int id = lerId();
        if (id < 0) return;
        try {
            Conta c = daoContas.carregar(id);
            JTextField fIdCliente = new JTextField(String.valueOf(c.getIdCliente()), 10);
            JTextField fSaldo     = new JTextField(String.valueOf(c.getSaldo()), 10);
            JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
            panel.add(new JLabel("ID Cliente:")); panel.add(fIdCliente);
            panel.add(new JLabel("Saldo:"));     panel.add(fSaldo);
            if (JOptionPane.showConfirmDialog(this, panel, "Editar Conta " + id, JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
            c.setCampos(new String[]{fIdCliente.getText(), fSaldo.getText()});
            daoContas.atualizar(c);
            try { daoContas.persistir(); } catch (PersistenceException ignored) {}
            carregarContas();
        } catch (PersistenceException | IllegalArgumentException ex) {
            erro(ex.getMessage());
        }
    }

    private void apagarConta() {
        int id = lerId();
        if (id < 0) return;
        if (JOptionPane.showConfirmDialog(this, "Apagar conta " + id + "?", "Confirmar",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            daoContas.apagar(id);
            try { daoContas.persistir(); } catch (PersistenceException ignored) {}
            carregarContas();
        } catch (PersistenceException ex) {
            erro(ex.getMessage());
        }
    }

    private int lerId() {
        try { return Integer.parseInt(campoId.getText().trim()); }
        catch (NumberFormatException ex) { erro("ID deve ser numérico"); return -1; }
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private static DefaultTableModel modelo(String... colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    private static JTable tabela(DefaultTableModel m) {
        JTable t = new JTable(m);
        t.setAutoCreateRowSorter(true);
        return t;
    }
}
