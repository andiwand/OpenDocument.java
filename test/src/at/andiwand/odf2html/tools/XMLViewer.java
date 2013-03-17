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
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import at.andiwand.commons.lwxml.LWXMLConstants;
import at.andiwand.commons.lwxml.LWXMLEvent;
import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.lwxml.reader.LWXMLStreamReader;
import at.andiwand.commons.swing.JTreeUtil;
import at.andiwand.commons.util.object.ObjectMatcher;


public class XMLViewer extends JFrame {
	
	private static final long serialVersionUID = 901256796884354336L;
	
	private static class NodeMatcher implements ObjectMatcher<TreeNode> {
		private final Pattern pattern;
		
		public NodeMatcher(Pattern pattern) {
			this.pattern = pattern;
		}
		
		public boolean matches(TreeNode o) {
			if (!(o instanceof DefaultMutableTreeNode)) return false;
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) o;
			
			String text = (String) node.getUserObject();
			Matcher matcher = pattern.matcher(text);
			return matcher.find();
		}
	}
	
	private JFileChooser fileChooser = new JFileChooser();
	
	private DefaultTreeModel treeModel = new DefaultTreeModel(null);
	private JTree tree = new JTree(treeModel);
	
	private Pattern lastPattern;
	private TreePath lastMatch;
	
	public XMLViewer() {
		super("XML Viewer");
		
		add(new JScrollPane(tree));
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu file = new JMenu("File");
		JMenuItem open = new JMenuItem("Open...");
		file.add(open);
		menuBar.add(file);
		
		JMenu treeMenu = new JMenu("Tree");
		JMenuItem expandAll = new JMenuItem("Expand all");
		treeMenu.add(expandAll);
		JMenuItem collapseAll = new JMenuItem("Collapse all");
		treeMenu.add(collapseAll);
		treeMenu.addSeparator();
		JMenuItem find = new JMenuItem("Find...");
		treeMenu.add(find);
		JMenuItem findNext = new JMenuItem("Find next");
		treeMenu.add(findNext);
		menuBar.add(treeMenu);
		
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
		
		expandAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTreeUtil.expandAll(tree);
			}
		});
		
		collapseAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTreeUtil.collapseAll(tree);
			}
		});
		
		find.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String pattern = JOptionPane.showInputDialog(XMLViewer.this,
						"Enter pattern to search with:", "Find...",
						JOptionPane.QUESTION_MESSAGE);
				if (pattern == null) return;
				
				lastPattern = Pattern
						.compile(pattern, Pattern.CASE_INSENSITIVE);
				lastMatch = JTreeUtil.findNode(tree, new NodeMatcher(
						lastPattern), null);
				System.out.println(lastMatch);
				tree.setSelectionPath(lastMatch);
				tree.scrollPathToVisible(lastMatch);
			}
		});
		
		findNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (lastPattern == null) return;
				
				lastMatch = JTreeUtil.findNode(tree, new NodeMatcher(
						lastPattern), lastMatch);
				System.out.println(lastMatch);
				tree.setSelectionPath(lastMatch);
				tree.scrollPathToVisible(lastMatch);
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
	
	public void setFileChooser(JFileChooser fileChooser) {
		this.fileChooser = fileChooser;
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
		// removed Deque because of Android 1.6
		//		Deque<DefaultMutableTreeNode> nodeStack = new LinkedList<DefaultMutableTreeNode>();
		LinkedList<DefaultMutableTreeNode> nodeStack = new LinkedList<DefaultMutableTreeNode>();
		nodeStack.addFirst(rootNode);
		
		DefaultMutableTreeNode attributeNode = null;
		
		while (true) {
			DefaultMutableTreeNode currentNode = nodeStack.getFirst();
			
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
				nodeStack.addFirst(node);
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
				nodeStack.removeFirst();
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
			default:
				break;
			}
		}
		
		treeModel.setRoot(rootNode);
		
		in.close();
	}
	
}