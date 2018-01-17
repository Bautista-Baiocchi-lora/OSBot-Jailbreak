package org.osbot.jailbreak.ui.logger;


import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.Date;

/**
 * Created by bautistabaiocchi-lora on 7/16/17.
 */

public class Logger extends JTextPane {

	private static final SimpleAttributeSet attribute = new SimpleAttributeSet();
	private static Logger instance;
	private static StyledDocument document;
	private final Color color = new Color(92, 98, 106);


	public Logger() {
		super();
		instance = this;
		this.document = getStyledDocument();
		setEditable(false);
		setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		final DefaultCaret caret = (DefaultCaret) getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		setBackground(color);
	}

	private static void write(final String str, final LoggerFlag... flags) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(new Date() + ": ");
		if (flags.length > 0) {
			for (LoggerFlag flag : flags) {
				stringBuilder.append("[" + flag.getFlag() + "] ");
				StyleConstants.setForeground(attribute, flag.getColor());
				StyleConstants.setBold(attribute, true);
			}
		} else {
			StyleConstants.setForeground(attribute, Color.WHITE);
			StyleConstants.setBold(attribute, false);
		}
		stringBuilder.append(str);
		stringBuilder.append("\n");
		System.out.println(stringBuilder.toString());
		if (instance == null && document == null) {
			return;
		}
		try {
			document.insertString(document.getLength(), stringBuilder.toString(), attribute);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public static void log(final String str) {
		write(str);
	}


	public static void logException(final String str) {
		write(str, LoggerFlag.EXCEPTION);
	}


	public static void logWarning(final String str) {
		write(str, LoggerFlag.WARNING);
	}


}