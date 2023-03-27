package cn.wulin.brace.examples.calc;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimpleCalculator {
    public static void main(String[] args) {
        CalculatorFrame calculatorFrame = new CalculatorFrame();
        calculatorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        calculatorFrame.setVisible(true);
    }
}

class CalculatorFrame extends JFrame {
    public CalculatorFrame() {
        setTitle("Simple Calculator");
        setSize(400, 400);
        CalculatorPanel calculatorPanel = new CalculatorPanel();
        add(calculatorPanel);
    }
}

class CalculatorPanel extends JPanel {
    private JTextField display;
    private double result;
    private String lastCommand;
    private boolean start;

    public CalculatorPanel() {
        setLayout(new BorderLayout());
        
        result = 0;
        lastCommand = "=";
        start = true;

        display = new JTextField("0");
        display.setEnabled(false);
        add(display, BorderLayout.NORTH);

        ActionListener insert = new InsertAction();
        ActionListener command = new CommandAction();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4));

        addButton("7", insert, panel);
        addButton("8", insert, panel);
        addButton("9", insert, panel);
        addButton("/", command, panel);

        addButton("4", insert, panel);
        addButton("5", insert, panel);
        addButton("6", insert, panel);
        addButton("*", command, panel);

        addButton("1", insert, panel);
        addButton("2", insert, panel);
        addButton("3", insert, panel);
        addButton("-", command, panel);

        addButton("0", insert, panel);
        addButton(".", insert, panel);
        addButton("=", command, panel);
        addButton("+", command, panel);

        add(panel, BorderLayout.CENTER);
    }

    private void addButton(String label, ActionListener listener, JPanel panel) {
        JButton button = new JButton(label);
        button.addActionListener(listener);
        panel.add(button);
    }

    private class InsertAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String input = event.getActionCommand();
            if (start) {
                display.setText("");
                start = false;
            }
            display.setText(display.getText() + input);
        }
    }

    private class CommandAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String command = event.getActionCommand();

            if (start) {
                if (command.equals("-")) {
                    display.setText(command);
                    start = false;
                } else {
                    lastCommand = command;
                }
            } else {
                calculate(Double.parseDouble(display.getText()));
                lastCommand = command;
                start = true;
            }
        }
    }

    public void calculate(double x) {
        switch (lastCommand) {
            case "+":
                result += x;
                break;
            case "-":
                result -= x;
                break;
            case "*":
                result *= x;
                break;
            case "/":
                result /= x;
                break;
            case "=":
                result = x;
                break;
        }
        display.setText("" + result);
    }
}
