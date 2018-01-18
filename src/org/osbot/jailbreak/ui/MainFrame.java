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
	private final LoggerPanel logger;
	private final JButton startScript;
	private final JList<String> scriptSelector;
	private final JMenuBar menuBar;
	private final JMenu settingsMenu;
	private final JCheckBoxMenuItem showLogger;

	public MainFrame(Instrumentation instrumentation) {
		super("OSBot Jailbreak");
		this.instrumentation = instrumentation;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);

		this.menuBar = new JMenuBar();
		this.settingsMenu = new JMenu("Settings");
		this.showLogger = new JCheckBoxMenuItem("Show Logger");
		this.showLogger.setActionCommand("show logger");
		this.showLogger.addActionListener(this::actionPerformed);
		this.showLogger.setSelected(true);
		this.settingsMenu.add(showLogger);
		this.menuBar.add(settingsMenu);
		this.setJMenuBar(menuBar);

		this.startScript = new JButton("Start Script");
		this.startScript.setActionCommand("start script");
		this.startScript.addActionListener(this::actionPerformed);
		this.add(startScript, BorderLayout.NORTH);

		this.scriptSelector = new JList<>();
		JScrollPane scriptSelectorPane = new JScrollPane(scriptSelector, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scriptSelectorPane.setPreferredSize(new Dimension(500, 150));
		this.add(scriptSelectorPane, BorderLayout.CENTER);

		this.logger = new LoggerPanel(new Logger());
		this.add(logger, BorderLayout.SOUTH);

		pack();
		setLocationRelativeTo(getOwner());
		setVisible(true);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		switch (e.getActionCommand()) {
			case "start script":
				new DownloadScript("Fuck OSBot", Constants.JAR_URLS[0]);
				break;
			case "show logger":
				if (showLogger.isSelected()) {
					this.add(logger, BorderLayout.SOUTH);
					refreshFrame();
				} else {
					this.remove(logger);
					refreshFrame();
				}
				break;
		}
	}

	private final void refreshFrame() {
		revalidate();
		pack();
	}
}
