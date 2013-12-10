package permlab.utilities;

import java.awt.Color;
import java.awt.Graphics;
import permlib.Permutation;
import permlib.utilities.RestrictedPermutation;

/**
 * Provides paint methods for permutations and restricted permutations. Having this
 * in a separate class avoid code duplication when exporting to image formats.
 * 
 * @author M Belton
 */
public class PaintUtilities {

    public static final Color RULE_RESTRICTED = new Color(50, 50, 50, 150);
    public static final Color USER_RESTRICTED = new Color(120, 120, 120, 100);
    public static final int DEFAULT_POINT_SIZE = 20;

    public static void paint(Graphics g, RestrictedPermutation restPerm, int width, int height, boolean monotoneConstraints, boolean showGrid) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        if (restPerm != null) {
            Permutation p = restPerm.getPerm();
            int dx = width / (p.length() + 1);
            int dy = height / (p.length() + 1);
            int r = Math.min(Math.min(dx, dy) / 4, DEFAULT_POINT_SIZE);
            for (int i = 0; i <= p.length(); i++) {
                for (int j = 0; j <= p.length(); j++) {
                    if (restPerm.propertiesForbid(i, j)) {
                        shadeSquare(g, p, width, height, i, j, dx, dy, RULE_RESTRICTED);
                    } else if (restPerm.userForbids(i, j)) {
                        shadeSquare(g, p, width, height, i, j, dx, dy, USER_RESTRICTED);
                    }
                    if (monotoneConstraints) {
                        int constraint = restPerm.montoneRequirements(i, j);
                        switch (constraint) {
                            case RestrictedPermutation.SINGLETON:
                                addCircle(g, height, i, j, dx, dy, r);
                                break;
                            case RestrictedPermutation.INCREASING:
                                addIncreasing(g, height, i, j, dx, dy);
                                break;
                            case RestrictedPermutation.DECREASING:
                                addDecreasing(g, height, i, j, dx, dy);
                                break;
                            default:
                            // Do nothing
                            }
                    }
                }
            }
            for (int i = 0; i < p.length(); i++) {
                int x = (i + 1) * dx;
                int y = height - (p.elements[i] + 1) * dy;
                if (showGrid) {
                    g.setColor(Color.BLACK);
                    g.drawLine(x, 0, x, height);
                    g.drawLine(0, y, width, y);
                }
                g.setColor(restPerm.hasProperties() ? Color.BLACK : Color.RED);
                g.fillOval(x - r, y - r, 2 * r, 2 * r);
            }
        }
    }

    public static void paint(Graphics g, Permutation p, int width, int height, boolean showGrid) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        if (p != null) {
            int dx = width / (p.length() + 1);
            int dy = height / (p.length() + 1);
            int r = Math.min(Math.min(dx, dy) / 4, DEFAULT_POINT_SIZE);
            for (int i = 0; i < p.length(); i++) {
                int x = (i + 1) * dx;
                int y = height - (p.elements[i] + 1) * dy;
                if (showGrid) {
                    g.setColor(Color.BLACK);
                    g.drawLine(x, 0, x, height);
                    g.drawLine(0, y, width, y);
                }
                g.setColor(Color.BLACK);
                g.fillOval(x - r, y - r, 2 * r, 2 * r);
            }
        }
    }

    private static void shadeSquare(Graphics g, Permutation p, int width, int height, int i, int j, int dx, int dy, Color c) {
        g.setColor(c);
        int squareWidth = (i != p.length()) ? dx : width - i * dx;
        int top = (j != p.length()) ? height - (j + 1) * dy : 0;
        int squareHeight = (j != p.length()) ? dy : height - p.length() * dy;
        g.fillRect(i * dx, top, squareWidth, squareHeight);
    }

    private static void addCircle(Graphics g, int height, int i, int j, int dx, int dy, int r) {
        g.setColor(Color.BLACK);
        g.drawOval(i * dx + dx / 2 - r / 2, height - (j) * dy - dy / 2 - r / 2, r, r);
    }

    private static void addIncreasing(Graphics g, int height, int i, int j, int dx, int dy) {
        g.setColor(Color.BLACK);
        g.drawLine(i * dx + dx / 4, height - (j) * dy - dy / 4, i * dx + 3 * dx / 4, height - (j) * dy - 3 * dy / 4);
    }

    private static void addDecreasing(Graphics g, int height, int i, int j, int dx, int dy) {
        g.setColor(Color.BLACK);
        g.drawLine(i * dx + dx / 4, height - (j) * dy - 3 * dy / 4, i * dx + 3 * dx / 4, height - (j) * dy - dy / 4);
    }
}
