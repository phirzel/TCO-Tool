package ch.softenvironment.view.swingext;
/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import ch.softenvironment.util.Tracer;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 * Utility to print JPanel contents.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class JPanelPrintable implements java.awt.print.Printable {

    private JPanel panel = null;

    /**
     * PrintWrapper constructor comment.
     *
     * <example>
     * java.awt.print.PrinterJob printJob = java.awt.print.PrinterJob.getPrinterJob(); printJob.setJobName(getTitle());
     * <p>
     * PageFormat pf = new PageFormat(); //printJob.pageDialog(printJob.defaultPage()); pf.setOrientation(java.awt.print.PageFormat.LANDSCAPE); printJob.setPrintable(new JPanelPrintable(graph), pf);
     * <p>
     * if (printJob.printDialog()) { try { printJob.print(); } catch(java.awt.print.PrinterException ex) { // handle exception } }
     * </example>
     */
    public JPanelPrintable(JPanel panel) {
        super();

        this.panel = panel;
    }

    /**
     * Prints the page at the specified index into the specified {@link Graphics} context in the specified format.  A <code>PrinterJob</code> calls the
     * <code>Printable</code> interface to request that a page be
     * rendered into the context specified by
     * <code>graphics</code>.  The format of the page to be drawn is
     * specified by <code>pageFormat</code>.  The zero based index of the requested page is specified by <code>pageIndex</code>. If the requested page does not exist then this method returns
     * NO_SUCH_PAGE; otherwise PAGE_EXISTS is returned. The <code>Graphics</code> class or subclass implements the {@link PrinterGraphics} interface to provide additional information.  If the
     * <code>Printable</code> object aborts the print job then it throws a {@link PrinterException}.
     *
     * @param graphics the context into which the page is drawn
     * @param pageFormat the size and orientation of the page being drawn
     * @param pageIndex the zero based index of the page to be drawn
     * @return PAGE_EXISTS if the page is rendered successfully or NO_SUCH_PAGE if <code>pageIndex</code> specifies a non-existent page.
     * @throws java.awt.print.PrinterException thrown when the print job is terminated.
     * @see Printable
     */
    @Override
    public int print(java.awt.Graphics graphics, java.awt.print.PageFormat pageFormat, int pageIndex) throws java.awt.print.PrinterException {
        // pageIndex 0 corresponds to page number 1
        if (pageIndex >= 1) {
            return java.awt.print.Printable.NO_SUCH_PAGE;
        }

        java.awt.print.PrinterGraphics gp = (java.awt.print.PrinterGraphics) graphics;
        Tracer.getInstance().logBackendCommand("<" + gp.getPrinterJob().getJobName() + "> is being printed");

        // set outer drawing limits
        double x0 = pageFormat.getImageableX() + 1.0;
        double y0 = pageFormat.getImageableY() + 1.0;
        double w0 = pageFormat.getImageableWidth() - 1.0;
        double h0 = pageFormat.getImageableHeight() - 1.0;

        Graphics2D g2 = (Graphics2D) graphics;
        //Rectangle2D printArea = new Rectangle2D.Double(x0, y0,
        //                          (int)pageFormat.getImageableWidth(),
        //                          (int)pageFormat.getImageableHeight());
        // g2.draw(printArea);  // paints black edge!!!
        g2.translate(x0, y0);   // move drawings to PrintArea-Offset(top/left)

        // fit to page
        Dimension dim = new Dimension(panel.getWidth(), panel.getHeight());
        double scaleX = 1.0;
        double scaleY = 1.0;
        double diag_w = dim.getWidth();
        double diag_h = dim.getHeight();
        if (diag_w > w0) {
            scaleX = w0 / diag_w;
        }
        if (diag_h > h0) {
            scaleY = h0 / diag_h;
        }

        if (scaleX < 1.0) {
            if (scaleX <= scaleY) {
                g2.scale(scaleX, scaleX);
            } else {
                // scaleY<scaleX<1.0
                g2.scale(scaleY, scaleY);
            }
        } else if (scaleY < 1.0) {
            g2.scale(scaleY, scaleY);
        }

        g2.clipRect(0, 0, (int) diag_w, (int) diag_h);

        panel.printAll(g2);
        //Rectangle2D printArea = new Rectangle2D.Double(0.0, 0.0,
        //      diag_w,
        //      diag_h);
        //g2.draw(printArea);
        //printArea = new Rectangle2D.Double(0.0, 0.0,
        //      diag_w+10.0,
        //      diag_h+10.0);
        //g2.draw(printArea);    

        return java.awt.print.Printable.PAGE_EXISTS;
    }
}
