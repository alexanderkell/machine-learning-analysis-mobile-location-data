package svm.advance;

import org.jfree.chart.event.ChartProgressListener;

public interface CustomChartProgressListener extends ChartProgressListener, svm.libsvm.svm_print_interface{
	public abstract void onAbort();
	public abstract void finish();
	public abstract void progressUpdated(int percent);
}
