package model;

import java.util.Arrays;
import java.util.prefs.Preferences;

public class Board {
    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 20;

    // Chaves para salvar o recorde
    private static final String HIGH_SCORE_KEY = "tetris_highscore";
    private static final String HIGH_SCORE_NAME_KEY = "tetris_highscore_name";
    private static final Preferences prefs = Preferences.userNodeForPackage(Board.class);

    private final Tetrominoes[] board;
    private Piece curPiece;
    private Piece nextPiece;
    private int curX;
    private int curY;
    private int score;
    private boolean gameOver;

    private final boolean[] linesToRemove;
    private boolean animating;
    private int animationStep;
    private static final int MAX_ANIMATION_STEP = 6;
    
    private int highScore;
    private String highScoreName;
    private String currentPlayerName;

    public Board() {
        board = new Tetrominoes[BOARD_WIDTH * BOARD_HEIGHT];
        linesToRemove = new boolean[BOARD_HEIGHT];
        loadHighScore();
        resetBoard();
        this.currentPlayerName = "JOGADOR"; // Nome inicial padrão
        newPiece();
    }

    public int getWidth() { return BOARD_WIDTH; }
    public int getHeight() { return BOARD_HEIGHT; }
    public Piece getCurrentPiece() { return curPiece; }
    public Piece getNextPiece() { return nextPiece; }
    public int getCurX() { return curX; }
    public int getCurY() { return curY; }
    public int getScore() { return score; }
    public boolean isGameOver() { return gameOver; }
    public boolean isAnimating() { return animating; }
    public int getAnimationStep() { return animationStep; }
    public boolean[] getLinesToRemove() { return linesToRemove.clone(); }
    public int getHighScore() { return highScore; }
    public String getHighScoreName() { return highScoreName; }
    public String getCurrentPlayerName() { return currentPlayerName; }

    // Método para carregar o recorde do sistema
    private void loadHighScore() {
        this.highScore = prefs.getInt(HIGH_SCORE_KEY, 0);
        this.highScoreName = prefs.get(HIGH_SCORE_NAME_KEY, "SEM NOME");
    }

    // Método para salvar o recorde no sistema
    private void saveHighScore() {
        prefs.putInt(HIGH_SCORE_KEY, this.highScore);
        prefs.put(HIGH_SCORE_NAME_KEY, this.highScoreName);
        try {
            prefs.flush();
        } catch (java.util.prefs.BackingStoreException e) {
            System.err.println("Erro ao salvar high score: " + e.getMessage());
        }
    }

    // Checa se o score atual é um recorde e salva o score E O NOME.
    public void checkAndUpdateHighScore() {
        if (score > highScore) {
            highScore = score;
            highScoreName = currentPlayerName; 
            saveHighScore();
        }
    }

    public void resetGame(String playerName) {
        checkAndUpdateHighScore();

        this.currentPlayerName = playerName.trim().toUpperCase().substring(0, Math.min(playerName.length(), 10));

        resetBoard();
        score = 0;
        gameOver = false;
        newPiece();
    }

    public void restart() {
        checkAndUpdateHighScore();
        
        resetBoard();
        score = 0;
        gameOver = false;
        newPiece();
    }

    private void resetBoard() {
        Arrays.fill(board, Tetrominoes.NoShape);
        Arrays.fill(linesToRemove, false);
        animating = false;
        animationStep = 0;
    }

    // Retorna a coordenada Y mais alta (mais próxima do topo = mais negativa ou zero)
    private int minPieceY(Piece p) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < 4; i++) min = Math.min(min, p.y(i));
        return min;
    }

    public void newPiece() {
        curPiece = nextPiece == null ? new Piece() : nextPiece;
        if (nextPiece == null || nextPiece.getShape() == Tetrominoes.NoShape) curPiece.setRandomShape();
        nextPiece = new Piece();
        nextPiece.setRandomShape();
        
        // CORREÇÃO do curX: Centraliza a peça
        curX = BOARD_WIDTH / 2 - 1; 
        
        // CORREÇÃO do curY: Coloca a peça logo acima do topo (ou no topo se for a peça 'I')
        curY = -minPieceY(curPiece); 
        
        if (!tryMove(curPiece, curX, curY)) {
            gameOver = true;
            checkAndUpdateHighScore();
        }
    }

    public boolean tryMove(Piece piece, int newX, int newY) {
        for (int i = 0; i < 4; i++) {
            int x = newX + piece.x(i);
            int y = newY + piece.y(i);
            if (x < 0 || x >= BOARD_WIDTH) return false;
            if (y >= BOARD_HEIGHT) return false;
            if (y >= 0 && board[y * BOARD_WIDTH + x] != Tetrominoes.NoShape) return false;
        }
        curPiece = piece;
        curX = newX;
        curY = newY;
        return true;
    }

    public void moveLeft() { tryMove(curPiece, curX - 1, curY); }
    public void moveRight() { tryMove(curPiece, curX + 1, curY); }
    public void rotateLeft() { tryMove(curPiece.rotateLeft(), curX, curY); }
    public void rotateRight() { tryMove(curPiece.rotateRight(), curX, curY); }

    public void oneLineDown() {
        if (!tryMove(curPiece, curX, curY + 1)) pieceDropped();
    }

    public void dropDown() {
        while (tryMove(curPiece, curX, curY + 1)) {}
        pieceDropped();
    }

    private void pieceDropped() {
        boolean aboveTop = false;
        for (int i = 0; i < 4; i++) {
            int x = curX + curPiece.x(i);
            int y = curY + curPiece.y(i);
            if (y < 0) { aboveTop = true; continue; }
            board[y * BOARD_WIDTH + x] = curPiece.getShape();
        }
        if (aboveTop) {
            gameOver = true;
            checkAndUpdateHighScore();
            return;
        }

        int linesRemovedCount = 0;
        
        for (int row = BOARD_HEIGHT - 1; row >= 0; row--) {
            boolean full = true;
            for (int x = 0; x < BOARD_WIDTH; x++) {
                if (shapeAt(x, row) == Tetrominoes.NoShape) {
                    full = false;
                    break;
                }
            }
            
            if (full) {
                linesRemovedCount++;
                // Lógica da animação de linha (aqui a linha estaria marcada)
            } else if (linesRemovedCount > 0) {
                // Move as linhas para baixo
                for (int x = 0; x < BOARD_WIDTH; x++) {
                    board[(row + linesRemovedCount) * BOARD_WIDTH + x] = shapeAt(x, row);
                }
            }
        }
        
        if (linesRemovedCount > 0) {
            // Limpa as linhas removidas no topo
            for (int y = 0; y < linesRemovedCount; y++) {
                for (int x = 0; x < BOARD_WIDTH; x++) {
                    board[y * BOARD_WIDTH + x] = Tetrominoes.NoShape;
                }
            }

            switch (linesRemovedCount) {
                case 1 -> score += 100;
                case 2 -> score += 300;
                case 3 -> score += 500;
                case 4 -> score += 800;
            }
            
            checkAndUpdateHighScore();
        }
        
        newPiece();
    }

    public Tetrominoes shapeAt(int x, int y) {
        if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) return Tetrominoes.NoShape;
        return board[y * BOARD_WIDTH + x];
    }
    
    public boolean advanceAnimation() {
        return false;
    }
}