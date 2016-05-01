package work;

public class Result {
	int numberOfFalseAlarms = 0;
	int numberOfMisses=0;
	int numberOfRealYes=0;
	int allNumber=0;
	double cost=0;
	private boolean costSet=false;
	
	public int getNumberOfFalseAlarms() {
		return numberOfFalseAlarms;
	}


	public int getNumberOfMisses() {
		return numberOfMisses;
	}


	public int getNumberOfRealYes() {
		return numberOfRealYes;
	}


	public int getAllNumber() {
		return allNumber;
	}


	public double getCost() {
		if(!costSet)
			throw new RuntimeException("Cost was not set.");
		return cost;
	}
	
	public double getFalseAlarmRate(){
		return (double)numberOfFalseAlarms/(allNumber-numberOfRealYes);
	}
	
	public double getMissRate(){
		return (double)numberOfMisses/(numberOfRealYes);
	}
	
	public double getPriorYesRate(){
		return (double)numberOfRealYes/allNumber;
	}

	public double getPriorNoRate(){
		return ((double)allNumber-numberOfRealYes)/allNumber;
	}
	
	public void setCost(double cost){
		this.cost=cost;
		this.costSet=true;
	}
	
	
	
	public Result(int numberOfMisses,int numberOfFalseAlarms,int numberOfRealYes,int totalNumber){
		this.numberOfFalseAlarms = numberOfFalseAlarms;
		this.numberOfMisses=numberOfMisses;
		this.numberOfRealYes=numberOfRealYes;
		this.allNumber = totalNumber;
		this.costSet=false;
	}
	
	
}
