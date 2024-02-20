package de.pauleduardkoenig.dwds_cf.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.therazzerapp.milorazlib.CollectionUtils;
import com.therazzerapp.milorazlib.container.Pair;
import com.therazzerapp.milorazlib.files.FileUtils;
import com.therazzerapp.milorazlib.logger.Logging;
import com.therazzerapp.milorazlib.swing.BasicNameTypeCellRenderer;
import com.therazzerapp.milorazlib.swing.SwingUtils;
import com.therazzerapp.milorazlib.swing.components.JPopupMenuBasic;
import com.therazzerapp.milorazlib.swing.components.JTextFieldLimit;
import de.pauleduardkoenig.dwds_cf.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.Year;
import java.util.List;
import java.util.*;

/**
 * <description>
 *
 * @author Paul Eduard Koenig <rezzer101@googlemail.com>
 * @since 0.1.0
 */
public class GUIMain{

	private final JPopupMenuBasic jPopupMenuBasic = new JPopupMenuBasic();
	private final JFrame frame;
	private final JMenuBar mainBar = new JMenuBar();
	private JTabbedPane tabbedPane;
	private JButton continueGeneralButton;
	private JTextField dccRequestField;
	private JButton continueWordsButton;
	private JButton returnWordsButton;
	private JButton continueCorporaButton;
	private JButton returnCorporaButton;
	private JButton finishButton;
	private JTextPane overviewPane;
	private JButton returnOverviewButton;
	private JPanel mPanel;
	private JComboBox<DWDSRequestBuilder.FormatType> formatCombobox;
	private JComboBox<DWDSRequestBuilder.SortType> sortCombobox;
	private JSpinner limitSpinner;
	private JSpinner pageSpinner;
	private JSpinner endSpinner;
	private JSpinner startSpinner;
	private JRadioButton belletristikFictionRadioButton;
	private JCheckBox cpKernCheck;
	private JCheckBox pageButton;
	private JTextArea wordsArea;
	private JTabbedPane viewSettingsTabbedPane;
	private JComboBox<DWDSRequestBuilder.ViewType> viewCombobox;
	private JCheckBox startButton;
	private JCheckBox endButton;
	private JCheckBox sortButton;
	private JCheckBox limitButton;
	private JButton continureExportButton;
	private JButton returnExportButton;
	private JSpinner csvSpacesSpinner;
	private JTextArea csvNewRowsArea;
	private JTextField csvSeparatorField;
	private JTextField csvEscapeField;
	private JRadioButton scienceRadioButton;
	private JRadioButton cosumerLiteraturRadioButton;
	private JRadioButton newspaperRadioButton;
	private JCheckBox cpCorpus21Check;
	private JCheckBox cpDtakCheck;
	private JCheckBox combineCorporaCheck;
	private JTabbedPane mTabbedPane;
	private JScrollPane logScrollPane;
	private JTextPane logPane;
	private JCheckBox cpdtaCheck;
	private JCheckBox cpdtaxlCheck;
	private JCheckBox cppublicCheck;
	private JCheckBox cpbzCheck;
	private JCheckBox cptspCheck;
	private JCheckBox cpblogsCheck;
	private JCheckBox cpdtaeCheck;
	private JCheckBox cpadgCheck;
	private JCheckBox cpdinglerCheck;
	private JCheckBox cpuntertitelCheck;
	private JCheckBox cpspkCheck;
	private JCheckBox cpddrCheck;
	private JCheckBox cppolitische_redenCheck;
	private JCheckBox cpbundestagCheck;
	private JCheckBox cpsoldatenbriefeCheck;
	private JCheckBox cpcopadocsCheck;
	private JCheckBox cpavhbernCheck;
	private JCheckBox cpbruedergemeineCheck;
	private JCheckBox cppitavalCheck;
	private JCheckBox cpjeanpaulCheck;
	private JCheckBox cpdekudeCheck;
	private JCheckBox cpnschatzdeuCheck;
	private JCheckBox cpstimmlosCheck;
	private JCheckBox cpwikibooksCheck;
	private JCheckBox cpwikipediaCheck;
	private JCheckBox cpwikivoyageCheck;
	private JCheckBox cpgesetzeCheck;
	private JSpinner jsonCollapseLevelSpinner;
	private JButton allButton;
	private JButton defaultButton;
	private JCheckBox showCorpusCheck;
	private JCheckBox jsonCombineCorporaCheck;
	private JSpinner tcfIndentLevel;
	private JTextField tcfIndentField;
	private JButton stopButton;
	private JCheckBox cpregionalCheck;
	private JCheckBox cpwebxlCheck;
	private JCheckBox cpbz_ppCheck;
	private JCheckBox cpfazCheck;
	private JCheckBox cpndCheck;
	private JButton allExtendedButton;
	private JCheckBox cpzeitCheck;
	private JCheckBox cpwendeCheck;
	private JCheckBox cptextbergCheck;
	private JCheckBox cpibk_dchatCheck;
	private JCheckBox cpwebCheck;
	private JCheckBox cpwebmonitorCheck;
	private JCheckBox cpballsportCheck;
	private JCheckBox cpjuraCheck;
	private JCheckBox cpmedizinCheck;
	private JCheckBox cpcoronaCheck;
	private JCheckBox cpmodeblogsBlogs;
	private JCheckBox cpit_blogsCheck;
	private JPanel generalPanel;
	private JLabel labelCopyright;
	private Thread fetchThread = null;

	public GUIMain(){
		this.frame = new JFrame(Constants.PROGRAMM_NAME + " " + Constants.VERSION);
		$$$setupUI$$$();
		createAndShowUI();
		initContent();
		createListener();
		createMenuBar();
		Logging.log(DWDS_CF.logger, "Running " + DWDS_CF.logger.getProgramName() + " v. " + Constants.VERSION + " | © 2024 - Paul Eduard Koenig");
		toggleViewSettings();
	}

	public static void init(){
		SwingUtilities.invokeLater(GUIMain::new);
	}

	private void createAndShowUI(){
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(true);
		frame.setContentPane(mPanel);
		frame.setSize(520, 600);
		frame.setMinimumSize(new Dimension(480, 500));
		frame.setLocationRelativeTo(null);
		frame.setIconImage(Constants.PROGRAMM_ICON);
		SwingUtils.registerJTextComponentPopupMenu(jPopupMenuBasic, mPanel);
		stopButton.setVisible(false);
	}

	private void initContent(){
		formatCombobox.setRenderer(new BasicNameTypeCellRenderer());
		sortCombobox.setRenderer(new BasicNameTypeCellRenderer());
		viewCombobox.setRenderer(new BasicNameTypeCellRenderer());
		for (DWDSRequestBuilder.SortType value : DWDSRequestBuilder.SortType.values()){
			sortCombobox.addItem(value);
		}
		for (DWDSRequestBuilder.ViewType value : DWDSRequestBuilder.ViewType.values()){
			viewCombobox.addItem(value);
		}
		for (DWDSRequestBuilder.FormatType value : DWDSRequestBuilder.FormatType.values()){
			formatCombobox.addItem(value);
		}
		wordsArea.setText(CollectionUtils.listToString(FileUtils.getFileContent(new File(DWDS_CF.config.getAsString(ConfigType.WORDLIST)), StandardCharsets.UTF_8, DWDS_CF.logger), "\n"));
		csvNewRowsArea.setText(CollectionUtils.listToString(FileUtils.getFileContent(new File(DWDS_CF.config.getAsString(ConfigType.CSV_CUSTOM_ROWS)), StandardCharsets.UTF_8, DWDS_CF.logger), "\n"));

		tabbedPane.setEnabledAt(1, false);
		tabbedPane.setEnabledAt(2, false);
		tabbedPane.setEnabledAt(3, false);
		tabbedPane.setEnabledAt(4, false);

		logScrollPane.setAutoscrolls(true);
		logScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		logPane.setText("");
		DWDS_CF.logger.setLogPane(logPane);
		DWDS_CF.logger.setScrollPane(logScrollPane);

		combineCorporaCheck.setEnabled(formatCombobox.getSelectedItem() == DWDSRequestBuilder.FormatType.KWIC);
		showCorpusCheck.setEnabled(formatCombobox.getSelectedItem() == DWDSRequestBuilder.FormatType.KWIC);

		csvEscapeField.setDocument(new JTextFieldLimit(1));
		csvSeparatorField.setDocument(new JTextFieldLimit(1));
	}

	private void createMenuBar(){
        /*
        File Menu
         */
		JMenu fileMenu = new JMenu("File");
		//Settings
		JMenuItem settings = new JMenuItem("Settings");
		settings.addActionListener(e -> {
			GUISettings guiSettings = new GUISettings();
			guiSettings.setVisible(true);
		});
		fileMenu.add(settings);
		fileMenu.add(new JSeparator());

		//Exit
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(e -> System.exit(0));
		fileMenu.add(exit);

        /*
        Help Menu
         */
		JMenu helpMenu = new JMenu("Help");

		//About
		JMenuItem about = new JMenuItem("About");
		about.addActionListener(e -> {
			GUIAbout guiAbout = new GUIAbout();
			guiAbout.setVisible(true);
		});

		//Changelog
		JMenuItem changelog = new JMenuItem("Changelog");
		changelog.addActionListener(e -> {
			GUIChangelog changeloggui = new GUIChangelog();
			changeloggui.setVisible(true);
		});
		helpMenu.add(changelog);
		helpMenu.add(about);

		mainBar.add(fileMenu);
		mainBar.add(helpMenu);
		frame.setJMenuBar(mainBar);
	}

	private void toggleViewSettings(){
		csvSeparatorField.setEditable(viewCombobox.getSelectedItem() == DWDSRequestBuilder.ViewType.CSV);
		csvSeparatorField.setEnabled(viewCombobox.getSelectedItem() == DWDSRequestBuilder.ViewType.CSV);
		switch ((DWDSRequestBuilder.ViewType) viewCombobox.getSelectedItem()){
			case CSV, TSV -> {
				viewSettingsTabbedPane.setSelectedIndex(0);
				viewSettingsTabbedPane.setEnabledAt(0, true);
				viewSettingsTabbedPane.setEnabledAt(1, false);
				viewSettingsTabbedPane.setEnabledAt(2, false);
			}
			case JSON -> {
				viewSettingsTabbedPane.setSelectedIndex(1);
				viewSettingsTabbedPane.setEnabledAt(0, false);
				viewSettingsTabbedPane.setEnabledAt(1, true);
				viewSettingsTabbedPane.setEnabledAt(2, false);
			}
			case TCF -> {
				viewSettingsTabbedPane.setSelectedIndex(2);
				viewSettingsTabbedPane.setEnabledAt(0, false);
				viewSettingsTabbedPane.setEnabledAt(1, false);
				viewSettingsTabbedPane.setEnabledAt(2, true);
			}
		}
	}

	private String compileOverviewText(){
		List<String> genres = new LinkedList<>();
		if (belletristikFictionRadioButton.isSelected()){
			genres.add("Fiction");
		}
		if (scienceRadioButton.isSelected()){
			genres.add("Science");
		}
		if (cosumerLiteraturRadioButton.isSelected()){
			genres.add("Consumer Literature");
		}
		if (newspaperRadioButton.isSelected()){
			genres.add("Newspaper");
		}
		String exportSettings = "";
		if (viewCombobox.getSelectedItem() == DWDSRequestBuilder.ViewType.CSV || viewCombobox.getSelectedItem() == DWDSRequestBuilder.ViewType.TSV){
			exportSettings = "<b>Combine Corpora:</b> " + (combineCorporaCheck.isSelected() ? "Yes" : "No") + "<br>" +
					"<b>Add Corpus Info:</b> " + (showCorpusCheck.isSelected() ? "Yes" : "No") + "<br>" +
					"<b>Spaces:</b> \"" + csvSpacesSpinner.getValue() + "\"<br>" +
					"<b>Separator:</b> \"" + csvSeparatorField.getText() + "\"<br>" +
					"<b>Escape:</b> \"" + csvEscapeField.getText() + "\"<br>" +
					"<b>New Rows:</b> \"" + CollectionUtils.listToString(Arrays.stream(csvNewRowsArea.getText().split("\n")).toList(), 6, "<br>") + "\"<br>";
		} else if (viewCombobox.getSelectedItem() == DWDSRequestBuilder.ViewType.JSON){
			exportSettings = "<b>Collapse Level:</b> \"" + jsonCollapseLevelSpinner.getValue() + "\"<br>";
		}
		return "<b>DCC:</b> \"" + dccRequestField.getText() + "\"<br>" +
				"<b>Year Start:</b> " + (startButton.isSelected() ? startSpinner.getValue() : "") + "<br>" +
				"<b>Year End:</b> " + (endButton.isSelected() ? endSpinner.getValue() : "") + "<br>" +
				"<b>Genre:</b> " + CollectionUtils.listToString(genres) + "<br>" +
				"<b>Format:</b> \"" + ((DWDSRequestBuilder.FormatType) formatCombobox.getSelectedItem()).getName() + "\"<br>" +
				"<b>Sort:</b> \"" + (sortButton.isSelected() ? ((DWDSRequestBuilder.SortType) sortCombobox.getSelectedItem()).getName() : "Date descending (default)") + "\"<br>" +
				"<b>Limit:</b> \"" + (limitButton.isSelected() ? limitSpinner.getValue() : "50 (default)") + "\"<br>" +
				"<b>Page:</b> " + (pageButton.isSelected() ? pageSpinner.getValue() : "") + "<br>" +
				"<b>Corpora:</b> \"" + CollectionUtils.listToString(getSelectedCorpora(), 6, "<br>") + "\"<br>" +
				"<b>Words:</b> \"" + CollectionUtils.listToString(Arrays.stream(wordsArea.getText().split("\n")).toList(), 6, "<br>") + "\"<br>" +
				"<b>View:</b> \"" + ((DWDSRequestBuilder.ViewType) viewCombobox.getSelectedItem()).getName() + "\"<br>" +
				exportSettings;
	}

	private void createListener(){
		SwingUtils.registerSettingsToggleButtonTextArea(startButton, DWDS_CF.config, ConfigType.REQ_YEAR_START_ENABLED, Set.of(startSpinner));
		SwingUtils.registerSettingsToggleButtonTextArea(endButton, DWDS_CF.config, ConfigType.REQ_YEAR_END_ENABLED, Set.of(endSpinner));
		SwingUtils.registerSettingsToggleButtonTextArea(sortButton, DWDS_CF.config, ConfigType.REQ_SORT_ENABLED, Set.of(sortCombobox));
		SwingUtils.registerSettingsToggleButtonTextArea(limitButton, DWDS_CF.config, ConfigType.REQ_LIMIT_ENABLED, Set.of(limitSpinner));
		SwingUtils.registerSettingsToggleButtonTextArea(pageButton, DWDS_CF.config, ConfigType.REQ_PAGE_ENABLED, Set.of(pageSpinner));
		SwingUtils.registerTextComponent(dccRequestField, DWDS_CF.config, ConfigType.REQ_DCC);
		SwingUtils.registerSettingsSpinnerInt(startSpinner, DWDS_CF.config, ConfigType.REQ_DATE_START);
		SwingUtils.registerSettingsSpinnerInt(endSpinner, DWDS_CF.config, ConfigType.REQ_DATE_END);
		SwingUtils.registerSettingsSpinnerInt(limitSpinner, DWDS_CF.config, ConfigType.REQ_LIMIT);
		SwingUtils.registerSettingsSpinnerInt(pageSpinner, DWDS_CF.config, ConfigType.REQ_PAGE);

		tabbedPane.addChangeListener(e -> {
			if (tabbedPane.getSelectedIndex() == tabbedPane.getTabCount() - 1){
				overviewPane.setText(compileOverviewText());
			}
		});

		// General
		sortButton.setSelected(DWDS_CF.config.getAsBoolean(ConfigType.REQ_SORT_ENABLED));
		sortButton.addActionListener(e -> {
			DWDS_CF.config.setConfigProperty(ConfigType.REQ_SORT_ENABLED, sortButton.isSelected());
			sortCombobox.setEnabled(sortButton.isSelected());
		});
		try{
			sortCombobox.setSelectedItem(DWDSRequestBuilder.SortType.valueOf(DWDS_CF.config.getAsString(ConfigType.REQ_SORT).toUpperCase()));
		} catch (IllegalArgumentException e){
			Logging.errorStackTrace(DWDS_CF.logger, e, "Invalid sort setting in config: " + DWDS_CF.config.getAsString(ConfigType.REQ_SORT));
			sortCombobox.setSelectedItem(null);
		}
		sortCombobox.addActionListener(e -> DWDS_CF.config.setConfigProperty(ConfigType.REQ_SORT, sortCombobox.getSelectedItem()));

		try{
			formatCombobox.setSelectedItem(DWDSRequestBuilder.FormatType.valueOf(DWDS_CF.config.getAsString(ConfigType.REQ_FORMAT).toUpperCase()));
		} catch (IllegalArgumentException e){
			Logging.errorStackTrace(DWDS_CF.logger, e, "Invalid format setting in config: " + DWDS_CF.config.getAsString(ConfigType.REQ_FORMAT));
			formatCombobox.setSelectedItem(null);
		}
		formatCombobox.addActionListener(e -> {
			DWDS_CF.config.setConfigProperty(ConfigType.REQ_FORMAT, formatCombobox.getSelectedItem());
			combineCorporaCheck.setEnabled(formatCombobox.getSelectedItem() == DWDSRequestBuilder.FormatType.KWIC);
			showCorpusCheck.setEnabled(formatCombobox.getSelectedItem() == DWDSRequestBuilder.FormatType.KWIC);
		});

		dccRequestField.getDocument().addDocumentListener(new DocumentListener(){
			@Override
			public void insertUpdate(DocumentEvent e){
				update();
			}

			@Override
			public void removeUpdate(DocumentEvent e){
				update();
			}

			@Override
			public void changedUpdate(DocumentEvent e){
				update();
			}

			private void update(){
				if (dccRequestField.getText().isBlank()){
					Logging.warning(DWDS_CF.logger, "No DCC prompt found!");
					dccRequestField.setBackground(Color.PINK);
					tabbedPane.setEnabledAt(1, false);
					tabbedPane.setEnabledAt(2, false);
					tabbedPane.setEnabledAt(3, false);
					tabbedPane.setEnabledAt(4, false);
				} else {
					dccRequestField.setBackground(Color.WHITE);
				}
			}
		});

		continueGeneralButton.addActionListener(e -> {
			if (dccRequestField.getText().isBlank()){
				dccRequestField.setBackground(Color.PINK);
				tabbedPane.setEnabledAt(1, false);
				tabbedPane.setEnabledAt(2, false);
				tabbedPane.setEnabledAt(3, false);
				tabbedPane.setEnabledAt(4, false);
			} else {
				dccRequestField.setBackground(Color.WHITE);
				tabbedPane.setEnabledAt(1, true);
				tabbedPane.setSelectedIndex(1);
			}
		});

		// Corpora
		returnCorporaButton.addActionListener(e ->
				tabbedPane.setSelectedIndex(0));
		continueCorporaButton.addActionListener(e -> {
			tabbedPane.setEnabledAt(2, true);
			tabbedPane.setSelectedIndex(2);
		});

		SwingUtils.registerSettingsToggleButton(cpKernCheck, DWDS_CF.config, ConfigType.CORPORA_KERN);
		SwingUtils.registerSettingsToggleButton(cpDtakCheck, DWDS_CF.config, ConfigType.CORPORA_DTAK);
		SwingUtils.registerSettingsToggleButton(cpCorpus21Check, DWDS_CF.config, ConfigType.CORPORA_KORPUS21);
		SwingUtils.registerSettingsToggleButton(cpdtaCheck, DWDS_CF.config, ConfigType.CORPORA_DTA);
		SwingUtils.registerSettingsToggleButton(cpdtaxlCheck, DWDS_CF.config, ConfigType.CORPORA_DTAXL);
		SwingUtils.registerSettingsToggleButton(cppublicCheck, DWDS_CF.config, ConfigType.CORPORA_PUBLIC);
		SwingUtils.registerSettingsToggleButton(cpbzCheck, DWDS_CF.config, ConfigType.CORPORA_BZ);
		SwingUtils.registerSettingsToggleButton(cptspCheck, DWDS_CF.config, ConfigType.CORPORA_TSP);
		SwingUtils.registerSettingsToggleButton(cpblogsCheck, DWDS_CF.config, ConfigType.CORPORA_BLOGS);
		SwingUtils.registerSettingsToggleButton(cpdtaeCheck, DWDS_CF.config, ConfigType.CORPORA_DTAE);
		SwingUtils.registerSettingsToggleButton(cpadgCheck, DWDS_CF.config, ConfigType.CORPORA_ADG);
		SwingUtils.registerSettingsToggleButton(cpdinglerCheck, DWDS_CF.config, ConfigType.CORPORA_DINGLER);
		SwingUtils.registerSettingsToggleButton(cpuntertitelCheck, DWDS_CF.config, ConfigType.CORPORA_UNTERTITEL);
		SwingUtils.registerSettingsToggleButton(cpspkCheck, DWDS_CF.config, ConfigType.CORPORA_SPK);
		SwingUtils.registerSettingsToggleButton(cpddrCheck, DWDS_CF.config, ConfigType.CORPORA_DDR);
		SwingUtils.registerSettingsToggleButton(cppolitische_redenCheck, DWDS_CF.config, ConfigType.CORPORA_POLITISCHEREDEN);
		SwingUtils.registerSettingsToggleButton(cpbundestagCheck, DWDS_CF.config, ConfigType.CORPORA_BUNDESTAG);
		SwingUtils.registerSettingsToggleButton(cpsoldatenbriefeCheck, DWDS_CF.config, ConfigType.CORPORA_SOLDATENBRIEFE);
		SwingUtils.registerSettingsToggleButton(cpcopadocsCheck, DWDS_CF.config, ConfigType.CORPORA_COPADOCS);
		SwingUtils.registerSettingsToggleButton(cpavhbernCheck, DWDS_CF.config, ConfigType.CORPORA_AVHBERN);
		SwingUtils.registerSettingsToggleButton(cpbruedergemeineCheck, DWDS_CF.config, ConfigType.CORPORA_BRUEDERGEMEINE);
		SwingUtils.registerSettingsToggleButton(cppitavalCheck, DWDS_CF.config, ConfigType.CORPORA_PITAVAL);
		SwingUtils.registerSettingsToggleButton(cpjeanpaulCheck, DWDS_CF.config, ConfigType.CORPORA_JEANPAUL);
		SwingUtils.registerSettingsToggleButton(cpdekudeCheck, DWDS_CF.config, ConfigType.CORPORA_DEKUDE);
		SwingUtils.registerSettingsToggleButton(cpnschatzdeuCheck, DWDS_CF.config, ConfigType.CORPORA_NSCHATZDEU);
		SwingUtils.registerSettingsToggleButton(cpstimmlosCheck, DWDS_CF.config, ConfigType.CORPORA_STIMMLOS);
		SwingUtils.registerSettingsToggleButton(cpwikibooksCheck, DWDS_CF.config, ConfigType.CORPORA_WIKIBOOKS);
		SwingUtils.registerSettingsToggleButton(cpwikipediaCheck, DWDS_CF.config, ConfigType.CORPORA_WIKIPEDIA);
		SwingUtils.registerSettingsToggleButton(cpwikivoyageCheck, DWDS_CF.config, ConfigType.CORPORA_WIKIVOYAGE);
		SwingUtils.registerSettingsToggleButton(cpgesetzeCheck, DWDS_CF.config, ConfigType.CORPORA_GESETZE);

		SwingUtils.registerSettingsToggleButton(cpregionalCheck, DWDS_CF.config, ConfigType.CORPORA_CPREGIONAL);
		SwingUtils.registerSettingsToggleButton(cpwebxlCheck, DWDS_CF.config, ConfigType.CORPORA_CPWEBXL);
		SwingUtils.registerSettingsToggleButton(cpbz_ppCheck, DWDS_CF.config, ConfigType.CORPORA_CPBZ_PP);
		SwingUtils.registerSettingsToggleButton(cpfazCheck, DWDS_CF.config, ConfigType.CORPORA_CPFAZ);
		SwingUtils.registerSettingsToggleButton(cpndCheck, DWDS_CF.config, ConfigType.CORPORA_CPND);
		SwingUtils.registerSettingsToggleButton(cpzeitCheck, DWDS_CF.config, ConfigType.CORPORA_CPZEIT);
		SwingUtils.registerSettingsToggleButton(cpwebCheck, DWDS_CF.config, ConfigType.CORPORA_CPWEB);
		SwingUtils.registerSettingsToggleButton(cpwebmonitorCheck, DWDS_CF.config, ConfigType.CORPORA_CPWEBMONITOR);
		SwingUtils.registerSettingsToggleButton(cpballsportCheck, DWDS_CF.config, ConfigType.CORPORA_CPBALLSPORT);
		SwingUtils.registerSettingsToggleButton(cpjuraCheck, DWDS_CF.config, ConfigType.CORPORA_CPJURA);
		SwingUtils.registerSettingsToggleButton(cpmedizinCheck, DWDS_CF.config, ConfigType.CORPORA_CPMEDIZIN);
		SwingUtils.registerSettingsToggleButton(cpcoronaCheck, DWDS_CF.config, ConfigType.CORPORA_CPCORONA);
		SwingUtils.registerSettingsToggleButton(cpmodeblogsBlogs, DWDS_CF.config, ConfigType.CORPORA_CPMODEBLOGS);
		SwingUtils.registerSettingsToggleButton(cpit_blogsCheck, DWDS_CF.config, ConfigType.CORPORA_CPIT_BLOGS);
		SwingUtils.registerSettingsToggleButton(cpibk_dchatCheck, DWDS_CF.config, ConfigType.CORPORA_CPIBK_DCHAT);
		SwingUtils.registerSettingsToggleButton(cptextbergCheck, DWDS_CF.config, ConfigType.CORPORA_CPTEXTBERG);
		SwingUtils.registerSettingsToggleButton(cpwendeCheck, DWDS_CF.config, ConfigType.CORPORA_CPWENDE);

		List<JCheckBox> corpusCheckBoxes = new LinkedList<>();
		corpusCheckBoxes.add(cpKernCheck);
		corpusCheckBoxes.add(cpDtakCheck);
		corpusCheckBoxes.add(cpCorpus21Check);
		corpusCheckBoxes.add(cpdtaCheck);
		corpusCheckBoxes.add(cpdtaxlCheck);
		corpusCheckBoxes.add(cppublicCheck);
		corpusCheckBoxes.add(cpbzCheck);
		corpusCheckBoxes.add(cptspCheck);
		corpusCheckBoxes.add(cpblogsCheck);
		corpusCheckBoxes.add(cpdtaeCheck);
		corpusCheckBoxes.add(cpadgCheck);
		corpusCheckBoxes.add(cpdinglerCheck);
		corpusCheckBoxes.add(cpuntertitelCheck);
		corpusCheckBoxes.add(cpspkCheck);
		corpusCheckBoxes.add(cpddrCheck);
		corpusCheckBoxes.add(cppolitische_redenCheck);
		corpusCheckBoxes.add(cpbundestagCheck);
		corpusCheckBoxes.add(cpsoldatenbriefeCheck);
		corpusCheckBoxes.add(cpcopadocsCheck);
		corpusCheckBoxes.add(cpavhbernCheck);
		corpusCheckBoxes.add(cpbruedergemeineCheck);
		corpusCheckBoxes.add(cppitavalCheck);
		corpusCheckBoxes.add(cpjeanpaulCheck);
		corpusCheckBoxes.add(cpdekudeCheck);
		corpusCheckBoxes.add(cpnschatzdeuCheck);
		corpusCheckBoxes.add(cpstimmlosCheck);
		corpusCheckBoxes.add(cpwikibooksCheck);
		corpusCheckBoxes.add(cpwikipediaCheck);
		corpusCheckBoxes.add(cpwikivoyageCheck);
		corpusCheckBoxes.add(cpgesetzeCheck);

		List<JCheckBox> corpusCheckBoxesExtended = new LinkedList<>(corpusCheckBoxes);
		corpusCheckBoxesExtended.add(cpregionalCheck);
		corpusCheckBoxesExtended.add(cpwebxlCheck);
		corpusCheckBoxesExtended.add(cpbz_ppCheck);
		corpusCheckBoxesExtended.add(cpfazCheck);
		corpusCheckBoxesExtended.add(cpndCheck);
		corpusCheckBoxesExtended.add(cpzeitCheck);
		corpusCheckBoxesExtended.add(cpwebCheck);
		corpusCheckBoxesExtended.add(cpwebmonitorCheck);
		corpusCheckBoxesExtended.add(cpballsportCheck);
		corpusCheckBoxesExtended.add(cpjuraCheck);
		corpusCheckBoxesExtended.add(cpmedizinCheck);
		corpusCheckBoxesExtended.add(cpcoronaCheck);
		corpusCheckBoxesExtended.add(cpmodeblogsBlogs);
		corpusCheckBoxesExtended.add(cpit_blogsCheck);
		corpusCheckBoxesExtended.add(cpibk_dchatCheck);
		corpusCheckBoxesExtended.add(cptextbergCheck);
		corpusCheckBoxesExtended.add(cpwendeCheck);
		allExtendedButton.addActionListener(e -> {
			for (JCheckBox corpusCheckBox : corpusCheckBoxesExtended){
				corpusCheckBox.setSelected(true);
				for (ActionListener actionListener : corpusCheckBox.getActionListeners()){
					actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""){
					});
				}
			}
		});

		allButton.addActionListener(e -> {
			for (JCheckBox corpusCheckBox : corpusCheckBoxesExtended){
				corpusCheckBox.setSelected(corpusCheckBoxes.contains(corpusCheckBox));
				for (ActionListener actionListener : corpusCheckBox.getActionListeners()){
					actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""){
					});
				}
			}
		});

		defaultButton.addActionListener(e -> {
			for (JCheckBox corpusCheckBox : corpusCheckBoxesExtended){
				corpusCheckBox.setSelected(false);
				for (ActionListener actionListener : corpusCheckBox.getActionListeners()){
					actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""){
					});
				}
			}
			cpKernCheck.doClick();
		});

		// Words
		returnWordsButton.addActionListener(e ->
				tabbedPane.setSelectedIndex(1));
		continueWordsButton.addActionListener(e -> {
			tabbedPane.setEnabledAt(3, true);
			tabbedPane.setSelectedIndex(3);
		});
		wordsArea.getDocument().addDocumentListener(new DocumentListener(){
			@Override
			public void insertUpdate(DocumentEvent e){
				update();
			}

			@Override
			public void removeUpdate(DocumentEvent e){
				update();
			}

			@Override
			public void changedUpdate(DocumentEvent e){
				update();
			}

			private void update(){
				FileUtils.exportFile(wordsArea.getText(), new File(DWDS_CF.config.getAsString(ConfigType.WORDLIST)), StandardCharsets.UTF_8, DWDS_CF.logger);
			}
		});

		// Export
		SwingUtils.registerSettingsToggleButton(combineCorporaCheck, DWDS_CF.config, ConfigType.CSV_EXPORT_COMBINE_CORPORA);
		try{
			viewCombobox.setSelectedItem(DWDSRequestBuilder.ViewType.valueOf(DWDS_CF.config.getAsString(ConfigType.REQ_VIEW).toUpperCase()));
		} catch (IllegalArgumentException e){
			Logging.errorStackTrace(DWDS_CF.logger, e, "Invalid view setting in config: " + DWDS_CF.config.getAsString(ConfigType.REQ_VIEW));
			viewCombobox.setSelectedItem(null);
		}
		viewCombobox.addActionListener(e -> DWDS_CF.config.setConfigProperty(ConfigType.REQ_VIEW, viewCombobox.getSelectedItem()));

		returnExportButton.addActionListener(e ->
				tabbedPane.setSelectedIndex(2));
		continureExportButton.addActionListener(e -> {
			tabbedPane.setEnabledAt(4, true);
			tabbedPane.setSelectedIndex(4);
		});
		SwingUtils.registerSettingsToggleButton(showCorpusCheck, DWDS_CF.config, ConfigType.CSV_SHOW_CORPUS);
		SwingUtils.registerSettingsToggleButton(jsonCombineCorporaCheck, DWDS_CF.config, ConfigType.JSON_COMBINE);
		SwingUtils.registerSettingsSpinnerInt(csvSpacesSpinner, DWDS_CF.config, ConfigType.CSV_SPACES);
		SwingUtils.registerSettingsSpinnerInt(tcfIndentLevel, DWDS_CF.config, ConfigType.TCF_INDENT_LEVEL);
		SwingUtils.registerSettingsSpinnerInt(jsonCollapseLevelSpinner, DWDS_CF.config, ConfigType.JSON_COLLAPSE_LEVEL);

		SwingUtils.registerTextComponent(tcfIndentField, DWDS_CF.config, ConfigType.TCF_INDENT);

		SwingUtils.registerTextComponent(csvEscapeField, DWDS_CF.config, ConfigType.CSV_ESCAPE);
		try{
			csvEscapeField.setText(String.valueOf(DWDS_CF.config.getAsString(ConfigType.CSV_ESCAPE).charAt(0)));
		} catch (IndexOutOfBoundsException ignored){
		}
		csvEscapeField.getDocument().addDocumentListener(SwingUtils.getSimpleTextFieldListener(e -> {
			try{
				DWDS_CF.config.setConfigProperty(ConfigType.CSV_ESCAPE, csvEscapeField.getText().charAt(0));
			} catch (IndexOutOfBoundsException ignored){
			}
		}));

		try{
			csvSeparatorField.setText(String.valueOf(DWDS_CF.config.getAsString(ConfigType.CSV_SEPARATOR).charAt(0)));
		} catch (IndexOutOfBoundsException ignored){
		}
		csvSeparatorField.getDocument().addDocumentListener(SwingUtils.getSimpleTextFieldListener(e -> {
			try{
				DWDS_CF.config.setConfigProperty(ConfigType.CSV_SEPARATOR, csvSeparatorField.getText().charAt(0));
			} catch (IndexOutOfBoundsException ignored){
			}
		}));

		csvNewRowsArea.getDocument().addDocumentListener(new DocumentListener(){
			@Override
			public void insertUpdate(DocumentEvent e){
				update();
			}

			@Override
			public void removeUpdate(DocumentEvent e){
				update();
			}

			@Override
			public void changedUpdate(DocumentEvent e){
				update();
			}

			private void update(){
				FileUtils.exportFile(csvNewRowsArea.getText(), new File(DWDS_CF.config.getAsString(ConfigType.CSV_CUSTOM_ROWS)), StandardCharsets.UTF_8, DWDS_CF.logger);
			}
		});
		viewCombobox.addActionListener(e -> toggleViewSettings());

		// Overview
		returnOverviewButton.addActionListener(e -> tabbedPane.setSelectedIndex(3));
		finishButton.addActionListener(e -> {
			RequestCompiler.compileThreadRunning = true;
			Pair<String, Boolean> save = SwingUtils.getFolderPath("Select folder to save files.", null, frame);
			if (!save.getValue()){
				Logging.error(DWDS_CF.logger, "No folder selected!");
				return;
			}
			DWDSRequestBuilder requestBuilder = compileRequest();
			fetchThread = new Thread(() -> {
				int origin = mTabbedPane.getSelectedIndex();
				inCompile(true, origin);
				boolean combine = (((requestBuilder.getView() == DWDSRequestBuilder.ViewType.CSV || requestBuilder.getView() == DWDSRequestBuilder.ViewType.TSV) && combineCorporaCheck.isSelected() && combineCorporaCheck.isEnabled()) || (requestBuilder.getView() == DWDSRequestBuilder.ViewType.JSON && jsonCombineCorporaCheck.isSelected()));
				boolean finished = RequestCompiler.compile(requestBuilder, wordsArea.getText(), getSelectedCorpora(), combine, save.getKey(), csvNewRowsArea.getText());
				inCompile(false, origin);
				if (finished){
					Utils.openPathInDesktop(save.getKey());
				}
			});
			fetchThread.start();
		});

		stopButton.addActionListener(e -> {
			RequestCompiler.compileThreadRunning = false;
			Logging.log(DWDS_CF.logger, "(Fetcher) Stopped!");
			tabbedPane.setSelectedIndex(0);
		});
	}

	private DWDSRequestBuilder compileRequest(){
		DWDSRequestBuilder requestBuilder = new DWDSRequestBuilder();
		requestBuilder.setDdc(dccRequestField.getText().trim());
		if (limitButton.isSelected()){
			requestBuilder.setLimit((int) SwingUtils.getJSpinnerValue(limitSpinner, DWDS_CF.logger));
		}
		requestBuilder.setFormat((DWDSRequestBuilder.FormatType) formatCombobox.getSelectedItem());
		if (sortButton.isSelected()){
			requestBuilder.setSort((DWDSRequestBuilder.SortType) sortCombobox.getSelectedItem());
		}
		requestBuilder.setView((DWDSRequestBuilder.ViewType) viewCombobox.getSelectedItem());
		if (pageButton.isSelected()){
			requestBuilder.setPage((int) SwingUtils.getJSpinnerValue(pageSpinner, DWDS_CF.logger));
		}
		if (startButton.isSelected()){
			requestBuilder.setDateStart((int) SwingUtils.getJSpinnerValue(startSpinner, DWDS_CF.logger));
		}
		if (endButton.isSelected()){
			requestBuilder.setDateEnd((int) SwingUtils.getJSpinnerValue(endSpinner, DWDS_CF.logger));
		}
		if (belletristikFictionRadioButton.isSelected()){
			requestBuilder.addGenre("Belletristik");
		}
		if (scienceRadioButton.isSelected()){
			requestBuilder.addGenre("Wissenschaft");
		}
		if (cosumerLiteraturRadioButton.isSelected()){
			requestBuilder.addGenre("Gebrauchsliteratur");
		}
		if (newspaperRadioButton.isSelected()){
			requestBuilder.addGenre("Zeitung");
		}
		if (!belletristikFictionRadioButton.isSelected() && !scienceRadioButton.isSelected() && !cosumerLiteraturRadioButton.isSelected() && !newspaperRadioButton.isSelected()){
			requestBuilder.setGenre(new HashSet<>());
		}
		return requestBuilder;
	}

	public Set<String> getSelectedCorpora(){
		Set<String> corpora = new LinkedHashSet<>();
		if (cpKernCheck.isSelected()){
			corpora.add("kern");
		}
		if (cpCorpus21Check.isSelected()){
			corpora.add("korpus21");
		}
		if (cpDtakCheck.isSelected()){
			corpora.add("dtak");
		}

		if (cpdtaCheck.isSelected()){
			corpora.add("dta");
		}
		if (cpdtaxlCheck.isSelected()){
			corpora.add("dtaxl");
		}
		if (cppublicCheck.isSelected()){
			corpora.add("public");
		}

		if (cpregionalCheck.isSelected()){
			corpora.add("regional");
		}
		if (cpwebxlCheck.isSelected()){
			corpora.add("webxl");
		}
		if (cpbz_ppCheck.isSelected()){
			corpora.add("bz_pp");
		}

		if (cpbzCheck.isSelected()){
			corpora.add("bz");
		}

		if (cpfazCheck.isSelected()){
			corpora.add("faz");
		}
		if (cpndCheck.isSelected()){
			corpora.add("nd");
		}

		if (cptspCheck.isSelected()){
			corpora.add("tsp");
		}

		if (cpzeitCheck.isSelected()){
			corpora.add("zeit");
		}
		if (cpwebCheck.isSelected()){
			corpora.add("web");
		}
		if (cpwebmonitorCheck.isSelected()){
			corpora.add("webmonitor");
		}
		if (cpballsportCheck.isSelected()){
			corpora.add("ballsport");
		}
		if (cpjuraCheck.isSelected()){
			corpora.add("jura");
		}
		if (cpmedizinCheck.isSelected()){
			corpora.add("medizin");
		}
		if (cpcoronaCheck.isSelected()){
			corpora.add("corona");
		}
		if (cpmodeblogsBlogs.isSelected()){
			corpora.add("modeblogs");
		}
		if (cpit_blogsCheck.isSelected()){
			corpora.add("it_blogs");
		}

		if (cpblogsCheck.isSelected()){
			corpora.add("blogs");
		}
		if (cpdtaeCheck.isSelected()){
			corpora.add("dtae");
		}
		if (cpadgCheck.isSelected()){
			corpora.add("adg");
		}
		if (cpdinglerCheck.isSelected()){
			corpora.add("dingler");
		}
		if (cpibk_dchatCheck.isSelected()){
			corpora.add("ibk_dchat");
		}
		if (cpuntertitelCheck.isSelected()){
			corpora.add("untertitel");
		}
		if (cpspkCheck.isSelected()){
			corpora.add("spk");
		}
		if (cptextbergCheck.isSelected()){
			corpora.add("textberg");
		}
		if (cpwendeCheck.isSelected()){
			corpora.add("wende");
		}
		if (cpddrCheck.isSelected()){
			corpora.add("ddr");
		}
		if (cppolitische_redenCheck.isSelected()){
			corpora.add("politische_reden");
		}
		if (cpbundestagCheck.isSelected()){
			corpora.add("bundestag");
		}
		if (cpsoldatenbriefeCheck.isSelected()){
			corpora.add("soldatenbriefe");
		}
		if (cpcopadocsCheck.isSelected()){
			corpora.add("copadocs");
		}
		if (cpavhbernCheck.isSelected()){
			corpora.add("avh-bern");
		}
		if (cpbruedergemeineCheck.isSelected()){
			corpora.add("bruedergemeine");
		}
		if (cppitavalCheck.isSelected()){
			corpora.add("pitaval");
		}
		if (cpjeanpaulCheck.isSelected()){
			corpora.add("jean_paul");
		}
		if (cpdekudeCheck.isSelected()){
			corpora.add("dekude");
		}
		if (cpnschatzdeuCheck.isSelected()){
			corpora.add("nschatz_deu");
		}
		if (cpstimmlosCheck.isSelected()){
			corpora.add("stimm-los");
		}
		if (cpwikibooksCheck.isSelected()){
			corpora.add("wikibooks");
		}
		if (cpwikipediaCheck.isSelected()){
			corpora.add("wikipedia");
		}
		if (cpwikivoyageCheck.isSelected()){
			corpora.add("wikivoyage");
		}
		if (cpgesetzeCheck.isSelected()){
			corpora.add("gesetze");
		}

		if (corpora.isEmpty()){
			Logging.error(DWDS_CF.logger, "No corpus selected. Picked default coprus (kern)!");
			corpora.add("kern");
		}
		return corpora;
	}

	public void inCompile(boolean isCompiling, int origin){
		stopButton.setVisible(isCompiling);
		stopButton.setEnabled(isCompiling);
		mTabbedPane.setSelectedIndex(isCompiling ? 1 : origin);
		mTabbedPane.setEnabledAt(0, !isCompiling);
		for (int i = 0; i < mainBar.getMenuCount(); i++){
			mainBar.getMenu(i).setEnabled(!isCompiling);
		}
	}

	private void createUIComponents(){
		startSpinner = SwingUtils.createDefaultSpinner(DWDS_CF.config.getAsInt(ConfigType.REQ_DATE_START), 0, Year.now().getValue(), 1);
		endSpinner = SwingUtils.createDefaultSpinner(DWDS_CF.config.getAsInt(ConfigType.REQ_DATE_END), 1, Year.now().getValue(), 1);
		limitSpinner = SwingUtils.createDefaultSpinner(DWDS_CF.config.getAsInt(ConfigType.REQ_LIMIT), 1, 5000, 1);
		pageSpinner = SwingUtils.createDefaultSpinner(DWDS_CF.config.getAsInt(ConfigType.REQ_PAGE), 1, 5000, 1);
		csvSpacesSpinner = SwingUtils.createDefaultSpinner(DWDS_CF.config.getAsInt(ConfigType.CSV_SPACES), 0, 200, 1);
		jsonCollapseLevelSpinner = SwingUtils.createDefaultSpinner(DWDS_CF.config.getAsInt(ConfigType.JSON_COLLAPSE_LEVEL), 0, 200, 1);
		tcfIndentLevel = SwingUtils.createDefaultSpinner(DWDS_CF.config.getAsInt(ConfigType.TCF_INDENT_LEVEL), 0, 100, 1);
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$(){
		createUIComponents();
		mPanel = new JPanel();
		mPanel.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
		labelCopyright = new JLabel();
		labelCopyright.setBackground(new Color(-11314853));
		labelCopyright.setForeground(new Color(-6776680));
		labelCopyright.setText("© 2024 - Paul Koenig | All Rights Reserved");
		mPanel.add(labelCopyright, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer1 = new Spacer();
		mPanel.add(spacer1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 6), new Dimension(-1, 6), new Dimension(-1, 6), 0, false));
		final Spacer spacer2 = new Spacer();
		mPanel.add(spacer2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 6), new Dimension(-1, 6), new Dimension(-1, 6), 0, false));
		mTabbedPane = new JTabbedPane();
		mTabbedPane.setTabLayoutPolicy(0);
		mTabbedPane.setTabPlacement(1);
		mPanel.add(mTabbedPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		mTabbedPane.addTab("Fetcher", panel1);
		tabbedPane = new JTabbedPane();
		tabbedPane.setEnabled(true);
		tabbedPane.setTabLayoutPolicy(0);
		tabbedPane.setTabPlacement(2);
		panel1.add(tabbedPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		panel2.setEnabled(true);
		tabbedPane.addTab("General", panel2);
		final JPanel panel3 = new JPanel();
		panel3.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
		panel2.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		continueGeneralButton = new JButton();
		continueGeneralButton.setHorizontalAlignment(4);
		continueGeneralButton.setHorizontalTextPosition(10);
		continueGeneralButton.setIcon(new ImageIcon(getClass().getResource("/icons/baseline_navigate_next_black_24dp.png")));
		continueGeneralButton.setText("Continue");
		panel3.add(continueGeneralButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer3 = new Spacer();
		panel3.add(spacer3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		final JScrollPane scrollPane1 = new JScrollPane();
		panel3.add(scrollPane1, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		scrollPane1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		generalPanel = new JPanel();
		generalPanel.setLayout(new GridLayoutManager(15, 5, new Insets(0, 0, 0, 0), -1, -1));
		scrollPane1.setViewportView(generalPanel);
		final Spacer spacer4 = new Spacer();
		generalPanel.add(spacer4, new GridConstraints(1, 0, 14, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(5, -1), new Dimension(5, -1), new Dimension(5, -1), 0, false));
		final Spacer spacer5 = new Spacer();
		generalPanel.add(spacer5, new GridConstraints(0, 3, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 5), new Dimension(-1, 5), new Dimension(-1, 5), 0, false));
		final Spacer spacer6 = new Spacer();
		generalPanel.add(spacer6, new GridConstraints(6, 4, 9, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(5, -1), new Dimension(5, -1), new Dimension(5, -1), 0, false));
		final Spacer spacer7 = new Spacer();
		generalPanel.add(spacer7, new GridConstraints(14, 1, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		final JLabel label1 = new JLabel();
		label1.setText("DCC-Request:");
		generalPanel.add(label1, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		dccRequestField = new JTextField();
		dccRequestField.setEditable(true);
		generalPanel.add(dccRequestField, new GridConstraints(2, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		final JLabel label2 = new JLabel();
		label2.setText("Year Start:");
		generalPanel.add(label2, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label3 = new JLabel();
		label3.setText("Genre:");
		generalPanel.add(label3, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label4 = new JLabel();
		label4.setText("Year End:");
		generalPanel.add(label4, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label5 = new JLabel();
		label5.setText("Format:");
		generalPanel.add(label5, new GridConstraints(10, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label6 = new JLabel();
		label6.setText("Sort:");
		generalPanel.add(label6, new GridConstraints(11, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label7 = new JLabel();
		label7.setText("Limit:");
		generalPanel.add(label7, new GridConstraints(12, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label8 = new JLabel();
		label8.setText("Page:");
		generalPanel.add(label8, new GridConstraints(13, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		formatCombobox = new JComboBox();
		generalPanel.add(formatCombobox, new GridConstraints(10, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		sortCombobox = new JComboBox();
		generalPanel.add(sortCombobox, new GridConstraints(11, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		generalPanel.add(limitSpinner, new GridConstraints(12, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		generalPanel.add(pageSpinner, new GridConstraints(13, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		generalPanel.add(endSpinner, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		generalPanel.add(startSpinner, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		belletristikFictionRadioButton = new JRadioButton();
		belletristikFictionRadioButton.setSelected(true);
		belletristikFictionRadioButton.setText("Belletristik (Fiction)");
		generalPanel.add(belletristikFictionRadioButton, new GridConstraints(6, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		scienceRadioButton = new JRadioButton();
		scienceRadioButton.setSelected(true);
		scienceRadioButton.setText("Wissenschaft (Science)");
		generalPanel.add(scienceRadioButton, new GridConstraints(7, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cosumerLiteraturRadioButton = new JRadioButton();
		cosumerLiteraturRadioButton.setSelected(true);
		cosumerLiteraturRadioButton.setText("Gebrauchsliteratur (Consumer Literature)");
		generalPanel.add(cosumerLiteraturRadioButton, new GridConstraints(8, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		newspaperRadioButton = new JRadioButton();
		newspaperRadioButton.setSelected(true);
		newspaperRadioButton.setText("Zeitung (Newspaper)");
		generalPanel.add(newspaperRadioButton, new GridConstraints(9, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label9 = new JLabel();
		label9.setText("Parameter: %WORD%");
		generalPanel.add(label9, new GridConstraints(3, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		pageButton = new JCheckBox();
		pageButton.setText("");
		generalPanel.add(pageButton, new GridConstraints(13, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		limitButton = new JCheckBox();
		limitButton.setText("");
		generalPanel.add(limitButton, new GridConstraints(12, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		sortButton = new JCheckBox();
		sortButton.setText("");
		generalPanel.add(sortButton, new GridConstraints(11, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		startButton = new JCheckBox();
		startButton.setText("");
		generalPanel.add(startButton, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		endButton = new JCheckBox();
		endButton.setText("");
		generalPanel.add(endButton, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel4 = new JPanel();
		panel4.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
		generalPanel.add(panel4, new GridConstraints(1, 1, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JLabel label10 = new JLabel();
		Font label10Font = this.$$$getFont$$$(null, Font.BOLD, 13, label10.getFont());
		if (label10Font != null) label10.setFont(label10Font);
		label10.setText("General Information");
		panel4.add(label10, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JSeparator separator1 = new JSeparator();
		panel4.add(separator1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer8 = new Spacer();
		panel3.add(spacer8, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 5), new Dimension(-1, 5), new Dimension(-1, 5), 0, false));
		final Spacer spacer9 = new Spacer();
		panel3.add(spacer9, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(5, -1), new Dimension(5, -1), new Dimension(5, -1), 0, false));
		final JPanel panel5 = new JPanel();
		panel5.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		tabbedPane.addTab("Corpora", panel5);
		final JPanel panel6 = new JPanel();
		panel6.setLayout(new GridLayoutManager(3, 5, new Insets(0, 0, 0, 0), -1, -1));
		panel5.add(panel6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		continueCorporaButton = new JButton();
		continueCorporaButton.setHorizontalAlignment(4);
		continueCorporaButton.setHorizontalTextPosition(10);
		continueCorporaButton.setIcon(new ImageIcon(getClass().getResource("/icons/baseline_navigate_next_black_24dp.png")));
		continueCorporaButton.setText("Continue");
		panel6.add(continueCorporaButton, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JScrollPane scrollPane2 = new JScrollPane();
		panel6.add(scrollPane2, new GridConstraints(0, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		scrollPane2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		final JPanel panel7 = new JPanel();
		panel7.setLayout(new GridLayoutManager(55, 4, new Insets(0, 0, 0, 0), -1, -1));
		scrollPane2.setViewportView(panel7);
		final Spacer spacer10 = new Spacer();
		panel7.add(spacer10, new GridConstraints(2, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(5, -1), new Dimension(5, -1), new Dimension(5, -1), 0, false));
		final Spacer spacer11 = new Spacer();
		panel7.add(spacer11, new GridConstraints(0, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 5), new Dimension(-1, 5), new Dimension(-1, 5), 0, false));
		final Spacer spacer12 = new Spacer();
		panel7.add(spacer12, new GridConstraints(3, 3, 52, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(5, -1), new Dimension(5, -1), new Dimension(5, -1), 0, false));
		final JLabel label11 = new JLabel();
		label11.setText("DWDS-Kernkorpus (1900–1999)");
		panel7.add(label11, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpKernCheck = new JCheckBox();
		cpKernCheck.setText("");
		panel7.add(cpKernCheck, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label12 = new JLabel();
		label12.setText("DWDS-Kernkorpus 21 (2000–2010)");
		panel7.add(label12, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpCorpus21Check = new JCheckBox();
		cpCorpus21Check.setText("");
		panel7.add(cpCorpus21Check, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label13 = new JLabel();
		label13.setText("DTA-Kern+Erweit. (1465–1969)");
		panel7.add(label13, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label14 = new JLabel();
		label14.setText("Historische Korpora (1465–1998)");
		panel7.add(label14, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label15 = new JLabel();
		label15.setText("Berliner Zeitung (1994–2005)");
		panel7.add(label15, new GridConstraints(14, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label16 = new JLabel();
		label16.setText("Der Tagesspiegel (ab 1996)");
		panel7.add(label16, new GridConstraints(17, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label17 = new JLabel();
		label17.setText("Blogs");
		panel7.add(label17, new GridConstraints(28, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpdtaCheck = new JCheckBox();
		cpdtaCheck.setText("");
		panel7.add(cpdtaCheck, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpdtaxlCheck = new JCheckBox();
		cpdtaxlCheck.setText("");
		panel7.add(cpdtaxlCheck, new GridConstraints(8, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cptspCheck = new JCheckBox();
		cptspCheck.setText("");
		panel7.add(cptspCheck, new GridConstraints(17, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpblogsCheck = new JCheckBox();
		cpblogsCheck.setText("");
		panel7.add(cpblogsCheck, new GridConstraints(28, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label18 = new JLabel();
		label18.setText("DTA-Erweiterungen (1465–1969)");
		panel7.add(label18, new GridConstraints(30, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpdtaeCheck = new JCheckBox();
		cpdtaeCheck.setText("");
		panel7.add(cpdtaeCheck, new GridConstraints(30, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label19 = new JLabel();
		label19.setText("Archiv der Gegenwart (1931–2000)");
		panel7.add(label19, new GridConstraints(31, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpadgCheck = new JCheckBox();
		cpadgCheck.setEnabled(true);
		cpadgCheck.setText("");
		cpadgCheck.setToolTipText("Currently not available.");
		panel7.add(cpadgCheck, new GridConstraints(31, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel8 = new JPanel();
		panel8.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
		panel7.add(panel8, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JLabel label20 = new JLabel();
		Font label20Font = this.$$$getFont$$$(null, Font.BOLD, 13, label20.getFont());
		if (label20Font != null) label20.setFont(label20Font);
		label20.setText("Corpora Information");
		panel8.add(label20, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JSeparator separator2 = new JSeparator();
		panel8.add(separator2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel9 = new JPanel();
		panel9.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
		panel7.add(panel9, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JLabel label21 = new JLabel();
		Font label21Font = this.$$$getFont$$$(null, Font.BOLD, 12, label21.getFont());
		if (label21Font != null) label21.setFont(label21Font);
		label21.setText("Reference corpora");
		panel9.add(label21, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JSeparator separator3 = new JSeparator();
		panel9.add(separator3, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel10 = new JPanel();
		panel10.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
		panel9.add(panel10, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		allButton = new JButton();
		allButton.setText("All (Public)");
		panel10.add(allButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		defaultButton = new JButton();
		defaultButton.setText("Default");
		panel10.add(defaultButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		allExtendedButton = new JButton();
		allExtendedButton.setText("All");
		panel10.add(allExtendedButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel11 = new JPanel();
		panel11.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
		panel7.add(panel11, new GridConstraints(6, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JLabel label22 = new JLabel();
		Font label22Font = this.$$$getFont$$$(null, Font.BOLD, 12, label22.getFont());
		if (label22Font != null) label22.setFont(label22Font);
		label22.setText("Meta corpora");
		panel11.add(label22, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JSeparator separator4 = new JSeparator();
		panel11.add(separator4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel12 = new JPanel();
		panel12.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
		panel7.add(panel12, new GridConstraints(12, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JLabel label23 = new JLabel();
		Font label23Font = this.$$$getFont$$$(null, Font.BOLD, 12, label23.getFont());
		if (label23Font != null) label23.setFont(label23Font);
		label23.setText("Newspaper corpora");
		panel12.add(label23, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JSeparator separator5 = new JSeparator();
		panel12.add(separator5, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel13 = new JPanel();
		panel13.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
		panel7.add(panel13, new GridConstraints(19, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JLabel label24 = new JLabel();
		Font label24Font = this.$$$getFont$$$(null, Font.BOLD, 12, label24.getFont());
		if (label24Font != null) label24.setFont(label24Font);
		label24.setText("Web corpora");
		panel13.add(label24, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JSeparator separator6 = new JSeparator();
		panel13.add(separator6, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel14 = new JPanel();
		panel14.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
		panel7.add(panel14, new GridConstraints(29, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JLabel label25 = new JLabel();
		Font label25Font = this.$$$getFont$$$(null, Font.BOLD, 12, label25.getFont());
		if (label25Font != null) label25.setFont(label25Font);
		label25.setText("Special corpora");
		panel14.add(label25, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JSeparator separator7 = new JSeparator();
		panel14.add(separator7, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label26 = new JLabel();
		label26.setText("Gesprochene Sprache");
		panel7.add(label26, new GridConstraints(35, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label27 = new JLabel();
		label27.setText("Politische Reden (1982–2020)");
		panel7.add(label27, new GridConstraints(39, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label28 = new JLabel();
		label28.setText("Soldatenbriefe (1745–1872)");
		panel7.add(label28, new GridConstraints(41, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label29 = new JLabel();
		label29.setText("A. v. Humboldts Publizistik (dt., 1790–1859)");
		panel7.add(label29, new GridConstraints(43, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label30 = new JLabel();
		label30.setText("Der Neue Pitaval (1842–1890)");
		panel7.add(label30, new GridConstraints(45, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label31 = new JLabel();
		label31.setText("Deutsche Kunst und Dekoration (1897–1932)");
		panel7.add(label31, new GridConstraints(47, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label32 = new JLabel();
		label32.setText("stimm-los – Wiedergefundene Perlen der Literatur");
		panel7.add(label32, new GridConstraints(49, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label33 = new JLabel();
		label33.setText("Gesetze und Verordnungen (1897–2023)");
		panel7.add(label33, new GridConstraints(53, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpspkCheck = new JCheckBox();
		cpspkCheck.setText("");
		panel7.add(cpspkCheck, new GridConstraints(35, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cppolitische_redenCheck = new JCheckBox();
		cppolitische_redenCheck.setText("");
		panel7.add(cppolitische_redenCheck, new GridConstraints(39, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpsoldatenbriefeCheck = new JCheckBox();
		cpsoldatenbriefeCheck.setText("");
		panel7.add(cpsoldatenbriefeCheck, new GridConstraints(41, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cppitavalCheck = new JCheckBox();
		cppitavalCheck.setText("");
		panel7.add(cppitavalCheck, new GridConstraints(45, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpdekudeCheck = new JCheckBox();
		cpdekudeCheck.setText("");
		panel7.add(cpdekudeCheck, new GridConstraints(47, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpstimmlosCheck = new JCheckBox();
		cpstimmlosCheck.setText("");
		panel7.add(cpstimmlosCheck, new GridConstraints(49, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpgesetzeCheck = new JCheckBox();
		cpgesetzeCheck.setText("");
		panel7.add(cpgesetzeCheck, new GridConstraints(53, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label34 = new JLabel();
		label34.setText("DTA-Kernkorpus (1598–1913)");
		panel7.add(label34, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpDtakCheck = new JCheckBox();
		cpDtakCheck.setText("");
		panel7.add(cpDtakCheck, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label35 = new JLabel();
		label35.setText("Referenz- und Zeitungskorpora ");
		panel7.add(label35, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cppublicCheck = new JCheckBox();
		cppublicCheck.setText("");
		panel7.add(cppublicCheck, new GridConstraints(9, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label36 = new JLabel();
		label36.setText("Polytechnisches Journal");
		panel7.add(label36, new GridConstraints(32, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpdinglerCheck = new JCheckBox();
		cpdinglerCheck.setText("");
		panel7.add(cpdinglerCheck, new GridConstraints(32, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label37 = new JLabel();
		label37.setText("Filmuntertitel");
		panel7.add(label37, new GridConstraints(34, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpuntertitelCheck = new JCheckBox();
		cpuntertitelCheck.setText("");
		panel7.add(cpuntertitelCheck, new GridConstraints(34, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label38 = new JLabel();
		label38.setText("DDR");
		panel7.add(label38, new GridConstraints(38, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label39 = new JLabel();
		label39.setText("Bundestagskorpus (1949–2017)");
		panel7.add(label39, new GridConstraints(40, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpddrCheck = new JCheckBox();
		cpddrCheck.setText("");
		panel7.add(cpddrCheck, new GridConstraints(38, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpbundestagCheck = new JCheckBox();
		cpbundestagCheck.setText("");
		panel7.add(cpbundestagCheck, new GridConstraints(40, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label40 = new JLabel();
		label40.setText("Korpus Patiententexte (1834–1957)");
		panel7.add(label40, new GridConstraints(42, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpcopadocsCheck = new JCheckBox();
		cpcopadocsCheck.setText("");
		panel7.add(cpcopadocsCheck, new GridConstraints(42, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpavhbernCheck = new JCheckBox();
		cpavhbernCheck.setText("");
		panel7.add(cpavhbernCheck, new GridConstraints(43, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label41 = new JLabel();
		label41.setText("Nachrichten aus der Brüdergemeine (1819–1894)");
		panel7.add(label41, new GridConstraints(44, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpbruedergemeineCheck = new JCheckBox();
		cpbruedergemeineCheck.setText("");
		panel7.add(cpbruedergemeineCheck, new GridConstraints(44, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label42 = new JLabel();
		label42.setText("Briefe von Jean Paul (1780–1825)");
		panel7.add(label42, new GridConstraints(46, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpjeanpaulCheck = new JCheckBox();
		cpjeanpaulCheck.setText("");
		panel7.add(cpjeanpaulCheck, new GridConstraints(46, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label43 = new JLabel();
		label43.setText("Neuer Deutscher Novellenschatz (1884–1887)");
		panel7.add(label43, new GridConstraints(48, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpnschatzdeuCheck = new JCheckBox();
		cpnschatzdeuCheck.setText("");
		panel7.add(cpnschatzdeuCheck, new GridConstraints(48, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label44 = new JLabel();
		label44.setText("Wikibooks-Korpus");
		panel7.add(label44, new GridConstraints(50, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpwikibooksCheck = new JCheckBox();
		cpwikibooksCheck.setText("");
		panel7.add(cpwikibooksCheck, new GridConstraints(50, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label45 = new JLabel();
		label45.setText("Wikipedia-Korpus");
		panel7.add(label45, new GridConstraints(51, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpwikipediaCheck = new JCheckBox();
		cpwikipediaCheck.setText("");
		panel7.add(cpwikipediaCheck, new GridConstraints(51, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label46 = new JLabel();
		label46.setText("Wikivoyage-Korpus");
		panel7.add(label46, new GridConstraints(52, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpwikivoyageCheck = new JCheckBox();
		cpwikivoyageCheck.setText("");
		panel7.add(cpwikivoyageCheck, new GridConstraints(52, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer13 = new Spacer();
		panel7.add(spacer13, new GridConstraints(54, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		cpbzCheck = new JCheckBox();
		cpbzCheck.setText("");
		panel7.add(cpbzCheck, new GridConstraints(14, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label47 = new JLabel();
		label47.setText("ZDL-Regionalkorpus (ab 1993)");
		panel7.add(label47, new GridConstraints(10, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpregionalCheck = new JCheckBox();
		cpregionalCheck.setHorizontalTextPosition(2);
		cpregionalCheck.setText("(Needs Auth)");
		panel7.add(cpregionalCheck, new GridConstraints(10, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label48 = new JLabel();
		label48.setText("WebXL");
		panel7.add(label48, new GridConstraints(11, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpwebxlCheck = new JCheckBox();
		cpwebxlCheck.setHorizontalTextPosition(2);
		cpwebxlCheck.setText("(Needs Auth)");
		panel7.add(cpwebxlCheck, new GridConstraints(11, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label49 = new JLabel();
		label49.setText("Berliner Zeitung (1945–1993)");
		panel7.add(label49, new GridConstraints(13, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpbz_ppCheck = new JCheckBox();
		cpbz_ppCheck.setHorizontalTextPosition(2);
		cpbz_ppCheck.setText("(Needs Auth)");
		panel7.add(cpbz_ppCheck, new GridConstraints(13, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label50 = new JLabel();
		label50.setText("Frankfurter Allgemeine Zeitung");
		panel7.add(label50, new GridConstraints(15, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpfazCheck = new JCheckBox();
		cpfazCheck.setHorizontalTextPosition(2);
		cpfazCheck.setText("(Needs Auth)");
		panel7.add(cpfazCheck, new GridConstraints(15, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label51 = new JLabel();
		label51.setText("neues deutschland (1946–1990)");
		panel7.add(label51, new GridConstraints(16, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpndCheck = new JCheckBox();
		cpndCheck.setHorizontalTextPosition(2);
		cpndCheck.setText("(Needs Auth)");
		panel7.add(cpndCheck, new GridConstraints(16, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label52 = new JLabel();
		label52.setText("Die ZEIT (1946–2023)");
		panel7.add(label52, new GridConstraints(18, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpzeitCheck = new JCheckBox();
		cpzeitCheck.setHorizontalTextPosition(2);
		cpzeitCheck.setText("(Needs Auth)");
		panel7.add(cpzeitCheck, new GridConstraints(18, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label53 = new JLabel();
		label53.setText("Berliner Wendekorpus");
		panel7.add(label53, new GridConstraints(37, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpwendeCheck = new JCheckBox();
		cpwendeCheck.setHorizontalTextPosition(2);
		cpwendeCheck.setText("(Needs Auth)");
		panel7.add(cpwendeCheck, new GridConstraints(37, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label54 = new JLabel();
		label54.setText("Text+Berg");
		panel7.add(label54, new GridConstraints(36, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cptextbergCheck = new JCheckBox();
		cptextbergCheck.setHorizontalTextPosition(2);
		cptextbergCheck.setText("(Needs Auth)");
		panel7.add(cptextbergCheck, new GridConstraints(36, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label55 = new JLabel();
		label55.setText("Dortmunder Chat-Korpus");
		panel7.add(label55, new GridConstraints(33, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpibk_dchatCheck = new JCheckBox();
		cpibk_dchatCheck.setHorizontalTextPosition(2);
		cpibk_dchatCheck.setText("(Needs Auth)");
		panel7.add(cpibk_dchatCheck, new GridConstraints(33, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label56 = new JLabel();
		label56.setText("Webkorpus");
		panel7.add(label56, new GridConstraints(20, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label57 = new JLabel();
		label57.setText("Webmonitor");
		panel7.add(label57, new GridConstraints(21, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label58 = new JLabel();
		label58.setText("Webkorpus Ballsportarten");
		panel7.add(label58, new GridConstraints(22, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label59 = new JLabel();
		label59.setText("Jurakorpus");
		panel7.add(label59, new GridConstraints(23, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label60 = new JLabel();
		label60.setText("Medizinkorpus");
		panel7.add(label60, new GridConstraints(24, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label61 = new JLabel();
		label61.setText("Corona-Korpus");
		panel7.add(label61, new GridConstraints(25, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label62 = new JLabel();
		label62.setText("Mode- und Beauty-Blogs");
		panel7.add(label62, new GridConstraints(26, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpwebCheck = new JCheckBox();
		cpwebCheck.setHorizontalTextPosition(2);
		cpwebCheck.setText("(Needs Auth)");
		panel7.add(cpwebCheck, new GridConstraints(20, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpwebmonitorCheck = new JCheckBox();
		cpwebmonitorCheck.setHorizontalTextPosition(2);
		cpwebmonitorCheck.setText("(Needs Auth)");
		panel7.add(cpwebmonitorCheck, new GridConstraints(21, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpballsportCheck = new JCheckBox();
		cpballsportCheck.setHorizontalTextPosition(2);
		cpballsportCheck.setText("(Needs Auth)");
		panel7.add(cpballsportCheck, new GridConstraints(22, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpjuraCheck = new JCheckBox();
		cpjuraCheck.setHorizontalTextPosition(2);
		cpjuraCheck.setText("(Needs Auth)");
		panel7.add(cpjuraCheck, new GridConstraints(23, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpmedizinCheck = new JCheckBox();
		cpmedizinCheck.setHorizontalTextPosition(2);
		cpmedizinCheck.setText("(Needs Auth)");
		panel7.add(cpmedizinCheck, new GridConstraints(24, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpcoronaCheck = new JCheckBox();
		cpcoronaCheck.setHorizontalTextPosition(2);
		cpcoronaCheck.setText("(Needs Auth)");
		panel7.add(cpcoronaCheck, new GridConstraints(25, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpmodeblogsBlogs = new JCheckBox();
		cpmodeblogsBlogs.setHorizontalTextPosition(2);
		cpmodeblogsBlogs.setText("(Needs Auth)");
		panel7.add(cpmodeblogsBlogs, new GridConstraints(26, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label63 = new JLabel();
		label63.setText("IT-Blogs");
		panel7.add(label63, new GridConstraints(27, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cpit_blogsCheck = new JCheckBox();
		cpit_blogsCheck.setHorizontalTextPosition(2);
		cpit_blogsCheck.setText("(Needs Auth)");
		panel7.add(cpit_blogsCheck, new GridConstraints(27, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer14 = new Spacer();
		panel6.add(spacer14, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 5), new Dimension(-1, 5), new Dimension(-1, 5), 0, false));
		final Spacer spacer15 = new Spacer();
		panel6.add(spacer15, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(5, -1), new Dimension(5, -1), new Dimension(5, -1), 0, false));
		returnCorporaButton = new JButton();
		returnCorporaButton.setHorizontalAlignment(2);
		returnCorporaButton.setIcon(new ImageIcon(getClass().getResource("/icons/baseline_navigate_before_black_24dp.png")));
		returnCorporaButton.setText("Return");
		panel6.add(returnCorporaButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer16 = new Spacer();
		panel6.add(spacer16, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(5, -1), new Dimension(5, -1), new Dimension(5, -1), 0, false));
		final Spacer spacer17 = new Spacer();
		panel6.add(spacer17, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		final JPanel panel15 = new JPanel();
		panel15.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		tabbedPane.addTab("Words", panel15);
		final JPanel panel16 = new JPanel();
		panel16.setLayout(new GridLayoutManager(3, 5, new Insets(0, 0, 0, 0), -1, -1));
		panel15.add(panel16, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		continueWordsButton = new JButton();
		continueWordsButton.setHorizontalAlignment(4);
		continueWordsButton.setHorizontalTextPosition(10);
		continueWordsButton.setIcon(new ImageIcon(getClass().getResource("/icons/baseline_navigate_next_black_24dp.png")));
		continueWordsButton.setText("Continue");
		panel16.add(continueWordsButton, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer18 = new Spacer();
		panel16.add(spacer18, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		final JScrollPane scrollPane3 = new JScrollPane();
		panel16.add(scrollPane3, new GridConstraints(0, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		scrollPane3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		final JPanel panel17 = new JPanel();
		panel17.setLayout(new GridLayoutManager(5, 4, new Insets(0, 0, 0, 0), -1, -1));
		scrollPane3.setViewportView(panel17);
		final Spacer spacer19 = new Spacer();
		panel17.add(spacer19, new GridConstraints(1, 0, 4, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(5, -1), new Dimension(5, -1), new Dimension(5, -1), 0, false));
		final Spacer spacer20 = new Spacer();
		panel17.add(spacer20, new GridConstraints(0, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 5), new Dimension(-1, 5), new Dimension(-1, 5), 0, false));
		final Spacer spacer21 = new Spacer();
		panel17.add(spacer21, new GridConstraints(3, 3, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(5, -1), new Dimension(5, -1), new Dimension(5, -1), 0, false));
		final Spacer spacer22 = new Spacer();
		panel17.add(spacer22, new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		final JLabel label64 = new JLabel();
		label64.setText("Words:");
		label64.setVerticalAlignment(0);
		label64.setVerticalTextPosition(0);
		panel17.add(label64, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JScrollPane scrollPane4 = new JScrollPane();
		scrollPane4.setHorizontalScrollBarPolicy(30);
		scrollPane4.setVerticalScrollBarPolicy(22);
		panel17.add(scrollPane4, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 60), new Dimension(-1, 400), null, 0, false));
		wordsArea = new JTextArea();
		wordsArea.setEditable(true);
		scrollPane4.setViewportView(wordsArea);
		final JPanel panel18 = new JPanel();
		panel18.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
		panel17.add(panel18, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JLabel label65 = new JLabel();
		Font label65Font = this.$$$getFont$$$(null, Font.BOLD, 13, label65.getFont());
		if (label65Font != null) label65.setFont(label65Font);
		label65.setText("Word Information");
		panel18.add(label65, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JSeparator separator8 = new JSeparator();
		panel18.add(separator8, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer23 = new Spacer();
		panel16.add(spacer23, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 5), new Dimension(-1, 5), new Dimension(-1, 5), 0, false));
		final Spacer spacer24 = new Spacer();
		panel16.add(spacer24, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(5, -1), new Dimension(5, -1), new Dimension(5, -1), 0, false));
		returnWordsButton = new JButton();
		returnWordsButton.setHorizontalAlignment(2);
		returnWordsButton.setHorizontalTextPosition(11);
		returnWordsButton.setIcon(new ImageIcon(getClass().getResource("/icons/baseline_navigate_before_black_24dp.png")));
		returnWordsButton.setText("Return");
		panel16.add(returnWordsButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer25 = new Spacer();
		panel16.add(spacer25, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(5, -1), new Dimension(5, -1), new Dimension(5, -1), 0, false));
		final JPanel panel19 = new JPanel();
		panel19.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		tabbedPane.addTab("Export", panel19);
		final JPanel panel20 = new JPanel();
		panel20.setLayout(new GridLayoutManager(3, 5, new Insets(0, 0, 0, 0), -1, -1));
		panel19.add(panel20, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		continureExportButton = new JButton();
		continureExportButton.setHorizontalAlignment(4);
		continureExportButton.setHorizontalTextPosition(10);
		continureExportButton.setIcon(new ImageIcon(getClass().getResource("/icons/baseline_navigate_next_black_24dp.png")));
		continureExportButton.setText("Continue");
		panel20.add(continureExportButton, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer26 = new Spacer();
		panel20.add(spacer26, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		final JScrollPane scrollPane5 = new JScrollPane();
		panel20.add(scrollPane5, new GridConstraints(0, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		scrollPane5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		final JPanel panel21 = new JPanel();
		panel21.setLayout(new GridLayoutManager(5, 4, new Insets(0, 0, 0, 0), -1, -1));
		scrollPane5.setViewportView(panel21);
		final Spacer spacer27 = new Spacer();
		panel21.add(spacer27, new GridConstraints(1, 0, 4, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(5, -1), new Dimension(5, -1), new Dimension(5, -1), 0, false));
		final JLabel label66 = new JLabel();
		Font label66Font = this.$$$getFont$$$(null, Font.BOLD, 13, label66.getFont());
		if (label66Font != null) label66.setFont(label66Font);
		label66.setText("Export Information");
		panel21.add(label66, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer28 = new Spacer();
		panel21.add(spacer28, new GridConstraints(0, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 5), new Dimension(-1, 5), new Dimension(-1, 5), 0, false));
		final Spacer spacer29 = new Spacer();
		panel21.add(spacer29, new GridConstraints(2, 3, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(5, -1), new Dimension(5, -1), new Dimension(5, -1), 0, false));
		final JSeparator separator9 = new JSeparator();
		panel21.add(separator9, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label67 = new JLabel();
		label67.setText("View:");
		panel21.add(label67, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		viewSettingsTabbedPane = new JTabbedPane();
		panel21.add(viewSettingsTabbedPane, new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
		final JPanel panel22 = new JPanel();
		panel22.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		viewSettingsTabbedPane.addTab("CSV/TSV", panel22);
		final JScrollPane scrollPane6 = new JScrollPane();
		panel22.add(scrollPane6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		final JPanel panel23 = new JPanel();
		panel23.setLayout(new GridLayoutManager(8, 5, new Insets(0, 0, 0, 0), -1, -1));
		scrollPane6.setViewportView(panel23);
		final Spacer spacer30 = new Spacer();
		panel23.add(spacer30, new GridConstraints(0, 1, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 5), new Dimension(-1, 5), new Dimension(-1, 5), 0, false));
		final Spacer spacer31 = new Spacer();
		panel23.add(spacer31, new GridConstraints(7, 1, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		final Spacer spacer32 = new Spacer();
		panel23.add(spacer32, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(5, -1), new Dimension(5, -1), new Dimension(5, -1), 0, false));
		final Spacer spacer33 = new Spacer();
		panel23.add(spacer33, new GridConstraints(7, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(5, -1), new Dimension(5, -1), new Dimension(5, -1), 0, false));
		final JLabel label68 = new JLabel();
		label68.setText("Spaces:");
		panel23.add(label68, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		panel23.add(csvSpacesSpinner, new GridConstraints(3, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label69 = new JLabel();
		label69.setText("New Rows:");
		panel23.add(label69, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JScrollPane scrollPane7 = new JScrollPane();
		scrollPane7.setHorizontalScrollBarPolicy(30);
		scrollPane7.setVerticalScrollBarPolicy(22);
		panel23.add(scrollPane7, new GridConstraints(6, 2, 1, 2, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 60), new Dimension(-1, 60), new Dimension(-1, 60), 0, false));
		csvNewRowsArea = new JTextArea();
		csvNewRowsArea.setEditable(true);
		csvNewRowsArea.setToolTipText("Format: \"rowNumber;headerName\"");
		scrollPane7.setViewportView(csvNewRowsArea);
		final JLabel label70 = new JLabel();
		label70.setText("Separator:");
		panel23.add(label70, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		csvSeparatorField = new JTextField();
		panel23.add(csvSeparatorField, new GridConstraints(4, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		final JLabel label71 = new JLabel();
		label71.setText("Escape:");
		panel23.add(label71, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		csvEscapeField = new JTextField();
		panel23.add(csvEscapeField, new GridConstraints(5, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		final JLabel label72 = new JLabel();
		label72.setText("Combine Corpora:");
		panel23.add(label72, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		combineCorporaCheck = new JCheckBox();
		combineCorporaCheck.setText("");
		combineCorporaCheck.setToolTipText("Only in KWIC format. If selected, all header will be mapped into kern header");
		panel23.add(combineCorporaCheck, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label73 = new JLabel();
		label73.setText("Add Corpus Info:");
		panel23.add(label73, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		showCorpusCheck = new JCheckBox();
		showCorpusCheck.setText("");
		showCorpusCheck.setToolTipText("If selected, the current corpus will be appended to the number row.");
		panel23.add(showCorpusCheck, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel24 = new JPanel();
		panel24.setLayout(new GridLayoutManager(4, 4, new Insets(0, 0, 0, 0), -1, -1));
		viewSettingsTabbedPane.addTab("JSON", panel24);
		final Spacer spacer34 = new Spacer();
		panel24.add(spacer34, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 5), new Dimension(-1, 5), new Dimension(-1, 5), 0, false));
		final Spacer spacer35 = new Spacer();
		panel24.add(spacer35, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		final Spacer spacer36 = new Spacer();
		panel24.add(spacer36, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(5, -1), new Dimension(5, -1), new Dimension(5, -1), 0, false));
		final Spacer spacer37 = new Spacer();
		panel24.add(spacer37, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(5, -1), new Dimension(5, -1), new Dimension(5, -1), 0, false));
		final JLabel label74 = new JLabel();
		label74.setText("Collapse Level:");
		panel24.add(label74, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		panel24.add(jsonCollapseLevelSpinner, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label75 = new JLabel();
		label75.setText("Combine Corpora:");
		panel24.add(label75, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		jsonCombineCorporaCheck = new JCheckBox();
		jsonCombineCorporaCheck.setText("");
		jsonCombineCorporaCheck.setToolTipText("Only in KWIC format. If selected, all header will be mapped into kern header");
		panel24.add(jsonCombineCorporaCheck, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel25 = new JPanel();
		panel25.setLayout(new GridLayoutManager(4, 4, new Insets(0, 0, 0, 0), -1, -1));
		viewSettingsTabbedPane.addTab("TCF", panel25);
		final JLabel label76 = new JLabel();
		label76.setText("Indent Level:");
		panel25.add(label76, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		panel25.add(tcfIndentLevel, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer38 = new Spacer();
		panel25.add(spacer38, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 5), new Dimension(-1, 5), new Dimension(-1, 5), 0, false));
		final Spacer spacer39 = new Spacer();
		panel25.add(spacer39, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(5, -1), new Dimension(5, -1), new Dimension(5, -1), 0, false));
		final Spacer spacer40 = new Spacer();
		panel25.add(spacer40, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		final Spacer spacer41 = new Spacer();
		panel25.add(spacer41, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		final Spacer spacer42 = new Spacer();
		panel25.add(spacer42, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(5, -1), new Dimension(5, -1), new Dimension(5, -1), 0, false));
		final JLabel label77 = new JLabel();
		label77.setText("Indent:");
		panel25.add(label77, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		tcfIndentField = new JTextField();
		panel25.add(tcfIndentField, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		viewCombobox = new JComboBox();
		panel21.add(viewCombobox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label78 = new JLabel();
		Font label78Font = this.$$$getFont$$$(null, Font.BOLD, 12, label78.getFont());
		if (label78Font != null) label78.setFont(label78Font);
		label78.setText("Advanced Settings");
		panel21.add(label78, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JSeparator separator10 = new JSeparator();
		panel21.add(separator10, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer43 = new Spacer();
		panel20.add(spacer43, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 5), new Dimension(-1, 5), new Dimension(-1, 5), 0, false));
		final Spacer spacer44 = new Spacer();
		panel20.add(spacer44, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(5, -1), new Dimension(5, -1), new Dimension(5, -1), 0, false));
		returnExportButton = new JButton();
		returnExportButton.setHorizontalAlignment(2);
		returnExportButton.setHorizontalTextPosition(11);
		returnExportButton.setIcon(new ImageIcon(getClass().getResource("/icons/baseline_navigate_before_black_24dp.png")));
		returnExportButton.setText("Return");
		panel20.add(returnExportButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer45 = new Spacer();
		panel20.add(spacer45, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(5, -1), new Dimension(5, -1), new Dimension(5, -1), 0, false));
		final JPanel panel26 = new JPanel();
		panel26.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		tabbedPane.addTab("Overview", panel26);
		final JPanel panel27 = new JPanel();
		panel27.setLayout(new GridLayoutManager(3, 5, new Insets(0, 0, 0, 0), -1, -1));
		panel26.add(panel27, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		finishButton = new JButton();
		finishButton.setBackground(new Color(-15482093));
		finishButton.setHorizontalAlignment(4);
		finishButton.setHorizontalTextPosition(10);
		finishButton.setIcon(new ImageIcon(getClass().getResource("/icons/baseline_navigate_finish_black_24dp.png")));
		finishButton.setText("Finish");
		panel27.add(finishButton, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer46 = new Spacer();
		panel27.add(spacer46, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		final JScrollPane scrollPane8 = new JScrollPane();
		panel27.add(scrollPane8, new GridConstraints(0, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		scrollPane8.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		final JPanel panel28 = new JPanel();
		panel28.setLayout(new GridLayoutManager(4, 4, new Insets(0, 0, 0, 0), -1, -1));
		scrollPane8.setViewportView(panel28);
		final Spacer spacer47 = new Spacer();
		panel28.add(spacer47, new GridConstraints(1, 0, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(5, -1), new Dimension(5, -1), new Dimension(5, -1), 0, false));
		final JLabel label79 = new JLabel();
		Font label79Font = this.$$$getFont$$$(null, Font.BOLD, 13, label79.getFont());
		if (label79Font != null) label79.setFont(label79Font);
		label79.setText("Overview");
		panel28.add(label79, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer48 = new Spacer();
		panel28.add(spacer48, new GridConstraints(0, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 5), new Dimension(-1, 5), new Dimension(-1, 5), 0, false));
		final Spacer spacer49 = new Spacer();
		panel28.add(spacer49, new GridConstraints(2, 3, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(5, -1), new Dimension(5, -1), new Dimension(5, -1), 0, false));
		final Spacer spacer50 = new Spacer();
		panel28.add(spacer50, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		final JSeparator separator11 = new JSeparator();
		panel28.add(separator11, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JScrollPane scrollPane9 = new JScrollPane();
		panel28.add(scrollPane9, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		overviewPane = new JTextPane();
		overviewPane.setContentType("text/html");
		overviewPane.setEditable(false);
		scrollPane9.setViewportView(overviewPane);
		final Spacer spacer51 = new Spacer();
		panel27.add(spacer51, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 5), new Dimension(-1, 5), new Dimension(-1, 5), 0, false));
		final Spacer spacer52 = new Spacer();
		panel27.add(spacer52, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(5, -1), new Dimension(5, -1), new Dimension(5, -1), 0, false));
		returnOverviewButton = new JButton();
		returnOverviewButton.setHorizontalAlignment(2);
		returnOverviewButton.setIcon(new ImageIcon(getClass().getResource("/icons/baseline_navigate_before_black_24dp.png")));
		returnOverviewButton.setText("Return");
		panel27.add(returnOverviewButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer53 = new Spacer();
		panel27.add(spacer53, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(5, -1), new Dimension(5, -1), new Dimension(5, -1), 0, false));
		final JPanel panel29 = new JPanel();
		panel29.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		mTabbedPane.addTab("Log", panel29);
		logScrollPane = new JScrollPane();
		logScrollPane.setVerticalScrollBarPolicy(20);
		panel29.add(logScrollPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		logPane = new JTextPane();
		logPane.setContentType("text/html");
		logPane.setEditable(false);
		logScrollPane.setViewportView(logPane);
		stopButton = new JButton();
		stopButton.setText("Stop");
		mPanel.add(stopButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
	}

	/**
	 * @noinspection ALL
	 */
	private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont){
		if (currentFont == null) return null;
		String resultName;
		if (fontName == null){
			resultName = currentFont.getName();
		} else {
			Font testFont = new Font(fontName, Font.PLAIN, 10);
			if (testFont.canDisplay('a') && testFont.canDisplay('1')){
				resultName = fontName;
			} else {
				resultName = currentFont.getName();
			}
		}
		Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
		boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
		Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
		return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$(){
		return mPanel;
	}
}
