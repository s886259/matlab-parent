package PamView;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class DBTextArea  {

	private int maxChars;
	
	private JTextArea textArea;
	
	private JScrollPane scrollPane;
	
	private static final int POLICY_STOP_TYPING = 1;
	private static final int POLICY_KEEP_TYPING = 2;
	
	private int policy = POLICY_STOP_TYPING;
	
	
	public DBTextArea(int rows, int columns, int maxChars) {
//		super(rows, columns);
		textArea = new JTextArea(rows, columns);
		this.maxChars = maxChars;
//		textArea.setPreferredSize(new Dimension(1, 50));
//		textArea.setBorder(BorderFactory.createLoweredBevelBorder());
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setToolTipText(String.format("Comments > %d characters long will be truncated in the database", 
				maxChars));
		textArea.addKeyListener(new CommentListener());
		scrollPane = new JScrollPane(textArea);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(300, 200));
	}

	class CommentListener implements KeyListener {
		public void keyPressed(KeyEvent e) {			
		}
		public void keyReleased(KeyEvent e) {			
		}
		public void keyTyped(KeyEvent e) {
			checkCommentLength();
		}
	}
	
	private void checkCommentLength() {
		int commentLength = 0;
		String txt = textArea.getText();
		if (txt != null) {
			commentLength = txt.length();
		}
		if (commentLength <= maxChars) {
			textArea.setBackground(Color.WHITE);
		}
		else {
			textArea.setBackground(Color.PINK);
			if (policy == POLICY_STOP_TYPING) {
				textArea.setText(txt.substring(0,maxChars));
			}
		}
	}

	public JComponent getComponent() {
		return scrollPane;
	}
	
	public void setText(String text) {
		textArea.setText(text);
	}
	
	public String getText() {
		return textArea.getText();
	}

}
