import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * The Gui class exports a simple text based user interface
 * that is used to draw characters, lines and other elements on
 * the Screen. It simplifies the Lanterna library.
 *
 * <p>The Gui uses coordinates where 0, 0 is the upper left corner of the
 * screen. x coordinates specify the number of columns to the right, and
 * y coordinates specify the number of rows down.
 *
 * <p>Colors in the Gui are specified as strings. The valid color strings are:
 * WHITE, BLACK, RED, ORANGE, YELLOW, GREEN, BLUE, INDIGO, VIOLET
 */
public class Gui {
    private static DefaultTerminalFactory defaultTerminalFactory
        = new DefaultTerminalFactory();

    private Terminal terminal;
    private Screen screen;
    private TextGraphics graphics;
    private FileOutputStream logfs;

    /**
    * Creates a Gui that occupies the whole screen.
    */
    public Gui() {
        try {
            terminal = defaultTerminalFactory.createTerminal();
            screen = new TerminalScreen(terminal);
            graphics = screen.newTextGraphics();
            logfs = new FileOutputStream("LOG.txt");
        } catch (Exception e) {
            System.err.println("Could not create GUI");
            System.exit(-1);
        }
    }

    /**
     * Start terminal drawing mode
     */
    public void start() {
        try {
            terminal.setCursorVisible(true);
            screen.clear();
            screen.startScreen();
        } catch (Exception e) {
            // do nothing
        }
    }

    /**
     * Stop terminal drawing mode and restore normal terminal
     */
    public void stop() {
        try {
            terminal.setCursorVisible(true);
            screen.stopScreen();
        } catch (Exception e) {
            // do nothing
        }
    }

    /**
    * Clear the in memory screen. This not not shown untill you call refresh
    */
    public void clear() {
        try {
            screen.clear();
        } catch (Exception e) {
            System.err.println("Could not clear screen");
            System.exit(-1);
        }
    }

    /**
    * Refresh the screen so that all the screen clears, text and lines you
    * have drawn shows up on the screen
    */
    public void refresh() {
        try {
            screen.refresh();
        } catch (Exception e) {
            System.err.println("Could not refresh GUI");
            System.exit(-1);
        }
    }

    /**
    * The Gui takes over the whole screen. This returns the height in an integer
    * number of rows of the screen
    */
    public int getScreenHeight() {
        return screen.getTerminalSize().getRows();
    }

    /**
    * The Gui takes over the whole screen. This returns the height in an integer
    * number of columns of the screen
    */
    public int getScreenWidth() {
        return screen.getTerminalSize().getColumns();
    }

    /**
    * Set the cursor position
    */
    public void setCursorPosition(int x, int y) {
        try {
            screen.setCursorPosition(new TerminalPosition(x, y));
        } catch (Exception e) {
            assert (false);
        }
    }

    /**
    * Private member function that converts string colors to Lanterna TextColor
    * objects.
    */
    private static TextColor toTextColor(String color) {
        color = color.toUpperCase();
        if (color.equals("WHITE")) {
            return TextColor.ANSI.WHITE;
        } else if (color.equals("BLACK")) {
            return TextColor.ANSI.BLACK;
        } else if (color.equals("RED")) {
            return TextColor.ANSI.RED;
        } else if (color.equals("ORANGE")) {
            return new TextColor.RGB(0xff, 0xa5, 0x00);
        } else if (color.equals("YELLOW")) {
            return TextColor.ANSI.YELLOW;
        } else if (color.equals("GREEN")) {
            return TextColor.ANSI.GREEN;
        } else if (color.equals("BLUE")) {
            return TextColor.ANSI.BLUE;
        } else if (color.equals("INDIGO")) {
            return TextColor.ANSI.CYAN;
        } else if (color.equals("VIOLET")) {
            return TextColor.ANSI.MAGENTA;
        } else {
            return TextColor.ANSI.DEFAULT;
        }
    }

    /**
    * Draw a character at the given screen position x and y with the
    * given colors
    *
    * @param x The x coordinate to draw the character
    * @param y The y coordinates to draw the character
    * @param c The character to draw
    * @param foregroundColor The foreground color of the character
    * @param backgroundColor The background color of the character
    */
    public void drawCharacter(int x, int y, char c,
        String foregroundColor, String backgroundColor) {
        TextColor foregroundTextColor = toTextColor(foregroundColor);
        TextColor backgroundTextColor = toTextColor(backgroundColor);
        screen.setCharacter(y, x, new TextCharacter(c, foregroundTextColor,
          backgroundTextColor));
    }

    // /**
    // * Draw a line at the given screen position x and y with the
    // * given colors
    // *
    // * @param sx The x coordinate to start the line
    // * @param sy The y coordinates to start the line
    // * @param ex The x coordinates to end the line
    // * @param ey The y coordinates to end the line
    // * @param c The character to draw as the line
    // * @param foregroundColor The foreground color of the character
    // * @param backgroundColor The background color of the character
    // */
    // public void drawLine(int sx, int sy, int ex, int ey, char c,
    //   String foregroundColor, String backgroundColor) {
    //   TextColor foregroundTextColor = toTextColor(foregroundColor);
    //   TextColor backgroundTextColor = toTextColor(backgroundColor);
    //   graphics.drawLine(sx, sy, ex, ey, new TextCharacter(c,
    //     foregroundTextColor, backgroundTextColor));
    // }

    /**
    * Return the character that the user pressed. If the user didn't press
    * a key, it returns (char) 0x0;
    *
    * @return The character that the user pressed, or 0x0 if none
    */
    public int getKeypress() {
        try {
            KeyStroke ks = screen.pollInput();
            KeyType kt = ks.getKeyType();
            switch (kt) {
            case ArrowDown:
                return Gui.DOWN_ARROW_KEY;

            case ArrowUp:
                return Gui.UP_ARROW_KEY;

            case ArrowRight:
                return Gui.RIGHT_ARROW_KEY;

            case ArrowLeft:
                return Gui.LEFT_ARROW_KEY;

             case Escape:
                return Gui.ESCAPE_KEY;

             case Backspace:
                return Gui.BACKSPACE_KEY;

             case Enter:
                return Gui.ENTER_KEY;

            default:
                return ks.getCharacter();
            }
        } catch (Exception e) {
            return (char) 0x0;
        }
    }

    /**
    * Use these to check if the user pressed the arrow and other keys
        <code>
         Gui gui = new Gui();
         int c = gui.getKeypress();
         if (c == Gui.UP_ARROW_KEY) { ... }
    </code>
    */
    public static final int UP_ARROW_KEY = 8593;
    public static final int DOWN_ARROW_KEY = 8595;
    public static final int RIGHT_ARROW_KEY = 8594;
    public static final int LEFT_ARROW_KEY = 8592;
    public static final int ESCAPE_KEY = 27;
    public static final int BACKSPACE_KEY = 8;
    public static final int ENTER_KEY = 13;

    /**
    * Log a string to the log file "LOG.txt". This is useful because
    * printf debugging does not work in drawing mode.
    */
    public void log(String text) {
        text += "\n";
        try {
          logfs.write(text.getBytes());
        } catch (Exception e) {
          // do nothing
        }
    }

    /**
    * Sleep the calling thread for n milliseconds
    */
    public void sleep(int milli) {
      try {
          Thread.sleep(milli);
      } catch (Exception e) {
          // do nothing
      }
    }

    /**
    * True if the character is printable
    */
    public boolean isPrintable(int c) {
      return c >= 32 && c <= 127;
    }
}
