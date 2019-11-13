package illumi.code.ddd.model.artifacts;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import illumi.code.ddd.model.DDDRating;
import illumi.code.ddd.model.DDDType;

public class InterfaceFitnessTest {

	@Test
	public void testEvaluateRepositoryInterface() {
		Interface artifact = new Interface("Repository", "de.test.Repository");
		artifact.setType(DDDType.REPOSITORY);
		artifact.addMethod(new Method("public", "findById", "test findById()"));
		artifact.addMethod(new Method("public", "getId", "test getId()"));
		artifact.addMethod(new Method("public", "save", "test save()"));
		artifact.addMethod(new Method("public", "add", "test add()"));
		artifact.addMethod(new Method("public", "insert", "test insert()"));
		artifact.addMethod(new Method("public", "delete", "test delete()"));
		artifact.addMethod(new Method("public", "remove", "test remove()"));
		artifact.addMethod(new Method("public", "contains", "test contains()"));
		artifact.addMethod(new Method("public", "exists", "test exists()"));
		artifact.addMethod(new Method("public", "update", "test update()"));
		artifact.addMethod(new Method("public", "toString", "test toString()"));
		
		artifact.evaluate();
		
		assertAll(	() -> assertEquals(100.0, 		artifact.getFitness(), 										"Fitness"),
				 	() -> assertEquals(DDDRating.A, artifact.getDDDFitness().getscore(), 						"Rating"),
				 	() -> assertEquals(5, 			artifact.getDDDFitness().getNumberOfCriteria(), 			"Total Criteria"),
				 	() -> assertEquals(5, 			artifact.getDDDFitness().getNumberOfFulfilledCriteria(),	"Fulfilled Criteria"),
				 	() -> assertEquals(0, 			artifact.getDDDFitness().getIssues().size(), 				"#Issues"));
	}
	
	@Test
	public void testEvaluateInvalidRepositoryInterface() {
		Interface artifact = new Interface("Repo", "de.test.Repo");
		artifact.setType(DDDType.REPOSITORY);
		artifact.addMethod(new Method("public", "toString", "test toString()"));
		
		artifact.evaluate();
		
		assertAll(	() -> assertEquals(0.0, 		artifact.getFitness(), 										"Fitness"),
				 	() -> assertEquals(DDDRating.F, artifact.getDDDFitness().getscore(), 						"Rating"),
				 	() -> assertEquals(5, 			artifact.getDDDFitness().getNumberOfCriteria(), 			"Total Criteria"),
				 	() -> assertEquals(0, 			artifact.getDDDFitness().getNumberOfFulfilledCriteria(),	"Fulfilled Criteria"),
				 	() -> assertEquals(6, 			artifact.getDDDFitness().getIssues().size(), 				"#Issues"));
	}
	
	@Test
	public void testEvaluateFactoryInterface() {
		Interface artifact = new Interface("Factory", "de.test.Factory");
		artifact.setType(DDDType.FACTORY);
		artifact.addField(new Field("private", "repository", "de.test.Repository"));
		artifact.addField(new Field("private", "name", "string"));
		artifact.addMethod(new Method("public", "create", "test create()"));
		artifact.addMethod(new Method("public", "toString", "test toString()"));
		
		artifact.evaluate();
		
		assertAll(	() -> assertEquals(100.0, 		artifact.getFitness(), 										"Fitness"),
				 	() -> assertEquals(DDDRating.A, artifact.getDDDFitness().getscore(), 						"Rating"),
				 	() -> assertEquals(2, 			artifact.getDDDFitness().getNumberOfCriteria(), 			"Total Criteria"),
				 	() -> assertEquals(2, 			artifact.getDDDFitness().getNumberOfFulfilledCriteria(),	"Fulfilled Criteria"),
				 	() -> assertEquals(0, 			artifact.getDDDFitness().getIssues().size(), 				"#Issues"));
	}
	
	@Test
	public void testEvaluateInvalidFactoryInterface() {
		Interface artifact = new Interface("Fac", "de.test.Fac");
		artifact.setType(DDDType.FACTORY);
		artifact.addField(new Field("private", "name", "string"));
		artifact.addMethod(new Method("public", "toString", "test toString()"));
		
		artifact.evaluate();
		
		assertAll(	() -> assertEquals(0.0, 		artifact.getFitness(), 										"Fitness"),
				 	() -> assertEquals(DDDRating.F, artifact.getDDDFitness().getscore(), 						"Rating"),
				 	() -> assertEquals(2, 			artifact.getDDDFitness().getNumberOfCriteria(), 			"Total Criteria"),
				 	() -> assertEquals(0, 			artifact.getDDDFitness().getNumberOfFulfilledCriteria(),	"Fulfilled Criteria"),
				 	() -> assertEquals(3, 			artifact.getDDDFitness().getIssues().size(), 				"#Issues"));
	}
	
	@Test
	public void testEvaluateServiceInterface() {
		Interface artifact = new Interface("Service", "de.test.Service");
		artifact.setType(DDDType.SERVICE);
		
		artifact.evaluate();
		
		assertAll(	() -> assertEquals(100.0, 		artifact.getFitness(), 										"Fitness"),
				 	() -> assertEquals(DDDRating.A, artifact.getDDDFitness().getscore(), 						"Rating"),
				 	() -> assertEquals(0, 			artifact.getDDDFitness().getNumberOfCriteria(), 			"Total Criteria"),
				 	() -> assertEquals(0, 			artifact.getDDDFitness().getNumberOfFulfilledCriteria(),	"Fulfilled Criteria"),
				 	() -> assertEquals(0, 			artifact.getDDDFitness().getIssues().size(), 				"#Issues"));
	}
}
