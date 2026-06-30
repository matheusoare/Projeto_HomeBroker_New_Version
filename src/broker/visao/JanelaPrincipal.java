package broker.visao;

import broker.modelos.*;
import javax.swing.*;
import java.awt.*;

public class JanelaPrincipal extends JFrame {

    public JanelaPrincipal() {
        super("Home Broker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Escolha uma entidade", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel(new GridLayout(3, 2, 15, 15));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));

        addBotao(painelBotoes, "Clientes", Cliente.class);
        JButton btnContas = new JButton("Contas");
        btnContas.addActionListener(e -> new JanelaConta().setVisible(true));
        painelBotoes.add(btnContas);
        addBotao(painelBotoes, "Ativos", Ativo.class);
        addBotao(painelBotoes, "Ordens", Ordem.class);
        addBotao(painelBotoes, "Históricos", Historico.class);
        addBotao(painelBotoes, "Carteiras", Carteira.class);

        add(painelBotoes, BorderLayout.CENTER);
    }

    private <E extends Entidade> void addBotao(JPanel painel, String texto, Class<E> tipo) {
        JButton botao = new JButton(texto);
        botao.addActionListener(e -> new JanelaEntidade<>(tipo, texto).setVisible(true));
        painel.add(botao);
    }
}
