package UserInterface.ModelsAndRenderers;

import java.awt.Color;
import java.awt.Paint;
import org.jfree.chart.renderer.category.BarRenderer;

/**
 *
 * @author Josie
 */
public class MyChartRenderer extends BarRenderer {

    public int selectedBar;

    public void setSelectedBar(int selectedBar) {
        this.selectedBar = selectedBar;
    }

    @Override
    public Paint getItemPaint(int row, int col) {
        if (col == this.selectedBar) {
            return new Color(170, 255, 128);
        } else {
            return super.getItemPaint(row, col);
        }
    }
}
