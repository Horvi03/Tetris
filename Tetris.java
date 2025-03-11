import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Tetris {
    public static void main(String[] args) {
        int height=1000;
        int width=501;

        JFrame frame = new JFrame("Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width,height);
        frame.setLocationRelativeTo(null);

        ScorePanel scorePanel=new ScorePanel();
        TetrisGrid gridPanel = new TetrisGrid(width,height,scorePanel);

        frame.setLayout(new BorderLayout());
        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(scorePanel, BorderLayout.EAST);

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
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

        private int score = 0;
        private ScorePanel scorePanel;

        public TetrisGrid(int width,int height,ScorePanel scorePanel){
            this.scorePanel=scorePanel;
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

            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
            getActionMap().put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                movePieceDown();
            }
            });

            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("Y"), "rotateRight");
            getActionMap().put("rotateRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotatePieceRight();
            }
            });

            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("X"), "rotateLeft");
            getActionMap().put("rotateLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotatePieceLeft();
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

            /*if(backgroundImage!=null){
            g.drawImage(backgroundImage,0,0,getWidth()-200,getHeight(),this);
            }*/

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

        private boolean hasLanded() {
            for (int[] block : currentPiece) {
                int blockY = pieceY + block[1];
                int blockX = pieceX + block[0];
                if (blockY >= 19 || (blockY >= 0 && blockY+1<20&&grid[blockY+1][blockX]!=null)) {
                    for (int[] b : currentPiece) {
                        int gridY = pieceY + b[1];
                        int gridX = pieceX + b[0];
                        // adding landed blocks to grid array
                        grid[gridY][gridX] = new Color(color.getRGB());
                    }
                    checkAndRemoveFullLines(); // Check and remove full lines
                    return true;
                }
            }
            return false;
        }



        private void movePieceLeft() {
            if (canMove(pieceX - 1, pieceY,currentPiece)) {
                pieceX--;  // Move piece to the left
            }
            repaint(); // Refresh the screen
        }
        
        // Move piece to the right
        private void movePieceRight() {
            if (canMove(pieceX + 1, pieceY,currentPiece)) {
                pieceX++;  // Move piece to the right
            }
            repaint(); // Refresh the screen
        }

        private void movePieceDown(){
            if(canMove(pieceX ,pieceY+1,currentPiece)){
                pieceY++;
            }
            repaint();
        }

        private void rotatePieceRight() {
            int[][] rotatedPiece = new int[currentPiece.length][2];
    
            for (int i = 0; i < currentPiece.length; i++) {
                rotatedPiece[i][0] = -currentPiece[i][1];
                rotatedPiece[i][1] = currentPiece[i][0];
            }
            
            if (canMove(pieceX, pieceY, rotatedPiece)) {
                currentPiece = rotatedPiece;
            }
            repaint();
        }
        private void rotatePieceLeft() {
            int[][] rotatedPiece = new int[currentPiece.length][2];
    
            for (int i = 0; i < currentPiece.length; i++) {
                rotatedPiece[i][0] = currentPiece[i][1];
                rotatedPiece[i][1] = -currentPiece[i][0];
            }
            
            if (canMove(pieceX, pieceY, rotatedPiece)) {
                currentPiece = rotatedPiece;
            }
            repaint();
        }
        
        private boolean canMove(int newX, int newY,int[][] piece) {
            for (int[] block : piece) {
                int blockX = newX + block[0];
                int blockY = newY + block[1];
        
                if (blockX < 0 || blockX >= 10 || blockY >= 20) {
                    return false;
                }
        
                if (blockY >= 0 && grid[blockY][blockX] != null) {
                    return false; 
                }
            }
            return true;
        }

        private void checkAndRemoveFullLines() {
            int linesRemoved=0;
            for (int row = 0; row < grid.length; row++) {
                boolean isFull = true;
                for (int col = 0; col < grid[row].length; col++) {
                    if (grid[row][col] == null) {
                        isFull = false;
                        break;
                    }
                }
                if (isFull) {
                    removeLine(row);
                    linesRemoved++;
                }
            }
            if(linesRemoved>0){
                applyBonus(linesRemoved);
            }
        }

        private void applyBonus(int linesRemoved) {
            int bonus = 0;
            switch (linesRemoved) {
                case 2:
                    bonus = 200; // Bonus for 2 lines
                    break;
                case 3:
                    bonus = 400; // Bonus for 3 lines
                    break;
                case 4:
                    bonus = 600; // Bonus for 4 lines
                    break;
            }
            score += bonus;
            scorePanel.updateScore(score);
        }
        
        private void removeLine(int row) {
            Timer delayTimer = new Timer(200, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (int r = row; r > 0; r--) {
                        for (int col = 0; col < grid[r].length; col++) {
                            grid[r][col] = grid[r - 1][col];
                        }
                    }
                    for (int col = 0; col < grid[0].length; col++) {
                        grid[0][col] = null;
                    }
                    score += 100; // Increase score by 100 for each line removed
                    scorePanel.updateScore(score);
                    repaint();
                }
            });
            delayTimer.setRepeats(false); // Ensure the timer only runs once
            delayTimer.start();
        }
    }

    public static class ScorePanel extends JPanel {
        private JLabel scoreLabel;
        private int score;

        public ScorePanel() {
            setPreferredSize(new Dimension(300, 1000));
            setBackground(Color.BLACK);
            scoreLabel = new JLabel("Score:");
            scoreLabel.setForeground(Color.WHITE);
            scoreLabel.setFont(new Font("Comic Sans", Font.BOLD, 30));
            add(scoreLabel);
        }

        public void updateScore(int newScore) {
            score = newScore;
            scoreLabel.setText("Score: " + score);
            //scoreLabel.setText("Lines: "+ score/100);
        }
    }
}
