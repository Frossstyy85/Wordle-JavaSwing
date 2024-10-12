package WordleApp;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Wordle extends JFrame implements ActionListener {
    static int cols = 5;
    static int rows = 6;

    JLabel[][] boxes = new JLabel[rows][cols];

    JLabel victoryScreen = new JLabel("You guessed correctly!");
    JPanel boxPanel = new JPanel();
    JTextField textField = new JTextField();
    JButton submit = new JButton("Guess");
    static int currentRow = 0;
    JButton newGame = new JButton("New word");
    int correctChars = 0;
    Random random = new Random();
    String dailyWord = getWord();


    Wordle() {



        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new LimitDocumentFilter(5));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Border blackLine = BorderFactory.createLineBorder(Color.DARK_GRAY, 2);
                boxes[i][j] = new JLabel();
                boxes[i][j].setBorder(blackLine);
                boxPanel.add(boxes[i][j]);
                boxes[i][j].setPreferredSize(new Dimension(50, 75));
                boxes[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                boxes[i][j].setFont(new Font("Arial", Font.BOLD, 35));
                boxes[i][j].setForeground(Color.WHITE);
                boxes[i][j].setOpaque(true);
                boxes[i][j].setBackground(Color.decode("#121213"));
            }
        }


        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                for (int i = 0; i < textField.getText().length(); i++) {
                    boxes[currentRow][i].setText("");
                }
                String text = textField.getText();
                for (int i = 0; i < text.length() && i < cols; i++) {
                    boxes[currentRow][i].setText(String.valueOf(text.charAt(i)).toUpperCase());
                }

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = textField.getText();
                for (int i = 0; i < cols; i++) {
                    if (i < text.length()) {
                        boxes[currentRow][i].setText(String.valueOf(text.charAt(i)).toUpperCase());
                    } else {
                        boxes[currentRow][i].setText("");
                    }
                }
            }


            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });


        boxPanel.setLayout(new GridLayout(6, 5, 3, 3));
        boxPanel.setBounds(200, 250, 400, 500);
        boxPanel.setBackground(Color.decode("#121213"));
        add(boxPanel);
        add(textField);
        add(submit);
        add(newGame);
        textField.setVisible(false);
        textField.setBounds(100,100,100,30);

        Font buttonFont = new Font("Helvetica", Font.PLAIN, 20);

        submit.setBounds(275, 175, 100, 50);
        submit.addActionListener(this);
        submit.setBackground(Color.decode("#3A3A3C"));
        submit.setBorder(null);
        submit.setFont(buttonFont);
        submit.setForeground(Color.white);
        submit.setFocusPainted(false);

        newGame.setBounds(425, 175, 100, 50);
        newGame.addActionListener(this);
        newGame.setBackground(Color.decode("#3A3A3C"));
        newGame.setBorder(null);
        newGame.setFont(buttonFont);
        newGame.setForeground(Color.white);
        newGame.setFocusPainted(false);

        victoryScreen.setVisible(false);
        victoryScreen.setSize(new Dimension(400, 50));
        victoryScreen.setFont(new Font("Helvetica", Font.PLAIN, 35));
        victoryScreen.setForeground(Color.white);
        victoryScreen.setLocation(250,75);
        add(victoryScreen);

        setTitle("Wordle");
        getContentPane().setBackground(Color.decode("#121213"));
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);
        setVisible(true);

    }
    private String getWord(){
        String[] wordList = {
                "apple", "grape", "melon", "peach", "berry",
                "mango", "lemon", "plumb", "olive", "kiwi",
                "tiger", "zebra", "eagle", "whale", "shark",
                "flame", "storm", "blaze", "cloud", "shine",
                "water", "earth", "stone", "light", "river",
                "chair", "table", "clock", "piano", "phone",
                "brick", "brush", "glass", "plate", "frame"
        };
        String word = wordList[random.nextInt(35)].toUpperCase();
        System.out.println(word);
        return word;
    }

    private int[] getColor(String userWord, int[] arr) {
        userWord = userWord.toUpperCase();
        boolean[] matched = new boolean[cols];

        for (int i = 0; i < cols; i++) {
            if (userWord.charAt(i) == dailyWord.charAt(i)) {
                arr[i] = 1;
                matched[i] = true;
            }
        }

        for (int i = 0; i < cols; i++) {
            if (arr[i] != 1) {
                int indexInWord = dailyWord.indexOf(userWord.charAt(i));
                if (indexInWord != -1 && !matched[indexInWord]) {
                    arr[i] = 0;
                    matched[indexInWord] = true;
                } else {
                    arr[i] = -1;
                }
            }
        }

        return arr;
    }



    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == submit) {
            if (currentRow == rows-1)
                textField.setEditable(false);
            textField.requestFocusInWindow();
            String userWord = textField.getText();
            if (userWord.length() == cols) {


                int[] arr = new int[cols];
                int [] colorArr = getColor(userWord, arr);

                for (int i = 0; i < cols; i++) {

                    switch (colorArr[i]) {
                        case 1: {boxes[currentRow][i].setBackground(Color.decode("#B2E1C6"));
                            correctChars++;
                        }
                        break;
                        case 0: boxes[currentRow][i].setBackground(Color.decode("#F9E39D"));
                            break;
                        case -1: boxes[currentRow][i].setBackground(Color.decode("#F6B4B0"));
                    }
                }
                if (correctChars == cols) {
                    textField.setEditable(false);
                    victoryScreen.setVisible(true);
                }
                if (currentRow < cols) {
                    currentRow++;
                    textField.setText("");
                    }

                System.out.println(currentRow);

                correctChars = 0;


            }
        }
        else if (e.getSource() == newGame) {
            dailyWord = getWord();
            textField.setEditable(true);
            textField.requestFocusInWindow();
            correctChars = 0;
            victoryScreen.setVisible(false);
            textField.setText("");
            currentRow = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    boxes[i][j].setText("");
                    boxes[i][j].setBackground(Color.decode("#121213"));

                }
            }
        }
    }
}