package de.metanome.algorithms.dvakmv;

import java.util.ArrayList;
import java.util.List;

import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.input.RelationalInput;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.result_receiver.BasicStatisticsResultReceiver;
import de.metanome.algorithm_integration.results.BasicStatistic;
import de.metanome.algorithm_integration.results.basic_statistic_values.BasicStatisticValueLong;
/**
 * * Implementation of AKMV.
 * * Reference:
 *    AKMV. Beyer, K., Gemulla, R., Haas, P. J., Reinwald, B., & Sismanis, Y. (2009). Distinct-value synopses for multiset operations. Communications of the ACM, 52(10), 87-95. 
 * * @author Hazar.Harmouch
 */

public class DVAKMVAlgorithm {
	
	protected RelationalInputGenerator inputGenerator = null;
	protected BasicStatisticsResultReceiver resultReceiver = null;
	
	protected String relationName;
	protected List<String> columnNames;
	private final String NUMBEROFDISTINCT = "Number of Distinct Values";
	private RelationalInput input;
	protected double eps=0.01;
	public void execute() throws AlgorithmExecutionException {
		////////////////////////////////////////////
		// THE DISCOVERY ALGORITHM LIVES HERE :-) //
		////////////////////////////////////////////
		
	
	input = this.inputGenerator.generateNewCopy();
    this.relationName = input.relationName();
    this.columnNames = input.columnNames();
    ArrayList<AKMV> Columns = new ArrayList<AKMV>();
    for (int i = 0; i < columnNames.size(); i++)
      Columns.add(new AKMV(eps));
     
    while (input.hasNext()) {
      List<String> CurrentTuple=input.next();
      for (int i = 0; i < columnNames.size(); i++)
           Columns.get(i).offer(CurrentTuple.get(i));
		
	}
  
    for (int i = 0; i < columnNames.size(); i++)
    {addStatistic(NUMBEROFDISTINCT, Columns.get(i).cardinality(), columnNames.get(i), relationName); }
    
	}
	
	 private void addStatistic(String StatisticName, long Value, String ColumnName,
         String RelationName) throws AlgorithmExecutionException {
       BasicStatistic result = new BasicStatistic(new ColumnIdentifier(RelationName, ColumnName));
       result.addStatistic(StatisticName, new BasicStatisticValueLong(Value));
      //System.out.println(StatisticName + " of " + ColumnName + " : " + Value);
       resultReceiver.receiveResult(result);
     }
	
}
