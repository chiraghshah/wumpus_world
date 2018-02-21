/**
 * 
 * @author Chirag Shah..
 *
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

public class TTEntailsAlgorithm {
	
	Set<String> symbolList = new HashSet<String>();	
	
	public boolean ttEntails(LogicalExpression kb_plus_rules, LogicalExpression alpha, Model model) {
		List<String> symbolList = extractSymbols(kb_plus_rules, alpha);
		symbolList = removeSymbols(model,symbolList);
		return ttCheckAll(kb_plus_rules, alpha, symbolList, model);
	}
	
	public boolean ttCheckAll(LogicalExpression kb, LogicalExpression alpha, List<String> symbols, Model model) {		
		if (symbols.isEmpty()) {			
			boolean pl_true = pl_true(kb, model);			
			if(pl_true){
				return pl_true(alpha, model);				
			}
			else{
				return true;
			}			
		} else {
			String P = (String) symbols.get(0);			
			List<String> rest = symbols.subList(1, symbols.size());			
			Model trueModel = model.extend(P, true);
			Model falseModel = model.extend(P,false);
			return (ttCheckAll(kb, alpha, rest, trueModel) && (ttCheckAll(kb, alpha, rest, falseModel)));
		}		
	}
	
	
	boolean pl_true(LogicalExpression kb, Model model){

		if(resolveLeaf(kb)){						
			return model.h.get(kb.getUniqueSymbol());			
		}
		else if(kb.getConnective()!=null && kb.getConnective().equalsIgnoreCase("not")){			
			return !(pl_true(kb.getNextSubexpression(),model));
		}
		else if(kb.getConnective()!=null && kb.getConnective().equalsIgnoreCase("or")){			
			Vector<LogicalExpression> vector = kb.getSubexpressions();
			Boolean b = false;
			for(int i=0;i<vector.size();i++){
				b = b || pl_true(vector.get(i),model);
			}
			return b;		
		}
		else if(kb.getConnective()!=null && kb.getConnective().equalsIgnoreCase("if")){		
			
			Vector<LogicalExpression> vector = kb.getSubexpressions();
			Boolean b = pl_true(vector.get(0),model);			
			b = !(b && !(pl_true(vector.get(1),model)));
			return b;			
		}
		else if(kb.getConnective()!=null && kb.getConnective().equalsIgnoreCase("iff")){			
			Vector<LogicalExpression> vector = kb.getSubexpressions();
			Boolean b = pl_true(vector.get(0),model);			
			return b == pl_true(vector.get(1),model);
		}
		else if(kb.getConnective()!=null && kb.getConnective().equalsIgnoreCase("and")){			
			Vector<LogicalExpression> vector = kb.getSubexpressions();
			
			Boolean b = true;
			for(int i=0;i<vector.size();i++){				
				b = b && pl_true(vector.get(i),model);
				if(b==false){	
					return b;
				}
			}
			return b;
		}
		else if(kb.getConnective()!=null && kb.getConnective().equalsIgnoreCase("xor")){			
			Vector<LogicalExpression> vector = kb.getSubexpressions();
			Boolean b = false;
			int truthCounter=0;
			for(int i=0;i<vector.size();i++){
				boolean retrieved = pl_true(vector.get(i),model);
				if(retrieved==true)truthCounter++;
				if(truthCounter>1)return false;
				b = ((b||retrieved) && !(b && retrieved));
			}
			return b;
		}
		return true;
	}
	
	boolean resolveLeaf(LogicalExpression kb){
		return kb.getConnective()==null;
	}

	
	
	List<String> extractSymbols(LogicalExpression kb, LogicalExpression alpha){		
		List<String> returnList = new ArrayList<String>(symbolList);
		getTheSymbols(kb);
		getTheSymbols(alpha);		
		return returnList;
	}
	
	void getTheSymbols(LogicalExpression sentence){
		if(sentence.getUniqueSymbol()!=null)
		{
			symbolList.add(sentence.getUniqueSymbol());
		}
		else
		{
		for(int i=0;i<sentence.getSubexpressions().size();i++)
		{
			LogicalExpression nextExpression = (LogicalExpression) sentence.getSubexpressions().get(i);
			getTheSymbols(nextExpression);
			if(nextExpression.getUniqueSymbol()!=null) 
			{
				symbolList.add(nextExpression.getUniqueSymbol());	
			}
		}
		}
	}	
	
	private List<String> removeSymbols(Model model, List<String> symbolList2) {
		
		Iterator<Entry<String,Boolean>> it = model.h.entrySet().iterator();
	    while (it.hasNext()) {	    	
	        Entry<String,Boolean> pair = (Entry<String,Boolean>)it.next();
	        symbolList2.remove(pair.getKey());	       
	    }
		return symbolList2;
	}
// making a separate class for Model..
	class Model{

		public HashMap<String,Boolean> h = new HashMap<String,Boolean>();

			public Model extend(String symbol, boolean b) {
			Model model = new Model();
			model.h.putAll(this.h);
			model.h.put(symbol, b);
			return model;
		}
		
	}

}
