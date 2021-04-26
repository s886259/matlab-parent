package clickDetector.offlineFuncs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import PamUtils.PamCalendar;
import PamView.PamColors;
import PamView.PamDialog;
import PamView.PamGridBagContraints;
import PamView.PamSymbol;
import PamguardMVC.PamDataBlock;

import clickDetector.ClickControl;
import clickDetector.ClickDataBlock;

/**
 * List of offline events which can be included in various dialogs 
 * associated with offline event creation and management. 
 * @author Doug Gillespie
 *
 */
public class OfflineEventListPanel {

	private JPanel mainPanel;
	
	private JPanel selectionPanel;

	private ClickControl clickControl;

	private OfflineEventDataBlock offlineEventDataBlock;

	private String[] colNames = {"", "Id", "Start Time", "End Time", "Type", "N Clicks",
			"Min", "Best", "Max", "Comment"};

	private JTable eventTable;

	private EventTableModel eventTableModel;
	
	private JRadioButton showAll, showCurrent;

	/**
	 * show all events
	 */
	public static final int SHOW_ALL = 1;
	/**
	 * Show only those events which overlap the current loaded click data. 
	 */
	public static final int SHOW_SELECTION = 2;
	
	public OfflineEventListPanel(ClickControl clickControl) {

		this.clickControl = clickControl;
		makeSelectionPanel();
		makeMainPanel();
	}
		
	private void makeSelectionPanel() {
		selectionPanel = new JPanel();
		selectionPanel.setBorder(new TitledBorder("Selection"));
		selectionPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new PamGridBagContraints();
		PamDialog.addComponent(selectionPanel, showAll = new JRadioButton("Show all"), c);
		c.gridy++;
		PamDialog.addComponent(selectionPanel, showCurrent = new JRadioButton("Show current period"), c);
		showAll.addActionListener(new ShowSelectionChanged());
		showCurrent.addActionListener(new ShowSelectionChanged());
		ButtonGroup bg = new ButtonGroup();
		bg.add(showAll);
		bg.add(showCurrent);		
	}
	
	public int getShowSelection() {
		if (showAll.isSelected()) {
			return SHOW_ALL;
		}
		else if (showCurrent.isSelected()) {
			return SHOW_SELECTION;
		}
		return SHOW_SELECTION;
	}
	
	public void setShowSelection(int showSelection) {
		showAll.setSelected(showSelection == SHOW_ALL);
		showCurrent.setSelected(showSelection == SHOW_SELECTION);
	}

	/**
	 * Called when the show selection has changed. 
	 */
	public void selectData() {
		int sel = getShowSelection();
		if (sel == SHOW_ALL) {
			eventTableModel.firstItem = 0;
			eventTableModel.nItems = 0;
		}
		else {
			ClickDataBlock cdb = clickControl.getClickDataBlock();
			long dataStart = cdb.getCurrentViewDataStart();
			long dataEnd = cdb.getCurrentViewDataEnd();
			int n = 0;
			int first = -1;
			int nEvents = offlineEventDataBlock.getUnitsCount();
			OfflineEventDataUnit anEvent;
			for (int i = 0; i < nEvents; i++) {
				anEvent = offlineEventDataBlock.getDataUnit(i, PamDataBlock.REFERENCE_CURRENT);
				if (anEvent.getTimeMilliseconds() > dataEnd) {
					break;
				}
				if (first < 0) { // search for the first event
					if (anEvent.getTimeMilliseconds() < dataStart) {
						continue;
					}
					first = i;
					n++;
				}
				else {
					n++;
				}
			}
			eventTableModel.firstItem = first;
			eventTableModel.nItems = n;
		}
	}

	private void makeMainPanel() {	
		mainPanel = new JPanel(new BorderLayout());
		offlineEventDataBlock = clickControl.getClickDetector().getOfflineEventDataBlock();
		eventTableModel = new EventTableModel();
		eventTable = new JTable(eventTableModel);
		eventTable.setRowSelectionAllowed(true);
		JScrollPane scrollPane = new JScrollPane(eventTable);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(700, 300));
		mainPanel.add(BorderLayout.CENTER, scrollPane);
		
		TableColumn tableCol;
		String toolTip;
		DefaultTableCellRenderer renderer;
		for (int i = 0; i < eventTableModel.getColumnCount(); i++) {
			tableCol = eventTable.getColumnModel().getColumn(i);
			tableCol.setPreferredWidth(eventTableModel.getRelativeWidth(i)*50);
			toolTip = eventTableModel.getToolTipText(i);
			if (toolTip != null) {
				renderer =
					new DefaultTableCellRenderer();
				renderer.setToolTipText(toolTip);
				tableCol.setCellRenderer(renderer);
			}
		}

		eventTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//		tableData.addTableModelListener(moduleTable);
		setShowSelection(SHOW_SELECTION);
	}
	 

	/**
	 * Call when an event has been added or removed in order to update the table. 
	 */
	public void tableDataChanged() {
		selectData();
		eventTableModel.fireTableDataChanged();
	}

	/**
	 * @return the mainPanel
	 */
	public JPanel getPanel() {
		return mainPanel;
	}
	
	public JTable getTable(){
		return eventTable;
	}

	/**
	 * Get a smaller panel of buttons allowing user to 
	 * select a sub set of data. 
	 * @return small panel. 
	 */
	public JPanel getSelectionPanel() {
		return selectionPanel;
	}

	public void addMouseListener(MouseListener mouseListener) {
		eventTable.addMouseListener(mouseListener);
	}

	public void addListSelectionListener(ListSelectionListener listSelectionListener) {
		eventTable.getSelectionModel().addListSelectionListener(listSelectionListener);
	}

	public void setSelectedEvent(OfflineEventDataUnit selectedEvent) {
		int iRow = findEventRow(selectedEvent);
		if (iRow < 0) {
			eventTable.clearSelection();
		}
		else {
			// see http://www.esus.com/docs/GetQuestionPage.jsp?uid=1295
			eventTable.setRowSelectionInterval(iRow, iRow);
			   eventTable.scrollRectToVisible(
					      new Rectangle(0, eventTable.getRowHeight()*(iRow),
					                    10, eventTable.getRowHeight()));
		}
	}
	
	/**
	 * Finds the row for a specific event. 
	 * @param event event to find
	 * @return row number, or -1 if event not found
	 */
	private int findEventRow(OfflineEventDataUnit event) {
		if (event == null) {
			return -1;
		}
		int nR = eventTableModel.getRowCount();
		for (int i = 0; i < nR; i++) {
			if (eventTableModel.getEventAt(i) == event) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 
	 * @return the event for the currently selected row, or null if nothing selected
	 */
	public OfflineEventDataUnit getSelectedEvent() {
		int rowIndex = eventTable.getSelectedRow();
		if (rowIndex < 0) {
			return null;
		}
		if (offlineEventDataBlock == null) {
			return null;
		}
		rowIndex += eventTableModel.firstItem;
		return offlineEventDataBlock.getDataUnit(rowIndex, PamDataBlock.REFERENCE_CURRENT);
	}
	
	/**the ev ent of the row specified by rowIndex
	 * Use this to select multiple events
	 * @param rowIndex
	 * @return
	 */
	public OfflineEventDataUnit getSelectedEvent(int rowIndex) {
	
		if (rowIndex < 0) {
			return null;
		}
		if (offlineEventDataBlock == null) {
			return null;
		}
		rowIndex += eventTableModel.firstItem;
		return offlineEventDataBlock.getDataUnit(rowIndex, PamDataBlock.REFERENCE_CURRENT);
	}

	private class ShowSelectionChanged implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			tableDataChanged();
		}
	}

	class EventTableModel extends AbstractTableModel {

		int firstItem = 0;
		int nItems = 0;
		
		@Override
		public int getColumnCount() {
			return colNames.length;
		}
		
		PamSymbol symbol = new PamSymbol(PamSymbol.SYMBOL_SQUARE,48,12,true,Color.BLACK,Color.BLUE);

		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
		 */
		@Override
		public Class<?> getColumnClass(int col) {
			if (col == 0) {
				return ImageIcon.class;
			}
			return super.getColumnClass(col);
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
		 */
		@Override
		public String getColumnName(int iCol) {
			return colNames[iCol];
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
		 */
		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return super.isCellEditable(arg0, arg1);
		}

		@Override
		public int getRowCount() {
			if (offlineEventDataBlock == null) {
				return 0;
			}
			if (firstItem < 0) {
				return 0;
			}
			else if (nItems == 0) {
				return offlineEventDataBlock.getUnitsCount();
			}
			else {
				return nItems;
			}
		}

		public int getRelativeWidth(int colIndex) {

			switch(colIndex) {
			case 0:
				return 1;
			case 1:
				return 1;
			case 2: 
				return 5;
			case 3:
				return 2;
			case 4:
				return 1;
			case 5:
				return 2;
			case 6:
				return 1;
			case 7:
				return 1;
			case 8:
				return 1;
			case 9:
				return 8;
			default:
				return 1;
			}
		}
		public String getToolTipText(int col) {
			switch(col) {
			case 0:
				return null;
			case 1:
				return "Event Number (Id in viewer database table)";
			case 2:
				return "Event start date and time";
			case 3:
				return "Event end time";
			case 4:
				return "Event type / species";
			case 5:
				return "Total number of clicks in event (number of clicks currently loaded in memory)";
			case 6:
				return "Minimum number of animals in event";
			case 7:
				return "Best estimate of number of animals in event";
			case 8:
				return "Maximum number of animals in event";
			case 9:
				return "Comment";
			default:
				return null;
					
			}
		}
		
		/**
		 * Find the event at a given row
		 * @param rowIndex row index
		 * @return event
		 */
		public OfflineEventDataUnit getEventAt(int rowIndex) {
			if (offlineEventDataBlock == null) {
				return null;
			}
			if (firstItem < 0) {
				return null;
			}
			else {
				rowIndex += firstItem;
			}
			return offlineEventDataBlock.getDataUnit(rowIndex, PamDataBlock.REFERENCE_CURRENT);
		}

		@Override
		public Object getValueAt(int rowIndex, int colIndex) {
			OfflineEventDataUnit edu = getEventAt(rowIndex);
			if (edu == null) {
				return null;
			}
			String str;
			/*
			 * private String[] colNames = {"Start Time", "End Time", "Type", "N Clicks", "Comment"};
			 */
			switch(colIndex) {
			case 0:
				Color col = PamColors.getInstance().getWhaleColor(edu.getColourIndex());
				symbol.setFillColor(col);
				symbol.setLineColor(col);
				return symbol;
			case 1:
				return edu.getDatabaseIndex();
			case 2: // start time
				str  = PamCalendar.formatDBDateTime(edu.getTimeMilliseconds());
				if (edu.isSuspectEventTimes()) {
					str = "?"+ str + "?";
				}
				return str;
			case 3:
				str  = PamCalendar.formatTime(edu.getEventEndTime());
				if (edu.isSuspectEventTimes()) {
					str = "?"+ str + "?";
				}
				return str;
			case 4:
				return edu.getEventType();
			case 5:
				return String.format("%d (%d)", edu.getNClicks(), edu.getSubDetectionsCount());
			case 6:
				return edu.getMinNumber();
			case 7:
				return edu.getBestNumber();
			case 8:
				return edu.getMaxNumber();
			case 9:
				return edu.getComment();
			}
			return null;
		}



	}
}
