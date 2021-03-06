package illumi.code.ddd.service.metric.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import illumi.code.ddd.model.DDDStructure;
import illumi.code.ddd.model.DDDType;
import illumi.code.ddd.model.artifacts.Artifact;
import illumi.code.ddd.model.artifacts.Class;
import illumi.code.ddd.model.artifacts.Package;
import illumi.code.ddd.model.fitness.DDDFitness;
import illumi.code.ddd.model.fitness.DDDIssueType;
import illumi.code.ddd.model.fitness.DDDRating;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MetricServiceImplTest {

  private MetricServiceImpl service;

  @BeforeAll
  void init() {
    final DDDStructure structure = new DDDStructure();
    Package domain = new Package("domain", "de.test.domain");
    domain.setType(DDDType.MODULE);
    DDDFitness fitness = new DDDFitness(3, 2);
    fitness.addFailedCriteria(DDDIssueType.INFO, "Test");
    domain.setFitness(fitness);
    structure.addPackage(domain);

    Class entity = new Class("Entity", "de.test.domain.Entity");
    entity.setType(DDDType.ENTITY);
    entity.setDomain("domain");
    entity.setFitness(new DDDFitness(15, 9));
    domain.addContains(entity);
    structure.addClass(entity);

    Class repo = new Class("EntityRepository", "de.test.domain.EntityRepository");
    repo.setType(DDDType.REPOSITORY);
    repo.setDomain("domain");
    repo.setFitness(new DDDFitness(5, 4));
    domain.addContains(repo);
    structure.addClass(repo);

    Package infrastructure = new Package("infrastructure", "de.test.infrastructure");
    infrastructure.setType(DDDType.MODULE);
    infrastructure.setFitness(new DDDFitness(5, 3));
    structure.addPackage(infrastructure);

    Class controller = new Class("EntityController", "de.test.infrastructure.EntityController");
    controller.setType(DDDType.INFRASTRUCTURE);
    controller.setFitness(new DDDFitness(12, 12));
    infrastructure.addContains(controller);
    structure.addClass(controller);

    ArrayList<Artifact> data = new ArrayList<>();
    data.add(domain);
    data.add(infrastructure);

    structure.setStructure(data);

    service = new MetricServiceImpl();
    service.setStructure(structure);
  }

  @Test
  void testGetMetric() {
    final JSONObject expected = new JSONObject()
        .put("DDD", new JSONObject()
            .put("metric", new JSONObject()
                .put("score", DDDRating.D)
                .put("fitness", 75)
                .put("statistic", new JSONObject()
                    .put("avg", 73.33)
                    .put("median", 66.67)
                    .put("standard deviation", 17)
                    .put("min", 60)
                    .put("max", 100))
                .put("#Issues", 1))
            .put("artifact", new JSONObject()
                .put("#MODULE", 2)
                .put("#REPOSITORY", 1)
                .put("#INFRASTRUCTUR", 1)
                .put("#ENTITY", 1))
            .put("hotspot", new JSONArray()
                .put(new JSONObject()
                    .put("name", "infrastructure")
                    .put("DDD", DDDType.MODULE)
                    .put("fitness", 60)
                    .put("issues", new JSONArray()))
                .put(new JSONObject()
                    .put("name", "Entity")
                    .put("domain", "domain")
                    .put("DDD", DDDType.ENTITY)
                    .put("fitness", 60)
                    .put("issues", new JSONArray()))
                .put(new JSONObject()
                    .put("name", "domain")
                    .put("DDD", DDDType.MODULE)
                    .put("fitness", 66.67)
                    .put("issues", new JSONArray()
                        .put("[INFO] Test")))
                .put(new JSONObject()
                    .put("name", "EntityRepository")
                    .put("domain", "domain")
                    .put("DDD", DDDType.REPOSITORY)
                    .put("fitness", 80)
                    .put("issues", new JSONArray()))))
        .put("OOD", new JSONObject()
            .put("module", new JSONObject()
                .put("de.test.domain", new JSONObject().put("abstractness", 0))
                .put("de.test.infrastructure", new JSONObject().put("abstractness", 0))));

    final JSONObject result = service.getMetric();
    assertEquals(expected.toString(), result.toString());
  }
}
