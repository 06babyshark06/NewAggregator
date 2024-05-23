package newsaggregator.controller.dashboard.trend.trendPackage.pairPackage;
import java.util.ArrayList;
import java.util.Collections;

import newsaggregator.controller.dashboard.trend.trendPackage.pairPackage.pairComparator.*;

public class PairArray extends ArrayList<Pair>{
	public void sortProperty() {
		Collections.sort(this, Collections.reverseOrder(new CompareProperty())); // Xếp theo giảm dần
	}

	public void sortValue() {
		Collections.sort(this, Collections.reverseOrder(new CompareValue()));
	}
	
	public void add(String first, double second){
		this.add(new Pair(first, second));
	}
	
	public String getProperty(int index) {
		return this.get(index).getProperty();
	}

	public double getValue(int index) {
		return this.get(index).getValue();
	}
	
	public void setProperty(int index, String s) {
		this.set(index, new Pair(s, this.getValue(index)));
	}
	
	public void setValue(int index, double d) {
		this.set(index, new Pair(this.getProperty(index), d));
	}
	
	public int indexOfProperty(String s) {
		for(int i = 0; i< this.size(); i++) {
			if(this.get(i).getProperty().equals(s)) {
				return i;
			}
		}
		return -1;
	}
	
	public int indexOfValue(double d) {
		for(int i = 0; i< this.size(); i++) {
			if(this.get(i).getValue() == d) {
				return i;
			}
		}
		return -1;
	
	}
	
	public PairArray subList(int begin, int end) {
		PairArray newList = new PairArray();
		for(int i = begin; i<end; i++) {
			newList.add(this.get(i));
		}
		return newList;
	}
	
	public void printPair() {
		for(Pair p : this) {
			System.out.println(p.getProperty() + " : " + p.getValue());
		}
	}

	public String propertyList() {
		StringBuilder propertyList = new StringBuilder();
		for(Pair pair : this){
			propertyList.append("#");
			propertyList.append(pair.getProperty());
			propertyList.append(", ");
		}
		return propertyList.toString();
	}



}
