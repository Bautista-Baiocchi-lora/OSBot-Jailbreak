package org.osbot.jailbreak.ui;


import org.osbot.jailbreak.ui.logger.Logger;
import org.osbot.jailbreak.ui.logger.LoggerPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.instrument.Instrumentation;

public class MainFrame extends JFrame implements ActionListener {


    private final Instrumentation instrumentation;
    private final JButton dump, load;
    private final JTextField packageName;
    private final JMenuBar menuBar;
    private final JMenu fileMenu;
    private final JMenuItem hookCollection;

    public MainFrame(Instrumentation instrumentation) {
        super("Jailbroken OSBot");
        this.instrumentation = instrumentation;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        Box controlsLayout = Box.createHorizontalBox();

        this.dump = new JButton("Dump");
        this.dump.setActionCommand("dump");
        this.dump.addActionListener(this::actionPerformed);
        controlsLayout.add(dump);

        this.load = new JButton("Search");
        this.load.setActionCommand("search");
        this.load.addActionListener(this::actionPerformed);
        controlsLayout.add(load);

        this.packageName = new JTextField();

        controlsLayout.add(packageName);

        this.add(controlsLayout, BorderLayout.NORTH);
        this.add(new LoggerPanel(new Logger()), BorderLayout.SOUTH);


        this.hookCollection = new JMenuItem("Strip hooks");
        this.hookCollection.setActionCommand("strip hooks");
        this.hookCollection.addActionListener(this::actionPerformed);
        this.fileMenu = new JMenu("File");
        this.fileMenu.add(hookCollection);
        this.menuBar = new JMenuBar();
        this.menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        pack();
        setLocationRelativeTo(getOwner());
        setVisible(true);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        switch (e.getActionCommand()) {
            case "search":

                break;
            case "dump":

                break;
            case "strip hooks":

                break;
        }
    }
}
