package cn.wulin.brace.examples.calc2;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class CalculatorLogic {

    private JTextField inputField;
    private JTextArea historyArea;

    public JPanel createButtonPanel(JTextField inputField, JTextArea historyArea) {
        this.inputField = inputField;
        this.historyArea = historyArea;

        JPanel buttonPanel = new JPanel(new GridLayout(5, 2));
        String[] buttonTexts = {"+", "-", "*", "/", "sqrt", "=", "C", "CE"};
        for (String text : buttonTexts) {
            JButton button = new JButton(text);
            button.addActionListener(new ButtonClickListener());
            buttonPanel.add(button);
        }
        return buttonPanel;
    }

    class ButtonClickListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            String text = source.getText();

            switch (text) {
                case "=":
                    calculateResult();
                    break;
                case "C":
                    inputField.setText("");
                    break;
                case "CE":
                    inputField.setText("");
                    historyArea.setText("");
                    break;
                default:
                    inputField.setText(inputField.getText() + text);
                    break;
            }
        }

        private void calculateResult() {
            String expressionText = inputField.getText();
            if (expressionText.isEmpty()) {
                return;
            }

            try {
                Expression expression = new ExpressionBuilder(expressionText).build();
                double result = expression.evaluate();

                inputField.setText(String.valueOf(result));
                historyArea.append(expressionText + " = " + result + "\n");
            } catch (Exception e) {
                inputField.setText("Invalid input");
            }
        }
    }
}
