import java.util.*;

public class FSAImplementation{
	
	public class State {

		int index;
		Condition[] conditions;
		ArrayList<Integer> transitions;

		public State(int newIndex, Condition[] newConditions) {
			this.index = newIndex;
			this.conditions = newConditions;
			transitions = new ArrayList<Integer>();
		}

		boolean isStateSatisfiedBy(int[] values) {
			if (this.conditions == null) {
		 		System.out.println("condition is null.");
			}
			for (int i = 0; i < this.conditions.length; i++) {
				//if (!this.conditions[i].isConditionSatisfiedBy(s2.conditions[i]))
				Condition c1 = this.conditions[i];
				if (!c1.isConditionSatisfiedBy(values[i])) {
					return false;
				}
			}
			return true;
		}

		// adds a transition to the FSA between the specified states only if it doesn't already exist
		void addTransition(int nextStateIndex) {
			// This makes sure duplicate transitions are not added. Thus it is
			// not necessary to check if the transition is present before calling this function
			if (!this.transitions.contains(nextStateIndex)) {
				this.transitions.add(nextStateIndex);
			}
		}

		/*
		void addTransition(int currentStateIndex, int nextStateIndex) {
			if (!transitionPresent) {
				states[currentStateIndex].transitions.add(nextStateIndex);
			}
		}
		*/

		String printState() {
			String conditionList = "Conditions: ";
			for (Condition c : conditions) {
				if (c.low == c.high) {
					conditionList += "(" + c.low + ") ";
				} else {
					conditionList += "(" + c.low + ", " + c.high + ") ";
				}
			}
			String output = conditionList + "\n";
			output += "Transitions: ";
			for (int t : transitions) {
				output += t + ", ";
			}
			return output;
		}
	}

	public class Condition {
		int low;
		int high;
		// should I have an int single, to make it more clear that it is not always a range?

		public Condition(int single) {
			low = single;
			high = single;
		}

		public Condition(int low, int high) {
			low = low;
			high = high;
		}

		boolean isConditionSatisfiedBy(int value) {
			if (this.low == this.high) {
				// if low = high, not a range, but a single value. so it doesn't matter whether you check low or high.
				return (this.low == value);
			} else {
				return ((this.low <= value) && (this.high > value));
			}
		}
	}


	ArrayList<State> states;
	int numVariables; // number of variables being recorded, or the number of variables that matter to the states. (should be equal ?)

	public FSAImplementation(int numVariables) {
		states = new ArrayList<State>();
	}

	int addNewState(int[] conditionValues) {
		int newIndex = states.size();
		Condition[] conds = new Condition[numVariables];
		for (int i = 0; i < numVariables; i++) {
			conds[i] = new Condition(conditionValues[i]);
		}
		states.add(new State(newIndex, conds));
		return newIndex;
	}

	boolean isTransitionPresent(int currentStateIndex, int nextStateIndex) {
		State currentState = states.get(currentStateIndex);
		ArrayList<Integer> transitions = currentState.transitions;
		return transitions.contains(nextStateIndex);
	}



	void createStateMachineFromData(int[][] allData) {

		/*
		 allData:
		 [
		 	[0,1,1],
			[1,1,1],
			[1,1,1]
		 ]
		 */

		// create first state / current state:
		int firstStateIndex = -1;
		for (int i = 0; i < states.size(); i++) {
			if (states.get(i).isStateSatisfiedBy(allData[0])) {
				firstStateIndex = i;
				break;
			}
		}
		if (firstStateIndex == -1) {
			firstStateIndex = addNewState(allData[0]);
		}

		State currentState = states.get(firstStateIndex);



		// read in and analyze FSA for rest of the data
		for (int numAnalyzed = 1; numAnalyzed < allData.length; numAnalyzed++) {
		 	int[] data = allData[numAnalyzed];

		 	if (data == null) {
		 		System.out.println("data is null.");
		 	}
		 	if (currentState == null) {
		 		System.out.println("current state is null.");
		 	}

			if (currentState.isStateSatisfiedBy(data)) {
				currentState.addTransition(currentState.index);
			} else {
				boolean matchFound = false;
				for (int i = 0; i < states.size(); i++) {
					if (states.get(i).isStateSatisfiedBy(data)) {
						currentState.addTransition(i);
						currentState = states.get(i);
						matchFound = true;
					}
				}
				if (!matchFound) {
					addNewState(data);
				}
			}
		}
	}

	public static void main(String[] args) {
		int[] data_a = {0,1,1,1,1,2,2};
		int[] data_b = {1,1,1,2,1,1,1};
		int[] data_c = {1,1,1,1,1,1,0};

		int[][] dataABC = new int[3][];
		dataABC[0] = data_a;
		dataABC[1] = data_b;
		dataABC[2] = data_c;

		FSAImplementation fsa = new FSAImplementation(3);

		fsa.createStateMachineFromData(dataABC);
		System.out.println(fsa.states.size());
		for (State s : fsa.states) {
			System.out.println(s.printState());
		}
	}

}
