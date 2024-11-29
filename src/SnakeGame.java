import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;


public class SnakeGame extends JPanel implements ActionListener, KeyListener {



    private final int TILE_SIZE = 25;
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final int MAX_TILES = (WIDTH * HEIGHT) / (TILE_SIZE * TILE_SIZE);
    public int score;

    private final int[] snakeX = new int[MAX_TILES];
    private final int[] snakeY = new int[MAX_TILES];

    private int snakeLength = 3;
    private int foodX, foodY;
    private char direction = 'R';  
    private boolean gameStarted = false;
    private boolean gamePaused = false;

    private boolean running = false;
    private Timer timer;

    private final HashSet<String> usedQuestions = new HashSet<>();
    private final Random random = new Random();

   

    public SnakeGame() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        initializeGame();
    }

    private void initializeGame() {
        for (int i = 0; i < snakeLength; i++) {
            snakeX[i] = 100 - (i * TILE_SIZE);
            snakeY[i] = 100;
        }
        spawnFood();
        running = true;
        timer = new Timer(100, this);
    }

    private void spawnFood() {
        foodX = random.nextInt(WIDTH / TILE_SIZE) * TILE_SIZE;
        foodY = random.nextInt(HEIGHT / TILE_SIZE) * TILE_SIZE;
    }

    private void move() {
        for (int i = snakeLength; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        switch (direction) {
            case 'U':
                snakeY[0] -= TILE_SIZE;
                break;
            case 'D':
                snakeY[0] += TILE_SIZE;
                break;
            case 'L':
                snakeX[0] -= TILE_SIZE;
                break;
            case 'R':
                snakeX[0] += TILE_SIZE;
                break;}
        }

    private void checkCollision() {
        
        if (snakeX[0] < 0 || snakeX[0] >= WIDTH || snakeY[0] < 0 || snakeY[0] >= HEIGHT) {
            running = false;
        }

       
        for (int i = 1; i < snakeLength; i++) {
            if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                running = false;
                break;
            }
        }

        
        if (snakeX[0] == foodX && snakeY[0] == foodY) {
            if (askMathQuestion()) {
                snakeLength++;
            }
            spawnFood();
            gamePaused = true;
        }
        if (snakeX[0] == foodX && snakeY[0] == foodY) {
            if (askMathQuestion()) {
                snakeLength++;
                score++; 
            }
            spawnFood();
            gamePaused = true;
        }
    }

    private boolean askMathQuestion() {
        String question = generateMathQuestion();
        String answer = JOptionPane.showInputDialog(this, question, "Math Question", JOptionPane.QUESTION_MESSAGE);

        if (answer == null) return false;  
        try {
            boolean correct = Integer.parseInt(answer) == evaluateMathQuestion(question);
            JOptionPane.showMessageDialog(this, correct ? "Correct!" : "Wrong!", "Result", JOptionPane.INFORMATION_MESSAGE);
            return correct;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Wrong!", "Result", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
    }

    private String generateMathQuestion() {
        String question;
        do {
            int type = random.nextInt(5); 
            switch (type) {
                case 0:
                   
                    {
                        int a = random.nextInt(20) + 1;
                        int b = random.nextInt(20) + 1;
                        question = a + " + " + b;
                    }
                    break;
                case 1:
                    
                    {
                        int a = random.nextInt(20) + 1;
                        int b = random.nextInt(a); 
                        question = a + " - " + b;
                    }
                    break;
                case 2:
                    
                    {
                        int a = random.nextInt(12) + 1;
                        int b = random.nextInt(12) + 1;
                        question = a + " * " + b;
                    }
                    break;
                case 3:
                  
                    {
                        int b = random.nextInt(12) + 1;
                        int a = b * (random.nextInt(10) + 1); 
                        question = a + " / " + b;
                    }
                    break;
                case 4:
                    
                    {
                        int a = random.nextInt(5) + 1; 
                        int x = random.nextInt(10) + 1; 
                        int b = -(a * x); 
                        question = a + "x + " + b + " = 0; Solve for x";
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + type);
            
            

            }
        } while (usedQuestions.contains(question));
        
        usedQuestions.add(question);
        return question;
    }    
    
    private int evaluateMathQuestion(String question) {
        if (question.contains("+")) { 
            String[] parts = question.split(" \\+ ");
            return Integer.parseInt(parts[0]) + Integer.parseInt(parts[1]);
        } else if (question.contains("-")) { 
            String[] parts = question.split(" - ");
            return Integer.parseInt(parts[0]) - Integer.parseInt(parts[1]);
        } else if (question.contains("*")) { 
            String[] parts = question.split(" \\* ");
            return Integer.parseInt(parts[0]) * Integer.parseInt(parts[1]);
        } else if (question.contains("/")) { 
            String[] parts = question.split(" / ");
            return Integer.parseInt(parts[0]) / Integer.parseInt(parts[1]);
        } else if (question.contains("^")) { 
            String[] parts = question.split("\\^");
            int base = Integer.parseInt(parts[0]);
            int exp = Integer.parseInt(parts[1]);
            return (int) Math.pow(base, exp);
        } else if (question.contains("√")) { 
            int square = Integer.parseInt(question.replaceAll("[^0-9]", ""));
            return (int) Math.sqrt(square);
        } else if (question.contains("x")) { 
          
            question = question.replace(" = 0", "");  
            
            question = question.replace("x", ""); 
            
           
            String[] parts = question.split("(?=[-+])");
            int a = Integer.parseInt(parts[0].trim());
            int b = Integer.parseInt(parts[1].trim());
            
           
            return -b / a;
        } else {
            throw new IllegalArgumentException("Unexpected question format: " + question);
        }
    }    
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    

        if (running) {
            
            g.setColor(Color.RED);
            g.fillRect(foodX, foodY, TILE_SIZE, TILE_SIZE);
    
            
            g.setColor(Color.GREEN);
            for (int i = 0; i < snakeLength; i++) {
                g.fillRect(snakeX[i], snakeY[i], TILE_SIZE, TILE_SIZE);
            }
    
            
            g.setColor(Color.YELLOW); 
            g.setFont(new Font("Arial", Font.ITALIC, 50)); 
            g.drawString("Score: " + score, WIDTH/2, HEIGHT/2); 
        } else {
            
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.ITALIC, 50));
            FontMetrics metrics = getFontMetrics(g.getFont());
            String message = "Game Over, Score " + score;
            g.drawString(message, (WIDTH - metrics.stringWidth(message)) / 2, HEIGHT / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running && gameStarted && !gamePaused) {
            move();
            checkCollision();
        }
        repaint(); 
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (!gameStarted) {
            gameStarted = true;
            timer.start();
        }

        if (gamePaused) {
            gamePaused = false;  
        }

        switch (key) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                if (direction != 'D') direction = 'U';
                break;
        
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                if (direction != 'U') direction = 'D';
                break;
        
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                if (direction != 'R') direction = 'L';
                break;
        
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') direction = 'R';
                break;
        }
        
        }
    

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame gamePanel = new SnakeGame();
    

    
        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}