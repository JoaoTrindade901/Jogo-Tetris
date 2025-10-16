package model;

import java.io.*;


 // Gerencia o recorde do jogo Tetris, carregando e salvando de um arquivo.

public class HighScoreManager {
    // O arquivo highscore.dat será criado na mesma pasta onde o JAR for executado.
    private static final String HIGH_SCORE_FILE = "highscore.dat";
    private int highScore = 0;

    public HighScoreManager() {
        loadHighScore();
    }

    public int getHighScore() {
        return highScore;
    }

    //Tenta carregar o recorde de um arquivo.
    private void loadHighScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader(HIGH_SCORE_FILE))) {
            String line = reader.readLine();
            if (line != null) {
                highScore = Integer.parseInt(line.trim());
            }
        } catch (FileNotFoundException e) {
            // Arquivo não existe, highScore permanece 0
            System.out.println("Arquivo de recorde não encontrado. Iniciando com 0.");
            highScore = 0;
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao carregar o recorde: " + e.getMessage());
            highScore = 0;
        }
    }

    /**
     * Verifica se a pontuação atual é um novo recorde e o salva se for.
     * @param newScore A pontuação a ser verificada.
     * @return true se um novo recorde foi estabelecido e salvo, false caso contrário.
     */
    public boolean updateAndSaveHighScore(int newScore) {
        if (newScore > highScore) {
            highScore = newScore;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(HIGH_SCORE_FILE))) {
                writer.write(String.valueOf(highScore));
                System.out.println("Novo recorde salvo: " + highScore);
                return true;
            } catch (IOException e) {
                System.err.println("Erro ao salvar o recorde: " + e.getMessage());
                return false;
            }
        }
        return false;
    }
}