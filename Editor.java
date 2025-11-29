import java.io.FileInputStream;
import java.io.FileOutputStream;


public class Editor {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("No file argument");
            return;
        }

        Document document = new Document();
        FileInputStream fis = new FileInputStream(args[0]);
        document.read(fis);
        Gui gui = new Gui();

        // This try block is used to catch any exceptions inside
        try {
            View view = new View(document, gui);
            gui.start();

            for (boolean quit = false; !quit;) {
                gui.clear();
                view.draw();
                gui.refresh();

                int c = 0;
                while ((c = gui.getKeypress()) == 0) {
                    gui.sleep(10);
                }
                switch (c) {
                case Gui.ESCAPE_KEY:
                    quit = true;
                    break;

                default:
                    if (gui.isPrintable(c)) {
                        view.insert((char) c);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            // An exception occurred. Stop the GUI and print the message, then exit
            // with an error code. We need to stop the GUI first so that
            // the error can be printed out correctly.
            gui.stop();
            System.out.println("\n" + e);
            e.printStackTrace();
            System.exit(1);
        }

        // Clean exit -- stop the GUI
        gui.stop();
    }
}
