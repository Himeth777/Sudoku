package org.example;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainUI {
    private static JPanel panel;
    private static JTextField[][] cells;
    private static SudokuGame game;
    private static String currentDifficulty = "Easy";

    public static void Window() {
        JFrame frame = new JFrame("Sudoku");
        panel = new JPanel(new GridLayout(9, 9));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 650);

        Border cellBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
        cells = new JTextField[9][9];

        JMenuBar menuBar = new JMenuBar();
        JMenu difficultyMenu = new JMenu("Difficulty");

        JMenuItem easy = new JMenuItem("Easy");
        JMenuItem medium = new JMenuItem("Medium");
        JMenuItem hard = new JMenuItem("Hard");

        difficultyMenu.add(easy);
        difficultyMenu.add(medium);
        difficultyMenu.add(hard);
        menuBar.add(difficultyMenu);
        frame.setJMenuBar(menuBar);

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(new Font("Arial", Font.BOLD, 20));
                cell.setEditable(true);
                Border border;
                if (row % 3 == 0 && col % 3 == 0) {
                    border = BorderFactory.createMatteBorder(3, 3, 1, 1, Color.BLACK);
                } else if (row % 3 == 0 && (col + 1) % 3 == 0) {
                    border = BorderFactory.createMatteBorder(3, 1, 1, 3, Color.BLACK);
                } else if ((row + 1) % 3 == 0 && col % 3 == 0) {
                    border = BorderFactory.createMatteBorder(1, 3, 3, 1, Color.BLACK);
                } else if ((row + 1) % 3 == 0 && (col + 1) % 3 == 0) {
                    border = BorderFactory.createMatteBorder(1, 1, 3, 3, Color.BLACK);
                } else if (row % 3 == 0) {
                    border = BorderFactory.createMatteBorder(3, 1, 1, 1, Color.BLACK);
                } else if ((row + 1) % 3 == 0) {
                    border = BorderFactory.createMatteBorder(1, 1, 3, 1, Color.BLACK);
                } else if (col % 3 == 0) {
                    border = BorderFactory.createMatteBorder(1, 3, 1, 1, Color.BLACK);
                } else if ((col + 1) % 3 == 0) {
                    border = BorderFactory.createMatteBorder(1, 1, 1, 3, Color.BLACK);
                } else {
                    border = cellBorder;
                }
                cell.setBorder(border);
                cells[row][col] = cell;
                panel.add(cell);

                int finalRow = row;
                int finalCol = col;
                cell.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        char c = e.getKeyChar();
                        if (!(c >= '1' && c <= '9')) {
                            e.consume();
                            return;
                        }
                        if (!cell.getText().isEmpty()) {
                            e.consume();
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                        validateInput(cell, finalRow, finalCol);
                        checkWin();
                    }
                });
            }
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton newButton = new JButton("New");
        JButton resetButton = new JButton("Reset");
        JButton showSolutionButton = new JButton("Show Solution");

        buttonPanel.add(newButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(showSolutionButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        easy.addActionListener(e -> startGame("Easy"));
        medium.addActionListener(e -> startGame("Medium"));
        hard.addActionListener(e -> startGame("Hard"));
        newButton.addActionListener(e -> startGame(currentDifficulty));
        resetButton.addActionListener(e -> resetGrid());
        showSolutionButton.addActionListener(e -> showSolution());

        game = new SudokuGame();
        startGame("Easy");
        frame.setVisible(true);
    }

    private static void checkWin() {
        boolean won = true;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String cellText = cells[i][j].getText();
                if (cellText.isEmpty() || cells[i][j].getBackground() == Color.RED) {
                    won = false;
                    break;
                }
            }
        }
        if (won) {
            JOptionPane.showMessageDialog(null, "Congratulations! You've solved the puzzle!", "Winner!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void validateInput(JTextField cell, int row, int col) {
        String input = cell.getText().trim();
        if (input.isEmpty()) {
            cell.setBackground(Color.WHITE);
            return;
        }

        int enteredValue = Integer.parseInt(input);
        int correctValue = game.getSolution()[row][col];

        if (enteredValue != correctValue) {
            cell.setBackground(Color.RED);
        } else {
            cell.setBackground(Color.WHITE);
        }
    }

    private static void startGame(String level) {
        currentDifficulty = level;
        game.setDifficulty(level);
        int[][] puzzle = game.generatePuzzle();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (puzzle[i][j] != 0) {
                    cells[i][j].setText(String.valueOf(puzzle[i][j]));
                    cells[i][j].setEditable(false);
                    cells[i][j].setBackground(Color.WHITE);
                } else {
                    cells[i][j].setText("");
                    cells[i][j].setEditable(true);
                    cells[i][j].setBackground(Color.WHITE);
                }
            }
        }
    }

    private static void resetGrid() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(cells[i][j].isEditable()) {
                    cells[i][j].setText("");
                    cells[i][j].setBackground(Color.WHITE);
                }
            }
        }
    }

    private static void showSolution() {
        int[][] solution = game.getSolution();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j].setText(String.valueOf(solution[i][j]));
                cells[i][j].setEditable(false);
                cells[i][j].setBackground(Color.WHITE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainUI start = new MainUI();
            start.Window();
        });
    }
}