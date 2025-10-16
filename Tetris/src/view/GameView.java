package view;

import model.Board;
import model.Piece;
import model.Tetrominoes;

import javax.swing.*;
import java.awt.*;

public class GameView extends JPanel {
    private final Board board;
    private final int TILE;             // tamanho do bloco em pixels
    private final int paddingX;
    private final int paddingY;

    public GameView(Board board, int tileSize, int padX, int padY) {
        this.board = board;
        this.TILE = tileSize;
        this.paddingX = padX;
        this.paddingY = padY;
        setPreferredSize(new Dimension(board.getWidth() * TILE + padX * 2,
                                       board.getHeight() * TILE + padY * 2));
        setBackground(new Color(8, 8, 30)); // fundo estilo NES
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        int boardX = paddingX;
        int boardY = paddingY;
        int cols = board.getWidth();
        int rows = board.getHeight();

        // painel com borda
        drawBoardFrame(g, boardX - 6, boardY - 6, cols * TILE + 12, rows * TILE + 12);

        // células do grid
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Tetrominoes shape = board.shapeAt(c, r);
                int x = boardX + c * TILE;
                int y = boardY + r * TILE;

                boolean blinking = false;
                if (board.isAnimating()) {
                    boolean[] lines = board.getLinesToRemove();
                    if (lines[r]) {
                        blinking = (board.getAnimationStep() % 2 == 0);
                    }
                }

                if (shape != Tetrominoes.NoShape && !blinking) {
                    drawTile(g, x, y, shape);
                } else if (shape != Tetrominoes.NoShape && blinking) {
                    g.setColor(Color.WHITE);
                    g.fillRect(x, y, TILE, TILE);
                    g.setColor(Color.DARK_GRAY);
                    g.drawRect(x, y, TILE - 1, TILE - 1);
                } else {
                    g.setColor(new Color(12, 12, 36));
                    g.fillRect(x, y, TILE, TILE);
                    g.setColor(new Color(20, 20, 50));
                    g.drawRect(x, y, TILE - 1, TILE - 1);
                }
            }
        }

        // desenha a peça atual (se não animando; se animando, peça já está fixada)
        if (!board.isAnimating() && !board.isGameOver()) {
            Piece p = board.getCurrentPiece();
            if (p != null) {
                Tetrominoes shape = p.getShape();
                for (int i = 0; i < 4; i++) {
                    int px = boardX + (board.getCurX() + p.x(i)) * TILE;
                    int py = boardY + (board.getCurY() + p.y(i)) * TILE;
                    drawTile(g, px, py, shape);
                }
            }
        }

        // desenha overlay de Game Over
        if (board.isGameOver()) {
            drawGameOver(g, boardX, boardY, cols * TILE, rows * TILE);
        }
    }

    private void drawBoardFrame(Graphics2D g, int x, int y, int w, int h) {
        // fundo do frame
        g.setColor(new Color(20, 20, 60));
        g.fillRect(x, y, w, h);

        // borda principal
        g.setColor(new Color(90, 120, 200));
        g.setStroke(new BasicStroke(3));
        g.drawRect(x, y, w, h);

        // borda interna
        g.setColor(new Color(40, 40, 80));
        g.setStroke(new BasicStroke(1));
        g.drawRect(x + 4, y + 4, w - 8, h - 8);
    }

    private void drawTile(Graphics2D g, int x, int y, Tetrominoes shape) {
        Color color = getColorForShape(shape);

        // bloco principal
        g.setColor(color);
        g.fillRect(x, y, TILE, TILE);

        // detalhe superior para 'retro shine'
        g.setColor(color.brighter());
        g.fillRect(x + 2, y + 2, TILE - 4, TILE / 3);

        // sombra inferior
        g.setColor(color.darker().darker());
        g.fillRect(x + 2, y + TILE / 2, TILE - 4, TILE / 3);

        // borda preta
        g.setColor(Color.BLACK);
        g.drawRect(x, y, TILE - 1, TILE - 1);
    }

    private Color getColorForShape(Tetrominoes shape) {
        // índice corresponde à ordem do enum
        switch (shape) {
            case ZShape: return new Color(220, 40, 40);
            case SShape: return new Color(40, 220, 40);
            case LineShape: return new Color(60, 200, 240);
            case TShape: return new Color(180, 80, 200);
            case SquareShape: return new Color(255, 200, 60);
            case LShape: return new Color(255, 120, 40);
            case MirroredLShape: return new Color(30, 180, 180);
            default: return new Color(12, 12, 36);
        }
    }

    private void drawGameOver(Graphics2D g, int ox, int oy, int w, int h) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(ox, oy, w, h);

        g.setFont(new Font("Courier New", Font.BOLD, 36));
        g.setColor(new Color(255, 80, 80));
        String gameOver = "GAME OVER";
        FontMetrics fm = g.getFontMetrics();
        int tx = ox + (w - fm.stringWidth(gameOver)) / 2;
        int ty = oy + h / 2 - fm.getHeight();
        g.drawString(gameOver, tx, ty);

        g.setFont(new Font("Courier New", Font.PLAIN, 14));
        g.setColor(Color.WHITE);
        String instr = "Pressione SPACE para reiniciar";
        fm = g.getFontMetrics();
        int ix = ox + (w - fm.stringWidth(instr)) / 2;
        g.drawString(instr, ix, ty + 30);
    }
}
