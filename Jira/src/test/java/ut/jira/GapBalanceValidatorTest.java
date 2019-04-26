package ut.jira.workflow;

import org.applebase.jira.workflow.GapBalanceValidator;

import com.atlassian.jira.issue.MutableIssue;
import com.opensymphony.workflow.InvalidInputException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GapBalanceValidatorTest {
	protected GapBalanceValidator validator;
	protected MutableIssue issue;

	@Before
	public void setup() {
		issue = mock(MutableIssue.class);
		validator = new GapBalanceValidator();
	}

	@Test
	public void smokeTest() throws InvalidInputException {
		Map transientVars = new HashMap();
		transientVars.put("issue", issue);
		when(issue.getSummary()).thenReturn("This is a regular summary");
		
		validator.validate(transientVars, null, null);
	}
	
	@Test
	public void correctGapAmountTest() throws InvalidInputException {
		Map transientVars = new HashMap();
		transientVars.put("issue", issue);
		when(issue.getSummary()).thenReturn("This is a correct summary title with ()");
		
		validator.validate(transientVars, null, null);
	}

	@Test(expected = InvalidInputException.class)
	public void failValidationTest() throws InvalidInputException {
		Map transientVars = new HashMap();
		transientVars.put("issue", issue);
		when(issue.getSummary()).thenReturn("This (summary ( is lacking a enclosing gap )");

		// Should throw the expected exception
		validator.validate(transientVars, null, null);
	}

}
