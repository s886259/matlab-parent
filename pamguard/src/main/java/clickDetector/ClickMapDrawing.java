package clickDetector;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;

import binaryFileStorage.BinaryOfflineDataMapPoint;

import clickDetector.ClickClassifiers.ClickIdentifier;

import PamUtils.BubbleSort;
import PamView.KeyPanel;
import PamView.LineKeyItem;
import PamView.PamSymbol;
import dataMap.DataMapDrawing;
import dataMap.OfflineDataMap;
import dataMap.OfflineDataMapPoint;
import dataMap.DataStreamPanel.DataGraph;

public class ClickMapDrawing implements DataMapDrawing {

	private ClickDetector clickDetector;

	public ClickMapDrawing(ClickDetector clickDetector) {
		super();
		this.clickDetector = clickDetector;
	}

	@Override
	public void drawDataRate(Graphics g, DataGraph dataGraph,
			OfflineDataMap map, Color dataStreamColour) {
		dataGraph.drawDataRate(g, map, Color.BLACK);

		ClickIdentifier clickIdentifier = clickDetector.getClickControl().getClickIdentifier();
		if (clickIdentifier == null) {
			return;
		}
		String[] speciesList = clickIdentifier.getSpeciesList();
		if (speciesList == null || speciesList.length == 0) {
			return;
		}
		int nSpecies = 0;
		if (speciesList != null) {
			nSpecies = speciesList.length;
		}
		Color[] colours = new Color[nSpecies];
		PamSymbol[] symbols = clickIdentifier.getSymbols();
		for (int i = 0; i < nSpecies; i++) {
			colours[i] = dataStreamColour;
			if (symbols != null && i < symbols.length) {
				colours[i] = symbols[i].getLineColor();
			}
		}
		int[] typesCount;
		int nTypes;

		OfflineDataMapPoint mapPoint;
		ClickBinaryModuleFooter clickFooter;
		int n;
		int x1, x2, y1, y2;
		//		boolean logScale = dataMapControl.dataMapParameters.vLogScale;
		long pointStart, pointEnd;
		//		int scaleType = dataMapControl.dataMapParameters.vScaleChoice;
		int h = dataGraph.getHeight();
		int w = dataGraph.getWidth();
		g.setColor(dataStreamColour);
		synchronized(map) {
			//			long startMillis = scrollingDataPanel.getScreenStartMillis();
			//			long endMillis = scrollingDataPanel.getScreenEndMillis();
			Iterator<OfflineDataMapPoint> iterator = map.getListIterator();
			/**
			 * Each map point needs to be drawn in order of decreasing number of 
			 * entries, otherwise, small numbers of entries can't be seen.
			 */
			int iType;
			while (iterator.hasNext()) {
				mapPoint = iterator.next();
				clickFooter = (ClickBinaryModuleFooter) ((BinaryOfflineDataMapPoint) mapPoint).getModuleFooter();
				if (clickFooter == null) {
					continue;
				}
				typesCount = clickFooter.getTypesCount();
				if (typesCount == null) {
					continue;
				}
				nTypes = Math.min(typesCount.length, nSpecies);			
				int[] clickNumbers = new int[nTypes];
				int[] sortIndexes = new int[nTypes];
				for (iType = 0; iType < nTypes; iType++) {
					clickNumbers[iType] = typesCount[iType];
				}
				BubbleSort.sortAcending(clickNumbers, sortIndexes);
				for (int j = nTypes-1; j >=0; j--) {
					iType = sortIndexes[j];
					n = typesCount[iType];
					g.setColor(colours[iType]);
					pointStart = mapPoint.getStartTime();
					pointEnd = mapPoint.getEndTime();
					x1 = dataGraph.getXCoord(pointStart);
					x2 = dataGraph.getXCoord(pointEnd);
					if (x2 < 0) {
						continue;
					}
					if (x1 > w) {
						break;
					}

					y2 = h; 
					y1 = dataGraph.getYCoord(n, pointEnd-pointStart);
					if (x1 == x2) {
						g.drawLine(x1, y1, x2, y2);
					}
					else {
						g.drawRect(x1, y1, x2-x1, y2-y1);
						g.fillRect(x1, y1, x2-x1, y2-y1);						
					}
				}
			}
		}

	}

	@Override
	public void drawEffort(Graphics g, DataGraph dataGraph, OfflineDataMap map,
			Color haveDataColour) {
		dataGraph.drawEffort(g, map, haveDataColour);

	}

	@Override
	public KeyPanel getKeyPanel() {
		KeyPanel keyPanel = new KeyPanel("Click Types",0);
		keyPanel.add(new LineKeyItem(Color.black, "Unclassified"));
		ClickIdentifier clickIdentifier = clickDetector.getClickControl().getClickIdentifier();
		if (clickIdentifier == null) {
			return keyPanel;
		}
		PamSymbol[] symbols = clickIdentifier.getSymbols();
		String[] species = clickIdentifier.getSpeciesList();
		if (symbols == null || species == null) {
			return keyPanel;
		}
		for (int i = 0; i < symbols.length; i++) {
			keyPanel.add(new LineKeyItem(symbols[i].getLineColor(), species[i]));
		}
		
		return keyPanel;
	}

}
