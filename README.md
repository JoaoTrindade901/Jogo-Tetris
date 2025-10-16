# 🕹️ Tetris Retro Edition

Uma reimplementação clássica do jogo Tetris, desenvolvida em Java com a biblioteca Swing, focada em entregar uma experiência visual retrô e nostálgica.

## 🚀 Como Executar

Este projeto é um aplicativo Java Swing. Você precisará ter o **Java Development Kit (JDK)** instalado em sua máquina.

### 1. Requisitos
* JDK 8 ou superior

### 2. Estrutura de Arquivos

```
Tetris/
├── controller/
│ └── TetrisController.java # Gerencia a entrada do usuário e as transições de estado do jogo.
│
├── model/ # Dados e regras do jogo
│ ├── Board.java # Tabuleiro e lógica central (movimento, colisão, linhas, etc.)
│ ├── Piece.java # Representação de uma peça do Tetris.
│ ├── Tetrominoes.java # Enum com os formatos das peças (I, O, T, S, Z, J, L).
│ └── HighScoreManager.java # Gerencia pontuações e ranking.
│
├── view/ # Interface gráfica (Swing)
│ ├── TetrisView.java # Painel principal: desenha o tabuleiro, status e menus.
│ └── RetroNameDialog.java # Diálogo customizado para entrada de nome com estilo retrô.
│
└── Main.java # Ponto de entrada do programa.
```


### 3. Execução (Via Terminal)

1.  **Compile o código:**
    ```bash
    # (Ajuste o caminho se seu código fonte não estiver em 'src')
    javac -d bin src/*.java src/controller/*.java src/model/*.java src/view/*.java
    ```

2.  **Execute o jogo:**
    ```bash
    java -cp bin Main
    ```

## 🎮 Como Jogar (Controles)

O jogo utiliza controles simples baseados em teclado:

| Ação | Tecla |
| :--- | :--- |
| **Mover Peça** | Setas **←** e **→** |
| **Girar (Horário)** | Seta **↑** (Cima) |
| **Girar (Anti-horário)** | Tecla **Z** |
| **Queda Suave (Soft Drop)** | Seta **↓** (Baixo) |
| **Queda Instantânea (Hard Drop)** | Tecla **ESPAÇO** |
| **Pausar/Continuar** | Tecla **P** |
| **Novo Jogo (Menu/Game Over)** | Tecla **N** |
| **Menu Inicial** | Tecla **M** |
| **Instruções (Toggle)** | Tecla **I** |

## ✨ Características e Melhorias

* **Tema Retro Customizado:** Interface de usuário e caixas de diálogo customizadas com cores neon e fonte "Courier New" para uma experiência visual consistente.
* **UX Aprimorada:** Caixa de diálogo para nome do jogador estilizada e com limite de 10 caracteres.
* **Controle de Layout:** Correção para garantir que botões não se sobreponham na tela de instruções.
* **Gestão de High Score:** O jogo salva o recorde de pontuação, incluindo o nome do jogador.
