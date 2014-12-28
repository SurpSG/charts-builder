package com.elance.charts.GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.elance.charts.data.CSVParser;
import com.elance.charts.data.ChartInfo;
import com.elance.charts.data.DataProcessor;
import com.elance.charts.data.DataProcessorGender;
import com.elance.charts.html.HTMLGeneratorController;
import com.elance.charts.records.Record;

public class MyFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1254269072443504913L;

	private JTextField textBox;
	private JCheckBox genderCheckBox;
	private JButton executeButton;

	private Box chartsContainer;

	private Map<ChartInfo, Map<String, Map<Double, Double>>> plots;

	public MyFrame() {

		plots = new LinkedHashMap<>();

		setSize(1000, 800);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(new BorderLayout());

		add(createTools(), BorderLayout.NORTH);
	}

	private Box createChartsContainer(List<String> chartsHtmlFilePath) {
		Box box = Box.createVerticalBox();
		for (String file : chartsHtmlFilePath) {

			JFXPanel panel = getJavaFXScene(file);
			box.add(panel);

		}

		return box;
	}

	private JPanel createTools() {
		JPanel tools = new JPanel();

		textBox = getTextField("csv file path");
		genderCheckBox = new JCheckBox("Gender");
		executeButton = createButton("Execute", new ExecuteActionListener());

		tools.add(textBox);
		tools.add(executeButton);
		tools.add(genderCheckBox);

		return tools;
	}

	private JTextField getTextField(String name) {
		JTextField textField = new JTextField();
		textField.setText("file.csv");
		textField.setPreferredSize(new Dimension(250, 40));
		textField.setBorder(new TitledBorder(name));
		return textField;
	}

	private JButton createButton(String name, ActionListener listener) {
		JButton button = new JButton(name);
		button.addActionListener(listener);
		return button;
	}

	private class ExecuteActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			File file = new File(textBox.getText());

			if (!file.exists()) {
				JOptionPane.showMessageDialog(
						null,
						String.format("File \"%s\" not found",
								textBox.getText()));
				return;
			}

			if (chartsContainer != null) {
				Platform.setImplicitExit(false);
				remove(chartsContainer);
				MyFrame.this.revalidate();
			}

			List<Record> records = new CSVParser(file).parseData();

			DataProcessor dataProcessor = dataProcessorFactory(records);
			dataProcessor.calculate();
			plots = dataProcessor.getPlots();
			HTMLGeneratorController htmlGeneratorController = new HTMLGeneratorController(plots);
			chartsContainer = null;
			System.gc();
			chartsContainer = createChartsContainer(htmlGeneratorController.createCharts());
			add(chartsContainer, BorderLayout.CENTER);
			MyFrame.this.paintComponents(MyFrame.this.getGraphics());
			System.gc();
		}

		private DataProcessor dataProcessorFactory(List<Record> records) {
			if (genderCheckBox.isSelected()) {
				return new DataProcessorGender(records);
			}

			return new DataProcessor(records);
		}
	}

	private JFXPanel getJavaFXScene(final String htmlPath) {
		final JFXPanel javafxPanel = new JFXPanel();
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				BorderPane borderPane = new BorderPane();
				WebView webComponent = new WebView();

				webComponent.getEngine().load("file:///" + htmlPath);

				borderPane.setCenter(webComponent);
				Scene scene = new Scene(borderPane, 700, 700);
				javafxPanel.setScene(scene);
				System.gc();
			}
		});
		

		return javafxPanel;
	}

}
