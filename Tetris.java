import javax.swing.*;
import java.awt.*;
import java.util.Random;


public class Tetris {
    public static void main(String[] args) {
        int height=1000;
        int width=500;

        JFrame frame = new JFrame("Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width,height);
        frame.setLocationRelativeTo(null);

        TetrisGrid gridPanel = new TetrisGrid(width,height);
        frame.add(gridPanel);

        frame.setVisible(true);
        frame.setResizable(false);
        frame.pack();
    }

    public  static class TetrisGrid extends JPanel{

        private int[][] iShape={{0,0},{1,0},{2,0},{3,0}};//long I shape
        private int[][] zShape={{0,0},{1,0},{1,1},{2,1}};//reversed S shape
        private int[][] tShape={{0,0},{0,1},{0,2},{1,1}};
        private int[][] lShape={{0,0},{1,0},{2,0},{2,1}};
        private int[][] jShape={{0,0},{1,0},{2,0},{0,1}};//reverse L shape
        private int[][] sShape={{1,0},{2,0},{0,1},{1,1}};
        private int[][] oShape={{0,0},{1,0},{0,1},{1,1}};
        private int[][] currentPiece;

        private int pieceX=4;
        private int pieceY=0;

        private String color;

        public TetrisGrid(int width,int height){
            setPreferredSize(new Dimension(width, height));
            setBackground(Color.BLACK);
            spawnPiece();
        }

        private void spawnPiece(){
            Random rand=new Random();
            int pieceIndex=rand.nextInt(7);

            switch (pieceIndex) {
                case 0:
                    currentPiece=iShape;
                    color="BLUE";
                    break;
                case 1:
                    currentPiece=jShape;
                    color="PINK";
                    break;
                case 2:
                    currentPiece=lShape;
                    color="ORANGE";
                    break;
                case 3:
                    currentPiece=oShape;
                    color="YELLOW";
                    break;
                case 4:
                    currentPiece=tShape;
                    color="PURPLE";
                    break;
                case 5:
                    currentPiece=sShape;
                    color="RED";
                    break;
                case 6:
                    currentPiece=zShape;
                    color="GREEN";
                    break;
                
            }
        }

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            int cellSize=50;
            for(int row=0;row<20;row++){
                for(int col=0;col<10;col++){
                    g.drawRect(col*cellSize, row*cellSize, cellSize, cellSize);
                }
            }

            switch (color) {
                case "BLUE":
                    g.setColor(Color.BLUE);
                    break;
                case "PINK":
                    g.setColor(Color.PINK);
                    break;
                case "ORANGE":
                    g.setColor(Color.ORANGE);
                    break;
                case "YELLOW":
                    g.setColor(Color.YELLOW);
                    break;
                case "PURPLE":
                    g.setColor(new Color(128, 0, 128)); // No predefined Color constant for purple
                    break;
                case "RED":
                    g.setColor(Color.RED);
                    break;
                case "GREEN":
                    g.setColor(Color.GREEN);
                    break;
            }
            for(int[] block : currentPiece){
                g.fillRect((pieceX+block[0])*cellSize, (pieceY+block[1])*cellSize, cellSize, cellSize);
            }
        }


        
    }
}
