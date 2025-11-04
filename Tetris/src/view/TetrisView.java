package view;

import model.Board;
import model.Piece;
import model.Tetrominoes;
import controller.TetrisController;
import controller.GameState;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TetrisView extends JPanel {
    private final Board board;
    private static final int TILE = 24; 
    private static final int PADDING_X = 20;
    private static final int PADDING_Y = 30; 
    private static final int PANEL_SIDE_WIDTH = 160; 
    private static final int PANEL_HORIZONTAL_OFFSET = 20; 
    private static final int PANEL_VERTICAL_SPACING = 20; 
    
    private TetrisController controller;
    
    private GameState currentViewState = GameState.MENU;
    
    private JButton newGameButton;
    private JButton pauseButton;
    private JButton instructionsButton;
    private JButton menuButton; 
    
    private final int BUTTON_WIDTH = 140; 
    private final int BUTTON_HEIGHT = 35; 
    private final int BUTTON_GROUP_SPACING = 12; 
    private final int BUTTON_HORIZONTAL_SPACING = 4; 

    private Timer menuBlinkTimer;
    private boolean showStartText = true; 
    
    private Timer fallTimer;
    private List<FallingMiniPiece> fallingPieces = new ArrayList<>();
    private final int MENU_FRAME_W = 400; 
    private final int MINI_TILE = TILE - 4; 
    private final Random random = new Random();

    private static final float MENU_PIECE_ALPHA = 0.4f; 

    private static final Color[] COLORS = {
        new Color(10,10,10), 
        new Color(220, 30, 30), 
        new Color(30, 220, 30), 
        new Color(40, 200, 255), 
        new Color(180, 80, 200), 
        new Color(255, 180, 30), 
        new Color(255, 100, 30), 
        new Color(30, 180, 180) 
    };

    private class FallingMiniPiece {
        Piece piece;
        int x; 
        int y; 

        public FallingMiniPiece(int x) {
            this.piece = new Piece(Tetrominoes.getRandomShape()); 
            this.x = x; 
            this.y = -5; 
        }
        
        public void fall() {
            y++;
        }
    }

    public TetrisView(Board board) {
        this.board = board;
        
        final int EXTRA_WIDTH_OFFSET = 100;
        
        int boardWidthTiles = board.getWidth() * TILE;
        int totalOriginalWidth = boardWidthTiles + PADDING_X * 2 + PANEL_SIDE_WIDTH + PANEL_HORIZONTAL_OFFSET;
        int width = totalOriginalWidth + EXTRA_WIDTH_OFFSET; 
        
        int height = board.getHeight() * TILE + PADDING_Y * 2; 
        
        setPreferredSize(new Dimension(width, height));
        setBackground(new Color(8, 8, 30)); 
        
        setLayout(null); 
        
        createButtons();
        createBlinkTimer(); 
        createFallTimer(); 
    }
    
    /**
     * SUBSTITUIÇÃO: Usa RetroNameDialog em vez de JOptionPane para o estilo retro.
     */
    public String getPlayerName() {
        Frame parentFrame = JOptionPane.getFrameForComponent(this);
        
        RetroNameDialog dialog = new RetroNameDialog(parentFrame);
        String name = dialog.getPlayerName();

        if (name == null || name.trim().isEmpty()) {
            return "JOGADOR";
        }
        
        return name.trim().toUpperCase().substring(0, Math.min(name.length(), 10));
    }
    
    private void createBlinkTimer() {
        menuBlinkTimer = new Timer(500, e -> { 
            if (currentViewState == GameState.MENU) {
                showStartText = !showStartText;
                repaint();
            }
        });
        menuBlinkTimer.start(); 
    }
    
    private void createFallTimer() {
        fallTimer = new Timer(100, e -> { 
            if (currentViewState == GameState.MENU) {
                fallingPieces.forEach(FallingMiniPiece::fall);
                fallingPieces.removeIf(p -> p.y > (getHeight() / MINI_TILE) + 5);

                if (random.nextInt(8) == 0) { 
                    int maxMiniTileX = MENU_FRAME_W / MINI_TILE;
                    int startX = random.nextInt(maxMiniTileX - 5) + 2; 
                    
                    if (fallingPieces.isEmpty() || Math.abs(fallingPieces.get(fallingPieces.size() - 1).x - startX) > 4) {
                        fallingPieces.add(new FallingMiniPiece(startX));
                    }
                }
                
                repaint();
            }
        });
    }
    
    private void createButtons() {
        newGameButton = createStyledButton("Novo Jogo");
        newGameButton.addActionListener(e -> {
            if (controller != null) controller.restartGame(); 
        });
        add(newGameButton);
        
        pauseButton = createStyledButton("Pausar");
        pauseButton.addActionListener(e -> {
            if (controller != null) controller.togglePause();
        });
        add(pauseButton);
        
        instructionsButton = createStyledButton("Instruções");
        instructionsButton.addActionListener(e -> {
            if (controller != null) controller.toggleInstructions();
        });
        add(instructionsButton);
        
        menuButton = createStyledButton("Menu");
        menuButton.addActionListener(e -> {
            if (controller != null) controller.returnToMenu();
        });
        add(menuButton);
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(60, 60, 100));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setFont(new Font("Courier New", Font.BOLD, 14)); 
        button.setVisible(false); 
        return button;
    }

    public void setController(TetrisController controller) {
        this.controller = controller;
        this.addKeyListener(controller);
        this.setFocusable(true);
    }

    public void setGameState(GameState state) {
        this.currentViewState = state;
        
        if (state == GameState.MENU) {
            if (!menuBlinkTimer.isRunning()) menuBlinkTimer.start();
            if (!fallTimer.isRunning()) fallTimer.start();
        } else {
            if (menuBlinkTimer.isRunning()) menuBlinkTimer.stop();
            if (fallTimer.isRunning()) fallTimer.stop();
        }
        
        if (newGameButton != null) newGameButton.setVisible(false);
        if (pauseButton != null) pauseButton.setVisible(false);
        if (instructionsButton != null) instructionsButton.setVisible(false);
        if (menuButton != null) menuButton.setVisible(false);

        if (state == GameState.PLAYING || state == GameState.PAUSED) {
            if (newGameButton != null) newGameButton.setVisible(true);
            if (pauseButton != null) {
                pauseButton.setVisible(true);
                pauseButton.setText(state == GameState.PAUSED ? "Continuar" : "Pausar");
            }
            if (instructionsButton != null) instructionsButton.setVisible(true);
            if (menuButton != null) menuButton.setVisible(true);
        } else if (state == GameState.INSTRUCTIONS) {

        } else if (state == GameState.GAME_OVER) {

        }
        
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        if (currentViewState == GameState.MENU) {
            drawMenuOverlay(g, getWidth(), getHeight());
            return;
        }

        int boardWidth = board.getWidth() * TILE;
        int totalOriginalWidth = boardWidth + PADDING_X * 2 + PANEL_SIDE_WIDTH + PANEL_HORIZONTAL_OFFSET;
        int adjustment = (getWidth() - totalOriginalWidth) / 2; 
        
        int boardX = PADDING_X + adjustment;
        int boardY = PADDING_Y;
        int boardW = board.getWidth() * TILE;
        int boardH = board.getHeight() * TILE;
        
        int sidePanelX = boardX + boardWidth + PANEL_HORIZONTAL_OFFSET;
        
        int nextPanelHeight = 120;
        int scoreDetailsPanelHeight = 140; 
        
        int currentY_after_panels = boardY + 
                                    nextPanelHeight + 
                                    PANEL_VERTICAL_SPACING + 
                                    scoreDetailsPanelHeight +
                                    PANEL_VERTICAL_SPACING; 
        
        int buttonY_start = currentY_after_panels; 
        
        int buttonX_center = sidePanelX + (PANEL_SIDE_WIDTH - BUTTON_WIDTH) / 2;
        
        int y_pos_1 = buttonY_start;
        int y_pos_2 = y_pos_1 + BUTTON_HEIGHT + BUTTON_GROUP_SPACING;
        int y_pos_3 = y_pos_2 + BUTTON_HEIGHT + BUTTON_GROUP_SPACING;
        int y_pos_4 = y_pos_3 + BUTTON_HEIGHT + BUTTON_GROUP_SPACING;

        newGameButton.setBounds(buttonX_center, y_pos_1, BUTTON_WIDTH, BUTTON_HEIGHT);
        pauseButton.setBounds(buttonX_center, y_pos_2, BUTTON_WIDTH, BUTTON_HEIGHT);
        instructionsButton.setBounds(buttonX_center, y_pos_3, BUTTON_WIDTH, BUTTON_HEIGHT);
        menuButton.setBounds(buttonX_center, y_pos_4, BUTTON_WIDTH, BUTTON_HEIGHT);

        if (currentViewState != GameState.INSTRUCTIONS) {
            drawRetroPanel(g, boardX - 6, boardY - 6, boardW + 12, boardH + 12);
            drawGrid(g, boardX, boardY);

            if (currentViewState == GameState.PLAYING && !board.isAnimating()) {
                drawCurrentPiece(g, boardX, boardY);
            }

            drawSidePanel(g, sidePanelX, boardY); 
        }
        
        if (currentViewState == GameState.GAME_OVER) {
            drawGameOver(g, boardX, boardY, boardW, boardH);
        }
        
        if (currentViewState == GameState.PAUSED) {
            drawPauseOverlay(g, boardX, boardY, boardW, boardH);
        }
        
        if (currentViewState == GameState.INSTRUCTIONS) {
            drawInstructionsOverlay(g);
        }
    }
    
    private void drawRetroBorder(Graphics2D g, int x, int y, int w, int h) {
        g.setColor(new Color(20, 20, 60)); 
        g.fillRect(x, y, w, h);
        
        g.setColor(new Color(150, 170, 255)); 
        g.setStroke(new BasicStroke(4));
        g.drawRect(x, y, w, h);
        
        g.setColor(new Color(40, 40, 80)); 
        g.setStroke(new BasicStroke(2));
        g.drawRect(x + 5, y + 5, w - 10, h - 10);
    }

    private void drawMenuOverlay(Graphics2D g, int w, int h) {
        g.setColor(new Color(8, 8, 30)); 
        g.fillRect(0, 0, w, h);
        
        int frameW = MENU_FRAME_W; 
        int frameH = 500;
        int frameX = (w - frameW) / 2;
        int frameY = (h - frameH) / 2;
        
        drawRetroBorder(g, frameX, frameY, frameW, frameH);

        g.setColor(new Color(20, 20, 50));
        g.fillRect(frameX + 10, frameY + frameH - 100, frameW - 20, 2); 

        drawFallingPieces(g, frameX, frameY);
        
        Color yellowRetro = new Color(255, 240, 100); 
        g.setColor(yellowRetro);
        
        g.setFont(new Font("Courier New", Font.BOLD, 48)); 
        String title = "TETRIS";
        FontMetrics fmTitle = g.getFontMetrics();
        int titleX = frameX + (frameW - fmTitle.stringWidth(title)) / 2;
        int titleY = frameY + 80;
        g.drawString(title, titleX, titleY);
        
        g.setFont(new Font("Courier New", Font.BOLD, 28)); 
        String subtitle = "RETRO EDITION";
        FontMetrics fmSubtitle = g.getFontMetrics();
        int subtitleX = frameX + (frameW - fmSubtitle.stringWidth(subtitle)) / 2;
        g.drawString(subtitle, subtitleX, titleY + 30);
        
        g.setColor(new Color(100, 100, 180));
        g.fillRect(frameX + 30, titleY + 70, frameW - 60, 2);
        
        g.setFont(new Font("Courier New", Font.BOLD, 20));
        g.setColor(new Color(255, 200, 60)); 
        
        String hsNameText = String.format("RECORDISTA: %s", board.getHighScoreName());
        FontMetrics fmHSName = g.getFontMetrics();
        int hsNameX = frameX + (frameW - fmHSName.stringWidth(hsNameText)) / 2;
        g.drawString(hsNameText, hsNameX, titleY + 160);

        g.setFont(new Font("Courier New", Font.PLAIN, 22));
        g.setColor(Color.WHITE);
        String highScoreText = String.format("SCORE: %06d", board.getHighScore());
        FontMetrics fmScore = g.getFontMetrics();
        int scoreX = frameX + (frameW - fmScore.stringWidth(highScoreText)) / 2;
        g.drawString(highScoreText, scoreX, titleY + 190);
        
        if (showStartText) {
            g.setColor(new Color(150, 220, 255));
            g.setFont(new Font("Courier New", Font.BOLD, 20)); 
            String startText = "PRESSIONE ENTER OU ESPAÇO";
            FontMetrics fmStart = g.getFontMetrics();
            int startX = frameX + (frameW - fmStart.stringWidth(startText)) / 2;
            g.drawString(startText, startX, frameY + frameH - 60); 
        }
    }
    
    private void drawFallingPieces(Graphics2D g, int frameX, int frameY) {
        int contentX = frameX + 10;
        int contentY = frameY + 10;
        
        int clipY = frameY + 180;
        int clipH = 240; 
        
        Shape originalClip = g.getClip();
        g.setClip(contentX, clipY, MENU_FRAME_W - 20, clipH);

        Composite originalComposite = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, MENU_PIECE_ALPHA));

        for (FallingMiniPiece fmp : fallingPieces) {
            Piece p = fmp.piece;
            for (int i = 0; i < 4; i++) {
                int px = contentX + (fmp.x + p.x(i)) * MINI_TILE;
                int py = contentY + (fmp.y + p.y(i)) * MINI_TILE;
                
                drawMiniTile(g, px, py, p.getShape(), MINI_TILE);
            }
        }
        
        g.setComposite(originalComposite);
        g.setClip(originalClip);
    }
    
    private void drawRetroPanel(Graphics2D g, int x, int y, int w, int h) {
        g.setColor(new Color(20, 20, 60));
        g.fillRect(x, y, w, h);
        g.setColor(new Color(90, 120, 200));
        g.setStroke(new BasicStroke(3));
        g.drawRect(x, y, w, h);
        g.setColor(new Color(40, 40, 80));
        g.setStroke(new BasicStroke(1));
        g.drawRect(x + 4, y + 4, w - 8, h - 8);
    }

    private void drawGrid(Graphics2D g, int ox, int oy) {
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                Tetrominoes shape = board.shapeAt(col, row);

                boolean blinking = false; 

                if (shape != Tetrominoes.NoShape && !blinking) {
                    drawTile(g, ox + col * TILE, oy + row * TILE, shape);
                } else if (shape != Tetrominoes.NoShape && blinking) {
                    g.setColor(Color.WHITE);
                    g.fillRect(ox + col * TILE, oy + row * TILE, TILE, TILE);
                    g.setColor(Color.DARK_GRAY);
                    g.drawRect(ox + col * TILE, oy + row * TILE, TILE - 1, TILE - 1);
                } else {
                    g.setColor(new Color(15, 15, 40));
                    g.fillRect(ox + col * TILE, oy + row * TILE, TILE, TILE);
                    g.setColor(new Color(30, 30, 60));
                    g.drawRect(ox + col * TILE, oy + row * TILE, TILE - 1, TILE - 1);
                }
            }
        }
    }

    private void drawTile(Graphics2D g, int x, int y, Tetrominoes shape) {
        Color color = COLORS[shape.ordinal()];
        g.setColor(color);
        g.fillRect(x, y, TILE, TILE);
        g.setColor(color.brighter());
        g.fillRect(x + 2, y + 2, TILE - 4, TILE / 3);
        g.setColor(color.darker().darker());
        g.fillRect(x + 2, y + TILE / 2, TILE - 4, TILE / 3);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, TILE - 1, TILE - 1);
    }

    private void drawCurrentPiece(Graphics2D g, int ox, int oy) {
        Piece p = board.getCurrentPiece();
        if (p == null) return;
        Tetrominoes shape = p.getShape();
        for (int i = 0; i < 4; i++) {
            int x = ox + (board.getCurX() + p.x(i)) * TILE;
            int y = oy + (board.getCurY() + p.y(i)) * TILE;
            if (y >= oy) { 
                 drawTile(g, x, y, shape);
            }
        }
    }

    private void drawSidePanel(Graphics2D g, int x, int y) {
        int currentY = y; 

        int nextPanelHeight = 120;
        drawPanel(g, x, currentY, PANEL_SIDE_WIDTH, nextPanelHeight, "NEXT");
        drawNext(g, x + 10, currentY + 40);
        currentY += nextPanelHeight + PANEL_VERTICAL_SPACING; 
        
        int scoreDetailsPanelHeight = 140; 
        drawPanel(g, x, currentY, PANEL_SIDE_WIDTH, scoreDetailsPanelHeight, "STATUS");
        
        final int ALIGN_X = x + 10;
        int textY = currentY + 30; 

        g.setFont(new Font("Courier New", Font.BOLD, 14));
        g.setColor(new Color(150, 200, 255));
        g.drawString("JOGADOR:", ALIGN_X, textY);
        g.setColor(new Color(100, 255, 100)); 
        g.drawString(board.getCurrentPlayerName(), ALIGN_X + 80, textY);
        textY += 35; 
        
        g.setFont(new Font("Courier New", Font.BOLD, 14));
        g.setColor(new Color(150, 200, 255));
        g.drawString("SCORE:", ALIGN_X, textY);
        g.setFont(new Font("Courier New", Font.BOLD, 18));
        g.setColor(Color.WHITE);
        g.drawString(String.format("%06d", board.getScore()), ALIGN_X + 70, textY);
        textY += 40;

        g.setFont(new Font("Courier New", Font.BOLD, 14));
        g.setColor(new Color(255, 200, 60)); 
        g.drawString("HIGH SCORE:", ALIGN_X, textY);
        textY += 25;
        
        g.setFont(new Font("Courier New", Font.BOLD, 16));
        g.setColor(new Color(255, 240, 100));
        
        String hsLine = String.format("%s - %06d", board.getHighScoreName(), board.getHighScore());
        
        g.drawString(hsLine, ALIGN_X, textY);
    }

    private void drawPanel(Graphics2D g, int x, int y, int w, int h, String title) {
        g.setColor(new Color(30, 30, 60));
        g.fillRect(x, y, w, h);
        g.setColor(new Color(100, 120, 200));
        g.setStroke(new BasicStroke(2));
        g.drawRect(x, y, w, h);
        g.setColor(new Color(255, 230, 120));
        g.setFont(new Font("Courier New", Font.BOLD, 16));
        g.drawString(title, x + 10, y + 20);
    }

    private void drawNext(Graphics2D g, int x, int y) {
        Piece next = board.getNextPiece();
        if (next == null || next.getShape() == Tetrominoes.NoShape) return;

        int miniTile = TILE * 2/3;
        
        int centerX = x + (PANEL_SIDE_WIDTH - 20 - (4 * miniTile)) / 2; 
        int centerY = y - 5; 

        int adjustmentX = 1;
        int adjustmentY = 1;

        Tetrominoes shape = next.getShape();
        if (shape == Tetrominoes.LineShape) {
            adjustmentX = 0; 
            adjustmentY = 1; 
        } else if (shape == Tetrominoes.SquareShape) {
            adjustmentX = 1; 
            adjustmentY = 1;
        } else {
            adjustmentX = 1; 
            adjustmentY = 1; 
        }
        
        for (int i = 0; i < 4; i++) {
            int px = centerX + (next.x(i) + adjustmentX) * miniTile;
            int py = centerY + (next.y(i) + adjustmentY) * miniTile;
            drawMiniTile(g, px, py, next.getShape(), miniTile);
        }
    }
    
    private void drawMiniTile(Graphics2D g, int x, int y, Tetrominoes shape, int tileSize) {
        Color color = COLORS[shape.ordinal()];
        g.setColor(color);
        g.fillRect(x, y, tileSize, tileSize);
        g.setColor(color.brighter());
        g.fillRect(x + 2, y + 2, tileSize - 4, tileSize / 3);
        g.setColor(color.darker().darker());
        g.fillRect(x + 2, y + tileSize / 2, tileSize - 4, tileSize / 3);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, tileSize - 1, tileSize - 1);
    }

    private void drawGameOver(Graphics2D g, int ox, int oy, int w, int h) {
        g.setColor(new Color(0, 0, 0, 170));
        g.fillRect(ox, oy, w, h);
        
        g.setFont(new Font("Courier New", Font.BOLD, 36)); 
        g.setColor(new Color(255, 80, 80));
        String text = "GAME OVER";
        FontMetrics fm = g.getFontMetrics();
        int ty = oy + h / 2 - 50; 
        int tx = ox + (w - fm.stringWidth(text)) / 2;
        g.drawString(text, tx, ty);
        
        g.setFont(new Font("Courier New", Font.PLAIN, 20));
        g.setColor(Color.WHITE);
        String finalScore = String.format("PONTUAÇÃO FINAL: %06d", board.getScore());
        fm = g.getFontMetrics(); 
        int fsX = ox + (w - fm.stringWidth(finalScore)) / 2;
        g.drawString(finalScore, fsX, ty + 40);
        
        g.setFont(new Font("Courier New", Font.BOLD, 18));
        g.setColor(new Color(255, 200, 60));
        String hsText = String.format("RECORD: %s %06d", board.getHighScoreName(), board.getHighScore());
        fm = g.getFontMetrics();
        int hsX = ox + (w - fm.stringWidth(hsText)) / 2;
        g.drawString(hsText, hsX, ty + 70);

        g.setFont(new Font("Courier New", Font.PLAIN, 16));
        g.setColor(new Color(180, 180, 180));

        String instr1 = "Pressione N para reiniciar";
        String instr2 = "ou M para Menu";

        fm = g.getFontMetrics();

        int ix1 = ox + (w - fm.stringWidth(instr1)) / 2;
        g.drawString(instr1, ix1, ty + 140);
        
        int ix2 = ox + (w - fm.stringWidth(instr2)) / 2;
        g.drawString(instr2, ix2, ty + 160);
    }
    
    private void drawPauseOverlay(Graphics2D g, int x, int y, int w, int h) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(x, y, w, h);
        g.setColor(new Color(255, 255, 100));
        g.setFont(new Font("Courier New", Font.BOLD, 30));
        FontMetrics fm = g.getFontMetrics();
        String pauseText = "PAUSADO";
        g.drawString(pauseText, x + (w - fm.stringWidth(pauseText)) / 2, y + h / 2);
        g.setColor(new Color(220, 220, 220));
        g.setFont(new Font("Courier New", Font.PLAIN, 16));
        String continueText = "Pressione P para continuar";
        g.drawString(continueText, x + (w - g.getFontMetrics().stringWidth(continueText)) / 2, y + h / 2 + 30);
    }
    
    private void drawInstructionsOverlay(Graphics2D g) {
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        
        g.setColor(Color.BLACK); 
        g.fillRect(0, 0, viewWidth, viewHeight); 
        
        Color yellowRetro = new Color(255, 210, 80);
        
        g.setColor(yellowRetro);
        g.setFont(new Font("Courier New", Font.BOLD, 28)); 
        String title = "INSTRUÇÕES DO JOGO";
        
        FontMetrics fmTitle = g.getFontMetrics();
        int titleX = (viewWidth - fmTitle.stringWidth(title)) / 2;
        int titleY = 40;
        g.drawString(title, titleX, titleY);
        
        Color lightYellow = new Color(255, 240, 150); 
        g.setColor(lightYellow);
        
        int contentXStart = (viewWidth / 2) - 160; 
        int lineY = titleY + 60; 
        int lineHeight = 20; 

        g.setFont(new Font("Courier New", Font.BOLD, 18));
        g.drawString("CONTROLES:", contentXStart, lineY);
        lineY += lineHeight + 5; 
        
        g.setFont(new Font("Courier New", Font.PLAIN, 16));
        g.drawString("← → : Mover peça", contentXStart, lineY); lineY += lineHeight;
        g.drawString("↑ : Girar no sentido horário", contentXStart, lineY); lineY += lineHeight;
        g.drawString("Z : Girar no sentido anti-horário", contentXStart, lineY); lineY += lineHeight;
        g.drawString("↓ : Descer mais rápido", contentXStart, lineY); lineY += lineHeight;
        g.drawString("ESPAÇO : Queda instantânea", contentXStart, lineY); lineY += lineHeight + 15; 
        
        g.drawString("P : Pausar/Continuar", contentXStart, lineY); lineY += lineHeight;
        g.drawString("N : Novo jogo", contentXStart, lineY); lineY += lineHeight;
        g.drawString("I : Mostrar/Esconder instruções", contentXStart, lineY); lineY += lineHeight;
        g.drawString("M : Voltar ao Menu Inicial", contentXStart, lineY); lineY += lineHeight + 20;
        
        g.setFont(new Font("Courier New", Font.BOLD, 18));
        g.drawString("PONTUAÇÃO:", contentXStart, lineY);
        lineY += lineHeight + 5;
        
        g.setFont(new Font("Courier New", Font.PLAIN, 16));
        g.drawString("1 linha : 100 pontos", contentXStart, lineY); lineY += lineHeight;
        g.drawString("2 linhas : 300 pontos", contentXStart, lineY); lineY += lineHeight;
        g.drawString("3 linhas : 500 pontos", contentXStart, lineY); lineY += lineHeight;
        g.drawString("4 linhas : 800 pontos (TETRIS!)", contentXStart, lineY); lineY += lineHeight + 20;
        
        g.setColor(yellowRetro);
        g.setFont(new Font("Courier New", Font.BOLD, 18));
        String closeText = "Pressione I para voltar ao jogo";
        
        FontMetrics fmClose = g.getFontMetrics();
        int closeX = (viewWidth - fmClose.stringWidth(closeText)) / 2;
        
        g.drawString(closeText, closeX, viewHeight - (fmClose.getHeight() * 2));
    }
}