package model;

import java.util.Random;

public class Piece {
    private Tetrominoes pieceShape;
    private int coords[][];

    public Piece() {
        coords = new int[4][2];
        setShape(Tetrominoes.NoShape);
    }
    
    // Construtor: Permite criar uma peça com uma forma específica 
    public Piece(Tetrominoes shape) { 
        coords = new int[4][2];
        setShape(shape);
    }

    public void setShape(Tetrominoes shape) {
        // Coordenadas das peças
        int[][][] coordsTable = new int[][][]{
            {{0,0},{0,0},{0,0},{0,0}},            // 0: NoShape
            {{0,-1},{0,0},{-1,0},{-1,1}},         // 1: ZShape
            {{0,-1},{0,0},{1,0},{1,1}},          // 2: SShape
            {{0,-1},{0,0},{0,1},{0,2}},          // 3: LineShape (Vertical I)
            {{-1,0},{0,0},{1,0},{0,1}},          // 4: TShape
            {{0,0},{1,0},{0,1},{1,1}},            // 5: SquareShape
            {{-1,-1},{0,-1},{0,0},{0,1}},         // 6: LShape
            {{1,-1},{0,-1},{0,0},{0,1}}           // 7: MirroredLShape
        };
        for(int i=0;i<4;i++){
            coords[i][0] = coordsTable[shape.ordinal()][i][0];
            coords[i][1] = coordsTable[shape.ordinal()][i][1];
        }
        pieceShape = shape;
    }

    public void setRandomShape() {
        setShape(Tetrominoes.getRandomShape());
    }

    public int x(int index){ return coords[index][0]; }
    public int y(int index){ return coords[index][1]; }
    public Tetrominoes getShape(){ return pieceShape; }

    public Piece rotateLeft() {
        if(pieceShape == Tetrominoes.SquareShape) return this;
        Piece result = new Piece();
        result.pieceShape = pieceShape;
        for(int i=0;i<4;i++){
            // Rotação anti-horária: (x, y) -> (y, -x)
            result.coords[i][0] = y(i);
            result.coords[i][1] = -x(i);
        }
        return result;
    }

    public Piece rotateRight() {
        if(pieceShape == Tetrominoes.SquareShape) return this;
        Piece result = new Piece();
        result.pieceShape = pieceShape;
        for(int i=0;i<4;i++){
            // Rotação horária: (x, y) -> (-y, x)
            result.coords[i][0] = -y(i);
            result.coords[i][1] = x(i);
        }
        return result;
    }

    public int minY() {
        int min = coords[0][1];
        for (int i = 1; i < 4; i++) min = Math.min(min, coords[i][1]);
        return min;
    }
}