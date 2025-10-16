package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Classe JDialog personalizada para capturar o nome do jogador com estilo retro.
public class RetroNameDialog extends JDialog implements ActionListener {
    private String playerName = null;
    private JTextField nameField;
    private final int MAX_CHARS = 10;
    private JButton okButton;
    private JButton cancelButton;

    public RetroNameDialog(Frame owner) {
        super(owner, "Novo Jogo", true); 
        
        // Configuração geral do diálogo
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Definições de Cores Retro 
        Color backgroundColor = new Color(20, 20, 60); // Fundo escuro do painel
        Color neonBorderColor = new Color(40, 200, 255); // Azul Neon para bordas/detalhes
        Color titleColor = new Color(255, 230, 120); // Amarelo para títulos
        Color inputTextColor = new Color(100, 255, 100); // Verde para o input
        
        // Criação do Painel Principal
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(10, 10));
        contentPanel.setBackground(backgroundColor);
        
        // Adiciona um CompoundBorder para o efeito de moldura retro
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(neonBorderColor.darker(), 5), // Borda externa de cor
            BorderFactory.createEmptyBorder(15, 15, 15, 15) // Padding interno
        ));
        
        // Título 
        JLabel titleLabel = new JLabel("NOVO JOGO", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Courier New", Font.BOLD, 24));
        titleLabel.setForeground(titleColor);
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        // Área de Input 
        JPanel inputPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        inputPanel.setBackground(backgroundColor);
        
        JLabel promptLabel = new JLabel("Entre com seu nome (máx. " + MAX_CHARS + " caracteres):");
        promptLabel.setFont(new Font("Courier New", Font.PLAIN, 14));
        promptLabel.setForeground(Color.WHITE);
        inputPanel.add(promptLabel);

        nameField = new JTextField(MAX_CHARS);
        nameField.setFont(new Font("Courier New", Font.BOLD, 18));
        nameField.setBackground(Color.BLACK);
        nameField.setForeground(inputTextColor);
        nameField.setCaretColor(Color.WHITE);
        
        // Adiciona um KeyListener para limitar o número de caracteres digitados
        nameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent e) {
                if (nameField.getText().length() >= MAX_CHARS) {
                    e.consume(); // Ignora a tecla se o limite for atingido
                }
            }
        });
        // Adiciona um ActionListener para o ENTER
        nameField.addActionListener(this); 
        inputPanel.add(nameField);
        
        contentPanel.add(inputPanel, BorderLayout.CENTER);

        // Botões 
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setBackground(backgroundColor);
        
        okButton = createStyledButton("OK");
        okButton.addActionListener(this);
        buttonPanel.add(okButton);
        
        cancelButton = createStyledButton("Cancel");
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);
        
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(contentPanel);
        pack(); // Ajusta o tamanho
        setLocationRelativeTo(owner); // Centraliza na janela principal
    }
    
    // Método auxiliar para criar botões com o mesmo estilo dos botões da view
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(60, 60, 100));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setFont(new Font("Courier New", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(80, 30));
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Ação acionada pelo botão OK, botão Cancel ou ENTER no campo de texto
        if (e.getSource() == okButton || e.getSource() == nameField) {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                playerName = "JOGADOR";
            } else {
                playerName = name.toUpperCase().substring(0, Math.min(name.length(), MAX_CHARS));
            }
            dispose(); // Fecha o diálogo
        } else if (e.getSource() == cancelButton) {
            playerName = "JOGADOR"; // Nome padrão se cancelar
            dispose(); // Fecha o diálogo
        }
    }

    // Método que a TetrisView chama para iniciar o diálogo
    public String getPlayerName() {
        this.setVisible(true);
        return playerName;
    }
}