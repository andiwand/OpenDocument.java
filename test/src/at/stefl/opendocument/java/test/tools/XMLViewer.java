package at.stefl.opendocument.java.test.tools;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

import javax.swing.AbstractButton;
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
import javax.swing.tree.TreePath;

import at.stefl.commons.lwxml.LWXMLConstants;
import at.stefl.commons.lwxml.LWXMLEvent;
import at.stefl.commons.lwxml.reader.LWXMLCountingReader;
import at.stefl.commons.lwxml.reader.LWXMLStreamReader;
import at.stefl.commons.swing.JTreeUtil;

// TODO: extend JComponent
public class XMLViewer extends JFrame {
    
    private static final long serialVersionUID = 901256796884354336L;
    
    private static final String ELEMENT_COUNT_PREFIX = "Element count: ";
    private static final String ATTRIBUTE_COUNT_PREFIX = "Attribute count: ";
    
    private static class NodeMatcher {
        
        private final Pattern pattern;
        
        public NodeMatcher(Pattern pattern) {
            this.pattern = pattern;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (!(obj instanceof DefaultMutableTreeNode)) return false;
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
            
            String text = (String) node.getUserObject();
            Matcher matcher = pattern.matcher(text);
            return matcher.find();
        }
    }
    
    private static final Clipboard CLIPBOARD = Toolkit.getDefaultToolkit()
            .getSystemClipboard();
    
    private static final ActionListener COPY_TEXT = new ActionListener() {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            String text;
            
            if (source instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) source;
                text = button.getText();
            } else {
                System.out.println("no text for class: " + source.getClass());
                return;
            }
            
            StringSelection selection = new StringSelection(text);
            CLIPBOARD.setContents(selection, selection);
        }
    };
    
    private final KeyListener copySelected = new KeyAdapter() {
        
        @Override
        public void keyTyped(KeyEvent e) {
            if (e.getKeyChar() != 3) return;
            
            Object source = tree.getSelectionPath().getLastPathComponent();
            if (!(source instanceof DefaultMutableTreeNode)) return;
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) source;
            
            StringSelection selection = new StringSelection(node.toString());
            CLIPBOARD.setContents(selection, selection);
        }
    };
    
    private JFileChooser fileChooser = new JFileChooser();
    
    private DefaultTreeModel treeModel = new DefaultTreeModel(null);
    private JTree tree = new JTree(treeModel);
    
    private JMenuItem elementCount = new JMenuItem(ELEMENT_COUNT_PREFIX + "n/a");
    private JMenuItem attributeCount = new JMenuItem(ATTRIBUTE_COUNT_PREFIX
            + "n/a");
    
    private Pattern lastPattern;
    private TreePath lastMatch;
    
    public XMLViewer() {
        super("XML Viewer");
        
        add(new JScrollPane(tree));
        
        tree.addKeyListener(copySelected);
        
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
        
        JMenu statistic = new JMenu("Statistic");
        elementCount.addActionListener(COPY_TEXT);
        statistic.add(elementCount);
        attributeCount.addActionListener(COPY_TEXT);
        statistic.add(attributeCount);
        menuBar.add(statistic);
        
        setJMenuBar(menuBar);
        
        open.addActionListener(new ActionListener() {
            
            @Override
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
            
            @Override
            public void actionPerformed(ActionEvent e) {
                JTreeUtil.expandAll(tree);
            }
        });
        
        collapseAll.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                JTreeUtil.collapseAll(tree);
            }
        });
        
        find.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                String pattern = JOptionPane.showInputDialog(XMLViewer.this,
                        "Enter pattern to search with:", "Find...",
                        JOptionPane.QUESTION_MESSAGE);
                if (pattern == null) return;
                
                lastPattern = Pattern
                        .compile(pattern, Pattern.CASE_INSENSITIVE);
                lastMatch = JTreeUtil.findNode(tree, new NodeMatcher(
                        lastPattern));
                tree.setSelectionPath(lastMatch);
                tree.scrollPathToVisible(lastMatch);
            }
        });
        
        findNext.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (lastPattern == null) return;
                
                lastMatch = JTreeUtil.findNode(tree, new NodeMatcher(
                        lastPattern), lastMatch);
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
        LWXMLCountingReader in = new LWXMLCountingReader(new LWXMLStreamReader(
                reader));
        
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootTitle);
        // removed Deque because of Android 1.6
        // Deque<DefaultMutableTreeNode> nodeStack = new
        // LinkedList<DefaultMutableTreeNode>();
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
        
        elementCount.setText(ELEMENT_COUNT_PREFIX
                + in.getCount(LWXMLEvent.START_ELEMENT));
        attributeCount.setText(ATTRIBUTE_COUNT_PREFIX
                + in.getCount(LWXMLEvent.ATTRIBUTE_NAME));
    }
    
    public static void main(String[] args) {
        XMLViewer viewer = new XMLViewer();
        viewer.setDefaultCloseOperation(EXIT_ON_CLOSE);
        viewer.setSize(600, 500);
        viewer.setLocationRelativeTo(null);
        viewer.setVisible(true);
    }
    
}