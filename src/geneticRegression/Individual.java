package geneticRegression;

public class Individual {
	Double gene[]=new Double[4];
	Double fitness;
	Double MSE;
	
	
	public Individual(Double a0,Double a1,Double a2,Double a3) {
		gene[0]=a0;//zaribe x0
		gene[1]=a1;//zaribe x1
		gene[2]=a2;//zaribe x2
		gene[3]=a3;//zaribe x3
	}
	
	

}
