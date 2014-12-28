package com.elance.charts.test;

import javax.swing.SwingUtilities;

import com.elance.charts.GUI.MyFrame;

public class Main {

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MyFrame();
			}
		});
	}
}
