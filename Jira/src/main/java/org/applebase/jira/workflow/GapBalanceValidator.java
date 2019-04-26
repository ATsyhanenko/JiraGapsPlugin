package org.applebase.jira.workflow;

import com.atlassian.jira.issue.Issue;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class GapBalanceValidator implements Validator {
	private static final Logger logger = LoggerFactory.getLogger(GapBalanceValidator.class);
	private static final char OPENGAP = '(', CLOSEGAP = ')';
	private static final String 
		EXCEPTION_MSG = "Open-close gaps are unbalanced. No closing gap found for open gap at position %d",
		UNBALANCED_EXCEPTION_MSG = "The amount of open gaps is %s than the closed ones by $d";

	public void validate(Map transientVars, Map args, PropertySet ps) throws InvalidInputException {
		Issue issue = (Issue) transientVars.get("issue");

		String summary = issue.getSummary();
		// TODO: learn how to define options in factory in order to have an option to provide 'validation strategy'
		gapValidation(summary);
	}

	/**
	 * This is a more agile version of the '()()()())))' validation checker, which
	 * checks if only the 'opened' gap '(' get's a ')' resolution.
	 * 
	 * This allows to exclude cases like ':)()' to be treated as a failure. If a
	 * full 'balance' of open and closed gaps must be maintained, the second version
	 * of the method can be used - gapBalanceValidation
	 * 
	 * @param data - String, that is required to be validated
	 * @throws InvalidInputException - is thrown in case of no closing match for an opened gap
	 */
	private void gapValidation(String data) throws InvalidInputException {
		// forming a char array
		char[] strArr = data.toCharArray();

		// forming a valiable to mark the position of the last 'close gap'
		int lastClosePos = 0;

		// going through the data
		for (int n = 0; n < strArr.length; n++) {
			// reacting to a '(' element
			if (strArr[n] == OPENGAP) {
				logger.debug("Found open gap at position {}", n);
				// setting a starting point for the closed gap to be searched
				if (lastClosePos == 0)	lastClosePos = n;
				logger.debug("Minimum closed gap position: {}", lastClosePos);
				// initializing a new 'last closing gap position' valirable
				int newLastClosePos = lastClosePos;

				// starting the search
				for (int m = lastClosePos + 1; m < strArr.length; m++) {
					if (strArr[m] == CLOSEGAP) {
						logger.debug("Close gap found at position: {}", m);
						newLastClosePos = m;
						break;
					}
				}

				// validating if a closed gap match has been found by last close gap old and new
				// comparison
				// if not - Exception
				if (newLastClosePos == lastClosePos) {
					throw new InvalidInputException(String.format(EXCEPTION_MSG, n));
				}
				lastClosePos = newLastClosePos;
			}
		}
	}

	/** A second option to validate the input in terms of gap balance.
	 *  This method goes by a straight-forward strategy of counting the opened and closed gaps,
	 *  returning, in case of difference in the amount, a message of 'There are more\less opened gaps
	 *  than the closed ones'. 
	 *  
	 * @param data - String, that is required to be validated
	 * @throws InvalidInputException - is thrown in case of open and close gap amount mismatch
	 */
	private void gapBalanceValidation(String data) throws InvalidInputException {
		// forming a char array
		char[] strArr = data.toCharArray();
		
		// declaring a 'balance' variable
		int balance = 0;
		
		// starting the search
		for(int n = 0; n<strArr.length; n++) {
			if (strArr[n] == OPENGAP) balance++;
			else if(strArr[n] == CLOSEGAP) balance--;
		}
		if (balance == 0) return;
		logger.debug("The balance of opened-closed gaps is: {}", balance);
		String dirLine = (balance > 0) ? "bigger" : "lesser";
		balance = Math.abs(balance);
		throw new InvalidInputException(String.format(UNBALANCED_EXCEPTION_MSG, dirLine, balance));
	}
}
