package org.osbot.jailbreak.ui;


import org.osbot.jailbreak.scripts.ScriptDownloader;
import org.osbot.jailbreak.ui.logger.Logger;
import org.osbot.jailbreak.ui.logger.LoggerPanel;
import org.osbot.jailbreak.util.NetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.instrument.Instrumentation;

public class MainFrame extends JFrame implements ActionListener {

    private final Instrumentation instrumentation;
    private final LoggerPanel logger;
    private final JButton startScript, refreshScripts;
    private final JList<String> scriptSelector;
    private final DefaultListModel<String> selectorModel;
    private final JMenuBar menuBar;
    private final JMenu settingsMenu;
    private final JCheckBoxMenuItem showLogger;
    private final String userId;

    public MainFrame(Instrumentation instrumentation, String userId) {
        super("OSBot Jailbreak - DiscountBotting");
        this.instrumentation = instrumentation;
        this.userId = userId;
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

        Box buttonLayout = Box.createHorizontalBox();
        this.startScript = new JButton("Start Script");
        this.startScript.setActionCommand("start script");
        this.startScript.addActionListener(this::actionPerformed);
        buttonLayout.add(startScript);
        this.refreshScripts = new JButton("Refresh Scripts");
        this.refreshScripts.setActionCommand("refresh scripts");
        this.refreshScripts.addActionListener(this);
        buttonLayout.add(refreshScripts);
        this.add(buttonLayout, BorderLayout.NORTH);

        this.selectorModel = new DefaultListModel<>();
        this.scriptSelector = new JList<>(selectorModel);
        JScrollPane scriptSelectorPane = new JScrollPane(scriptSelector, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scriptSelectorPane.setPreferredSize(new Dimension(500, 150));
        this.add(scriptSelectorPane, BorderLayout.CENTER);
        refreshScripts();

        this.logger = new LoggerPanel(new Logger());
        this.add(logger, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getOwner());
        setVisible(true);
    }


    private final void refreshScripts() {
        final String SCRIPTS_REPO_URL = "http://discountbotting.com/private/check/scripts.php?";
        final StringBuilder parameters = new StringBuilder();
        parameters.append("uid=").append(userId).append("&submit=Search");
        String response = null;
        try {
            response = NetUtils.postResponse(SCRIPTS_REPO_URL, parameters.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null) {
            selectorModel.clear();
            String[] scripts = response.trim().split(" => ");
            for (int i = 0; i < scripts.length; i++) {
                String script = scripts[i];
                if (script.contains(".jar")) {
                    selectorModel.addElement(script.split(" ")[0].replace("_", " ").replace(".jar", "").replace(")", ""));
                }
            }
        } else {
            Logger.log("Error refreshing scripts.");
        }
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        switch (e.getActionCommand()) {
            case "start script":
                if (!scriptSelector.isSelectionEmpty()) {
                    final String BASE_DOWNLOAD_URL = "http://discountbotting.com/private/scripts/current/";
                    StringBuilder scriptUrl = new StringBuilder();
                    String scriptName = selectorModel.getElementAt(scriptSelector.getSelectedIndex());
                    scriptUrl.append(BASE_DOWNLOAD_URL).append(scriptName.replace(" ", "_"));
                    scriptUrl.append(".jar");
                    Logger.log("Downloading Script...");
                    new ScriptDownloader(scriptName, scriptUrl.toString());
                    dispose();
                }
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
            case "refresh scripts":
                refreshScripts();
                break;
        }
    }

    private final void refreshFrame() {
        revalidate();
        pack();
    }
}
