import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.Dimension;
import java.util.ArrayList;
import com.fazecast.jSerialComm.SerialPort;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ArtificialHorizon extends JFrame {

    static int cross = 160;
    static int bank = 0;
    static int pitch = 0;
    ArrayList<Integer> list = new ArrayList<>();
    Font font = new Font("SansSerif", Font.PLAIN, 12);
    static JPanel p;

    public ArtificialHorizon() {

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        p = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                Point borderTopLeft = new Point(5, 5);
                Point borderBottomRight = new Point(325, 325);
                Point blueTopLeft = new Point(5, 5);
                Point blueBottomRight = new Point(325, Math.min(cross, 325));
                Point orangeTopLeft = new Point(5, Math.max(cross, 5));
                Point orangeBottomRight = new Point(325, 325);
                Graphics2D g2 = (Graphics2D) g;
                Shape plane = new Line2D.Double(5, 160 + (bank * 3.554), 325, 160 + (bank * -3.554));
                Shape blue = GetRectangle(blueTopLeft, blueBottomRight);
                Shape orange = GetRectangle(orangeTopLeft, orangeBottomRight);
                Shape rect = GetRectangle(borderTopLeft, borderBottomRight);
                g2.setColor(Color.black);
                g2.draw(rect);
                g2.setColor(Color.blue);
                g2.fill(blue);
                g2.setColor(Color.orange);
                g2.fill(orange);
                g2.setColor(Color.white);
                int degrees = (cross - 5) / 10;
                int negativeDegrees = (325 - cross) / 10;
                int tenDegreeLines = degrees / 10;
                for (int i = 1; i <= tenDegreeLines; i++) {
                    if (cross - (i * 100) < 325) {
                        Shape tenDegreeLine = new Line2D.Double(65, cross - (i * 100), 265, cross - (i * 100));
                        g2.setColor(Color.white);
                        g2.draw(tenDegreeLine);
                        g2.setFont(font);
                        g2.drawString(Integer.toString(i * 10), 275, cross - (i * 100));
                        g2.drawString(Integer.toString(i * 10), 40, cross - (i * 100));
                    }
                }
                int fiveDegreeLines = (degrees / 5);
                for (int i = 1; i <= fiveDegreeLines; i++) {
                    if (cross - (i * 50) < 325) {
                        Shape fiveDegreeLine = new Line2D.Double(105, cross - (i * 50), 225, cross - (i * 50));
                        g2.setColor(Color.white);
                        g2.draw(fiveDegreeLine);
                    }
                }
                int twoPointFiveDegreeLines = (int) (degrees / 2.5);
                for (int i = 1; i <= twoPointFiveDegreeLines; i++) {
                    if (cross - (i * 25) < 325) {
                        Shape twoPointFiveDegreeLine = new Line2D.Double(145, cross - (i * 25), 185, cross - (i * 25));
                        g2.setColor(Color.white);
                        g2.draw(twoPointFiveDegreeLine);
                    }
                }

                int negativeTenDegreeLines = negativeDegrees / 10;
                for (int i = 1; i <= negativeTenDegreeLines; i++) {
                    if (cross + (i * 100) > 5) {
                        Shape tenDegreeLine = new Line2D.Double(65, cross + (i * 100), 265, cross + (i * 100));
                        g2.setColor(Color.white);
                        g2.draw(tenDegreeLine);
                        g2.setFont(font);
                        g2.drawString(Integer.toString(i * 10), 275, cross + (i * 100));
                        g2.drawString(Integer.toString(i * 10), 40, cross + (i * 100));
                    }
                }
                int negativeFiveDegreeLines = (negativeDegrees / 5);
                for (int i = 1; i <= negativeFiveDegreeLines; i++) {
                    if (cross + (i * 50) > 5) {
                        Shape fiveDegreeLine = new Line2D.Double(105, cross + (i * 50), 225, cross + (i * 50));
                        g2.setColor(Color.white);
                        g2.draw(fiveDegreeLine);
                    }
                }
                int negativeTwoPointFiveDegreeLines = (int) (negativeDegrees / 2.5);
                for (int i = 1; i <= negativeTwoPointFiveDegreeLines; i++) {
                    if (cross + (i * 25) > 5) {
                        Shape twoPointFiveDegreeLine = new Line2D.Double(145, cross + (i * 25), 185, cross + (i * 25));
                        g2.setColor(Color.white);
                        g2.draw(twoPointFiveDegreeLine);
                    }
                }
                g2.setColor(Color.black);
                g2.draw(plane);
            }

        };
        setTitle("Artificial Horizon");

        this.getContentPane().add(p);

    }

    public static Rectangle GetRectangle(Point topLeft, Point bottomRight) {
        Dimension size = new Dimension((bottomRight.x - topLeft.x), (bottomRight.y - topLeft.y));
        return new Rectangle(topLeft, size);
    }

    public static void main(String arg[]) throws IOException, InterruptedException {
        ArtificialHorizon horiz = new ArtificialHorizon();
        SerialPort sp = SerialPort.getCommPort("/dev/cu.usbserial-14210");
        sp.setComPortParameters(115200, 8, 1, 0);
        sp.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        if (sp.openPort()) {
            System.out.println("Port is open :)");
        } else {
            System.out.println("Failed to open port :(");
            return;
        }
        InputStream in = sp.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        while (true) {
            try {
                String line = reader.readLine();
                String[] pitchAndRoll = line.split("[,]", 0);
                pitch = Integer.parseInt(pitchAndRoll[0]);
                bank =  Integer.parseInt(pitchAndRoll[1]);
            } catch (Exception e) {
                System.out.println(e);
            }
            cross = (165 + (pitch * 10));
            p.repaint();
        }
    }

}
