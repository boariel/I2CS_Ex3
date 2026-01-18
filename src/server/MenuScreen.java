package server;

import java.awt.Color;
import java.awt.Font;

/**
 * Main menu screen for the Pac-Man game.
 * Provides options for Play, Controls, and Settings.
 */
public class MenuScreen {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 500;

    public enum MenuState {
        MAIN_MENU,
        CONTROLS,
        SETTINGS,
        PLAY
    }

    private MenuState currentState = MenuState.MAIN_MENU;

    // Button positions and sizes
    private Button playButton;
    private Button controlsButton;
    private Button settingsButton;
    private Button backButton;

    // Settings buttons
    private Button levelUpButton;
    private Button levelDownButton;
    private Button cyclicToggle;
    private Button ghostCountUp;
    private Button ghostCountDown;

    public MenuScreen() {
        initializeButtons();
    }

    private void initializeButtons() {
        // Main menu buttons - PLAY at top, CONTROLS middle, SETTINGS bottom
        playButton = new Button(WIDTH / 2, 250, 200, 50, "PLAY");
        controlsButton = new Button(WIDTH / 2, 180, 200, 50, "CONTROLS");
        settingsButton = new Button(WIDTH / 2, 110, 200, 50, "SETTINGS");

        // Back button (used in sub-menus) - at bottom
        backButton = new Button(WIDTH / 2, 50, 150, 40, "BACK");

        // Settings buttons
        levelDownButton = new Button(200, 280, 40, 40, "<");
        levelUpButton = new Button(400, 280, 40, 40, ">");

        cyclicToggle = new Button(WIDTH / 2, 200, 200, 40, "Toggle Cyclic");
    }

    /**
     * Show the menu and wait for user selection.
     * Returns the selected MenuState.
     */
    public MenuState show() {
        StdDraw.setCanvasSize(WIDTH, HEIGHT);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.enableDoubleBuffering();

        currentState = MenuState.MAIN_MENU;

        while (currentState != MenuState.PLAY) {
            StdDraw.clear(Color.BLACK);

            switch (currentState) {
                case MAIN_MENU:
                    drawMainMenu();
                    handleMainMenuInput();
                    break;
                case CONTROLS:
                    drawControlsScreen();
                    handleControlsInput();
                    break;
                case SETTINGS:
                    drawSettingsScreen();
                    handleSettingsInput();
                    break;
            }

            StdDraw.show();
            StdDraw.pause(20);
        }

        return MenuState.PLAY;
    }

    private void drawMainMenu() {
        // Title
        StdDraw.setPenColor(Color.YELLOW);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 60));
        StdDraw.text(WIDTH / 2, HEIGHT - 100, "PAC-MAN");

        // Subtitle
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 20));
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT - 140, "Custom Server Implementation - Press Space after 'play' to start");

        // Buttons
        playButton.draw(Color.GREEN);
        controlsButton.draw(Color.CYAN);
        settingsButton.draw(Color.ORANGE);

        // Footer
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 14));
        StdDraw.setPenColor(Color.GRAY);
        StdDraw.text(WIDTH / 2, 30, "Ex3 - Ariel University 2026");
    }

    private void drawControlsScreen() {
        // Title
        StdDraw.setPenColor(Color.CYAN);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 40));
        StdDraw.text(WIDTH / 2, HEIGHT - 50, "CONTROLS");

        // Controls list
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 18));

        int y = HEIGHT - 110;
        int spacing = 35;

        StdDraw.text(WIDTH / 2, y, "SPACE - Start/Pause Game"); y -= spacing;
        StdDraw.text(WIDTH / 2, y, "W - Move Up"); y -= spacing;
        StdDraw.text(WIDTH / 2, y, "A - Move Left"); y -= spacing;
        StdDraw.text(WIDTH / 2, y, "S - Move Down"); y -= spacing;
        StdDraw.text(WIDTH / 2, y, "D - Move Right"); y -= spacing;
        StdDraw.text(WIDTH / 2, y, "H - Help"); y -= spacing;

        StdDraw.setPenColor(Color.YELLOW);
        y -= 20;
        StdDraw.text(WIDTH / 2, y, "Goal: Eat all dots while avoiding ghosts!");
        y -= spacing;
        StdDraw.text(WIDTH / 2, y, "Green dots make ghosts eatable!");

        // Back button at bottom
        backButton.draw(Color.RED);
    }

    private void drawSettingsScreen() {
        // Title
        StdDraw.setPenColor(Color.ORANGE);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 40));
        StdDraw.text(WIDTH / 2, HEIGHT - 50, "SETTINGS");

        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 20));

        // Level setting
        StdDraw.text(WIDTH / 2, 310, "Level: " + ServerConfig.CASE_SCENARIO);
        levelDownButton.draw(Color.BLUE);
        levelUpButton.draw(Color.BLUE);

        // Cyclic mode
        String cyclicStatus = ServerConfig.CYCLIC_MODE ? "ON" : "OFF";
        StdDraw.text(WIDTH / 2, 230, "Cyclic Mode: " + cyclicStatus);
        cyclicToggle.draw(Color.MAGENTA);

        // Ghost info based on level
        int numGhosts = ServerConfig.CASE_SCENARIO + 1;
        String ghostInfo;
        if (ServerConfig.CASE_SCENARIO == 4) {
            ghostInfo = numGhosts + " ghosts (4 random + 1 smart)";
        } else {
            ghostInfo = numGhosts + " ghost" + (numGhosts > 1 ? "s" : "") + " (random)";
        }
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 16));
        StdDraw.setPenColor(Color.YELLOW);
        StdDraw.text(WIDTH / 2, 160, ghostInfo);

        // Info
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 14));
        StdDraw.setPenColor(Color.GRAY);
        StdDraw.text(WIDTH / 2, 130, "Same maze for all levels");
        StdDraw.text(WIDTH / 2, 110, "Each level adds one ghost");
        StdDraw.text(WIDTH / 2, 90, "Level 4 has one smart ghost");

        // Back button at bottom
        backButton.draw(Color.GREEN);
    }

    private void handleMainMenuInput() {
        if (StdDraw.isMousePressed()) {
            double mx = StdDraw.mouseX();
            double my = StdDraw.mouseY();

            if (playButton.isClicked(mx, my)) {
                currentState = MenuState.PLAY;
                waitForMouseRelease();
            } else if (controlsButton.isClicked(mx, my)) {
                currentState = MenuState.CONTROLS;
                waitForMouseRelease();
            } else if (settingsButton.isClicked(mx, my)) {
                currentState = MenuState.SETTINGS;
                waitForMouseRelease();
            }
        }
    }

    private void handleControlsInput() {
        if (StdDraw.isMousePressed()) {
            double mx = StdDraw.mouseX();
            double my = StdDraw.mouseY();

            if (backButton.isClicked(mx, my)) {
                currentState = MenuState.MAIN_MENU;
                waitForMouseRelease();
            }
        }
    }

    private void handleSettingsInput() {
        if (StdDraw.isMousePressed()) {
            double mx = StdDraw.mouseX();
            double my = StdDraw.mouseY();

            if (levelDownButton.isClicked(mx, my)) {
                if (ServerConfig.CASE_SCENARIO > 0) {
                    ServerConfig.CASE_SCENARIO--;
                }
                waitForMouseRelease();
            } else if (levelUpButton.isClicked(mx, my)) {
                if (ServerConfig.CASE_SCENARIO < 4) {
                    ServerConfig.CASE_SCENARIO++;
                }
                waitForMouseRelease();
            } else if (cyclicToggle.isClicked(mx, my)) {
                ServerConfig.CYCLIC_MODE = !ServerConfig.CYCLIC_MODE;
                waitForMouseRelease();
            } else if (backButton.isClicked(mx, my)) {
                currentState = MenuState.MAIN_MENU;
                waitForMouseRelease();
            }
        }
    }

    private void waitForMouseRelease() {
        while (StdDraw.isMousePressed()) {
            StdDraw.pause(10);
        }
        StdDraw.pause(100); // Debounce
    }

    /**
     * Simple button class for menu interactions.
     */
    private static class Button {
        private double x, y;
        private double width, height;
        private String label;

        public Button(double x, double y, double width, double height, String label) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.label = label;
        }

        public void draw(Color color) {
            // Draw button background
            StdDraw.setPenColor(color);
            StdDraw.filledRectangle(x, y, width / 2, height / 2);

            // Draw border
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.rectangle(x, y, width / 2, height / 2);

            // Draw label
            StdDraw.setFont(new Font("Arial", Font.BOLD, 18));
            StdDraw.text(x, y, label);
        }

        public boolean isClicked(double mx, double my) {
            return mx >= x - width / 2 && mx <= x + width / 2 &&
                    my >= y - height / 2 && my <= y + height / 2;
        }
    }
}