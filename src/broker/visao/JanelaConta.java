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
    private final EntidadeDAO<Cliente>   daoClientes;
    private final EntidadeDAO<Ativo>     daoAtivos;
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
        daoClientes  = f.getDAO(Cliente.class);
        daoAtivos    = f.getDAO(Ativo.class);
        daoOrdens    = f.getDAO(Ordem.class);
        daoHistorico = f.getDAO(Historico.class);
        daoCarteira  = f.getDAO(Carteira.class);

        daoContas.recuperar();
        daoClientes.recuperar();
        daoAtivos.recuperar();
        daoOrdens.recuperar();
        daoHistorico.recuperar();
        daoCarteira.recuperar();

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Ordens",    painelOrdens());
        abas.addTab("Histórico", new JScrollPane(tabela(modeloHistorico)));
        abas.addTab("Carteira",  new JScrollPane(tabela(modeloCarteira)));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelContas(), abas);
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

        JButton buscar = new JButton("Buscar");
        JButton novo   = new JButton("Novo");
        JButton editar = new JButton("Editar");
        JButton apagar = new JButton("Apagar");

        JPanel linhaBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linhaBusca.add(new JLabel("ID:"));
        linhaBusca.add(campoId);
        linhaBusca.add(buscar);

        JPanel linhaAcoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linhaAcoes.add(novo);
        linhaAcoes.add(editar);
        linhaAcoes.add(apagar);

        JPanel barra = new JPanel(new BorderLayout());
        barra.add(linhaBusca, BorderLayout.NORTH);
        barra.add(linhaAcoes, BorderLayout.SOUTH);
        painel.add(barra, BorderLayout.SOUTH);

        buscar.addActionListener(e -> buscarConta());
        novo.addActionListener(e -> novaConta());
        editar.addActionListener(e -> editarConta());
        apagar.addActionListener(e -> apagarConta());
        return painel;
    }

    private JPanel painelOrdens() {
        JPanel painel = new JPanel(new BorderLayout(4, 4));
        painel.add(new JScrollPane(tabela(modeloOrdens)), BorderLayout.CENTER);
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNovaOrdem = new JButton("Nova Ordem");
        btnNovaOrdem.addActionListener(e -> novaOrdem());
        barra.add(btnNovaOrdem);
        painel.add(barra, BorderLayout.SOUTH);
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

        modeloHistorico.setRowCount(0);
        try {
            for (Historico h : daoHistorico.carregarTodos())
                if (idsOrdens.contains(h.getIdOrdem()))
                    modeloHistorico.addRow(new Object[]{h.getId(), h.getIdOrdem(), h.getData(), h.getStatus()});
        } catch (PersistenceException ignored) {}

        modeloCarteira.setRowCount(0);
        try {
            for (Carteira c : daoCarteira.carregarTodos())
                if (c.getIdCliente() == conta.getIdCliente())
                    modeloCarteira.addRow(new Object[]{c.getId(), c.getIdCliente(), c.getIdAtivo(), c.getQuantidade()});
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
        JTextField fId        = new JTextField(10);
        JTextField fIdCliente = new JTextField(10);
        JTextField fSaldo     = new JTextField(10);
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("ID:"));         panel.add(fId);
        panel.add(new JLabel("ID Cliente:")); panel.add(fIdCliente);
        panel.add(new JLabel("Saldo:"));      panel.add(fSaldo);
        if (JOptionPane.showConfirmDialog(this, panel, "Nova Conta", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            int id        = Integer.parseInt(fId.getText().trim());
            int idCliente = Integer.parseInt(fIdCliente.getText().trim());
            validarCliente(idCliente);
            Conta c = new Conta(id);
            c.setCampos(new String[]{fIdCliente.getText(), fSaldo.getText()});
            daoContas.salvar(c);
            try { daoContas.persistir(); } catch (PersistenceException ignored) {}
            carregarContas();
        } catch (NumberFormatException ex) {
            erro("ID e ID Cliente devem ser numéricos");
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
            panel.add(new JLabel("Saldo:"));      panel.add(fSaldo);
            if (JOptionPane.showConfirmDialog(this, panel, "Editar Conta " + id, JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
            int idCliente = Integer.parseInt(fIdCliente.getText().trim());
            validarCliente(idCliente);
            c.setCampos(new String[]{fIdCliente.getText(), fSaldo.getText()});
            daoContas.atualizar(c);
            try { daoContas.persistir(); } catch (PersistenceException ignored) {}
            carregarContas();
        } catch (NumberFormatException ex) {
            erro("ID Cliente deve ser numérico");
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

    private void novaOrdem() {
        int row = tabelaContas.getSelectedRow();
        if (row < 0) { erro("Selecione uma conta na tabela à esquerda."); return; }
        int contaId = (int) modeloContas.getValueAt(tabelaContas.convertRowIndexToModel(row), 0);

        Conta conta;
        try { conta = daoContas.carregar(contaId); }
        catch (PersistenceException e) { erro(e.getMessage()); return; }

        JTextField fIdAtivo     = new JTextField(6);
        JComboBox<String> fTipo = new JComboBox<>(new String[]{"COMPRA", "VENDA"});
        JTextField fQtd         = new JTextField(6);
        JTextField fPreco       = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("ID Ativo:"));     panel.add(fIdAtivo);
        panel.add(new JLabel("Tipo:"));         panel.add(fTipo);
        panel.add(new JLabel("Quantidade:"));   panel.add(fQtd);
        panel.add(new JLabel("Preço Limite:")); panel.add(fPreco);

        if (JOptionPane.showConfirmDialog(this, panel, "Nova Ordem — Conta " + contaId,
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;

        try {
            int    idAtivo = Integer.parseInt(fIdAtivo.getText().trim());
            String tipo    = (String) fTipo.getSelectedItem();
            int    qtd     = Integer.parseInt(fQtd.getText().trim());
            if (qtd <= 0) throw new IllegalArgumentException("Quantidade deve ser positiva");
            double preco   = Double.parseDouble(fPreco.getText().trim().replace(",", "."));
            if (preco < 0) throw new IllegalArgumentException("Preço não pode ser negativo");
            double total   = qtd * preco;

            try { daoAtivos.carregar(idAtivo); }
            catch (PersistenceException e) { erro("Ativo " + idAtivo + " não encontrado."); return; }

            if ("COMPRA".equals(tipo)) {
                if (conta.getSaldo() < total) {
                    erro(String.format("Saldo insuficiente. Saldo: %.2f  |  Total da ordem: %.2f", conta.getSaldo(), total));
                    return;
                }
            } else {
                int qtdEmCarteira = qtdNaCarteira(conta.getIdCliente(), idAtivo);
                if (qtdEmCarteira < qtd) {
                    erro(String.format("Carteira insuficiente. Possui %d unidade(s) do ativo %d.", qtdEmCarteira, idAtivo));
                    return;
                }
            }

            Ordem ordem = new Ordem(proximoId(daoOrdens));
            ordem.setCampos(new String[]{
                String.valueOf(contaId), String.valueOf(idAtivo), tipo, String.valueOf(qtd), String.valueOf(preco)
            });
            daoOrdens.salvar(ordem);

            Historico hist = new Historico(proximoId(daoHistorico));
            hist.setCampos(new String[]{String.valueOf(ordem.getId()), java.time.LocalDate.now().toString(), "EXECUTADO"});
            daoHistorico.salvar(hist);

            double novoSaldo = "COMPRA".equals(tipo) ? conta.getSaldo() - total : conta.getSaldo() + total;
            conta.setCampos(new String[]{String.valueOf(conta.getIdCliente()), String.valueOf(novoSaldo)});
            daoContas.atualizar(conta);

            atualizarCarteira(conta.getIdCliente(), idAtivo, "COMPRA".equals(tipo) ? qtd : -qtd);

            carregarContas();
            atualizarAbas();
            JOptionPane.showMessageDialog(this,
                String.format("Ordem executada! Novo saldo: %.2f", novoSaldo),
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            erro("Valores numéricos inválidos.");
        } catch (IllegalArgumentException ex) {
            erro(ex.getMessage());
        } catch (PersistenceException ex) {
            erro(ex.getMessage());
        }
    }

    private void validarCliente(int idCliente) {
        try {
            Cliente cli = daoClientes.carregar(idCliente);
            if (cli.getNome() == null || cli.getNome().isEmpty() || cli.getCpf() == null || cli.getCpf().isEmpty())
                throw new IllegalArgumentException("Cliente " + idCliente + " não possui nome e CPF cadastrados.");
        } catch (PersistenceException e) {
            throw new IllegalArgumentException("Cliente com ID " + idCliente + " não encontrado.");
        }
    }

    private int qtdNaCarteira(int idCliente, int idAtivo) {
        try {
            for (Carteira c : daoCarteira.carregarTodos())
                if (c.getIdCliente() == idCliente && c.getIdAtivo() == idAtivo)
                    return c.getQuantidade();
        } catch (PersistenceException ignored) {}
        return 0;
    }

    private void atualizarCarteira(int idCliente, int idAtivo, int delta) throws PersistenceException {
        Carteira found = null;
        try {
            for (Carteira c : daoCarteira.carregarTodos())
                if (c.getIdCliente() == idCliente && c.getIdAtivo() == idAtivo) { found = c; break; }
        } catch (PersistenceException ignored) {}

        if (found != null) {
            int novaQtd = found.getQuantidade() + delta;
            if (novaQtd <= 0) {
                daoCarteira.apagar(found.getId());
            } else {
                found.setCampos(new String[]{String.valueOf(idCliente), String.valueOf(idAtivo), String.valueOf(novaQtd)});
                daoCarteira.atualizar(found);
            }
        } else if (delta > 0) {
            Carteira nova = new Carteira(proximoId(daoCarteira));
            nova.setCampos(new String[]{String.valueOf(idCliente), String.valueOf(idAtivo), String.valueOf(delta)});
            daoCarteira.salvar(nova);
        }
    }

    private <E extends Entidade> int proximoId(EntidadeDAO<E> dao) {
        try {
            E[] todos = dao.carregarTodos();
            int max = 0;
            for (E e : todos) if (e.getId() > max) max = e.getId();
            return max + 1;
        } catch (PersistenceException e) {
            return 1;
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
