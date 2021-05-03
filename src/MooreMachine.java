// A state machine of Moore type that detects a sequence of character (or string)
// form the intended String sent to the constructor during the instantiation.

import java.util.HashMap;

public class MooreMachine {

    private String currentState;
    private HashMap<String, Boolean> states = new HashMap<>(); // < state, output >

    public MooreMachine(String sequence) {

        states.put("", false); // Reset/initial state.

        // Make a state for each valid sequence: "t", "tw", "twa", "twad", "twado".
        for (int i = 0; i < sequence.length(); i++) {

            if (i == sequence.length() - 1) {
                states.put(sequence, true);

            } else {
                states.put(sequence.substring(0, i + 1), false);
            }
        }

        currentState = ""; // Initial state.

    }

    /**
     * Method takes an input and changes current state depending on the input. The combinatory of nextstate.
     * @param charInput
     * @return
     */
    public boolean input(String charInput) {

        switch (charInput) {

            case "t" : currentState = "t"; break;

            default : // If input respects the sequence, new current state is a valid sequence. Else, restart.

                if (states.containsKey(currentState + charInput)) currentState += charInput;
                else currentState = "";

                break;
        }

        return states.get(currentState); // Return output of new states.

    }

}
