package cn.wulin.brace.examples.calc;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import cn.hutool.core.math.Calculator;

public class CalculatorGUI extends JFrame implements ActionListener {

	// 定义计算器需要的组件
	private JPanel contentPanel; // 内容面板
	private JTextField displayField; // 显示面板
	private JButton addButton, subtractButton, multiplyButton, divideButton; // 加减乘除按钮
	private JButton squareButton, clearButton, decimalButton, equalButton; // 平方、清空、小数点、等于按钮
	private double result = 0; // 计算结果
	private String operator = ""; // 当前的操作符
	private boolean isFirstInput = true; // 是否是第一次输入数字
	private boolean isDecimalEntered = false; // 是否已输入小数点

	public CalculatorGUI() {
		// 设置窗口标题和大小
		setTitle("计算器");
		setSize(270, 360);
		setResizable(false);

		// 创建内容面板和布局
		contentPanel = new JPanel();
		contentPanel.setLayout(null);

		// 创建显示面板
		displayField = new JTextField("0");
		displayField.setEditable(false);
		displayField.setHorizontalAlignment(SwingConstants.RIGHT);
		displayField.setFont(new Font("宋体", Font.PLAIN, 30));
		displayField.setBounds(0, 0, 270, 60);
		contentPanel.add(displayField);

		// 创建加减乘除按钮
		addButton = createButton("+", 0, 60, 68, 60);
		subtractButton = createButton("-", 68, 60, 68, 60);
		multiplyButton = createButton("*", 136, 60, 68, 60);
		divideButton = createButton("/", 204, 60, 66, 60);

		// 创建平方、清空、小数点、等于按钮
		squareButton = createButton("x²", 0, 120, 68, 60);
		clearButton = createButton("C", 68, 120, 68, 60);
		decimalButton = createButton(".", 136, 120, 68, 60);
		equalButton = createButton("=", 204, 120, 66, 120);

		// 将所有按钮添加到内容面板
		contentPanel.add(addButton);
		contentPanel.add(subtractButton);
		contentPanel.add(multiplyButton);
		contentPanel.add(divideButton);
		contentPanel.add(squareButton);
		contentPanel.add(clearButton);
		contentPanel.add(decimalButton);
		contentPanel.add(equalButton);

		// 将内容面板添加到窗口中心
		setContentPane(contentPanel);
	}

	// 创建按钮的工具方法
	private JButton createButton(String label, int x, int y, int width, int height) {
		JButton button = new JButton(label);
		button.setFont(new Font("宋体", Font.PLAIN, 24));
		button.setBounds(x, y, width, height);
		button.addActionListener(this); // 监听按钮点击事件
		return button;
	}

	// 监听按钮点击事件
	public void actionPerformed(ActionEvent event) {
		String input = event.getActionCommand();

		if (input.equals("C")) { // 点击清空按钮
			result = 0;
			operator = "";
			displayField.setText("0");
			isFirstInput = true;
			isDecimalEntered = false;
		} else if (input.matches("[0-9]")) { // 点击数字按钮
			if (isFirstInput) {
				displayField.setText(input);
				isFirstInput = false;
			} else {
				displayField.setText(displayField.getText() + input);
			}
		} else if (input.equals(".")) { // 点击小数点按钮
			if (!isDecimalEntered) {
				displayField.setText(displayField.getText() + ".");
				isDecimalEntered = true;
			}
		} else if (input.equals("x²")) { // 点击平方按钮
			double number = Double.parseDouble(displayField.getText());
			result = number * number;
			displayResult(result);
			isFirstInput = true;
			isDecimalEntered = false;
		} else if (input.equals("+") || input.equals("-") || input.equals("*") || input.equals("/")) {
			// 点击加减乘除按钮
			double number = Double.parseDouble(displayField.getText());
			if (operator.equals("+")) {
				result += number;
			} else if (operator.equals("-")) {
				result -= number;
			} else if (operator.equals("*")) {
				result *= number;
			} else if (operator.equals("/")) {
				result /= number;
			} else {
				result = number;
			}
			operator = input;
			displayResult(result);
			isFirstInput = true;
			isDecimalEntered = false;
		} else if (input.equals("=")) { // 点击等于按钮
			double number = Double.parseDouble(displayField.getText());
			if (operator.equals("+")) {
				result += number;
			} else if (operator.equals("-")) {
				result -= number;
			} else if (operator.equals("*")) {
				result *= number;
			} else if (operator.equals("/")) {
				result /= number;
			} else {
				result = number;
			}
			operator = "";
			displayResult(result);
			isFirstInput = true;
			isDecimalEntered = false;
		}
	}

	// 更新显示结果的方法
	private void displayResult(double result) {
		if (result == (long) result) { // 如果结果为整数，去掉小数点后面的0
			displayField.setText(String.format("%d", (long) result));
		} else {
			displayField.setText(String.format("%s", result));
		}
	}

	public static void main(String[] args) {
		CalculatorGUI calculator = new CalculatorGUI();
		calculator.setVisible(true);
	}
}
