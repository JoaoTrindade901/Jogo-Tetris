# 🕹️ Tetris Retro Edition

Uma reimplementação clássica do jogo Tetris, desenvolvida em Java com a biblioteca Swing, focada em entregar uma experiência visual retrô e nostálgica.

## 🚀 Como Executar

Este projeto é um aplicativo Java Swing. Você precisará ter o **Java Development Kit (JDK)** instalado em sua máquina.

### 1. Requisitos
* JDK 8 ou superior

### 2. Estrutura de Arquivos

Certifique-se de que seus arquivos estejam organizados da seguinte forma:

Perdão, o sistema interrompeu a geração do código anterior.

Aqui estão os três arquivos completos que você solicitou (Main.java, view/TetrisView.java, e view/RetroNameDialog.java) e o README.md (separado) para você copiar e colar no seu projeto.

1. README.md
Salve este conteúdo na pasta raiz do seu projeto como README.md.

Markdown

# 🕹️ Tetris Retro Edition

Uma reimplementação clássica do jogo Tetris, desenvolvida em Java com a biblioteca Swing, focada em entregar uma experiência visual retrô e nostálgica.

## 🚀 Como Executar

Este projeto é um aplicativo Java Swing. Você precisará ter o **Java Development Kit (JDK)** instalado em sua máquina.

### 1. Requisitos
* JDK 8 ou superior

### 2. Estrutura de Arquivos

Certifique-se de que seus arquivos estejam organizados da seguinte forma:
src/
├── controller/        # Lógica de Controle
│   ├── GameState.java
│   └── TetrisController.java  # Gerencia a entrada do usuário e a transição de estados.
├── model/             # Dados e Regras
│   ├── Board.java             # O tabuleiro e a lógica central do jogo (movimento, colisão, etc.).
│   ├── Piece.java
│   ├── HighScoreManager.java
│   └── Tetrominoes.java
└── view/              # Interface Gráfica
    ├── TetrisView.java        # O painel principal que desenha o tabuleiro, status e menus.
    └── RetroNameDialog.java   # Diálogo customizado para entrada de nome com estilo retro.
├── Main.java          # Ponto de entrada do programa.


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
