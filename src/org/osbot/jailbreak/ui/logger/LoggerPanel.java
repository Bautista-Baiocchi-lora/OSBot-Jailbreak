package org.osbot.jailbreak.ui.logger;


import javax.swing.*;
import java.awt.*;

/**
 * Created by bautistabaiocchi-lora on 7/16/17.
 */

public class LoggerPanel extends JPanel {
	private final JScrollPane scrollPane;

	public LoggerPanel(final Logger logger) {
		scrollPane = new JScrollPane(logger, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		setPreferredSize(new Dimension(500, 200));
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}


}