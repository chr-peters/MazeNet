package ai;

import generated.*;

import java.util.List;

public class CombinedEvaluator implements BoardEvaluator {
    private List<Double> weights;
    private List<BoardEvaluator> evaluators;

    /*
     * saves weights and evaluators
     */
    public CombindedEvaluator(List<Double> wei, List<BoardEvaluator> eval) {
	if(wei.size()!=eval.size()) {
	    System.err.println("number of weights and evaluators do not match");
	    System.exit(1);
	}
	this.weight = wei;
	this.evaluator = eval;
    }

    /*
     * calculates the weighted sum of all given evaluators
     */
    public double evaluate(Board board, int playerID, TreasureType treasure) {
	double value = 0;
	for(int i=0; i<this.weights.size(); i++) {
	    value += this.weights.get(i)*this.evaluators.get(i).evaluate(board, playerID, treasure);
	}
	return value;
    }
}
