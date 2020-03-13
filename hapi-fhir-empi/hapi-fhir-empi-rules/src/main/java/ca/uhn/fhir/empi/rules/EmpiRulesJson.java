package ca.uhn.fhir.empi.rules;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class EmpiRulesJson implements IModelJson, Iterable<EmpiMatchFieldJson> {
	@JsonProperty("test")
	String myTest = "test";
	@JsonProperty("matchFields")
	List<EmpiMatchFieldJson> myMatchFieldJsonList = new ArrayList<>();
	@JsonProperty("vectorWeights")
	Map<Long, Double> myVectorWeights = new HashMap<>();

	public void addMatchField(EmpiMatchFieldJson theMatchRuleName) {
		myMatchFieldJsonList.add(theMatchRuleName);
	}

	public int size() {
		return myMatchFieldJsonList.size();
	}

	public EmpiMatchFieldJson get(int theIndex) {
		return myMatchFieldJsonList.get(theIndex);
	}

	@NotNull
	@Override
	public Iterator<EmpiMatchFieldJson> iterator() {
		return myMatchFieldJsonList.iterator();
	}

	@Override
	public void forEach(Consumer<? super EmpiMatchFieldJson> action) {
		myMatchFieldJsonList.forEach(action);
	}

	@Override
	public Spliterator<EmpiMatchFieldJson> spliterator() {
		return myMatchFieldJsonList.spliterator();
	}

	public double getWeight(Long theMatchVector) {
		Double result = myVectorWeights.get(theMatchVector);
		return MoreObjects.firstNonNull(result, 0.0);
	}

	public void putWeight(Long theMatchVector, double theWeight) {
		myVectorWeights.put(theMatchVector, theWeight);
	}
}
