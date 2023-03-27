package cn.wulin.brace.examples.http.download;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;

public class FileDownloader extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final int BUFFER_SIZE = 1024 * 1024;
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
    private final JLabel fileNameLabel;
    private final JLabel fileSizeLabel;
    private final JLabel statusLabel;
    private final JProgressBar progressBar;
    private final JButton downloadButton;
    private final JButton cancelButton;
    private File downloadFile;
    private Call downloadCall;
    private OkHttpClient httpClient;
    private boolean isDownloading;

    public FileDownloader() {
        super("File Downloader");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fileNameLabel = new JLabel();
        fileSizeLabel = new JLabel();
        statusLabel = new JLabel("Idle");
        progressBar = new JProgressBar();
        downloadButton = new JButton("Download");
        cancelButton = new JButton("Cancel");
        cancelButton.setEnabled(false);

        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isDownloading) {
                    JOptionPane.showMessageDialog(FileDownloader.this, "A download is already in progress.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JFileChooser fileChooser = new JFileChooser();
                    int result = fileChooser.showSaveDialog(FileDownloader.this);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        downloadFile = fileChooser.getSelectedFile();
                        download();
                    }
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isDownloading && downloadCall != null) {
                    downloadCall.cancel();
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(fileNameLabel, BorderLayout.NORTH);
        panel.add(fileSizeLabel, BorderLayout.CENTER);
        panel.add(progressBar, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(downloadButton);
        buttonPanel.add(cancelButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.NORTH);
        getContentPane().add(statusLabel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setSize(400, 150);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void download() {
        isDownloading = true;
        downloadButton.setEnabled(false);
        cancelButton.setEnabled(true);
        progressBar.setValue(0);
        statusLabel.setText("Connecting...");

        
        httpClient = new OkHttpClient.Builder().dispatcher(new Dispatcher(Executors.newSingleThreadExecutor())).build();
        
//        httpClient = new OkHttpClient.Builder().executor(Executors.newSingleThreadExecutor()).build();
        Request request = new Request.Builder().url("http://www.notescloud.top/rssdld/dldfl/downloader.zip").build();
        downloadCall = httpClient.newCall(request);
        downloadCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                isDownloading = false;
                downloadButton.setEnabled(true);
                cancelButton.setEnabled(false);
            progressBar.setValue(0);
            statusLabel.setText("Failed");
            JOptionPane.showMessageDialog(FileDownloader.this, "Failed to download file.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                long contentLength = response.body().contentLength();
                String fileName = downloadFile.getName();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        fileNameLabel.setText("File Name: " + fileName);
                        fileSizeLabel.setText("File Size: " + DECIMAL_FORMAT.format(contentLength / (1024.0 * 1024.0)) + " MB");
                    }
                });

                try (InputStream inputStream = response.body().byteStream();
                        FileOutputStream outputStream = new FileOutputStream(downloadFile)) {
                    byte[] buffer = new byte[BUFFER_SIZE];
                    long totalBytesRead = 0;
                    int bytesRead;
                    statusLabel.setText("Downloading...");
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                        totalBytesRead += bytesRead;
                        int percentCompleted = (int) ((totalBytesRead * 100) / contentLength);
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setValue(percentCompleted);
                            }
                        });
                    }
                } catch (IOException e) {
                    isDownloading = false;
                    downloadButton.setEnabled(true);
                    cancelButton.setEnabled(false);
                    progressBar.setValue(0);
                    statusLabel.setText("Failed");
                    JOptionPane.showMessageDialog(FileDownloader.this, "Failed to save file.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                isDownloading = false;
                downloadButton.setEnabled(true);
                cancelButton.setEnabled(false);
                progressBar.setValue(100);
                statusLabel.setText("Completed");
                JOptionPane.showMessageDialog(FileDownloader.this, "File downloaded successfully.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                isDownloading = false;
                downloadButton.setEnabled(true);
                cancelButton.setEnabled(false);
                progressBar.setValue(0);
                statusLabel.setText("Failed");
                JOptionPane.showMessageDialog(FileDownloader.this, "Failed to download file.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    });
}

public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
            new FileDownloader();
        }
    });
}
}