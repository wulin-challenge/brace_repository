package cn.wulin.brace.examples.calc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator {

	private JFrame frame;
	private JTextField textField;
	private JTextArea historyArea;
	private String currentOperator;
	private Double previousValue;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				Calculator calculator = new Calculator();
				calculator.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public Calculator() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		textField = new JTextField();
		textField.setBounds(10, 11, 309, 39);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		historyArea = new JTextArea();
		historyArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(historyArea);
		scrollPane.setBounds(10, 61, 309, 200);
		frame.getContentPane().add(scrollPane);

		JPanel panel = new JPanel();
		panel.setBounds(329, 11, 105, 250);
		frame.getContentPane().add(panel);
		panel.setLayout(new GridLayout(5, 2, 0, 0));

		String[] buttonTexts = { "+", "-", "*", "/", "sqrt", "=", "C", "CE" };
		for (String text : buttonTexts) {
			JButton button = new JButton(text);
			button.addActionListener(new ButtonClickListener());
			panel.add(button);
		}
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
				textField.setText("");
				break;
			case "CE":
				textField.setText("");
				historyArea.setText("");
				break;
			default:
				setOperator(text);
				break;
			}
		}

		private void setOperator(String operator) {
			if (textField.getText().isEmpty()) {
				return;
			}

			try {
				previousValue = Double.parseDouble(textField.getText());
				textField.setText("");
				currentOperator = operator;
			} catch (NumberFormatException e) {
				textField.setText("Invalid input");
			}
		}

		private void calculateResult() {
			if (previousValue == null || currentOperator == null || textField.getText().isEmpty()) {
				return;
			}

			try {
				double currentValue = Double.parseDouble(textField.getText());
				double result = 0;
				switch (currentOperator) {
				case "+":
					result = previousValue + currentValue;
					break;
				case "-":
					result = previousValue - currentValue;
					break;
				case "*":
					result = previousValue * currentValue;
					break;
				case "/":
					if (currentValue == 0) {
						throw new ArithmeticException("Division by zero");
					}
					result = previousValue / currentValue;
					break;
				case "sqrt":
					if (previousValue < 0) {
						throw new ArithmeticException("Invalid input for sqrt");
					}
					result = Math.sqrt(previousValue);
					break;
				}

				textField.setText(String.valueOf(result));
				historyArea.append(previousValue + " " + currentOperator + " "
						+ (currentOperator.equals("sqrt") ? "" : currentValue) + " = " + result + "\n");

				currentOperator = null;
				previousValue = null;
			} catch (NumberFormatException e) {
				textField.setText("Invalid input");
			} catch (ArithmeticException e) {
				textField.setText(e.getMessage());
			}
		}
	}
}
