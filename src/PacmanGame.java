import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PacmanGame extends JFrame implements ActionListener, KeyListener {
    private static final ImageIcon VERTICAL_WALL = new ImageIcon("src/images/verticalWall.png");
    private static final ImageIcon HORIZONTAL_WALL = new ImageIcon("src/images/horizontalWall.png");
    private static final ImageIcon TOP_LEFT_CORNER = new ImageIcon("src/images/leftUpCorner.png");
    private static final ImageIcon TOP_RIGHT_CORNER = new ImageIcon("src/images/rightUpCorner.png");
    private static final ImageIcon BOTTOM_LEFT_CORNER = new ImageIcon("src/images/bottomRightCorner.png");
    private static final ImageIcon BOTTOM_RIGHT_CORNER = new ImageIcon("src/images/rightBottomCorner.png");
    private static final ImageIcon FOOD = new ImageIcon("src/images/food.png");
    private static final ImageIcon BLOCKS = new ImageIcon("src/images/block.png");
    private static final ImageIcon GHOSTS = new ImageIcon("src/images/ghost.png");
    private static final ImageIcon PACMANRIGHT = new ImageIcon("src/images/pacman.png");
    private static final ImageIcon PACMANLEFT = new ImageIcon("src/images/pacmanleft.png");
    private static final ImageIcon PACMANUP = new ImageIcon("src/images/pacmanup.png");
    private static final ImageIcon PACMANDOWN = new ImageIcon("src/images/pacmandown.png");
    private static final ImageIcon PACMAN0 = new ImageIcon("src/images/pacman0.png");
    private static final ImageIcon BONUS = new ImageIcon("src/images/bonus.png");
    private static final ImageIcon image = new ImageIcon("src/images/mongratulations-congratulations.gif");
    static JFrame gameWindow = new JFrame("Pacman Game");
    private final DefaultListModel<GameRecord> listModel;
    private final JLabel counterLabel;
    private final ImageIcon imageIcon1 = new ImageIcon("src/images/heart.png");
    private final ImageIcon imageIcon2 = new ImageIcon("src/images/2hearts.png");
    private final ImageIcon imageIcon3 = new ImageIcon("src/images/3hearts.png");
    private final boolean isMoving = true;
    JPanel panel = new JPanel(new BorderLayout());
    JFrame congrats;
    JPanel contentPane;
    JFrame scoreWindow;
    JTextField nameField;
    JPanel namePanel;
    JLabel RESULT;
    JLabel label = new JLabel(image);
    int[][] matrix = MatrixGenerator.generateMatrix();
    int[][] foodMatrix = new int[matrix.length][matrix[0].length];
    int delay = 5000;
    JTable table = new JTable(new PacmanTableModel(25, 25));
    JFrame nameWindow;
    int dies = 3;
    boolean openMouth = false;
    private JPanel scorePanel;
    private int keyCode;
    private int counter = 0;
    private Thread pacThread;
    private boolean menuOpened = true;
    private Thread ghostThread;
    private List<GameRecord> highScores;


    public PacmanGame() {
        highScores = new ArrayList<>();
        listModel = new DefaultListModel<>();
        if (congrats == null) {
            congrats = new JFrame();
        }
        congrats.setUndecorated(true);
        congrats.setExtendedState(JFrame.MAXIMIZED_BOTH);
        congrats.add(label);
        congrats.setVisible(false);

        setTitle("Pacman Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton newGameButton = createStyledButton("Start new game");


        counterLabel = new JLabel("Counter: " + counter);
        counterLabel.setFont(new Font("Arial", Font.BOLD, 24));
        counterLabel.setBackground(Color.WHITE);
        counterLabel.setHorizontalAlignment(SwingConstants.LEFT);


        newGameButton.addActionListener(this);
        newGameButton.setPreferredSize(new Dimension(200, 100));

        JButton highScoresButton = createStyledButton("High scores");
        highScoresButton.addActionListener(this);
        highScoresButton.setPreferredSize(new Dimension(200, 100));

        JButton exitButton = createStyledButton("Exit");
        exitButton.addActionListener(this);
        exitButton.setPreferredSize(new Dimension(200, 100));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50));
        buttonPanel.add(newGameButton);
        buttonPanel.add(highScoresButton);
        buttonPanel.add(exitButton);

        contentPane = new JPanel(new BorderLayout());
        contentPane.add(buttonPanel, BorderLayout.CENTER);

        setContentPane(contentPane);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        table.addKeyListener(this);

        KeyListener exitShortcutListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_Q) {
                    gameWindow.dispose();
                    getContentPane().setVisible(true);
                    for (Frame frame : Frame.getFrames()) {
                        if (frame instanceof JFrame && frame != gameWindow && frame != nameWindow && frame != scoreWindow && frame != congrats) {
                            frame.setVisible(true);
                        }
                    }
                    startNewGame();
                }
            }
        };
        table.addKeyListener(exitShortcutListener);
    }

        private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 100));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.BLACK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.WHITE);
            }
        });

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });

        button.setUI(new StyledButtonUI());

        return button;
    }

    public static void main(String[] args) {
        PacmanGame pc = new PacmanGame();
        pc.movePac();
        pc.moveGhost();

    }

    private void increaseCounter() {
        counter++;
        counterLabel.setText("Counter: " + counter);
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public void updateTable() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                switch (matrix[i][j]) {
                    case 1 -> table.setValueAt(TOP_LEFT_CORNER, i, j);
                    case 2 -> table.setValueAt(BOTTOM_LEFT_CORNER, i, j);
                    case 3 -> table.setValueAt(TOP_RIGHT_CORNER, i, j);
                    case 4 -> table.setValueAt(BOTTOM_RIGHT_CORNER, i, j);
                    case 5 -> table.setValueAt(GHOSTS, i, j);
                    case 6 -> table.setValueAt(BLOCKS, i, j);
                    case 7 -> table.setValueAt(FOOD, i, j);
                    case 8 -> table.setValueAt(PACMANRIGHT, i, j);
                    case 9 -> table.setValueAt(VERTICAL_WALL, i, j);
                    case 10 -> table.setValueAt(HORIZONTAL_WALL, i, j);
                    case 11 -> table.setValueAt(PACMANLEFT, i, j);
                    case 12 -> table.setValueAt(PACMANUP, i, j);
                    case 13 -> table.setValueAt(PACMANDOWN, i, j);
                    case 14 -> table.setValueAt(PACMAN0, i, j);
                    case 16 -> table.setValueAt(BONUS, i, j);

                    default -> table.setValueAt(null, i, j);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("Start new game")) {
            counter = 0;

            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel label = new JLabel();
                    if (value instanceof ImageIcon) {
                        label.setIcon((ImageIcon) value);
                        label.setHorizontalAlignment(JLabel.CENTER);
                        label.setVerticalAlignment(JLabel.CENTER);
                    }
                    return label;
                }
            };

            table.setDefaultRenderer(Object.class, renderer);
            table.setRowHeight(30);
            table.setBackground(Color.BLACK);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setPreferredWidth(30);
            }

            panel.add(table, BorderLayout.CENTER);
            panel.add(counterLabel, BorderLayout.SOUTH);
            gameWindow.getContentPane().add(panel, BorderLayout.CENTER);
            gameWindow.setResizable(false);

            gameWindow.pack();

            getContentPane().setVisible(false);
            for (Frame frame : Frame.getFrames()) {
                if (frame instanceof JFrame) {
                    frame.dispose();
                }
            }
            this.setVisible(false);
            gameWindow.setVisible(true);


        } else if (e.getActionCommand().equals("High scores")) {
            loadHighScoresFromFile();

            showHighScoresWindow();

        } else if (e.getActionCommand().equals("Exit")) {
            System.exit(0);
        }
    }
    private void loadHighScoresFromFile() {
        try {
            File file = new File("records.txt");
            if (file.exists()) {
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                highScores = (List<GameRecord>) in.readObject();
                in.close();
                fileIn.close();
                System.out.println("High scores loaded from file.");
            } else {
                highScores = new ArrayList<>();
                System.out.println("No high scores found. Starting with empty list.");
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private void showHighScoresWindow() {
        JFrame scoreWindow = new JFrame("High Scores");
        scoreWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JList<String> scoreList = new JList<>();

        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (GameRecord record : highScores) {
            String line = record.getPlayerName() + " - " + record.getScore() + " points (" + record.getTimestamp() + ")";
            listModel.addElement(line);
        }

        scoreList.setModel(listModel);

        DefaultListCellRenderer renderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                Font font = new Font("Arial", Font.PLAIN, 17);
                setFont(font);

                setHorizontalAlignment(SwingConstants.CENTER);

                return component;
            }
        };

        scoreList.setCellRenderer(renderer);

        JScrollPane scrollPane = new JScrollPane(scoreList);
        scoreWindow.add(scrollPane);

        scoreWindow.setSize(400, 300);
        scoreWindow.setLocationRelativeTo(null);
        scoreWindow.setVisible(true);
    }



    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        this.keyCode = e.getKeyCode();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void openNewWindow() {
        if (nameWindow == null) {
            nameWindow = new JFrame("Enter your name");
        }

        gameWindow.getContentPane().removeAll();
        nameWindow.setPreferredSize(new Dimension(800, 800));
        nameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        namePanel = new JPanel();
        nameField = new JTextField(20);
        namePanel.add(new JLabel("Enter your name:"));
        namePanel.add(nameField);
        RESULT = new JLabel("Counter: " + counter);
        namePanel.add(RESULT);
        JButton exitToMenuButton = new JButton("Exit to menu");
        nameWindow.getContentPane().add(namePanel, BorderLayout.CENTER);
        nameWindow.add(exitToMenuButton, BorderLayout.SOUTH);

        nameWindow.pack();
        nameWindow.setLocationRelativeTo(null);
        nameWindow.setVisible(true);

        exitToMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerName = nameField.getText();
                if (!playerName.isEmpty()) {
                    nameWindow.dispose();
                    namePanel.setVisible(false);
                    namePanel.setOpaque(false);
                    namePanel.setEnabled(false);
                    getContentPane().setVisible(true);
                    for (Frame frame : Frame.getFrames()) {
                        if (frame instanceof JFrame && frame != gameWindow && frame != nameWindow && frame != scoreWindow && frame != congrats) {
                            frame.setVisible(true);
                        }
                    }

                    GameRecord record = new GameRecord(playerName, getCounter(), LocalDateTime.now());

                    highScores.add(record);

                    saveHighScoresToFile();
                } else {
                    JOptionPane.showMessageDialog(nameWindow, "Please enter your name");
                }
            }
        });
    }
    private void saveHighScoresToFile() {
        try {
            File file = new File("records.txt");
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(highScores);
            out.close();
            fileOut.close();
            System.out.println("High scores saved to file.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void createFoodMatrix() {
        for (int i = 0; i < foodMatrix.length; i++) {
            for (int j = 0; j < foodMatrix[i].length; j++) {
                if (matrix[i][j] == 5 || matrix[i][j] == 8 || matrix[i][j] == 11 || matrix[i][j] == 12 || matrix[i][j] == 13 || matrix[i][j] == 14) {
                    foodMatrix[i][j] = 7;
                } else {
                    foodMatrix[i][j] = matrix[i][j];
                }
            }
        }
    }

    public void startNewGame() {
        matrix = MatrixGenerator.generateMatrix();
        createFoodMatrix();
    }

    public boolean winGame() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 7) {
                    return false;
                }
            }
        }
        return true;
    }

    public void movePac() {
        pacThread = new Thread() {

            @Override
            public void run() {
                while (isMoving) {
                    try {
                        Thread.sleep(250);
                        updateTable();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (winGame()) {
                        keyCode = KeyEvent.VK_D;
                        menuOpened = true;
                        counterLabel.setIcon(null);
                        if (menuOpened) {
                            gameWindow.setVisible(false);
                            dies = 3;
                            startNewGame();
                            openNewWindow();
                            menuOpened = false;
                        }
                        congrats.setVisible(true);
                        Timer timer = new Timer(delay, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                congrats.dispose();
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();

                    }
                    boolean atePoint = false;
                    int currentRow = -1;
                    int currentCol = -1;

                    for (int i = 0; i < matrix.length; i++) {
                        for (int j = 0; j < matrix[0].length; j++) {
                            if (matrix[i][j] == 8 || matrix[i][j] == 11 || matrix[i][j] == 12 || matrix[i][j] == 13 || matrix[i][j] == 14) {
                                currentRow = i;
                                currentCol = j;
                                break;
                            }
                        }
                    }


                    if (dies == 3) {
                        counterLabel.setIcon(imageIcon3);

                    } else if (dies == 2) {
                        counterLabel.setIcon(imageIcon2);
                    } else if (dies == 1) {
                        counterLabel.setIcon(imageIcon1);
                    } else if (dies == 0) {
                        keyCode = KeyEvent.VK_D;
                        menuOpened = true;
                        counterLabel.setIcon(null);
                        if (menuOpened) {
                            gameWindow.setVisible(false);
                            dies = 3;
                            startNewGame();
                            openNewWindow();
                            menuOpened = false;
                        }
                    }
                    if (keyCode == KeyEvent.VK_LEFT) {
                        if (currentCol > 0 && matrix[currentRow][currentCol - 1] != 6 && matrix[currentRow][currentCol - 1] != 9 && matrix[currentRow][currentCol - 1] != 5) {
                            matrix[currentRow][currentCol] = 0;
                            foodMatrix[currentRow][currentCol] = 0;
                            if (openMouth) {
                                openMouth = false;
                                matrix[currentRow][currentCol - 1] = 11;
                            } else if (!openMouth) {
                                openMouth = true;
                                matrix[currentRow][currentCol - 1] = 14;
                            }
                            if (foodMatrix[currentRow][currentCol - 1] == 7) {

                                atePoint = true;
                            }

                            if (atePoint) {
                                increaseCounter();
                            }

                        } else if (matrix[currentRow][currentCol - 1] == 5) {
                            matrix[currentRow][currentCol] = 0;
                            matrix[19][4] = 8;
                            keyCode = KeyEvent.VK_D;
                            dies--;
                        }else if(matrix[currentRow][currentCol - 1] == 16){
                            matrix[currentRow][currentCol] = 0;
                            matrix[currentRow][currentCol - 1] = 12;
                            matrix[currentRow][currentCol - 1] = 0;
                            counter+=2;
                        }

                        if (matrix[currentRow][currentCol - 1] == 16) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else if (keyCode == KeyEvent.VK_RIGHT) {

                        if (currentCol < matrix[0].length - 1 && matrix[currentRow][currentCol + 1] != 6 && matrix[currentRow][currentCol + 1] != 9 && matrix[currentRow][currentCol + 1] != 5) {
                            matrix[currentRow][currentCol] = 0;
                            foodMatrix[currentRow][currentCol] = 0;
                            if (foodMatrix[currentRow][currentCol + 1] == 7) {
                                atePoint = true;
                            }
                            if (atePoint) {
//                                counter++;

                                increaseCounter();
                            }
                            if (openMouth) {
                                openMouth = false;
                                matrix[currentRow][currentCol + 1] = 8;
                            } else if (!openMouth) {
                                openMouth = true;
                                matrix[currentRow][currentCol + 1] = 14;
                            }
                        } else if (matrix[currentRow][currentCol + 1] == 5) {
                            matrix[currentRow][currentCol] = 0;
                            matrix[19][4] = 8;
                            keyCode = KeyEvent.VK_D;
                            dies--;
                        }

                        if (matrix[currentRow][currentCol + 1] == 16) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else if (keyCode == KeyEvent.VK_UP) {

                        // Код для движения вверх
                        if (currentRow > 0 && matrix[currentRow - 1][currentCol] != 6 && matrix[currentRow - 1][currentCol] != 10 && matrix[currentRow - 1][currentCol] != 5 && matrix[currentRow - 1][currentCol] != 16) {
                            matrix[currentRow][currentCol] = 0;
                            foodMatrix[currentRow][currentCol] = 0;
                            if (foodMatrix[currentRow - 1][currentCol] == 7) {
                                atePoint = true;
                            }
                            if (atePoint) {
//                                counter++;
                                increaseCounter();
                            }
                            if (openMouth) {
                                openMouth = false;
                                matrix[currentRow - 1][currentCol] = 12;
                            } else if (!openMouth) {
                                openMouth = true;
                                matrix[currentRow - 1][currentCol] = 14;
                            }
                        } else if (matrix[currentRow - 1][currentCol] == 5) {
                            matrix[currentRow][currentCol] = 0;
                            matrix[19][4] = 8;
                            keyCode = KeyEvent.VK_D;
                            dies--;
                        }else if(matrix[currentRow - 1][currentCol] == 16){
                            matrix[currentRow][currentCol] = 0;
                            matrix[currentRow - 1][currentCol] = 12;
                            foodMatrix[currentRow - 1][currentCol] = 0;
                            counter+=2;
                        }
                        if (matrix[currentRow - 1][currentCol] == 16) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else if (keyCode == KeyEvent.VK_DOWN) {

                        if (currentRow < matrix.length - 1 && matrix[currentRow + 1][currentCol] != 6 && matrix[currentRow + 1][currentCol] != 10 && matrix[currentRow + 1][currentCol] != 5) {
                            matrix[currentRow][currentCol] = 0;
                            foodMatrix[currentRow][currentCol] = 0;
                            if (foodMatrix[currentRow + 1][currentCol] == 7) {
                                atePoint = true;
                            }
                            if (atePoint) {
//                                counter++;
                                increaseCounter();
                            }
                            if (openMouth) {
                                openMouth = false;
                                matrix[currentRow + 1][currentCol] = 13;
                            } else if (!openMouth) {
                                openMouth = true;
                                matrix[currentRow + 1][currentCol] = 14;
                            }
                        } else if (matrix[currentRow + 1][currentCol] == 5) {
                            matrix[currentRow][currentCol] = 0;
                            matrix[19][4] = 8;
                            keyCode = KeyEvent.VK_D;
                            dies--;
                        }else if(matrix[currentRow + 1][currentCol] == 16){
                            matrix[currentRow][currentCol] = 0;
                            matrix[currentRow + 1][currentCol] = 12;
                            matrix[currentRow + 1][currentCol] = 0;

                        }
                        if (matrix[currentRow + 1][currentCol] == 16) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }


                }
            }


        };
        pacThread.start();

    }

    public void moveGhost() {
        createFoodMatrix();

        ghostThread = new Thread() {
            @Override
            public void run() {
                int bonusDelay = 0;
                while (isMoving) {
                    try {
                        Thread.sleep(250);
                        bonusDelay += 250;

                        if (bonusDelay >= 5000) {
                            placeBonus();
                            bonusDelay = 0; // Сбрасываем задержку после размещения бонуса
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }


                    List<int[]> ghostCoords = new ArrayList<>();
                    for (int row = 0; row < matrix.length; row++) {
                        for (int col = 0; col < matrix[row].length; col++) {
                            if (matrix[row][col] == 5) {
                                ghostCoords.add(new int[]{row, col});
                            }
                        }
                    }

                    for (int[] ghostCoord : ghostCoords) {
                        int row = ghostCoord[0];
                        int col = ghostCoord[1];

                        int direction = (int) (Math.random() * 4);

                        if (direction == 0 && row > 0 && matrix[row - 1][col] != 6 && matrix[row - 1][col] != 10 && matrix[row - 1][col] != 5) { // Вверх
                            if (matrix[row - 1][col] == 8 || matrix[row - 1][col] == 11 || matrix[row - 1][col] == 12 || matrix[row - 1][col] == 13 || matrix[row - 1][col] == 14 || matrix[row - 1][col] == 15) {
                                matrix[row][col] = foodMatrix[row][col];
                                matrix[row - 1][col] = 5;
                                matrix[19][4] = 8;
                                dies--;
                                keyCode = KeyEvent.VK_D;
                            } else {
                                matrix[row - 1][col] = 5;
                                matrix[row][col] = foodMatrix[row][col];
                            }


//
                        } else if (direction == 1 && row < matrix.length - 1 && matrix[row + 1][col] != 6 && matrix[row + 1][col] != 10 && matrix[row + 1][col] != 5) { // Вниз
                            if (matrix[row + 1][col] == 8 || matrix[row + 1][col] == 11 || matrix[row + 1][col] == 12 || matrix[row + 1][col] == 13 || matrix[row + 1][col] == 14 || matrix[row + 1][col] == 15) {
                                matrix[row][col] = foodMatrix[row][col];
                                matrix[row + 1][col] = 5;
                                matrix[19][4] = 8;
                                dies--;
                                keyCode = KeyEvent.VK_D;
                            } else {
                                matrix[row + 1][col] = 5;
                                matrix[row][col] = foodMatrix[row][col];
                            }


                        } else if (direction == 2 && col > 0 && matrix[row][col - 1] != 6 && matrix[row][col - 1] != 9 && matrix[row][col - 1] != 5) { // Влево
                            if (matrix[row][col - 1] == 8 || matrix[row][col - 1] == 11 || matrix[row][col - 1] == 12 || matrix[row][col - 1] == 13 || matrix[row][col - 1] == 14 || matrix[row][col - 1] == 15) {
                                matrix[row][col] = foodMatrix[row][col];
                                matrix[row][col - 1] = 5;
                                matrix[19][4] = 8;
                                dies--;
                                keyCode = KeyEvent.VK_D;


                            } else {

                                matrix[row][col - 1] = 5;
                                matrix[row][col] = foodMatrix[row][col];
                            }


                        } else if (direction == 3 && col < matrix[row].length - 1 && matrix[row][col + 1] != 6 && matrix[row][col + 1] != 9 && matrix[row][col + 1] != 5) { // Вправо
                            if (matrix[row][col + 1] == 8 || matrix[row][col + 1] == 11 || matrix[row][col + 1] == 12 || matrix[row][col + 1] == 13 || matrix[row][col + 1] == 14 || matrix[row][col + 1] == 15) {
                                matrix[row][col] = foodMatrix[row][col];
                                matrix[row][col + 1] = 5;
                                matrix[19][4] = 8;
                                dies--;
                                keyCode = KeyEvent.VK_D;
                            } else {
                                matrix[row][col + 1] = 5;
                                matrix[row][col] = foodMatrix[row][col];
                            }


                        }
                    }


                }
            }

        };
        ghostThread.start();
    }
    private void placeBonus() {
        Random random = new Random();
        int row, col;
        do {
            row = random.nextInt(matrix.length);
            col = random.nextInt(matrix[0].length);
        } while (matrix[row][col] != 7);

        matrix[row][col] = 16;
        foodMatrix[row][col]= 16;
    }

}