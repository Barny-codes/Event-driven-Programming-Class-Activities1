import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * ============================================================================
 * SCHOOL OF THOUGHTS (SOT) — STUDENT PORTAL APPLICATION
 * ============================================================================
 * Features:
 *   1. Fixed dark mode text contrast issues for perfect visibility.
 *   2. Upgraded University Inbox into a fully interactive email client (Read & Compose).
 *   3. Friday Free Day master schedule & KSH student accounts ledger.
 */
public class StudentPortal {

    // --- GUI NAVIGATION & STATE VARIABLES ---
    private static CardLayout cardLayout;             
    private static JPanel dynamicCardContainer;       
    private static String studentName = "Ecobar";   
    private static JLabel welcomeLabel;               
    private static JLabel profNameLabel;              
    private static boolean isDarkMode = false;        

    // --- THEME COLOR PALETTES ---
    private static final Color BLUE_PRIMARY = new Color(0, 53, 107);     // SOT Navy Blue
    private static final Color DARK_ORANGE = new Color(217, 107, 0);     // SOT Accent Orange
    
    // Light Mode Palette Constants
    private static final Color LIGHT_BG = Color.WHITE;
    private static final Color LIGHT_CARD_BG = new Color(255, 247, 240);
    private static final Color LIGHT_SIDEBAR_BG = new Color(245, 246, 250);
    private static final Color LIGHT_TEXT = new Color(20, 30, 40);
    private static final Color LIGHT_MUTED = new Color(110, 120, 130);
    private static final Color LIGHT_BORDER = new Color(230, 232, 240);

    // Dark Mode Palette Constants
    private static final Color DARK_BG = new Color(30, 30, 35);
    private static final Color DARK_CARD_BG = new Color(45, 45, 52);
    private static final Color DARK_SIDEBAR_BG = new Color(38, 38, 45);
    private static final Color DARK_TEXT = new Color(240, 240, 245);
    private static final Color DARK_MUTED = new Color(180, 185, 195);
    private static final Color DARK_BORDER = new Color(60, 60, 70);

    // Structural Panels
    private static JFrame frame;
    private static JPanel header, sidebar, statusBar;

    // Cross-platform Emoji Font Setup
    private static final Font HEADER_ICON_FONT = getCompatibleFont(22);
    private static final Font EMOJI_FONT = getCompatibleFont(20);
    private static final Font SMALL_EMOJI_FONT = getCompatibleFont(14);

    private static Font getCompatibleFont(int size) {
        String[] fontNames = {"Segoe UI Emoji", "Segoe UI Symbol", "DejaVu Sans", "Arial"};
        for (String name : fontNames) {
            Font font = new Font(name, Font.PLAIN, size);
            if (font.canDisplay('\u231A') || font.canDisplay('\u270F') || font.canDisplay('\u2611')) {
                return font;
            }
        }
        return new Font("SansSerif", Font.PLAIN, size);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentPortal::createAndShow);
    }

    private static void createAndShow() {
        frame = new JFrame("School of Thoughts (SOT) — Student Portal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1180, 780);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
        String formattedDate = currentDate.format(formatter);

        // HEADER
        header = new JPanel(new BorderLayout());
        header.setBackground(BLUE_PRIMARY);
        header.setBorder(new EmptyBorder(15, 25, 15, 25));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titlePanel.setOpaque(false);
        
        JLabel logoIcon = new JLabel("🏛");
        logoIcon.setFont(HEADER_ICON_FONT);
        logoIcon.setForeground(DARK_ORANGE);

        JPanel textStack = new JPanel(new GridLayout(2, 1, 0, 2));
        textStack.setOpaque(false);
        
        JLabel title = new JLabel("School of Thoughts (SOT) Student Portal");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Georgia", Font.BOLD, 20));
        
        JLabel subtitle = new JLabel("Cogito, Ergo Sum  •  Think | Innovate | Lead");
        subtitle.setForeground(DARK_ORANGE);
        subtitle.setFont(new Font("Georgia", Font.ITALIC, 12));
        
        textStack.add(title);
        textStack.add(subtitle);
        titlePanel.add(logoIcon);
        titlePanel.add(textStack);

        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        rightHeader.setOpaque(false);

        JButton toggleModeBtn = new JButton("🌙  Night Mode");
        toggleModeBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        toggleModeBtn.setBackground(DARK_ORANGE);
        toggleModeBtn.setForeground(Color.WHITE);
        toggleModeBtn.setFocusPainted(false);
        toggleModeBtn.setBorder(new EmptyBorder(8, 14, 8, 14));
        toggleModeBtn.addActionListener(e -> {
            isDarkMode = !isDarkMode;
            if (isDarkMode) {
                toggleModeBtn.setText("☀️  Light Mode");
                toggleModeBtn.setBackground(new Color(240, 200, 80));
                toggleModeBtn.setForeground(Color.DARK_GRAY);
            } else {
                toggleModeBtn.setText("🌙  Night Mode");
                toggleModeBtn.setBackground(DARK_ORANGE);
                toggleModeBtn.setForeground(Color.WHITE);
            }
            refreshTheme(formattedDate);
        });

        JButton logoutButton = new JButton("Sign Out");
        logoutButton.setBackground(new Color(175, 40, 40));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(new EmptyBorder(8, 16, 8, 16));
        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Signed out successfully.", "SOT Session Closed", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        });

        rightHeader.add(toggleModeBtn);
        rightHeader.add(logoutButton);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(rightHeader, BorderLayout.EAST);
        frame.add(header, BorderLayout.NORTH);

        // SIDEBAR NAVIGATION
        buildSidebar(formattedDate);

        // CARD CONTAINER
        cardLayout = new CardLayout();
        dynamicCardContainer = new JPanel(cardLayout);

        dynamicCardContainer.add(createDashboardView(formattedDate, frame), "Dashboard");
        dynamicCardContainer.add(createCoursesView(formattedDate, frame), "CoursesEnrolment");
        dynamicCardContainer.add(createGradesView(formattedDate), "AcademicGrades");
        dynamicCardContainer.add(createAccountsView(formattedDate), "StudentAccounts");
        dynamicCardContainer.add(createScheduleView(formattedDate), "ClassSchedule");
        dynamicCardContainer.add(createInboxView(formattedDate), "UniversityInbox");

        frame.add(dynamicCardContainer, BorderLayout.CENTER);

        // STATUS BAR
        statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(new EmptyBorder(8, 20, 8, 20));
        
        JLabel statusLeft = new JLabel("● Online Status: Active Connection (SOT Portal Node)");
        statusLeft.setFont(new Font("Arial", Font.PLAIN, 11));
        
        JLabel statusRight = new JLabel("Today: " + formattedDate + "  |  Currency: KSH");
        statusRight.setFont(new Font("Arial", Font.PLAIN, 11));

        statusBar.add(statusLeft, BorderLayout.WEST);
        statusBar.add(statusRight, BorderLayout.EAST);
        frame.add(statusBar, BorderLayout.SOUTH);

        applyCurrentTheme();
        frame.setVisible(true);
    }

    private static void buildSidebar(String formattedDate) {
        if (sidebar != null) frame.remove(sidebar);

        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));

        JLabel quickLinks = new JLabel("NAVIGATION");
        quickLinks.setFont(new Font("Arial", Font.BOLD, 11));
        quickLinks.setForeground(isDarkMode ? DARK_MUTED : LIGHT_MUTED);
        quickLinks.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(quickLinks);
        sidebar.add(Box.createVerticalStrut(14));

        String[][] menuData = {
            {"🏠  Dashboard", "Dashboard"},
            {"📚  Courses & Enrolment", "CoursesEnrolment"},
            {"📊  Academic Grades", "AcademicGrades"},
            {"💳  Student Accounts (KSH)", "StudentAccounts"},
            {"📅  Class Schedule", "ClassSchedule"},
            {"✉️  University Inbox", "UniversityInbox"}
        };

        JButton[] navButtons = new JButton[menuData.length];
        for (int i = 0; i < menuData.length; i++) {
            final String tabKey = menuData[i][1];
            JButton btn = new JButton(menuData[i][0]);
            btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setFocusPainted(false);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            btn.setBorder(new EmptyBorder(10, 15, 10, 15));
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);

            if (i == 0) {
                btn.setBackground(DARK_ORANGE);
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(isDarkMode ? DARK_CARD_BG : Color.WHITE);
                btn.setForeground(isDarkMode ? DARK_TEXT : LIGHT_TEXT);
            }

            btn.addActionListener(e -> {
                for (JButton b : navButtons) {
                    b.setBackground(isDarkMode ? DARK_CARD_BG : Color.WHITE);
                    b.setForeground(isDarkMode ? DARK_TEXT : LIGHT_TEXT);
                }
                btn.setBackground(DARK_ORANGE);
                btn.setForeground(Color.WHITE);
                cardLayout.show(dynamicCardContainer, tabKey);
            });

            navButtons[i] = btn;
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(8));
        }

        // Online Status Box
        sidebar.add(Box.createVerticalGlue());
        JPanel onlineBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        onlineBox.setOpaque(true);
        onlineBox.setBackground(isDarkMode ? new Color(35, 50, 35) : new Color(230, 248, 230));
        onlineBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        onlineBox.setBorder(BorderFactory.createLineBorder(new Color(150, 210, 150), 1));
        
        JLabel onlineDot = new JLabel("●");
        onlineDot.setFont(new Font("Arial", Font.BOLD, 14));
        onlineDot.setForeground(new Color(40, 160, 40));
        
        JLabel onlineText = new JLabel("Status: Online");
        onlineText.setFont(new Font("Arial", Font.BOLD, 12));
        onlineText.setForeground(new Color(40, 130, 40));
        
        onlineBox.add(onlineDot);
        onlineBox.add(onlineText);
        sidebar.add(onlineBox);

        sidebar.setBackground(isDarkMode ? DARK_SIDEBAR_BG : LIGHT_SIDEBAR_BG);
        frame.add(sidebar, BorderLayout.WEST);
        frame.revalidate();
        frame.repaint();
    }

    // =========================================================================
    // PAGE 1: DASHBOARD (FIXED CONTRAST FONTS)
    // =========================================================================
    private static JPanel createDashboardView(String currentDateStr, JFrame parentFrame) {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));
        topRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        topRow.setOpaque(false);

        JPanel welcomePanel = new JPanel(new GridLayout(2, 1, 0, 4));
        welcomePanel.setOpaque(false);
        
        welcomeLabel = new JLabel("Welcome back, " + studentName + ".");
        welcomeLabel.setFont(new Font("Georgia", Font.BOLD, 22));
        // Fixed: Use white in dark mode and primary blue in light mode
        welcomeLabel.setForeground(isDarkMode ? DARK_TEXT : BLUE_PRIMARY);
        
        JLabel welcomeSub = new JLabel("Academic summary and institutional announcements for " + currentDateStr + ".");
        welcomeSub.setFont(new Font("Arial", Font.PLAIN, 13));
        welcomeSub.setForeground(isDarkMode ? DARK_MUTED : LIGHT_MUTED);
        
        welcomePanel.add(welcomeLabel);
        welcomePanel.add(welcomeSub);

        JPanel profileBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        profileBox.setOpaque(false);

        JButton editNameBtn = new JButton("🖋 Edit Name");
        editNameBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));
        editNameBtn.setFocusPainted(false);
        editNameBtn.setBackground(isDarkMode ? DARK_CARD_BG : new Color(240, 242, 246));
        editNameBtn.setForeground(isDarkMode ? DARK_TEXT : LIGHT_TEXT);
        editNameBtn.setMargin(new Insets(4, 8, 4, 8));
        editNameBtn.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog(parentFrame, "Enter your preferred student name:", "Edit Name", JOptionPane.QUESTION_MESSAGE);
            if (newName != null && !newName.trim().isEmpty()) {
                studentName = newName.trim();
                welcomeLabel.setText("Welcome back, " + studentName + ".");
                profNameLabel.setText(studentName);
            }
        });

        JPanel profileText = new JPanel(new GridLayout(2, 1, 0, 2));
        profileText.setOpaque(false);
        
        profNameLabel = new JLabel(studentName, SwingConstants.RIGHT);
        profNameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        profNameLabel.setForeground(isDarkMode ? DARK_TEXT : LIGHT_TEXT);
        
        JLabel profId = new JLabel("ID: SOT-654102 | Software Engineering", SwingConstants.RIGHT);
        profId.setFont(new Font("Arial", Font.PLAIN, 11));
        profId.setForeground(isDarkMode ? DARK_MUTED : LIGHT_MUTED);
        
        profileText.add(profNameLabel);
        profileText.add(profId);

        JLabel userAvatar = new JLabel("🎓");
        userAvatar.setFont(HEADER_ICON_FONT);

        profileBox.add(editNameBtn);
        profileBox.add(profileText);
        profileBox.add(userAvatar);

        topRow.add(welcomePanel, BorderLayout.WEST);
        topRow.add(profileBox, BorderLayout.EAST);
        
        content.add(topRow);
        content.add(Box.createVerticalStrut(25));

        // Stats Cards Row
        JPanel cardsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        cardsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));
        cardsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardsPanel.setOpaque(false);
        
        cardsPanel.add(createStatCard("📈", "Cumulative GPA", "3.88", isDarkMode ? DARK_CARD_BG : LIGHT_CARD_BG, isDarkMode ? DARK_TEXT : BLUE_PRIMARY));
        cardsPanel.add(createStatCard("📚", "Enrolled Units", "7 Courses (25 Credits)", isDarkMode ? DARK_CARD_BG : new Color(255, 243, 230), DARK_ORANGE));
        
        content.add(cardsPanel);
        content.add(Box.createVerticalStrut(30));

        // Notifications Card Panel
        JPanel notifCardWrapper = new JPanel();
        notifCardWrapper.setLayout(new BoxLayout(notifCardWrapper, BoxLayout.Y_AXIS));
        notifCardWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        notifCardWrapper.setBackground(isDarkMode ? DARK_CARD_BG : LIGHT_BG);
        notifCardWrapper.setBorder(BorderFactory.createLineBorder(isDarkMode ? DARK_BORDER : LIGHT_BORDER, 1));

        JPanel notifHeaderBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        notifHeaderBar.setBackground(isDarkMode ? new Color(38, 38, 45) : new Color(248, 250, 252));
        notifHeaderBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        JLabel bellIcon = createStyledLabel("🔔", SMALL_EMOJI_FONT, DARK_ORANGE);
        JLabel notifTitle = new JLabel("School of Thoughts (SOT) Official Notifications — Click Row for Details");
        notifTitle.setFont(new Font("Georgia", Font.BOLD, 14));
        notifTitle.setForeground(isDarkMode ? DARK_TEXT : BLUE_PRIMARY);
        
        notifHeaderBar.add(bellIcon);
        notifHeaderBar.add(notifTitle);
        notifCardWrapper.add(notifHeaderBar);

        Object[][] notifications = {
            {"📄", BLUE_PRIMARY, "Midterm Grades Posted in KSH & GPA Metrics", "Your instructor has uploaded graded assessments for Software Engineering.", currentDateStr, "Midterm Grade: A\nClass Average: B+\nInstructor Feedback: Excellent design patterns!"},
            {"🎉", DARK_ORANGE, "Friday Free Day & Campus Activity Schedule!", "No classes on Friday! Join optional hackathons, sports, and coffee social hours.", currentDateStr, "Friday Free Day Schedule (Optional):\n• 09:00 AM - Innovation & Hackathon Club\n• 10:30 AM - Coffee Social & Tech Fireside Chat\n• 01:00 PM - E-Sports League\n• 02:30 PM - Creative Film Workshop"},
            {"📅", new Color(40, 140, 70), "Semester Registration & Fee Clearance Open", "Finance holds cleared for 7-unit undergraduate course selection.", currentDateStr, "Registration is OPEN.\nPlease review your 'Student Accounts (KSH)' tab for fee statements."}
        };

        for (int i = 0; i < notifications.length; i++) {
            Object[] notif = notifications[i];
            JPanel item = new JPanel(new BorderLayout(15, 0));
            Color defaultBg = isDarkMode ? DARK_CARD_BG : LIGHT_BG;
            item.setBackground(defaultBg);
            item.setBorder(new EmptyBorder(14, 18, 14, 18));
            item.setCursor(new Cursor(Cursor.HAND_CURSOR));

            JLabel badgeIcon = new JLabel((String) notif[0], SwingConstants.CENTER);
            badgeIcon.setFont(SMALL_EMOJI_FONT);
            badgeIcon.setForeground(Color.WHITE);
            badgeIcon.setOpaque(true);
            badgeIcon.setBackground((Color) notif[1]);
            badgeIcon.setPreferredSize(new Dimension(38, 38));

            JPanel leftNotif = new JPanel(new GridLayout(2, 1, 0, 3));
            leftNotif.setOpaque(false);
            
            JLabel nHeader = new JLabel((String) notif[2]);
            nHeader.setFont(new Font("Arial", Font.BOLD, 13));
            nHeader.setForeground(isDarkMode ? DARK_TEXT : LIGHT_TEXT);
            
            JLabel nDesc = new JLabel((String) notif[3]);
            nDesc.setFont(new Font("Arial", Font.PLAIN, 12));
            nDesc.setForeground(isDarkMode ? DARK_MUTED : LIGHT_MUTED);
            
            leftNotif.add(nHeader);
            leftNotif.add(nDesc);

            JLabel nDate = new JLabel((String) notif[4] + "  ➔", SwingConstants.RIGHT);
            nDate.setFont(new Font("Arial", Font.BOLD, 12));
            nDate.setForeground(DARK_ORANGE);

            JPanel centerWrap = new JPanel(new BorderLayout(14, 0));
            centerWrap.setOpaque(false);
            centerWrap.add(badgeIcon, BorderLayout.WEST);
            centerWrap.add(leftNotif, BorderLayout.CENTER);

            item.add(centerWrap, BorderLayout.CENTER);
            item.add(nDate, BorderLayout.EAST);

            item.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    item.setBackground(isDarkMode ? new Color(60, 60, 72) : new Color(245, 248, 255));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    item.setBackground(defaultBg);
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    JOptionPane.showMessageDialog(parentFrame, 
                        (String) notif[5], 
                        "Notification Details", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            });

            notifCardWrapper.add(item);

            if (i < notifications.length - 1) {
                JSeparator sep = new JSeparator();
                sep.setForeground(isDarkMode ? DARK_BORDER : LIGHT_BORDER);
                notifCardWrapper.add(sep);
            }
        }

        content.add(notifCardWrapper);
        content.setBackground(isDarkMode ? DARK_BG : LIGHT_BG);
        return content;
    }

    // =========================================================================
    // PAGE 2: COURSES & ENROLMENT (7 UNITS)
    // =========================================================================
    private static JPanel createCoursesView(String dateStr, JFrame parentFrame) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));
        panel.setBackground(isDarkMode ? DARK_BG : LIGHT_BG);

        JPanel titleBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titleBox.setOpaque(false);
        titleBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel pageIcon = createStyledLabel("📚", HEADER_ICON_FONT, BLUE_PRIMARY);
        JLabel title = new JLabel("Enrolled Academic Courses — 7 Units Enrolled");
        title.setFont(new Font("Georgia", Font.BOLD, 20));
        title.setForeground(isDarkMode ? DARK_TEXT : BLUE_PRIMARY);

        titleBox.add(pageIcon);
        titleBox.add(title);
        panel.add(titleBox);
        panel.add(Box.createVerticalStrut(15));

        String[] columns = {"Course Code", "Course Title", "Credits", "Instructor", "Schedule / Room", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(new Object[][]{
            {"CS-323", "Advanced Data Structures & Algorithms", "4.0", "Prof. Dana Angluin", "Mon/Wed 10:30 AM (Lab 201)", "Enrolled"},
            {"CS-377", "Operating Systems & Kernels", "4.0", "Prof. Zhong Shao", "Tue/Thu 01:00 PM (Hall 17)", "Enrolled"},
            {"SE-401", "Software Engineering Architecture", "4.0", "Prof. Lin Zhong", "Mon/Wed 02:30 PM (AKW 200)", "Enrolled"},
            {"MATH-225", "Linear Algebra & Matrix Theory", "3.0", "Prof. Richard Beals", "Tue/Thu 09:00 AM (Linsly)", "Enrolled"},
            {"STAT-241", "Probability & Applied Statistics", "3.0", "Prof. Harrison Zhou", "Mon/Wed 09:00 AM (Hall 101)", "Enrolled"},
            {"AI-450", "Machine Learning & Neural Networks", "4.0", "Prof. Steven Zucker", "Tue/Thu 03:00 PM (AI Lab)", "Enrolled"},
            {"CYB-310", "Network Security & Cryptography", "3.0", "Prof. Joan Feigenbaum", "Mon/Wed 01:00 PM (Cyber Center)", "Enrolled"}
        }, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = createStyledTable(tableModel);

        JPanel actionToolBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionToolBar.setOpaque(false);
        actionToolBar.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton addCourseBtn = new JButton("+ Add / Register Course");
        addCourseBtn.setBackground(DARK_ORANGE);
        addCourseBtn.setForeground(Color.WHITE);
        addCourseBtn.setFont(new Font("Arial", Font.BOLD, 12));
        addCourseBtn.setFocusPainted(false);
        addCourseBtn.addActionListener(e -> {
            JTextField codeField = new JTextField(10);
            JTextField titleField = new JTextField(15);
            JTextField creditsField = new JTextField(5);
            JTextField instructorField = new JTextField(12);
            JTextField scheduleField = new JTextField(15);

            JPanel myPanel = new JPanel(new GridLayout(5, 2, 5, 5));
            myPanel.add(new JLabel("Course Code (e.g. CS-500):"));
            myPanel.add(codeField);
            myPanel.add(new JLabel("Course Title:"));
            myPanel.add(titleField);
            myPanel.add(new JLabel("Credits (e.g. 3.0 or 4.0):"));
            myPanel.add(creditsField);
            myPanel.add(new JLabel("Instructor Name:"));
            myPanel.add(instructorField);
            myPanel.add(new JLabel("Schedule & Room:"));
            myPanel.add(scheduleField);

            int result = JOptionPane.showConfirmDialog(parentFrame, myPanel, 
                "Register New Academic Unit", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String cCode = codeField.getText().trim();
                String cTitle = titleField.getText().trim();
                String cCred = creditsField.getText().trim();
                String cInst = instructorField.getText().trim();
                String cSched = scheduleField.getText().trim();

                if (!cCode.isEmpty() && !cTitle.isEmpty()) {
                    tableModel.addRow(new Object[]{cCode, cTitle, cCred.isEmpty() ? "3.0" : cCred, 
                        cInst.isEmpty() ? "TBA" : cInst, cSched.isEmpty() ? "TBA" : cSched, "Enrolled"});
                    JOptionPane.showMessageDialog(parentFrame, "Course " + cCode + " successfully registered!", "Enrolment Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "Course Code and Title cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton dropCourseBtn = new JButton("- Drop Selected");
        dropCourseBtn.setBackground(new Color(180, 60, 60));
        dropCourseBtn.setForeground(Color.WHITE);
        dropCourseBtn.setFont(new Font("Arial", Font.BOLD, 12));
        dropCourseBtn.setFocusPainted(false);
        dropCourseBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String courseName = (String) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(parentFrame, 
                    "Are you sure you want to drop unit " + courseName + "?", 
                    "Confirm Drop Unit", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(parentFrame, "Course " + courseName + " dropped.", "Enrolment Update", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Please click a row in the table to select a course to drop.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        actionToolBar.add(addCourseBtn);
        actionToolBar.add(dropCourseBtn);
        panel.add(actionToolBar);
        panel.add(Box.createVerticalStrut(15));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.getViewport().setBackground(isDarkMode ? DARK_CARD_BG : LIGHT_BG);
        
        panel.add(scrollPane);
        return panel;
    }

    // =========================================================================
    // PAGE 3: ACADEMIC GRADES
    // =========================================================================
    private static JPanel createGradesView(String dateStr) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));
        panel.setBackground(isDarkMode ? DARK_BG : LIGHT_BG);

        JPanel titleBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titleBox.setOpaque(false);
        titleBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel pageIcon = createStyledLabel("📊", HEADER_ICON_FONT, BLUE_PRIMARY);
        JLabel title = new JLabel("Official Academic Grades & Transcript Summary");
        title.setFont(new Font("Georgia", Font.BOLD, 20));
        title.setForeground(isDarkMode ? DARK_TEXT : BLUE_PRIMARY);

        titleBox.add(pageIcon);
        titleBox.add(title);
        panel.add(titleBox);
        panel.add(Box.createVerticalStrut(15));

        JPanel summaryBox = new JPanel(new GridLayout(1, 3, 15, 0));
        summaryBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        summaryBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        summaryBox.setOpaque(false);

        summaryBox.add(createMiniCard("Cumulative GPA", "3.88", BLUE_PRIMARY));
        summaryBox.add(createMiniCard("Major GPA", "3.92", new Color(40, 140, 70)));
        summaryBox.add(createMiniCard("Academic Standing", "Good Standing", new Color(40, 100, 180)));

        panel.add(summaryBox);
        panel.add(Box.createVerticalStrut(20));

        String[] columns = {"Semester", "Course Code", "Course Description", "Credits", "Grade", "Points"};
        Object[][] data = {
            {"Fall 2026", "CS-323", "Advanced Data Structures", "4.0", "A", "16.0"},
            {"Fall 2026", "CS-377", "Operating Systems & Kernels", "4.0", "A-", "14.8"},
            {"Fall 2026", "SE-401", "Software Engineering Architecture", "4.0", "A", "16.0"},
            {"Fall 2026", "MATH-225", "Linear Algebra & Matrix Theory", "3.0", "B+", "9.9"},
            {"Fall 2026", "STAT-241", "Probability & Statistics", "3.0", "A", "12.0"},
            {"Fall 2026", "AI-450", "Machine Learning Systems", "4.0", "A", "16.0"},
            {"Fall 2026", "CYB-310", "Network Security", "3.0", "A-", "11.1"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable table = createStyledTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.getViewport().setBackground(isDarkMode ? DARK_CARD_BG : LIGHT_BG);

        panel.add(scrollPane);
        return panel;
    }

    // =========================================================================
    // PAGE 4: STUDENT ACCOUNTS (IN KSH)
    // =========================================================================
    private static JPanel createAccountsView(String dateStr) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));
        panel.setBackground(isDarkMode ? DARK_BG : LIGHT_BG);

        JPanel titleBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titleBox.setOpaque(false);
        titleBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel pageIcon = createStyledLabel("💳", HEADER_ICON_FONT, BLUE_PRIMARY);
        JLabel title = new JLabel("Student Accounts & Financial Ledger Statement (KSH)");
        title.setFont(new Font("Georgia", Font.BOLD, 20));
        title.setForeground(isDarkMode ? DARK_TEXT : BLUE_PRIMARY);

        titleBox.add(pageIcon);
        titleBox.add(title);
        panel.add(titleBox);
        panel.add(Box.createVerticalStrut(15));

        JPanel feeBanner = new JPanel(new BorderLayout());
        feeBanner.setBackground(new Color(230, 248, 230));
        feeBanner.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(140, 200, 140), 1),
            new EmptyBorder(15, 20, 15, 20)
        ));
        feeBanner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        feeBanner.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel feeText = new JLabel("Current Account Balance: KSH 0.00 (Paid in Full)");
        feeText.setFont(new Font("Arial", Font.BOLD, 16));
        feeText.setForeground(new Color(30, 120, 30));

        JButton payBtn = new JButton("Make M-Pesa / Online Payment");
        payBtn.setBackground(DARK_ORANGE);
        payBtn.setForeground(Color.WHITE);
        payBtn.setFocusPainted(false);
        payBtn.setFont(new Font("Arial", Font.BOLD, 12));

        feeBanner.add(feeText, BorderLayout.WEST);
        feeBanner.add(payBtn, BorderLayout.EAST);

        panel.add(feeBanner);
        panel.add(Box.createVerticalStrut(20));

        String[] columns = {"Transaction Date", "Description", "Term", "Charges (KSH)", "Payments / Credits (KSH)", "Balance (KSH)"};
        Object[][] data = {
            {"15 Aug 2026", "Tuition Fee (25 Credits / 7 Units)", "Fall 2026", "KSH 220,750.00", "KSH 0.00", "KSH 220,750.00"},
            {"15 Aug 2026", "Campus Technology & IT Lab Fee", "Fall 2026", "KSH 12,500.00", "KSH 0.00", "KSH 233,250.00"},
            {"15 Aug 2026", "Library & Student Activity Fees", "Fall 2026", "KSH 6,300.00", "KSH 0.00", "KSH 239,550.00"},
            {"20 Aug 2026", "SOT Merit Scholarship Grant", "Fall 2026", "KSH 0.00", "-KSH 150,000.00", "KSH 89,550.00"},
            {"25 Aug 2026", "Constituency Bursary / Subsidies", "Fall 2026", "KSH 0.00", "-KSH 89,550.00", "KSH 0.00"},
            {"01 Sep 2026", "M-Pesa Express Payment Confirmation", "Fall 2026", "KSH 0.00", "KSH 0.00", "KSH 0.00"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable table = createStyledTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.getViewport().setBackground(isDarkMode ? DARK_CARD_BG : LIGHT_BG);

        panel.add(scrollPane);
        return panel;
    }

    // =========================================================================
    // PAGE 5: CLASS SCHEDULE (FRIDAY IS FREE DAY WITH OPTIONAL ACTIVITIES)
    // =========================================================================
    private static JPanel createScheduleView(String dateStr) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));
        panel.setBackground(isDarkMode ? DARK_BG : LIGHT_BG);

        JPanel titleBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titleBox.setOpaque(false);
        titleBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel pageIcon = createStyledLabel("📅", HEADER_ICON_FONT, BLUE_PRIMARY);
        JLabel title = new JLabel("Weekly Master Class Schedule — Friday is Free Day! (Optional Activities)");
        title.setFont(new Font("Georgia", Font.BOLD, 20));
        title.setForeground(isDarkMode ? DARK_TEXT : BLUE_PRIMARY);

        titleBox.add(pageIcon);
        titleBox.add(title);
        panel.add(titleBox);
        panel.add(Box.createVerticalStrut(20));

        String[] columns = {"Time Slot", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday (FREE DAY 🎉)"};
        Object[][] data = {
            {"09:00 AM - 10:15 AM", "STAT-241 (Hall 101)", "MATH-225 (Linsly)", "STAT-241 (Hall 101)", "MATH-225 (Linsly)", "🎨 Innovation & Hackathon Club (Optional)"},
            {"10:30 AM - 11:45 AM", "CS-323 (Lab 201)", "—", "CS-323 (Lab 201)", "—", "☕ Coffee Social & Tech Talk (Optional)"},
            {"01:00 PM - 02:15 PM", "CYB-310 (Cyber Center)", "CS-377 (Hall 17)", "CYB-310 (Cyber Center)", "CS-377 (Hall 17)", "⚽ E-Sports & Sports League (Optional)"},
            {"02:30 PM - 03:45 PM", "SE-401 (AKW 200)", "—", "SE-401 (AKW 200)", "—", "🎬 Creative Film Workshop (Optional)"},
            {"03:00 PM - 04:15 PM", "—", "AI-450 (AI Lab)", "—", "AI-450 (AI Lab)", "🌿 Campus Recreation & Chill"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable table = createStyledTable(model);
        table.setRowHeight(45);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.getViewport().setBackground(isDarkMode ? DARK_CARD_BG : LIGHT_BG);

        panel.add(scrollPane);
        return panel;
    }

    // =========================================================================
    // PAGE 6: ENHANCED UNIVERSITY INBOX (SUPER COOL INTERACTIVE EMAIL CLIENT)
    // =========================================================================
    private static JPanel createInboxView(String dateStr) {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));
        panel.setBackground(isDarkMode ? DARK_BG : LIGHT_BG);

        // Top Title & Compose Button Bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JPanel titleBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titleBox.setOpaque(false);
        JLabel pageIcon = createStyledLabel("✉️", HEADER_ICON_FONT, BLUE_PRIMARY);
        JLabel title = new JLabel("Official SOT Communications Inbox");
        title.setFont(new Font("Georgia", Font.BOLD, 20));
        title.setForeground(isDarkMode ? DARK_TEXT : BLUE_PRIMARY);
        titleBox.add(pageIcon);
        titleBox.add(title);

        JButton composeBtn = new JButton("✉️ + Compose Message");
        composeBtn.setBackground(DARK_ORANGE);
        composeBtn.setForeground(Color.WHITE);
        composeBtn.setFont(new Font("Arial", Font.BOLD, 12));
        composeBtn.setFocusPainted(false);

        topBar.add(titleBox, BorderLayout.WEST);
        topBar.add(composeBtn, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        // Table Model with Rich Email Data
        String[] columns = {"Status", "Sender", "Subject Line", "Received Date"};
        Object[][] data = {
            {"✉️ Unread", "Office of the Registrar", "Official Announcement: Friday Free Day & Campus Activity Schedule", dateStr},
            {"✉️ Unread", "Prof. Zhong Shao (CS377)", "CS-377 Kernel Assignment 2 Grading Matrix & Rubric Posted", dateStr},
            {"✅ Read", "SOT Finance Department", "Fall Semester Tuition Ledger Statement & M-Pesa Receipt", "15 Sep 2026"},
            {"✅ Read", "Computer Science Department", "Invitation: Annual Faculty-Student Research Symposium & Poster Session", "01 Sep 2026"},
            {"✅ Read", "Prof. Dana Angluin (CS323)", "CS-323 Midterm Examination Solutions & Extra Credit Opportunity", "28 Aug 2026"},
            {"✅ Read", "University Library Services", "Course Reserve Textbooks Available for AI-450 & CYB-310", "20 Aug 2026"}
        };

        String[] fullBodies = {
            "Dear Student,\n\nPlease note that Friday has been designated as an official Free Day at the School of Thoughts (SOT) starting this semester. Attendance for campus activities and club meets is completely optional.\n\nBest regards,\nOffice of the Registrar",
            "Dear Class,\n\nThe grading matrix for Kernel Assignment 2 has been uploaded to the portal. Please review the rubric before submission.\n\nRegards,\nProf. Zhong Shao",
            "Dear Ecobar,\n\nYour tuition fee balance for Fall 2026 has been successfully cleared via M-Pesa express payment. Your financial ledger statement shows a balance of KSH 0.00.\n\nSOT Finance Office",
            "You are cordially invited to attend the Annual SOT Faculty-Student Research Symposium at Dunham Laboratory Auditorium. Refreshments will be served.\n\nComputer Science Department",
            "Great job on the CS-323 midterm! Solution sets and extra credit problem statements are now available on the course website.\n\nProf. Dana Angluin",
            "Assigned textbooks and course reserve readings for AI-450 and CYB-310 are now accessible at the campus library service desk.\n\nUniversity Library Services"
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable table = createStyledTable(model);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.getViewport().setBackground(isDarkMode ? DARK_CARD_BG : LIGHT_BG);

        // Email Reading Preview Pane at the bottom
        JPanel previewPane = new JPanel(new BorderLayout());
        previewPane.setBackground(isDarkMode ? DARK_CARD_BG : new Color(248, 250, 252));
        previewPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? DARK_BORDER : LIGHT_BORDER, 1),
            new EmptyBorder(15, 20, 15, 20)
        ));
        previewPane.setPreferredSize(new Dimension(0, 160));

        JTextArea emailBodyArea = new JTextArea("Select an email from the table above to read its contents.");
        emailBodyArea.setFont(new Font("Arial", Font.PLAIN, 13));
        emailBodyArea.setEditable(false);
        emailBodyArea.setLineWrap(true);
        emailBodyArea.setWrapStyleWord(true);
        emailBodyArea.setOpaque(false);
        emailBodyArea.setForeground(isDarkMode ? DARK_TEXT : LIGHT_TEXT);

        previewPane.add(new JLabel("📥  Message Reading Pane"), BorderLayout.NORTH);
        previewPane.add(new JScrollPane(emailBodyArea), BorderLayout.CENTER);

        // When user clicks an email row, display its body and mark as Read
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    emailBodyArea.setText("From: " + table.getValueAt(row, 1) + "\nSubject: " + table.getValueAt(row, 2) + "\nDate: " + table.getValueAt(row, 3) + "\n\n" + fullBodies[row]);
                    table.setValueAt("✅ Read", row, 0);
                }
            }
        });

        // Compose Button Action: Send new message
        composeBtn.addActionListener(e -> {
            JTextField recipientField = new JTextField(20);
            JTextField subjectField = new JTextField(20);
            JTextArea bodyArea = new JTextArea(5, 20);
            bodyArea.setLineWrap(true);

            JPanel composePanel = new JPanel(new GridLayout(6, 1, 5, 5));
            composePanel.add(new JLabel("To (Department / Professor):"));
            composePanel.add(recipientField);
            composePanel.add(new JLabel("Subject Line:"));
            composePanel.add(subjectField);
            composePanel.add(new JLabel("Message Body:"));
            composePanel.add(new JScrollPane(bodyArea));

            int result = JOptionPane.showConfirmDialog(frame, composePanel, "Compose New Message", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String recipient = recipientField.getText().trim();
                String subject = subjectField.getText().trim();
                String body = bodyArea.getText().trim();

                if (!recipient.isEmpty() && !subject.isEmpty()) {
                    model.insertRow(0, new Object[]{"✅ Sent", recipient, subject, "Just Now"});
                    JOptionPane.showMessageDialog(frame, "Message sent successfully to " + recipient + "!", "Delivery Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Recipient and Subject cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScroll, previewPane);
        splitPane.setDividerLocation(260);
        splitPane.setOpaque(false);

        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
    }

    // Helper method to create styled tables
    private static JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(isDarkMode ? new Color(50, 50, 60) : new Color(240, 242, 248));
        table.getTableHeader().setForeground(isDarkMode ? DARK_TEXT : LIGHT_TEXT);
        table.setGridColor(isDarkMode ? DARK_BORDER : LIGHT_BORDER);
        table.setShowGrid(true);
        table.setBackground(isDarkMode ? DARK_CARD_BG : LIGHT_BG);
        table.setForeground(isDarkMode ? DARK_TEXT : LIGHT_TEXT);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setFont(SMALL_EMOJI_FONT);
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        };
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        return table;
    }

    private static JLabel createStyledLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    private static JPanel createMiniCard(String title, String val, Color valColor) {
        JPanel p = new JPanel(new GridLayout(2, 1, 0, 2));
        p.setBackground(isDarkMode ? DARK_CARD_BG : LIGHT_CARD_BG);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? DARK_BORDER : LIGHT_BORDER, 1),
            new EmptyBorder(10, 15, 10, 15)
        ));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Arial", Font.BOLD, 11));
        t.setForeground(isDarkMode ? DARK_MUTED : LIGHT_MUTED);

        JLabel v = new JLabel(val);
        v.setFont(new Font("Georgia", Font.BOLD, 18));
        v.setForeground(valColor);

        p.add(t);
        p.add(v);
        return p;
    }

    private static JPanel createStatCard(String iconStr, String title, String value, Color bgColor, Color textColor) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? DARK_BORDER : LIGHT_BORDER, 1),
            new EmptyBorder(16, 18, 16, 18)
        ));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);
        
        JLabel iconLbl = createStyledLabel(iconStr, EMOJI_FONT, textColor);
        
        JPanel textStack = new JPanel(new GridLayout(2, 1, 0, 2));
        textStack.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(textColor);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        textStack.add(titleLabel);
        leftPanel.add(iconLbl);
        leftPanel.add(textStack);

        JLabel valueLabel = new JLabel(value, SwingConstants.RIGHT);
        valueLabel.setForeground(isDarkMode ? DARK_TEXT : LIGHT_TEXT);
        valueLabel.setFont(new Font("Georgia", Font.BOLD, 16));

        card.add(leftPanel, BorderLayout.WEST);
        card.add(valueLabel, BorderLayout.EAST);
        return card;
    }

    private static void refreshTheme(String formattedDate) {
        buildSidebar(formattedDate);
        
        cardLayout = new CardLayout();
        dynamicCardContainer.removeAll();
        dynamicCardContainer.setLayout(cardLayout);

        dynamicCardContainer.add(createDashboardView(formattedDate, frame), "Dashboard");
        dynamicCardContainer.add(createCoursesView(formattedDate, frame), "CoursesEnrolment");
        dynamicCardContainer.add(createGradesView(formattedDate), "AcademicGrades");
        dynamicCardContainer.add(createAccountsView(formattedDate), "StudentAccounts");
        dynamicCardContainer.add(createScheduleView(formattedDate), "ClassSchedule");
        dynamicCardContainer.add(createInboxView(formattedDate), "UniversityInbox");

        applyCurrentTheme();
        frame.revalidate();
        frame.repaint();
    }

    private static void applyCurrentTheme() {
        Color bg = isDarkMode ? DARK_BG : LIGHT_BG;
        Color statusBg = isDarkMode ? new Color(38, 38, 45) : new Color(235, 238, 242);
        Color statusText = isDarkMode ? DARK_TEXT : new Color(60, 70, 80);

        statusBar.setBackground(statusBg);
        for (Component c : statusBar.getComponents()) {
            if (c instanceof JLabel) {
                c.setForeground(statusText);
            }
        }
        dynamicCardContainer.setBackground(bg);
    }
}