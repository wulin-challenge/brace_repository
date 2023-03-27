package cn.wulin.brace.examples.video.download;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class VideoDownloader {

	private OkHttpClient client = new OkHttpClient();
	private JFrame frame;
	private JTextField videoUrlField;
	private JTextField downloadLocationField;
	private JButton downloadButton;
	private JProgressBar progressBar;
	private DefaultListModel<String> downloadListModel;
	private JList<String> downloadList;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			new VideoDownloader().createAndShowGUI();
		});
	}

	private void createAndShowGUI() {
		frame = new JFrame("Video Downloader");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		frame.add(panel);

		// Video URL
		videoUrlField = new JTextField(50);
		videoUrlField.setMaximumSize(new Dimension(Integer.MAX_VALUE, videoUrlField.getPreferredSize().height));
		panel.add(new JLabel("Video URL:"));
		panel.add(videoUrlField);

		// Download location
		downloadLocationField = new JTextField(50);
		downloadLocationField
				.setMaximumSize(new Dimension(Integer.MAX_VALUE, downloadLocationField.getPreferredSize().height));
		panel.add(new JLabel("Download Location:"));
		panel.add(downloadLocationField);

		// Download button
		downloadButton = new JButton("Download");
		downloadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				downloadButton.setEnabled(false);
				downloadVideo();
			}
		});
		panel.add(downloadButton);

		// Progress bar
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		panel.add(progressBar);

		// Download list
		downloadListModel = new DefaultListModel<>();
		downloadList = new JList<>(downloadListModel);
		panel.add(new JScrollPane(downloadList));

		frame.pack();
		frame.setVisible(true);
	}

	private void downloadVideo() {
		String videoUrl = videoUrlField.getText().trim();
		String downloadLocation = downloadLocationField.getText().trim();
		progressBar.setValue(0);

		new Thread(() -> {
			try {
				if (videoUrl.contains(".m3u8")) {
					downloadM3U8Video(videoUrl, downloadLocation);
				} else if (videoUrl.contains(".mp4")) {
					downloadMP4Video(videoUrl, downloadLocation);
				} else {
					throw new IllegalArgumentException("Unsupported video format");
				}
				downloadListModel.addElement("Downloaded: " + videoUrl + " -> " + downloadLocation);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage(), "Download error",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} finally {
				downloadButton.setEnabled(true);
			}
		}).start();
	}

	private void downloadM3U8Video(String m3u8Url, String downloadLocation) throws IOException {
		Request request = new Request.Builder().url(m3u8Url).build();
		List<String> tsUrls = new ArrayList<>();

		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Failed to download m3u8 file: " + response);
			}

			String m3u8Content = response.body().string();
			String[] lines = m3u8Content.split("\n");
			for (String line : lines) {
				if (line.contains(".ts")) {
					tsUrls.add(line);
				}
			}
		}

		List<File> tsFiles = new ArrayList<>();
		for (int i = 0; i < tsUrls.size(); i++) {
			String tsUrl = tsUrls.get(i);
			File tsFile = File.createTempFile("video_downloader_", ".ts");
			tsFiles.add(tsFile);

			Request tsRequest = new Request.Builder().url(tsUrl).build();
			try (Response tsResponse = client.newCall(tsRequest).execute();
					ResponseBody responseBody = tsResponse.body()) {
				if (!tsResponse.isSuccessful()) {
					throw new IOException("Failed to download ts file: " + tsResponse);
				}

				try (InputStream inputStream = responseBody.byteStream();
						FileOutputStream outputStream = new FileOutputStream(tsFile)) {
					byte[] buffer = new byte[8192];
					int bytesRead;
					while ((bytesRead = inputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, bytesRead);
					}
				}
			}

			int progress = (int) (((double) (i + 1) / tsUrls.size()) * 100);
			SwingUtilities.invokeLater(() -> progressBar.setValue(progress));
		}

		mergeTSFilesToMP4(tsFiles, downloadLocation);
	}

	private void downloadMP4Video(String videoUrl, String downloadLocation) throws IOException {
		
		
		Request request = new Request.Builder().url(videoUrl)
				.header("referer", "https://www.xiaohua.com/")
				.build();

		try (Response response = client.newCall(request).execute(); ResponseBody responseBody = response.body()) {
			if (!response.isSuccessful()) {
				throw new IOException("Failed to download mp4 file: " + response);
			}

			long contentLength = responseBody.contentLength();
			try (InputStream inputStream = responseBody.byteStream();
					FileOutputStream outputStream = new FileOutputStream(new File(downloadLocation))) {
				byte[] buffer = new byte[8192];
				int bytesRead;
				long totalBytesRead = 0;

				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
					totalBytesRead += bytesRead;
					int progress = (int) (((double) totalBytesRead / contentLength) * 100);
					SwingUtilities.invokeLater(() -> progressBar.setValue(progress));
				}
			}
		}
	}

	private void mergeTSFilesToMP4(List<File> tsFiles, String mp4Path) throws IOException {
		try (FileOutputStream outputStream = new FileOutputStream(mp4Path)) {
			for (File tsFile : tsFiles) {
				Files.copy(tsFile.toPath(), outputStream);
				tsFile.delete();
			}
		}
	}
}
