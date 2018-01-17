package org.osbot.jailbreak.ui;


import org.osbot.jailbreak.data.Constants;
import org.osbot.jailbreak.scripts.DownloadScript;
import org.osbot.jailbreak.ui.logger.Logger;
import org.osbot.jailbreak.ui.logger.LoggerPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.instrument.Instrumentation;

public class MainFrame extends JFrame implements ActionListener {


	private final Instrumentation instrumentation;
	private final JButton startScript;
	private final JList<String> scriptSelector;

	public MainFrame(Instrumentation instrumentation) {
		super("OSBot Jailbreak");
		this.instrumentation = instrumentation;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		this.startScript = new JButton("Start Script");
		this.startScript.addActionListener(this::actionPerformed);
		this.add(startScript, BorderLayout.NORTH);

		this.scriptSelector = new JList<>();
		this.add(scriptSelector, BorderLayout.CENTER);

		this.add(new LoggerPanel(new Logger()), BorderLayout.SOUTH);

		pack();
		setLocationRelativeTo(getOwner());
		setVisible(true);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		new DownloadScript("Fuck OSBot", Constants.JAR_URLS[0]);
	}
}
