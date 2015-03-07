package svm.advance;

import org.jfree.chart.event.ChartProgressListener;

public interface CustomChartProgressListener extends ChartProgressListener {
		public abstract void onAbort();
	
}
