package ca.uhn.fhir.empi;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.rules.EmpiMatchFieldJson;
import ca.uhn.fhir.empi.rules.EmpiRulesJson;
import ca.uhn.fhir.empi.rules.metric.DistanceMetricEnum;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Before;

public abstract class BaseTest {
	protected static final FhirContext ourFhirContext = FhirContext.forR4();
	public static final double EXPECTED_WEIGHT = 0.83;
	public static final double NAME_THRESHOLD = 0.8;
	public static final double NAME_DELTA = 0.0001;

	protected Patient myPatient1;
	protected Patient myPatient2;
	protected EmpiMatchFieldJson myGivenNameMatchField;
	protected EmpiRulesJson myEmpiRules;

	@Before
	public void before() {
		myGivenNameMatchField = new EmpiMatchFieldJson("Patient", "name.given", DistanceMetricEnum.COSINE, NAME_THRESHOLD);
		myEmpiRules = new EmpiRulesJson();
		myEmpiRules.addMatchField(myGivenNameMatchField);
		myEmpiRules.putWeight(1L, EXPECTED_WEIGHT);
		initializePatients();
	}

	private void initializePatients() {
		myPatient1 = new Patient();
		myPatient1.addName().addGiven("John");
		myPatient1.setId("Patient/1");
		myPatient2 = new Patient();
		myPatient2.addName().addGiven("Johny");
		myPatient2.setId("Patient/2");
	}
}
