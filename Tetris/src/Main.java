import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import model.Board;
import view.TetrisView;
import controller.TetrisController;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Board model = new Board();
            TetrisView view = new TetrisView(model);
            
            // O Controller é criado. Ele define o estado inicial como MENU na View.
            TetrisController controller = new TetrisController(model, view);

            JFrame frame = new JFrame("TETRIS RETRO");
            
            frame.add(view);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        
            view.requestFocusInWindow(); 
        });
    }
}