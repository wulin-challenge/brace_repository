package cn.wulin.brace.examples.calc2;
import javax.swing.*;
import java.awt.*;

public class CalculatorUI {

    private CalculatorLogic calculatorLogic;

    public CalculatorUI() {
        calculatorLogic = new CalculatorLogic();
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());

        JTextField inputField = new JTextField();
        pane.add(inputField, BorderLayout.NORTH);

        JTextArea historyArea = new JTextArea();
        historyArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(historyArea);
        pane.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = calculatorLogic.createButtonPanel(inputField, historyArea);
        pane.add(buttonPanel, BorderLayout.EAST);

        frame.setSize(450, 300);
        frame.setResizable(true);
        frame.setVisible(true);
    }
}
