package controller;

import model.Board;
import view.TetrisView;

import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class TetrisController implements KeyListener {
    private Board board;
    private TetrisView view;
    private Timer timer;
    private Timer animTimer;
    private static final int SPEED = 500;
    
    private GameState currentState = GameState.MENU; 
    
    private boolean isPaused = false; 

    public TetrisController(Board board, TetrisView view){
        this.board = board;
        this.view = view;
        
        // Passa o próprio Controller para a View
        this.view.setController(this); 
        this.view.setGameState(currentState); 

        // Timer de Queda Automática
        timer = new Timer(SPEED,e->{
            if(currentState == GameState.PLAYING && !board.isAnimating()){
                board.oneLineDown();
                
                if(board.isGameOver()) { 
                    setGameState(GameState.GAME_OVER);
                } 
                view.repaint();
            }
        });

    }

    // Implementação da interface KeyListener
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e){
        final int keyCode = e.getKeyCode();

        // Lógica de Transição (MENU, GAME OVER)
        if (currentState == GameState.GAME_OVER || currentState == GameState.MENU) {
            // Inicia ou Reinicia o jogo (Game Over/Menu)
            if (keyCode == KeyEvent.VK_N || keyCode == KeyEvent.VK_SPACE || keyCode == KeyEvent.VK_ENTER) {
                // Solicita o nome APENAS ao sair do Menu ou Game Over
                String playerName = view.getPlayerName();
                if (playerName != null) startNewGame(playerName);
            }
            if (keyCode == KeyEvent.VK_M) {
                returnToMenu();
            }
            return;
        }
        
        // Controles Globais
        if (keyCode == KeyEvent.VK_M) {
            returnToMenu();
            return;
        }
        
        if (keyCode == KeyEvent.VK_I) {
            toggleInstructions();
            return;
        }
        
        if (keyCode == KeyEvent.VK_P) {
            togglePause();
            return;
        }
        
        // Bloqueia movimentos se não estiver PLAYING ou estiver animando
        if(currentState != GameState.PLAYING || board.isAnimating()) return;
        
        // Reiniciar Jogo (Mantém o nome do jogador atual)
        if (keyCode == KeyEvent.VK_N) {
            restartGame(); 
            return;
        }
        
        // Lógica de Jogo
        boolean moved = true; 
        
        switch(keyCode){
            case KeyEvent.VK_LEFT -> board.moveLeft();
            case KeyEvent.VK_RIGHT -> board.moveRight();
            case KeyEvent.VK_DOWN -> board.oneLineDown();
            case KeyEvent.VK_UP -> board.rotateRight();
            case KeyEvent.VK_Z -> board.rotateLeft();
            case KeyEvent.VK_SPACE -> board.dropDown();
            default -> moved = false;
        }
        
        if (moved) {
            view.repaint();
        }
    }
    
    // MÉTODOS DE CONTROLE DE ESTADO
    
    private void setGameState(GameState newState) {
        this.currentState = newState;
        this.view.setGameState(newState);
        
        if (newState == GameState.PLAYING) {
            if (!board.isAnimating()) {
                timer.start();
            }
        } else {
            timer.stop();
            animTimer.stop();
        }
    }
    
    public void startNewGame(String playerName) {
        board.resetGame(playerName); // Usa o novo método com nome
        isPaused = false;
        setGameState(GameState.PLAYING);
        view.requestFocus();
    }

    public void restartGame(){
        board.restart(); // Reinicia mantendo o nome que já está no board.
        isPaused = false;
        setGameState(GameState.PLAYING); 
        view.requestFocus(); 
    }
    
    public void togglePause() {
        if (currentState == GameState.PLAYING) {
            isPaused = true;
            setGameState(GameState.PAUSED);
        } else if (currentState == GameState.PAUSED) {
            isPaused = false;
            setGameState(GameState.PLAYING);
        }
        view.requestFocus(); 
    }
    
    public void toggleInstructions() {
        if (currentState == GameState.INSTRUCTIONS) {
            setGameState(isPaused ? GameState.PAUSED : GameState.PLAYING);
        } else {
            isPaused = (currentState == GameState.PAUSED); 
            setGameState(GameState.INSTRUCTIONS);
        }
        view.requestFocus(); 
    }
    
    public void returnToMenu() {
        board.restart();
        isPaused = false;
        setGameState(GameState.MENU);
        view.requestFocus(); 
    }
    
    public GameState getCurrentState() {
        return currentState;
    }
}