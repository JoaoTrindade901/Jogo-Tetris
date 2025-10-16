package view;

import model.Board;
import model.Piece;
import model.Tetrominoes;

import javax.swing.*;
import java.awt.*;

public class NextPiecePanel extends JPanel {

    private final Board board;
    private static final int MAX_NEXT_TILE_SIZE = 30;
    private static final int CANVAS_SIZE_BLOCKS = 4;

    public NextPiecePanel(Board board, int tileSize) {
        this.board = board;
        // O tileSize passado no construtor é ignorado, pois o painel calcula o seu próprio
        setPreferredSize(new Dimension(180, 150));
        setBackground(new Color(12, 12, 30));
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Desenha painel de fundo e borda
        g.setColor(new Color(30, 30, 60));
        g.fillRect(0, 0, panelWidth, panelHeight);
        g.setColor(new Color(90, 120, 200));
        g.setStroke(new BasicStroke(2));
        g.drawRect(0, 0, panelWidth - 1, panelHeight - 1);

        drawNextPiece(g, panelWidth, panelHeight);
    }

    private void drawNextPiece(Graphics2D g, int panelWidth, int panelHeight) {
        Piece next = board.getNextPiece();
        // Adiciona um null check robusto no getter
        if (next == null || next.getShape() == Tetrominoes.NoShape) return;

        // Calcula as dimensões da Bounding Box (Caixa Delimitadora) da peça
        int minX = 4, minY = 4, maxX = -1, maxY = -1;
        for (int i = 0; i < 4; i++) {
            minX = Math.min(minX, next.x(i));
            minY = Math.min(minY, next.y(i));
            maxX = Math.max(maxX, next.x(i));
            maxY = Math.max(maxY, next.y(i));
        }
        int pieceWidth = maxX - minX + 1;
        int pieceHeight = maxY - minY + 1;

        // Determina o tamanho ideal do bloco (tileSize)
        int availableWidth = panelWidth;
        int availableHeight = panelHeight;
        
        // Define o tamanho máximo baseado no tamanho do painel e no limite MAX_NEXT_TILE_SIZE
        int tileSize = Math.min(availableWidth / CANVAS_SIZE_BLOCKS, availableHeight / CANVAS_SIZE_BLOCKS);
        tileSize = Math.min(tileSize, MAX_NEXT_TILE_SIZE);
        if (tileSize <= 0) tileSize = 1;

        // Calcula o padding necessário no CANVAS 4x4
        // Posição para centralizar a peça dentro do CANVAS 4x4
        int paddingX = (CANVAS_SIZE_BLOCKS - pieceWidth) / 2;
        int paddingY = (CANVAS_SIZE_BLOCKS - pieceHeight) / 2;

        //  Calcula o offset real na tela (centralizar o CANVAS no Painel)
        int canvasTotalWidth = CANVAS_SIZE_BLOCKS * tileSize;
        int canvasTotalHeight = CANVAS_SIZE_BLOCKS * tileSize;

        int offsetX = (panelWidth - canvasTotalWidth) / 2;
        int offsetY = (panelHeight - canvasTotalHeight) / 2;


        //  Desenha a peça
        for (int i = 0; i < 4; i++) {
            // next.x(i) - minX: Normaliza a peça para começar em (0,0)
            // + paddingX: Posiciona a peça no centro do CANVAS 4x4
            // * tileSize: Transforma em pixel
            // + offsetX: Desloca para o centro do Painel
            int x = offsetX + ((next.x(i) - minX) + paddingX) * tileSize;
            int y = offsetY + ((next.y(i) - minY) + paddingY) * tileSize;
            
            drawBlock(g, x, y, next.getShape(), tileSize);
        }
    }

    private void drawBlock(Graphics2D g, int x, int y, Tetrominoes shape, int size) {
        Color color = getColorForShape(shape);
        g.setColor(color);
        g.fillRect(x, y, size, size);
        
        // Detalhes de brilho e sombra
        g.setColor(color.brighter());
        g.fillRect(x + size / 8, y + size / 8, size * 3 / 4, size / 3);
        g.setColor(color.darker().darker());
        g.fillRect(x + size / 8, y + size / 2, size * 3 / 4, size / 3);
        
        // Borda preta
        g.setColor(Color.BLACK);
        g.drawRect(x, y, size - 1, size - 1);
    }

    private Color getColorForShape(Tetrominoes shape) {
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
}