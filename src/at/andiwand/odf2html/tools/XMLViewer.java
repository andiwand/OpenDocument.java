package at.andiwand.odf2html.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Deque;
import java.util.LinkedList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import at.andiwand.commons.lwxml.LWXMLConstants;
import at.andiwand.commons.lwxml.LWXMLEvent;
import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.lwxml.reader.LWXMLStreamReader;
import at.andiwand.commons.swing.JFrameUtil;


public class XMLViewer extends JFrame {
	
	private static final long serialVersionUID = 901256796884354336L;
	
	private JFileChooser fileChooser = new JFileChooser();
	
	private DefaultTreeModel treeModel = new DefaultTreeModel(null);
	
	public XMLViewer() {
		super("XML Viewer");
		
		JTree tree = new JTree(treeModel);
		add(new JScrollPane(tree));
		
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem open = new JMenuItem("Open");
		file.add(open);
		menuBar.add(file);
		setJMenuBar(menuBar);
		
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int result = fileChooser.showOpenDialog(XMLViewer.this);
				if (result == JFileChooser.CANCEL_OPTION) return;
				
				try {
					File file = fileChooser.getSelectedFile();
					open(file);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(XMLViewer.this, e,
							"Exception", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	public XMLViewer(File file) throws IOException {
		this();
		open(file);
	}
	
	public XMLViewer(String string) throws IOException {
		this();
		open(string);
	}
	
	public XMLViewer(InputStream in, String rootTitle) throws IOException {
		this();
		open(in, rootTitle);
	}
	
	public XMLViewer(Reader in, String rootTitle) throws IOException {
		this();
		open(in, rootTitle);
	}
	
	public void open(File file) throws IOException {
		open(new FileReader(file), file.getName());
	}
	
	public void open(String string) throws IOException {
		open(string, "xml string");
	}
	
	public void open(String string, String rootTitle) throws IOException {
		open(new StringReader(string), rootTitle);
	}
	
	public void open(InputStream in, String rootTitle) throws IOException {
		open(new InputStreamReader(in), rootTitle);
	}
	
	public void open(Reader reader, String rootTitle) throws IOException {
		LWXMLReader in = new LWXMLStreamReader(reader);
		
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootTitle);
		Deque<DefaultMutableTreeNode> nodeStack = new LinkedList<DefaultMutableTreeNode>();
		nodeStack.push(rootNode);
		
		DefaultMutableTreeNode attributeNode = null;
		
		while (true) {
			DefaultMutableTreeNode currentNode = nodeStack.peek();
			
			LWXMLEvent event = in.readEvent();
			if (event == LWXMLEvent.END_DOCUMENT) break;
			
			switch (event) {
			case PROCESSING_INSTRUCTION_TARGET:
				currentNode
						.add(new DefaultMutableTreeNode("<?" + in.readValue()
								+ " " + in.readFollowingValue() + "?>"));
				break;
			case START_ELEMENT:
				DefaultMutableTreeNode node = new DefaultMutableTreeNode("<"
						+ in.readValue() + ">");
				currentNode.add(node);
				attributeNode = null;
				nodeStack.push(node);
				break;
			case ATTRIBUTE_NAME:
				if (attributeNode == null) {
					attributeNode = new DefaultMutableTreeNode("attributes");
					currentNode.add(attributeNode);
				}
				
				attributeNode.add(new DefaultMutableTreeNode(in.readValue()
						+ " = " + in.readFollowingValue()));
				break;
			case END_EMPTY_ELEMENT:
			case END_ELEMENT:
				attributeNode = null;
				nodeStack.pop();
				break;
			case CHARACTERS:
				String characters = in.readValue();
				if (LWXMLConstants.isWhitespace(characters)) break;
				currentNode.add(new DefaultMutableTreeNode(characters));
				break;
			case CDATA:
				currentNode.add(new DefaultMutableTreeNode("<![CDATA["
						+ in.readValue() + "]]>"));
				break;
			case COMMENT:
				currentNode.add(new DefaultMutableTreeNode("<!-- "
						+ in.readValue() + " -->"));
				break;
			}
		}
		
		treeModel.setRoot(rootNode);
	}
	
	public static void main(String[] args) {
		XMLViewer viewer = new XMLViewer();
		viewer.setSize(400, 400);
		JFrameUtil.centerFrame(viewer);
		viewer.setDefaultCloseOperation(EXIT_ON_CLOSE);
		viewer.setVisible(true);
	}
	
}