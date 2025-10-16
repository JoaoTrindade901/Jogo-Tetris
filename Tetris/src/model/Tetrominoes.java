package model;

import java.util.Random;

public enum Tetrominoes {
    NoShape, ZShape, SShape, LineShape, TShape, SquareShape, LShape, MirroredLShape;

    /**
     * Retorna uma forma Tetrominoes aleatória, excluindo a NoShape (índice 0).
     */
    public static Tetrominoes getRandomShape() {
        // Obter todos os valores da enumeração
        Tetrominoes[] values = Tetrominoes.values();
        
        // Excluir NoShape (índice 0). Gerar um número entre 1 e values.length - 1
        int index = new Random().nextInt(values.length - 1) + 1; 
        
        return values[index];
    }
}