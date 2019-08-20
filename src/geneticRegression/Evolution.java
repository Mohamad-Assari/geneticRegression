package geneticRegression;

import java.io.*;
import java.util.Random;


public class Evolution {
	Double Ys[]=new Double[100];
	int populationSize=5000;
	int geneRange=100; //gene can be between -geneRange to + gene Range
	Individual population[]=new Individual[populationSize];
	Individual parents[]=new Individual[2*populationSize];
	int tornumentSize=10;
	Individual children[]=new Individual[populationSize];
	Double mutationRate=0.01;
	Double variance=1.0;
	int numberOfGenerations=10;

	
	
	
	public Evolution() {
		
		getCSV();//Ys[] is now ready
		phaseOne();//creating first population
		
//		System.out.println("best           average             worst  ");

		for(int i=0;i<numberOfGenerations;i++) {
			phaseTwo();//calculating fitness function
			phaseThree();//choosing parents
			phaseFour();//producing children
			phaseFive();//mutation
			phaseSix();//bazgasht	
			
			Double averageFitness=0.0;
			for(int j=0;j<populationSize;j++){
				averageFitness+=population[j].fitness;
			}averageFitness/=populationSize;
			
//			System.out.println(""+population[0].fitness+","+averageFitness+","+population[populationSize-1].fitness);
			System.out.println(""+population[0].fitness);

		}
		
		System.out.println("and its coefficents are: "+population[0].gene[3]+","+population[0].gene[2]+","+population[0].gene[1]+","+population[0].gene[0]);
//		System.out.println("and variance was:  "+variance+" and mutationRate was: "+ mutationRate);
	}
	
//___________________________________________________________________________		
	public void phaseSix() {
		Individual total[]=new Individual[2*populationSize];
		for(int i=0;i<populationSize;i++) {
			total[i]=population[i];
		}
		for(int i=0;i<populationSize;i++) {
			total[i+populationSize]=children[i];
		}
		//insertion Sort
		   Individual temp;
		   for (int i = 1; i < total.length; i++) {
		    for (int j = i; j > 0; j--) {
		     if (total[j].fitness > total [j - 1].fitness) {
		      temp = total[j];
		      total[j] = total[j - 1];
		      total[j - 1] = temp;
		     }
		    }
		   }
//		   for (int i = 0; i < total.length; i++) {
//		     System.out.println(total[i].fitness);
//		   }
		   for (int i = 0; i < populationSize; i++) {
			   population[i]=total[i];
		   }
		
//		   for (int i = 0; i < population.length; i++) {
//			     System.out.println(population[i].fitness);
//			   }
		   
		   
	}
//___________________________________________________________________________		

	public void printPopulation() {
		System.out.println("******************************************************************************************************");
		for(int i=0;i<populationSize;i++) {
			System.out.println(i+"  "+population[i].gene[0]+"&"+population[i].gene[1]+"&"+population[i].gene[2]+"&"+population[i].gene[3]+"&");
		}
	}

	public void printChildren() {
		System.out.println("children are heereeeeeeeee******************************************************************************************************");
		for(int i=0;i<populationSize;i++) {
			System.out.println(i+"  "+children[i].gene[0]+"&"+children[i].gene[1]+"&"+children[i].gene[2]+"&"+children[i].gene[3]);
		}
	}
	
//___________________________________________________________________________		
	public void phaseFive() {
		for(int i=0;i<populationSize;i++) {
			mutation(children[i]);
		}
	}
//___________________________________________________________________________		
	public void mutation(Individual a) {
		for(int i=0;i<4;i++) {
			double a0 = Math.random() ;
			if(a0<mutationRate) {
				Random r = new java.util.Random();
				Double noise = r.nextGaussian() * Math.sqrt(variance);
				
				a.gene[i]+=noise;
			}	
		}
		a.MSE=getMSE(a);
		a.fitness=1/(1+a.MSE);
	}
//___________________________________________________________________________		
	public void phaseFour() {//producing children
		for(int i=0;i<populationSize;i++) {
			Individual parent1=parents[2*i];
			Individual parent2=parents[2*i+1];
			
			int index1=(int )(Math.random() * 4);
			int index2=(int )(Math.random() * 3);
			if(index2>=index1)
				index2++;
			Individual child=new Individual(0.0,0.0,0.0,0.0);
			child.gene[0]=parent1.gene[0];
			child.gene[1]=parent1.gene[1];
			child.gene[2]=parent1.gene[2];
			child.gene[3]=parent1.gene[3];
			child.gene[index1]=parent2.gene[index1];
			child.gene[index2]=parent2.gene[index2];
			child.MSE=getMSE(child);
			child.fitness=1/(1+child.MSE);
			children[i]=child;
		}
	}
	
//___________________________________________________________________________	
	public void phaseThree() {//choosing parents
		for(int i=0;i<parents.length;i++) {
			parents[i]=chooseOneParent();
		}
		
	}
//___________________________________________________________________________	
	
	public Individual chooseOneParent() {
		
		Individual contestant[]=new Individual[tornumentSize];
		for(int i=0;i<tornumentSize;i++) {
			int k=(int )(Math.random() * populationSize);
			contestant[i]=population[k];
		}
		//now we should pick the best from contestants
		Individual bestIndividual=contestant[0];
		
		for(int i=0;i<tornumentSize;i++) {
			if(contestant[i].fitness>bestIndividual.fitness) 
				bestIndividual=contestant[i];
		}
		
		return bestIndividual;
	}
	
//___________________________________________________________________________	
	
	public void phaseTwo() {//calculating fitness function
		Double fitness;
		Double MSE;
		for(int i=0;i<populationSize;i++) {
			MSE=getMSE(population[i]);
			fitness=1/(1+MSE);
			population[i].fitness=fitness;
			population[i].MSE=MSE;
		}
	}

//___________________________________________________________________________	
	public Double getMSE(Individual m) {
		Double MSE=0.0;
		for(int i=0;i<100;i++) {
			Double x=i*0.1;
			Double temp=Ys[i]-(m.gene[0]+m.gene[1]*x+m.gene[2]*x*x+m.gene[3]*x*x*x);
			MSE+=temp*temp;
		}
		
	return MSE/populationSize;	
	}
	
	
	
//___________________________________________________________________________	
	
	public void phaseOne() {//creating first population
		for(int i=0;i<populationSize;i++) {
			population[i]=createIndividual();
		}
	}
//___________________________________________________________________________	

	public Individual createIndividual() {
		Double a0 = Math.random() * geneRange ;
		int negORpos=(int )(Math.random() * 2);
		if(negORpos==0)a0=-a0;
		
		Double a1 = Math.random() * geneRange ;
		negORpos=(int )(Math.random() * 2);
		if(negORpos==0)a1=-a1;
		
		Double a2 = Math.random() * geneRange ;
		negORpos=(int )(Math.random() * 2);
		if(negORpos==0)a2=-a2;

		Double a3 = Math.random() * geneRange ;
		negORpos=(int )(Math.random() * 2);
		if(negORpos==0)a3=-a3;
		
		return new Individual(a0,a1,a2,a3);
	}
	
//___________________________________________________________________________	
	public void getCSV() {
		
		 File file = new File("/home/m/Desktop/numbers.csv"); 
		  
		  BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 

		int i=0;
		  String st; 
		  try {
			while ((st = br.readLine()) != null) {
			    Ys[i]=Double.valueOf(st);
			    i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		  
		  } 

}
