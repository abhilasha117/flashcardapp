import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class FlashcardApp extends JFrame {
    
    // Flashcard data structure
    private static class Flashcard {
        String question;
        String answer;
        String category;
        
        Flashcard(String category, String question, String answer) {
            this.category = category;
            this.question = question;
            this.answer = answer;
        }
    }
    
    private List<Flashcard> flashcards;
    private int currentIndex = 0;
    private boolean showingAnswer = false;
    
    // UI Components
    private JLabel categoryLabel;
    private JTextArea contentArea;
    private JButton prevButton, nextButton, flipButton;
    private JLabel indexLabel;
    private JPanel categoryPanel;
    
    // Category buttons
    private JButton allButton, javaButton, htmlButton, cssButton;
    
    // Colors
    private final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private final Color SECONDARY_COLOR = new Color(46, 204, 113);
    private final Color ACCENT_COLOR = new Color(155, 89, 182);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    private final Color DARK_TEXT = new Color(44, 62, 80);
    
    public FlashcardApp() {
        initializeFlashcards();
        setupUI();
        displayCurrentCard();
        setVisible(true);
    }
    
    private void initializeFlashcards() {
        flashcards = new ArrayList<>();
        
        // Java Flashcards
        flashcards.add(new Flashcard("Java", 
                "What is the basic syntax to declare a variable in Java?", 
                "DataType variableName = value; // e.g., int x = 5;"));
        
        flashcards.add(new Flashcard("Java", 
                "How do you create a simple for loop in Java?", 
                "for (int i = 0; i < 10; i++) { // code here }"));
        
        flashcards.add(new Flashcard("Java", 
                "What is the difference between public and private access modifiers?", 
                "Public: Accessible from anywhere. Private: Accessible only within the same class."));
        
        flashcards.add(new Flashcard("Java", 
                "How do you instantiate an object of a class?", 
                "ClassName objectName = new ClassName();"));
        
        flashcards.add(new Flashcard("Java", 
                "What does the 'main' method signature look like?", 
                "public static void main(String[] args) { // code }"));
        
        flashcards.add(new Flashcard("Java", 
                "What are the primitive data types in Java?", 
                "byte, short, int, long, float, double, boolean, char"));
        
        // HTML Flashcards
        flashcards.add(new Flashcard("HTML", 
                "What is the purpose of the <div> tag in HTML?", 
                "It defines a division or section in an HTML document, used for grouping elements."));
        
        flashcards.add(new Flashcard("HTML", 
                "How do you apply a CSS class to an HTML element?", 
                "<element class=\"classname\">Content</element>"));
        
        flashcards.add(new Flashcard("HTML", 
                "What is the difference between ids and classes in HTML?", 
                "IDs should be unique per page, while classes can be used on multiple elements."));
        
        flashcards.add(new Flashcard("HTML", 
                "What does the <head> tag contain in HTML?", 
                "Metadata, title, links to stylesheets, and scripts."));
        
        flashcards.add(new Flashcard("HTML", 
                "Which tag is used to create a hyperlink?", 
                "<a href=\"url\">Link text</a>"));
        
        // CSS Flashcards
        flashcards.add(new Flashcard("CSS", 
                "What is the CSS property to set the background color?", 
                "background-color: color; // e.g., background-color: blue;"));
        
        flashcards.add(new Flashcard("CSS", 
                "How do you center text in CSS?", 
                "text-align: center;"));
        
        flashcards.add(new Flashcard("CSS", 
                "What is the Box Model in CSS?", 
                "It defines how elements are rendered with content, padding, border, and margin."));
        
        flashcards.add(new Flashcard("CSS", 
                "How do you make an element's corners rounded?", 
                "border-radius: value; // e.g., border-radius: 5px;"));
        
        flashcards.add(new Flashcard("CSS", 
                "What is the difference between padding and margin?", 
                "Padding is inside the border, margin is outside the border."));
    }
    
    private void setupUI() {
        setTitle("Programming Flashcards - Java, HTML & CSS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        getContentPane().setBackground(LIGHT_BG);
        
        // Top panel: Category selection
        categoryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        categoryPanel.setBackground(LIGHT_BG);
        
        allButton = createCategoryButton("All", PRIMARY_COLOR);
        javaButton = createCategoryButton("Java", new Color(220, 80, 70));
        htmlButton = createCategoryButton("HTML", new Color(240, 150, 50));
        cssButton = createCategoryButton("CSS", new Color(40, 120, 180));
        
        allButton.addActionListener(e -> filterCards("All"));
        javaButton.addActionListener(e -> filterCards("Java"));
        htmlButton.addActionListener(e -> filterCards("HTML"));
        cssButton.addActionListener(e -> filterCards("CSS"));
        
        categoryPanel.add(allButton);
        categoryPanel.add(javaButton);
        categoryPanel.add(htmlButton);
        categoryPanel.add(cssButton);
        
        add(categoryPanel, BorderLayout.NORTH);
        
        // Center: Content area for question/answer
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(LIGHT_BG);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        contentArea = new JTextArea();
        contentArea.setFont(new Font("Arial", Font.PLAIN, 18));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setBackground(Color.WHITE);
        contentArea.setForeground(DARK_TEXT);
        contentArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setBorder(null);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Info panel with category and index
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoPanel.setBackground(LIGHT_BG);
        
        categoryLabel = new JLabel("Category: ");
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 14));
        categoryLabel.setForeground(DARK_TEXT);
        
        indexLabel = new JLabel("Card 1 of " + flashcards.size());
        indexLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        indexLabel.setForeground(DARK_TEXT);
        
        infoPanel.add(categoryLabel);
        infoPanel.add(Box.createHorizontalStrut(20));
        infoPanel.add(indexLabel);
        
        centerPanel.add(infoPanel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Bottom panel: Navigation buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        bottomPanel.setBackground(LIGHT_BG);
        
        prevButton = createStyledButton("Previous", PRIMARY_COLOR);
        nextButton = createStyledButton("Next", PRIMARY_COLOR);
        flipButton = createStyledButton("Flip to Answer", SECONDARY_COLOR);
        
        prevButton.addActionListener(e -> navigate(-1));
        nextButton.addActionListener(e -> navigate(1));
        flipButton.addActionListener(e -> flipCard());
        
        bottomPanel.add(prevButton);
        bottomPanel.add(flipButton);
        bottomPanel.add(nextButton);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Disable previous on first card
        updateNavigationButtons();
    }
    
    private JButton createCategoryButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setBorderPainted(false);
        return button;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setBorderPainted(false);
        return button;
    }
    
    private void displayCurrentCard() {
        if (flashcards.isEmpty()) return;
        
        Flashcard card = flashcards.get(currentIndex);
        categoryLabel.setText("Category: " + card.category);
        contentArea.setText(showingAnswer ? card.answer : card.question);
        contentArea.setCaretPosition(0);
        indexLabel.setText("Card " + (currentIndex + 1) + " of " + flashcards.size());
        updateNavigationButtons();
    }
    
    private void flipCard() {
        showingAnswer = !showingAnswer;
        flipButton.setText(showingAnswer ? "Flip to Question" : "Flip to Answer");
        displayCurrentCard();
    }
    
    private void navigate(int direction) {
        int newIndex = currentIndex + direction;
        if (newIndex >= 0 && newIndex < flashcards.size()) {
            currentIndex = newIndex;
            showingAnswer = false;
            flipButton.setText("Flip to Answer");
            displayCurrentCard();
        }
    }
    
    private void updateNavigationButtons() {
        prevButton.setEnabled(currentIndex > 0);
        nextButton.setEnabled(currentIndex < flashcards.size() - 1);
    }
    
    private void filterCards(String category) {
        // Reset to show all cards
        List<Flashcard> originalCards = new ArrayList<>();
        originalCards.addAll(flashcards);
        
        if (!category.equals("All")) {
            flashcards.removeIf(card -> !card.category.equalsIgnoreCase(category));
        }
        
        currentIndex = 0;
        showingAnswer = false;
        flipButton.setText("Flip to Answer");
        displayCurrentCard();
        
        // Restore the original list for future filtering
        if (!category.equals("All")) {
            flashcards.clear();
            flashcards.addAll(originalCards);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FlashcardApp());
    }
}
