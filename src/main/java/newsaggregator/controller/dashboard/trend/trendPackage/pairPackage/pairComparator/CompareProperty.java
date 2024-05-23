package newsaggregator.controller.dashboard.trend.trendPackage.pairPackage.pairComparator;

import java.util.Comparator;

import newsaggregator.controller.dashboard.trend.trendPackage.pairPackage.Pair;

public class CompareProperty implements Comparator<Pair> {

	@Override
	public int compare(Pair p1, Pair p2) {
		return p1.getProperty().compareTo(p2.getProperty()); //Thêm - để sắp 
	}
	
}
