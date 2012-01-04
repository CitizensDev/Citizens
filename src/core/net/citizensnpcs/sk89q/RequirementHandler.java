package net.citizensnpcs.sk89q;

import java.lang.annotation.Annotation;

public interface RequirementHandler {
	Class<? extends Annotation> getRequirementAnnotation();

	void evaluateRequirements(Annotation annotation, Object... arguments)
			throws RequirementMissingException;
}
