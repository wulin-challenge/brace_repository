package cn.wulin.brace.examples.calc2;
public class Calculator {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            CalculatorUI calculatorUI = new CalculatorUI();
            calculatorUI.createAndShowGUI();
        });
    }
}
