package permlab.utilities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintStream;
import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.SimpleDoc;
import javax.print.StreamPrintService;
import javax.print.StreamPrintServiceFactory;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import permlib.Permutation;
import permlab.ui.ExportDialog;
import permlab.ui.TextFrame;
import permlib.utilities.RestrictedPermutation;
import static permlab.utilities.ExportInterface.DEFAULT_HEIGHT;
import static permlab.utilities.ExportInterface.DEFAULT_WIDTH;
import static permlab.utilities.ExportInterface.fileChooser;

/**
 * An enumerated class that provides export implementations from StaticPermFrame
 * with various formats.
 *
 * @author M Belton
 */
public enum ExportType implements ExportInterface {

    GIF {
        private RestrictedPermutation restPerm = null;
        private boolean monotoneConstraints = false;
        private int width;
        private int height;
        private Permutation p;
        private boolean showGrid;

        @Override
        public void export(RestrictedPermutation restPerm) {
            this.restPerm = restPerm;
            monotoneConstraints = false;
            export(restPerm.getPerm());
        }

        @Override
        public void export(Permutation perm) {
            export(null, perm, DEFAULT_WIDTH, DEFAULT_HEIGHT, true);
        }

        @Override
        public void export(RestrictedPermutation restPerm, boolean monotoneConstraints, boolean showGrid) {
            this.restPerm = restPerm;
            this.monotoneConstraints = monotoneConstraints;
            export(restPerm.getPerm(), showGrid);

        }

        @Override
        public void export(Permutation perm, boolean showGrid) {
            export(null, perm, DEFAULT_WIDTH, DEFAULT_HEIGHT, showGrid);
        }

        @Override
        public void export(RestrictedPermutation restPerm, int width, int height, boolean monotoneConstraints, boolean showGrid) {
            this.restPerm = restPerm;
            this.monotoneConstraints = monotoneConstraints;
            export(restPerm.getPerm(), width, height, showGrid);
        }

        @Override
        public void export(Permutation p, int width, int height, boolean showGrid) {
            export(null, p, width, height, showGrid);
        }

        public void export(JFrame parent, RestrictedPermutation restPerm, int width, int height, boolean monotoneConstraints, boolean showGrid) {
            this.restPerm = restPerm;
            this.monotoneConstraints = monotoneConstraints;
            export(parent, restPerm.getPerm(), width, height, showGrid);
        }

        @Override
        public void export(JFrame parent, Permutation p, int width, int height, boolean showGrid) {
            this.p = p;
            this.width = width;
            this.height = height;
            this.showGrid = showGrid;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();

            if (restPerm != null) {
                PaintUtilities.paint(g, restPerm, width, height, monotoneConstraints, showGrid);
                resetRestPerm();
            } else {
                PaintUtilities.paint(g, p, width, height, showGrid);
            }

            File savingFile = new File(this.toString() + ".gif");
            fileChooser.setSelectedFile(savingFile);
            int returnVal = fileChooser.showSaveDialog(parent);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                savingFile = fileChooser.getSelectedFile();
                if (FileUtilities.canSaveFileHere(parent, savingFile)) {
                    try {
                        ImageIO.write(image, "gif", savingFile);
                    } catch (Exception excp) {
                    }
                }
            }
        }

        private void resetRestPerm() {
            restPerm = null;
            monotoneConstraints = false;
        }
    },
    PS {
        private RestrictedPermutation restPerm = null;
        private boolean monotoneConstraints = false;
        private int width;
        private int height;
        private Permutation p;
        private boolean showGrid;

        @Override
        public void export(RestrictedPermutation restPerm) {
            this.restPerm = restPerm;
            monotoneConstraints = false;
            export(restPerm.getPerm());
        }

        @Override
        public void export(Permutation perm) {
            export(null, perm, DEFAULT_WIDTH, DEFAULT_HEIGHT, true);
        }

        @Override
        public void export(RestrictedPermutation restPerm, boolean monotoneConstraints, boolean showGrid) {
            this.restPerm = restPerm;
            this.monotoneConstraints = monotoneConstraints;
            export(restPerm.getPerm(), showGrid);

        }

        @Override
        public void export(Permutation perm, boolean showGrid) {
            export(null, perm, DEFAULT_WIDTH, DEFAULT_HEIGHT, showGrid);
        }

        @Override
        public void export(RestrictedPermutation restPerm, int width, int height, boolean monotoneConstraints, boolean showGrid) {
            this.restPerm = restPerm;
            this.monotoneConstraints = monotoneConstraints;
            export(restPerm.getPerm(), width, height, showGrid);
        }

        @Override
        public void export(Permutation p, int width, int height, boolean showGrid) {
            export(null, p, width, height, showGrid);
        }

        public void export(JFrame parent, RestrictedPermutation restPerm, int width, int height, boolean monotoneConstraints, boolean showGrid) {
            this.restPerm = restPerm;
            this.monotoneConstraints = monotoneConstraints;
            export(parent, restPerm.getPerm(), width, height, showGrid);
        }

        @Override
        public void export(JFrame parent, Permutation p, int width, int height, boolean showGrid) {
            File savingFile = new File(this.toString() + ".ps");
            fileChooser.setSelectedFile(savingFile);
            int returnVal = fileChooser.showSaveDialog(parent);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                savingFile = fileChooser.getSelectedFile();
                if (FileUtilities.canSaveFileHere(parent, savingFile)) {
                    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = image.createGraphics();

                    if (restPerm != null) {
                        PaintUtilities.paint(g, restPerm, width, height, monotoneConstraints, showGrid);
                        resetRestPerm();
                    } else {
                        PaintUtilities.paint(g, p, width, height, showGrid);
                    }

                    File tempFile = new File(this.toString() + ".gif");
                    try {
                        ImageIO.write(image, "gif", tempFile);
                    } catch (Exception excp) {
                    }
                    String infile = this.toString() + ".gif";
                    DocFlavor flavor = DocFlavor.INPUT_STREAM.GIF;
                    String mimeType = DocFlavor.INPUT_STREAM.POSTSCRIPT.getMimeType();
                    StreamPrintServiceFactory[] factories = StreamPrintServiceFactory
                            .lookupStreamPrintServiceFactories(flavor, mimeType);
                    try {
                        PrintStream fos = new PrintStream(savingFile);
                        StreamPrintService sps = factories[0].getPrintService(fos);
                        FileInputStream fis = new FileInputStream(infile);
                        DocPrintJob dpj = sps.createPrintJob();
                        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
                        Doc doc = new SimpleDoc(fis, flavor, null);
                        dpj.print(doc, pras);
                        fos.close();
                    } catch (FileNotFoundException e1) {
                    } catch (PrintException e2) {
                    }
                    tempFile.delete();
                }
            }
        }

        private void resetRestPerm() {
            restPerm = null;
            monotoneConstraints = false;
        }
    },
    PSTricks {
        private Permutation p;
        private TextFrame exportFrame;
        private static final int DX = 10;
        private static final int DY = 10;
        private static final int WIDTH = 200;
        private RestrictedPermutation restPerm = null;
        private boolean monotoneConstraints = false;

        @Override
        public void export(RestrictedPermutation restPerm) {
            this.restPerm = restPerm;
            monotoneConstraints = false;
            export(restPerm.getPerm());
        }

        @Override
        public void export(Permutation perm) {
            export(null, perm, DEFAULT_WIDTH, DEFAULT_HEIGHT, true);
        }

        @Override
        public void export(RestrictedPermutation restPerm, boolean monotoneConstraints, boolean showGrid) {
            this.restPerm = restPerm;
            this.monotoneConstraints = monotoneConstraints;
            export(restPerm.getPerm(), showGrid);

        }

        @Override
        public void export(Permutation perm, boolean showGrid) {
            export(null, perm, DEFAULT_WIDTH, DEFAULT_HEIGHT, showGrid);
        }

        @Override
        public void export(RestrictedPermutation restPerm, int width, int height, boolean monotoneConstraints, boolean showGrid) {
            this.restPerm = restPerm;
            this.monotoneConstraints = monotoneConstraints;
            export(restPerm.getPerm(), width, height, showGrid);
        }

        @Override
        public void export(Permutation p, int width, int height, boolean showGrid) {
            export(null, p, width, height, showGrid);
        }

        public void export(JFrame parent, RestrictedPermutation restPerm, int width, int height, boolean monotoneConstraints, boolean showGrid) {
            this.restPerm = restPerm;
            this.monotoneConstraints = monotoneConstraints;
            export(parent, restPerm.getPerm(), width, height, showGrid);
        }

        @Override
        public void export(JFrame parent, Permutation p, int width, int height, boolean showGrid) {
            this.p = p;
            StringBuilder result = new StringBuilder();
            result.append("%ToPStricks output\n");
            result.append(PSTricksPreamble());
            if (restPerm != null) {
                result.append("%Forbidden regions\n");
                for (int i = 0; i <= p.length(); i++) {
                    for (int j = 0; j <= p.length(); j++) {
                        if (restPerm.propertiesForbid(i, j)) {
                            result.append(PSTricksDarkGrayRectangle(i, j));
                        } else if (restPerm.userForbids(i, j)) {
                            result.append(PSTricksLightGrayRectangle(i, j));
                        }
                        if (monotoneConstraints) {
                            int constraint = restPerm.montoneRequirements(i, j);
                            switch (constraint) {
                                case RestrictedPermutation.SINGLETON:
                                    result.append(PSTricksEmptyCircle(i, j));
                                    break;
                                case RestrictedPermutation.INCREASING:
                                    result.append(PSTricksIncreasingLine(i, j));
                                    break;
                                case RestrictedPermutation.DECREASING:
                                    result.append(PSTricksDecreasingLine(i, j));
                                    break;
                                default:
                                // Do nothing
                            }
                        }
                    }
                }
            }
            if (showGrid) {
                result.append("%Gridlines\n");
                for (int i = 0; i <= p.length() + 1; i++) {
                    result.append(PSTricksGridLines(i));
                }
            }
            boolean allGood = restPerm == null || (restPerm.hasProperties() /*&& (!restPerm.simple || PermUtilities.isSimple(p))*/);
            result.append("%Points\n");
            for (int i = 0; i < p.length(); i++) {
                result.append(PSTricksCircle(i, allGood));
            }
            result.append("\\end{pspicture}\n\n");
            resetRestPerm();

            if (!ExportDialog.saveAsFile) {
                if (exportFrame == null) {
                    exportFrame = new TextFrame(result.toString());
                    exportFrame.setLocation(parent.getX() + parent.getWidth() + 10, parent.getY());
                } else {
                    exportFrame.addText(result.toString());
                }
                exportFrame.setVisible(true);
            } else {
                File savingFile = new File(this.toString() + ".tex");
                fileChooser.setSelectedFile(savingFile);
                int returnVal = fileChooser.showSaveDialog(parent);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    savingFile = fileChooser.getSelectedFile();
                    if (FileUtilities.canSaveFileHere(parent, savingFile)) {
                        try {
                            FileWriter fileWriter = new FileWriter(savingFile);
                            fileWriter.write(result.toString());
                            fileWriter.close();
                        } catch (Exception excp) {
                        }
                    }
                }
            }
        }

        private void resetRestPerm() {
            restPerm = null;
            monotoneConstraints = false;
        }

        private String PSTricksPreamble() {
            int unit = WIDTH / ((p.length() + 1) * DX);
            return "\\psset{" + "xunit=" + unit + "pt, " + "yunit=" + unit + "pt, " + "runit=" + unit + "pt" + "}\n" + "\\begin{pspicture}(0,0)" + "(" + ((p.length() + 1) * DX) + "," + ((p.length() + 1) * DY) + ")\n";
        }

        private String PSTricksDarkGrayRectangle(int i, int j) {
            return "\\pspolygon*[linecolor=darkgray]" + PSTricksRectangle(i, j);
        }

        private String PSTricksLightGrayRectangle(int i, int j) {
            return "\\pspolygon*[linecolor=lightgray]" + PSTricksRectangle(i, j);
        }

        private String PSTricksRectangle(int i, int j) {
            int x = i * DX;
            int y = j * DY;
            return "(" + x + "," + y + ")" + "(" + (x + DX) + "," + y + ")" + "(" + (x + DX) + "," + (y + DY) + ")" + "(" + x + "," + (y + DY) + ")" + "\n";
        }

        private String PSTricksCircle(int i, boolean allGood) {
            int x = (i + 1) * DX;
            int y = (p.elements[i] + 1) * DY;
            String color = allGood ? "black" : "red";
            return "\\pscircle*[linecolor=" + color + "]" + "(" + x + "," + y + ")" + "{" + (DX / 5.0) + "}" + "\n";
        }

        private String PSTricksEmptyCircle(int i, int j) {
            double r = (DX / 5.0);
            double x = ((i * DX) + (DX / 2));
            double y = (j + 1) * DY - DY / 2;
            return "\\pscircle[linecolor=black]" + "(" + x + "," + y + ")" + "{" + r + "}" + "\n";
        }

        private String PSTricksGridLines(int i) {
            return "\\psline" + "(" + (i * DX) + "," + 0 + ")" + "(" + (i * DX) + "," + (p.length() + 1) * DY + ")" + "\n" + "\\psline" + "(" + (0) + "," + (i * DY) + ")" + "(" + ((p.length() + 1) * DX) + "," + (i * DY) + ")" + "\n";
        }

        private String PSTricksDecreasingLine(int i, int j) {
            double x1 = (i * DX + DX / 4.0);
            double y1 = (j + 1) * DY - DY / 4.0;
            double x2 = (i * DX + 3 * DX / 4.0);
            double y2 = (j + 1) * DY - 3 * DY / 4.0;
            return "\\psline" + "(" + x1 + "," + y1 + ")" + "(" + x2 + "," + y2 + ")" + "\n";
        }

        private String PSTricksIncreasingLine(int i, int j) {
            double x1 = (i * DX + DX / 4);
            double y1 = (j + 1) * DY - 3 * DY / 4;
            double x2 = (i * DX + 3 * DX / 4);
            double y2 = (j + 1) * DY - DY / 4;
            return "\\psline" + "(" + x1 + "," + y1 + ")" + "(" + x2 + "," + y2 + ")" + "\n";
        }
    },
    SVG {
        private PrintStream out;
        private int width;
        private int height;
        private int DX;
        private int DY;
        private Permutation p;
        private RestrictedPermutation restPerm = null;
        private boolean monotoneConstraints = false;

        @Override
        public void export(RestrictedPermutation restPerm) {
            this.restPerm = restPerm;
            monotoneConstraints = false;
            export(restPerm.getPerm());
        }

        @Override
        public void export(Permutation perm) {
            export(null, perm, DEFAULT_WIDTH, DEFAULT_HEIGHT, true);
        }

        @Override
        public void export(RestrictedPermutation restPerm, boolean monotoneConstraints, boolean showGrid) {
            this.restPerm = restPerm;
            this.monotoneConstraints = monotoneConstraints;
            export(restPerm.getPerm(), showGrid);

        }

        @Override
        public void export(Permutation perm, boolean showGrid) {
            export(null, perm, DEFAULT_WIDTH, DEFAULT_HEIGHT, showGrid);
        }

        @Override
        public void export(RestrictedPermutation restPerm, int width, int height, boolean monotoneConstraints, boolean showGrid) {
            this.restPerm = restPerm;
            this.monotoneConstraints = monotoneConstraints;
            export(restPerm.getPerm(), width, height, showGrid);
        }

        @Override
        public void export(Permutation p, int width, int height, boolean showGrid) {
            export(null, p, width, height, showGrid);
        }

        public void export(JFrame parent, RestrictedPermutation restPerm, int width, int height, boolean monotoneConstraints, boolean showGrid) {
            this.restPerm = restPerm;
            this.monotoneConstraints = monotoneConstraints;
            export(parent, restPerm.getPerm(), width, height, showGrid);
        }

        @Override
        public void export(JFrame parent, Permutation p, int width, int height, boolean showGrid) {
            File savingFile = new File(this.toString() + ".svg");
            fileChooser.setSelectedFile(savingFile);
            int returnVal = fileChooser.showSaveDialog(parent);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                savingFile = fileChooser.getSelectedFile();
                if (FileUtilities.canSaveFileHere(parent, savingFile)) {
                    try {
                        this.out = new PrintStream(savingFile);
                    } catch (Exception excp) {
                    }
                }
            }

            this.p = p;
            this.width = width;
            this.height = height;
            this.DX = width / (p.length() + 1);
            this.DY = height / (p.length() + 1);

            addPreamble();

                int r = Math.min(Math.min(DX, DY) / 4, PaintUtilities.DEFAULT_POINT_SIZE);
                for (int i = 0; i <= p.length(); i++) {
                    for (int j = 0; j <= p.length(); j++) {
                        if (restPerm != null) {
                            if (restPerm.propertiesForbid(i, j)) {
                                addGreyRect(i, j);
                            } else if (restPerm.userForbids(i, j)) {
                                addLightGreyRect(i, j);
                            } else {
                                addWhiteRect(i, j);
                            }
                        } else if (showGrid) {
                            addWhiteRect(i, j);
                        }
                        if (monotoneConstraints) {
                            int constraint = restPerm.montoneRequirements(i, j);
                            switch (constraint) {
                                case RestrictedPermutation.SINGLETON:
                                    addCircle(i, j, r);
                                    break;
                                case RestrictedPermutation.INCREASING:
                                    addIncreasing(i, j);
                                    break;
                                case RestrictedPermutation.DECREASING:
                                    addDecreasing(i, j);
                                    break;
                                default:
                                // Do nothing
                            }
                        }
                    }
                }
            boolean allGood = restPerm == null || (restPerm.hasProperties()/* && (!restPerm.simple || PermUtilities.isSimple(p))*/);
            for (int i = 0; i < p.length(); i++) {
                if (allGood) {
                    addBlackCircle(i);
                } else {
                    addRedCircle(i);
                }
            }
            addPostamble();
            resetRestPerm();
        }

        private void resetRestPerm() {
            restPerm = null;
            monotoneConstraints = false;
        }

        private void addPreamble() {
            out.println("<svg width=\""
                    + width
                    + "\" height=\""
                    + height
                    + "\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">");
            out.println();
        }

        private void addGreyRect(int x, int y) {
            out.println(
                    "<rect "
                    + "x=\"" + (x * DX) + "\" "
                    + "y=\"" + ((p.length() - y) * DY) + "\" "
                    + "width=\"" + DX + "\" "
                    + "height=\"" + DY + "\" "
                    + "fill=\"grey\" "
                    + "stroke=\"black\" " + "/>");
        }

        private void addLightGreyRect(int x, int y) {
            out.println(
                    "<rect "
                    + "x=\"" + (x * DX) + "\" "
                    + "y=\"" + ((p.length() - y) * DY) + "\" "
                    + "width=\"" + DX + "\" "
                    + "height=\"" + DY + "\" "
                    + "fill=\"lightgrey\" "
                    + "stroke=\"black\" " + "/>");
        }

        private void addWhiteRect(int x, int y) {
            out.println(
                    "<rect "
                    + "x=\"" + (x * DX) + "\" "
                    + "y=\"" + ((p.length() - y) * DY) + "\" "
                    + "width=\"" + DX + "\" "
                    + "height=\"" + DY + "\" "
                    + "fill=\"white\" "
                    + "stroke=\"black\" " + "/>");
        }

        private void addBlackCircle(int i) {
            int x = (i + 1) * DX;
            int y = (p.length() - p.elements[i]) * DY;
            out.println(
                    "<circle "
                    + "cx=\"" + x + "\" "
                    + "cy=\"" + y + "\" "
                    + "r=\"" + (DX / 5.0) + "\" "
                    + "fill=\"black\" />");
        }

        private void addRedCircle(int i) {
            int x = (p.length() - i) * DX;
            int y = (p.elements[i] + 1) * DY;
            out.println(
                    "<circle "
                    + "cx=\"" + x + "\" "
                    + "cy=\"" + y + "\" "
                    + "r=\"" + (DX / 5.0) + "\" "
                    + "fill=\"red\" />");
        }

        private void addCircle(int i, int j, int r) {
            out.println(
                    "<circle "
                    + "cx=\"" + ((i * DX) + (DX / 2)) + "\" "
                    + "cy=\"" + (height - (j) * DY - DY / 2) + "\" "
                    + "r=\"" + r + "\" "
                    + "stroke=\"black\" "
                    + "fill=\"none\" />");
        }

        private void addIncreasing(int i, int j) {
            out.println(
                    "<line "
                    + "x1=\"" + (i * DX + DX / 4) + "\" "
                    + "y1=\"" + (height - (j) * DY - DY / 4) + "\" "
                    + "x2=\"" + (i * DX + 3 * DX / 4) + "\" "
                    + "y2=\"" + (height - (j) * DY - 3 * DY / 4) + "\" "
                    + "stroke=\"black\" " + "/>");
        }

        private void addDecreasing(int i, int j) {
            out.println(
                    "<line "
                    + "x1=\"" + (i * DX + DX / 4) + "\" "
                    + "y1=\"" + (height - (j) * DY - 3 * DY / 4) + "\" "
                    + "x2=\"" + (i * DX + 3 * DX / 4) + "\" "
                    + "y2=\"" + (height - (j) * DY - DY / 4) + "\" "
                    + "stroke=\"black\" " + "/>");
        }

        private void addPostamble() {
            out.print("</svg>");
        }
    },
    TikZ {
        private TextFrame exportFrame;
        private javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        private Permutation p;
        private static final int TDX = 1;
        private static final int TDY = TDX;
        private RestrictedPermutation restPerm = null;
        private boolean monotoneConstraints = false;

        @Override
        public void export(RestrictedPermutation restPerm) {
            this.restPerm = restPerm;
            monotoneConstraints = false;
            export(restPerm.getPerm());
        }

        @Override
        public void export(Permutation perm) {
            export(null, perm, DEFAULT_WIDTH, DEFAULT_HEIGHT, true);
        }

        @Override
        public void export(RestrictedPermutation restPerm, boolean monotoneConstraints, boolean showGrid) {
            this.restPerm = restPerm;
            this.monotoneConstraints = monotoneConstraints;
            export(restPerm.getPerm(), showGrid);

        }

        @Override
        public void export(Permutation perm, boolean showGrid) {
            export(null, perm, DEFAULT_WIDTH, DEFAULT_HEIGHT, showGrid);
        }

        @Override
        public void export(RestrictedPermutation restPerm, int width, int height, boolean monotoneConstraints, boolean showGrid) {
            this.restPerm = restPerm;
            this.monotoneConstraints = monotoneConstraints;
            export(restPerm.getPerm(), width, height, showGrid);
        }

        @Override
        public void export(Permutation p, int width, int height, boolean showGrid) {
            export(null, p, width, height, showGrid);
        }

        public void export(JFrame parent, RestrictedPermutation restPerm, int width, int height, boolean monotoneConstraints, boolean showGrid) {
            this.restPerm = restPerm;
            this.monotoneConstraints = monotoneConstraints;
            export(parent, restPerm.getPerm(), width, height, showGrid);
        }

        @Override
        public void export(JFrame parent, Permutation p, int width, int height, boolean showGrid) {
            StringBuilder result = new StringBuilder();
            result.append("%Tikz output\n");
            result.append(TikzPreamble());
            result.append("%Forbidden regions\n");
            this.p = p;
            if (restPerm != null) {
                for (int i = 0; i <= p.length(); i++) {
                    for (int j = 0; j <= p.length(); j++) {
                        if (restPerm.propertiesForbid(i, j)) {
                            result.append(TikzDarkGrayRectangle(i, j));
                        } else if (restPerm.userForbids(i, j)) {
                            result.append(TikzLightGrayRectangle(i, j));
                        }
                        if (monotoneConstraints) {
                            int constraint = restPerm.montoneRequirements(i, j);
                            switch (constraint) {
                                case RestrictedPermutation.SINGLETON:
                                    result.append(TikzEmptyCircle(i, j));
                                    break;
                                case RestrictedPermutation.INCREASING:
                                    result.append(TikzIncreasingLine(i, j));
                                    break;
                                case RestrictedPermutation.DECREASING:
                                    result.append(TikzDecreasingLine(i, j));
                                    break;
                                default:
                                // Do nothing
                                }
                        }
                    }
                }
            }
            
            if (showGrid) {
                result.append("%Gridlines\n");
                for (int i = 0; i <= p.length() + 1; i++) {
                    result.append(TikzGridLines(i));
                }
            }
            
            boolean allGood = restPerm == null || (restPerm.hasProperties() /*&& (!restPerm.simple || PermUtilities.isSimple(p))*/);
            result.append("%Points\n");
            for (int i = 0; i < p.length(); i++) {
                result.append(TikzCircle(i, allGood));
            }
            
            result.append("\\end{tikzpicture}\n\n");
            resetRestPerm();

            if (!ExportDialog.saveAsFile) {
                if (exportFrame == null) {
                    exportFrame = new TextFrame(result.toString());
                    exportFrame.setLocation(parent.getX() + parent.getWidth() + 10, parent.getY());
                } else {
                    exportFrame.addText(result.toString());
                }
                exportFrame.setVisible(true);
            } else {
                File savingFile = new File(this.toString() + ".tex");
                fileChooser.setSelectedFile(savingFile);
                int returnVal = fileChooser.showSaveDialog(parent);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    savingFile = fileChooser.getSelectedFile();
                    if (FileUtilities.canSaveFileHere(parent, savingFile)) {
                        try {
                            FileWriter fileWriter = new FileWriter(savingFile);
                            fileWriter.write(result.toString());
                            fileWriter.close();
                        } catch (Exception excp) {
                        }
                    }
                }
            }
        }

        private void resetRestPerm() {
            restPerm = null;
            monotoneConstraints = false;
        }

        private String TikzPreamble() {
            return "\\definecolor{light-gray}{gray}{0.5}\n\\definecolor{dark-gray}{gray}{0.35}\n\\begin{tikzpicture}[scale=1]\n";
        }

        private String TikzBothRectangle(int i, int j) {
            return "\\filldraw[light-gray, pattern=north west lines, pattern color=dark-gray]" + TikzRectangle(i, j);
        }

        private String TikzDarkGrayRectangle(int i, int j) {
            return "\\filldraw[dark-gray]" + TikzRectangle(i, j);
        }

        private String TikzLightGrayRectangle(int i, int j) {
            return "\\filldraw[light-gray]" + TikzRectangle(i, j);
        }

        private String TikzRectangle(int i, int j) {
            int x = i * TDX;
            int y = j * TDY;
            return "(" + x + "," + y + ") rectangle (" + (x + TDX) + "," + (y + TDY) + ");\n";
        }

        private String TikzCircle(int i, boolean allGood) {
            int x = (i + 1) * TDX;
            int y = (p.elements[i] + 1) * TDY;
            String color = allGood ? "black" : "red";
            return "\\draw[" + color + ", fill=" + color + "] (" + x + "," + y + ") circle (" + (TDX / 5.0) + ");\n";
        }

        private String TikzEmptyCircle(int i, int j) {
           double r = (TDX / 5.0);
            double x = ((i * TDX) + (TDX / 2.0));
            double y = (j * TDY) + (TDY/2.0);
            return "\\draw[black, fill=none] (" + x + "," + y + ") circle (" + r + ");\n";
        }

        private String TikzGridLines(int i) {
            int s = 0;
            int e = p.length() + 1;
            return "\\draw[thick]" + "(" + (i * TDX) + "," + s + ")--" + "(" + (i * TDX) + "," + e * TDY + ");" + "\n" + "\\draw[thick]" + "(" + (s) + "," + (i * TDY) + ")--" + "(" + ((e) * TDX) + "," + (i * TDY) + ");" + "\n";
        }

        private String TikzDecreasingLine(int i, int j) {
            double x1 = (i * TDX + TDX / 4.0);
            double y1 = (j + 1) * TDY - TDY / 4.0;
            double x2 = (i * TDX + 3 * TDX / 4.0);
            double y2 = (j + 1) * TDY - 3 * TDY / 4.0;
            return "\\draw[thick]" + "(" + x1 + "," + y1 + ")--" + "(" + x2 + "," + y2 + ");" + "\n";
        }

        private String TikzIncreasingLine(int i, int j) {
            double x1 = (i * TDX + TDX / 4.0);
            double y1 = (j + 1) * TDY - 3 * TDY / 4.0;
            double x2 = (i * TDX + 3 * TDX / 4.0);
            double y2 = (j + 1) * TDY - TDY / 4.0;
            return "\\draw[thick]" + "(" + x1 + "," + y1 + ")--" + "(" + x2 + "," + y2 + ");" + "\n";
        }
    },;
}