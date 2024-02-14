package de.pauleduardkoenig.dwds_cf.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.therazzerapp.milorazlib.swing.SwingUtils;
import com.therazzerapp.milorazlib.swing.components.JPopupMenuBasic;
import de.pauleduardkoenig.dwds_cf.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * <description>
 *
 * @author Paul Eduard Koenig <rezzer101@googlemail.com>
 * @since 1.0.0
 */
public class GUIChangelog extends JDialog {
    private JPanel mPanel;
    private JTextPane logPane;

    public GUIChangelog() {
        $$$setupUI$$$();
        setTitle("Changelog");
        setResizable(false);
        setModal(true);
        setContentPane(mPanel);
        setSize(550, 400);
        setLocationRelativeTo(null);
        setIconImage(Constants.PROGRAMM_ICON);
        SwingUtils.registerJTextComponentPopupMenu(new JPopupMenuBasic(), mPanel);

        logPane.setText("""
                    <h1 id="changelog">Changelog</h1>
                    <p>All notable changes to this project will be documented in this file.</p>
                    <h2 id="-beta-builds-">[Beta Builds]</h2>
                    <h2 id="-1-1-1-14-02-2024">[1.1.1] - 14.02.2024</h2>
                    <h3 id="add">Add</h3>
                    <ul>
                    <li>Stop Button</li>
                    <li>Automatic Backups</li>
                    </ul>
                    <h3 id="fix">Fix</h3>
                    <ul>
                    <li>Bug if no export Folder was selected.</li>
                    <li>Refactoring</li>
                    </ul>
                    <h2 id="-1-0-0-14-02-2024">[1.0.0] - 14.02.2024</h2>
                    <h3 id="add">Add</h3>
                    <ul>
                    <li>Initial Release</li>
                    </ul>
                """);
        logPane.setCaretPosition(0);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mPanel = new JPanel();
        mPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JScrollPane scrollPane1 = new JScrollPane();
        mPanel.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        logPane = new JTextPane();
        logPane.setContentType("text/html");
        logPane.setEditable(false);
        scrollPane1.setViewportView(logPane);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mPanel;
    }

}
