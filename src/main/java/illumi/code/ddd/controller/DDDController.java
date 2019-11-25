package illumi.code.ddd.controller;

import javax.inject.Inject;

import illumi.code.ddd.service.*;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller()
public class DDDController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DDDController.class);

	@Inject AnalyseService analyseService;
	@Inject FitnessService fitnessService;
	@Inject MetricService metricService;
	@Inject RefactorService refactorService;

	private StructureService structureService;

    @Get("/analyse/{path}") 
    @Produces(MediaType.APPLICATION_JSON) 
    public HttpResponse<String> getArtifacts(String path) {
    	LOGGER.info("HTTP GET: analyse/{}", path);
    	structureService = new StructureService();
		analyseService.setStructureService(structureService);
		fitnessService.setStructureService(structureService);
    	analyseService.analyzeStructure(path);
    	return HttpResponse.ok(fitnessService.getStructureWithFitness().toString());
    }

    @Get("/metric") 
    @Produces(MediaType.APPLICATION_JSON) 
    public HttpResponse<String> getMetrics() {
		LOGGER.info("HTTP GET: metric/");
		if (structureService != null) {
			metricService.setStructureService(structureService);
			return HttpResponse.ok(metricService.getMetric().toString());
		}
		return HttpResponse.badRequest("{\"message\":\"No project has been analyzed!\"}");
    }



	@Get("/refactor")
	@Produces(MediaType.APPLICATION_JSON)
	public HttpResponse<String> refactor() {
		LOGGER.info("HTTP GET: refactor/");
		if (structureService != null) {
			refactorService.setStructureService(structureService);
			structureService = refactorService.refactor();
			fitnessService.setStructureService(structureService);

			return HttpResponse.ok(new JSONArray(structureService.getStructure()).toString());
//			return HttpResponse.ok(fitnessService.getStructureWithFitness().toString());
		}
		return HttpResponse.badRequest("{\"message\":\"No project has been analyzed!\"}");
	}
}