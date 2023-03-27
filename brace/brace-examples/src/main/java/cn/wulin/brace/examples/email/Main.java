package cn.wulin.brace.examples.email;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Email Sender");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            // 创建主面板
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            frame.add(mainPanel);

            // 创建顶部菜单栏
            JMenuBar menuBar = new JMenuBar();
            frame.setJMenuBar(menuBar);

            // 添加菜单项
            JMenu importMenu = new JMenu("导入");
            menuBar.add(importMenu);

            JMenuItem importRecipientsMenuItem = new JMenuItem("导入接收人");
            importMenu.add(importRecipientsMenuItem);

            // 创建左侧面板
            JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
            mainPanel.add(leftPanel, BorderLayout.WEST);

            // 创建右侧面板
            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            mainPanel.add(rightPanel, BorderLayout.CENTER);

            // 创建"添加接收人"按钮
            JButton addRecipientButton = new JButton("添加接收人");
            leftPanel.add(addRecipientButton);

            // 创建"添加发送人"按钮
            JButton addSenderButton = new JButton("添加发送人");
            leftPanel.add(addSenderButton);

            // 创建"发送邮件"按钮
            JButton sendEmailButton = new JButton("发送邮件");
            leftPanel.add(sendEmailButton);

            // 创建接收人列表
            DefaultListModel<String> recipientListModel = new DefaultListModel<>();
            JList<String> recipientList = new JList<>(recipientListModel);
            rightPanel.add(new JScrollPane(recipientList));

            // 创建发送人列表
            DefaultListModel<String> senderListModel = new DefaultListModel<>();
            JList<String> senderList = new JList<>(senderListModel);
            rightPanel.add(new JScrollPane(senderList));

            // 为按钮添加事件监听器
//            addRecipientButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    // 添加接收人的逻辑
//                }
//            });
            
            addRecipientButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 创建一个对话框用于输入接收人信息
                    JDialog addRecipientDialog = new JDialog(frame, "添加接收人", true);
                    addRecipientDialog.setSize(300, 150);
                    addRecipientDialog.setLayout(new GridLayout(0, 2));

                    // 创建文本字段和标签
                    JLabel nameLabel = new JLabel("姓名：");
                    JTextField nameField = new JTextField();
                    JLabel emailLabel = new JLabel("邮箱：");
                    JTextField emailField = new JTextField();

                    // 添加组件到对话框
                    addRecipientDialog.add(nameLabel);
                    addRecipientDialog.add(nameField);
                    addRecipientDialog.add(emailLabel);
                    addRecipientDialog.add(emailField);

                    // 创建"添加"按钮并添加事件监听器
                    JButton addButton = new JButton("添加");
                    addButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String name = nameField.getText().trim();
                            String email = emailField.getText().trim();

                            if (name.isEmpty() || email.isEmpty()) {
                                JOptionPane.showMessageDialog(addRecipientDialog, "请填写所有字段", "错误", JOptionPane.ERROR_MESSAGE);
                            } else {
                                // 在这里将接收人信息存储到数据库中
                                // DatabaseHandler.addRecipient(new Recipient(name, email));

                                // 将接收人添加到列表中
                                recipientListModel.addElement(name + " <" + email + ">");

                                // 关闭对话框
                                addRecipientDialog.dispose();
                            }
                        }
                    });

                    // 创建"取消"按钮并添加事件监听器
                    JButton cancelButton = new JButton("取消");
                    cancelButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            addRecipientDialog.dispose();
                        }
                    });

                    // 添加按钮到对话框
                    addRecipientDialog.add(addButton);
                    addRecipientDialog.add(cancelButton);

                    // 显示对话框
                    addRecipientDialog.setVisible(true);
                }
            });


//            addSenderButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    // 添加发送人的逻辑
//                }
//            });
            
            addSenderButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 创建一个对话框用于输入发送人信息
                    JDialog addSenderDialog = new JDialog(frame, "添加发送人", true);
                    addSenderDialog.setSize(300, 200);
                    addSenderDialog.setLayout(new GridLayout(0, 2));

                    // 创建文本字段和标签
                    JLabel nameLabel = new JLabel("姓名：");
                    JTextField nameField = new JTextField();
                    JLabel emailLabel = new JLabel("邮箱：");
                    JTextField emailField = new JTextField();
                    JLabel passwordLabel = new JLabel("密码：");
                    JPasswordField passwordField = new JPasswordField();

                    // 添加组件到对话框
                    addSenderDialog.add(nameLabel);
                    addSenderDialog.add(nameField);
                    addSenderDialog.add(emailLabel);
                    addSenderDialog.add(emailField);
                    addSenderDialog.add(passwordLabel);
                    addSenderDialog.add(passwordField);

                    // 创建"添加"按钮并添加事件监听器
                    JButton addButton = new JButton("添加");
                    addButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String name = nameField.getText().trim();
                            String email = emailField.getText().trim();
                            String password = new String(passwordField.getPassword()).trim();

                            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                                JOptionPane.showMessageDialog(addSenderDialog, "请填写所有字段", "错误", JOptionPane.ERROR_MESSAGE);
                            } else {
                                // 在这里将发送人信息存储到数据库中
                                // DatabaseHandler.addSender(new Sender(name, email, password));

                                // 将发送人添加到列表中
                                senderListModel.addElement(name + " <" + email + ">");

                                // 关闭对话框
                                addSenderDialog.dispose();
                            }
                        }
                    });

                    // 创建"取消"按钮并添加事件监听器
                    JButton cancelButton = new JButton("取消");
                    cancelButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            addSenderDialog.dispose();
                        }
                    });

                    // 添加按钮到对话框
                    addSenderDialog.add(addButton);
                    addSenderDialog.add(cancelButton);

                    // 显示对话框
                    addSenderDialog.setVisible(true);
                }
            });


            sendEmailButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 发送邮件的逻辑
                }
            });

            // 为导入菜单项添加事件监听器
            importRecipientsMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 导入接收人的逻辑
                }
            });

            frame.setVisible(true);
        });
    }
}
