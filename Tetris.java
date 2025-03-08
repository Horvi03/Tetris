import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


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

        private Color color;

        private Timer timer;
    
        private Color[][] grid = new Color[20][10]; // 20 rows, 10 columns

        public TetrisGrid(int width,int height){
            setPreferredSize(new Dimension(width, height));
            setBackground(Color.BLACK);
            spawnPiece();
            
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
            getActionMap().put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                movePieceLeft();
            }
            });

            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
            getActionMap().put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                movePieceRight();
            }
            });

            timer = new Timer(500, new ActionListener() { // Runs every 500ms
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(!hasLanded()){
                        pieceY++;
                    }else{
                        spawnPiece();
                    }
                    repaint(); // Refresh the screen
                }
            });
            timer.start();
        }

        private void spawnPiece(){
            Random rand=new Random();
            int pieceIndex=rand.nextInt(7);
            
            switch (pieceIndex) {
                case 0:
                    currentPiece=iShape;
                    color=Color.BLUE;
                    break;
                case 1:
                    currentPiece=jShape;
                    color=Color.PINK;
                    break;
                case 2:
                    currentPiece=lShape;
                    color=Color.ORANGE;
                    break;
                case 3:
                    currentPiece=oShape;
                    color=Color.YELLOW;
                    break;
                case 4:
                    currentPiece=tShape;
                    color=new Color(128,0,128);
                    break;
                case 5:
                    currentPiece=sShape;
                    color=Color.RED;
                    break;
                case 6:
                    currentPiece=zShape;
                    color=Color.GREEN;
                    break;
                
            }
            pieceY=0;
            pieceX=4;
        }

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            int cellSize=50;
            for(int row=0;row<20;row++){
                for(int col=0;col<10;col++){
                    if(grid[row][col]!=null){
                        g.setColor(grid[row][col]);
                        g.fillRect(col*cellSize, row*cellSize, cellSize, cellSize);
                    }
                    g.setColor(Color.WHITE);
                    g.drawRect(col*cellSize, row*cellSize, cellSize, cellSize);
                }
            }

            g.setColor(color);
            for(int[] block : currentPiece){
                g.fillRect((pieceX+block[0])*cellSize, (pieceY+block[1])*cellSize, cellSize, cellSize);
            }
        }

        private boolean hasLanded(){
            for(int[] block : currentPiece){
                int blockY=pieceY+block[1];
                int blockX=pieceX+block[0];
                if(blockY>=19|| grid[blockY+1][blockX]!=null){
                    for(int[]b : currentPiece){
                        int gridY = pieceY + b[1];
                        int gridX = pieceX + b[0];
                        grid[gridY][gridX] = new Color(color.getRGB());
                    }
                    return true;
                }
            }
            return false;
        }

        private void movePieceLeft() {
            if (canMove(pieceX - 1, pieceY)) {
                pieceX--;  // Move piece to the left
            }
            repaint(); // Refresh the screen
        }
        
        // Move piece to the right
        private void movePieceRight() {
            if (canMove(pieceX + 1, pieceY)) {
                pieceX++;  // Move piece to the right
            }
            repaint(); // Refresh the screen
        }
        
        private boolean canMove(int newX, int newY) {
            for (int[] block : currentPiece) {
                int blockX = newX + block[0];
                int blockY = newY + block[1];
        
                // Check if the block is within bounds
                if (blockX < 0 || blockX >= 10 || blockY >= 20) {
                    return false; // Out of bounds
                }
        
                // Check if the block collides with an already landed block
                if (blockY >= 0 && grid[blockY][blockX] != null) {
                    return false; // Collision with existing block
                }
            }
            return true;
        }
    }
}
