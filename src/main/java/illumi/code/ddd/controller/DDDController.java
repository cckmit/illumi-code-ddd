package illumi.code.ddd.controller;

import illumi.code.ddd.model.DDDStructure;
import illumi.code.ddd.service.analyse.AnalyseService;
import illumi.code.ddd.service.fitness.FitnessService;
import illumi.code.ddd.service.metric.MetricService;
import illumi.code.ddd.service.refactor.RefactorService;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller()
@SuppressWarnings("CheckStyle")
public class DDDController {
  private static final Logger LOGGER = LoggerFactory.getLogger(DDDController.class);

  private long timeStart;

  @Inject
  AnalyseService analyseService;
  @Inject
  FitnessService fitnessService;
  @Inject
  MetricService metricService;
  @Inject
  RefactorService refactorService;

  private DDDStructure newStructure;

  /**
   * HTTP GET: analyse.
   *
   * @param path : fully qualified name of the system module
   * @return HttpResponse as JSON
   */
  @Get("/analyse/{path}")
  @Produces(MediaType.APPLICATION_JSON)
  public HttpResponse<String> getArtifacts(String path) {
    startTime();
    LOGGER.info("HTTP GET: analyse/{}", path);
    newStructure = new DDDStructure();
    analyseService.setStructure(newStructure);
    fitnessService.setStructure(newStructure);
    analyseService.analyzeStructure(path);
    JSONArray response = fitnessService.getStructureWithFitness();
    stopTimestamp();
    return HttpResponse.ok(response.toString());
  }

  /**
   * HTTP GET: metric.
   *
   * @return HttpResponse as JSON
   */
  @Get("/metric")
  @Produces(MediaType.APPLICATION_JSON)
  public HttpResponse<String> getMetrics() {
    startTime();
    LOGGER.info("HTTP GET: metric/");
    if (newStructure != null) {
      metricService.setStructure(newStructure);
      JSONObject response = metricService.getMetric();
      stopTimestamp();
      return HttpResponse.ok(response.toString());
    }
    stopTimestamp();
    return HttpResponse.badRequest("{\"message\":\"No project has been analyzed!\"}");
  }

  /**
   * HTTP GET: refactor.
   *
   * @return HttpResponse as JSON
   */
  @Get("/refactor")
  @Produces(MediaType.APPLICATION_JSON)
  public HttpResponse<String> refactor() {
    startTime();
    LOGGER.info("HTTP GET: refactor/");
    if (newStructure != null) {
      refactorService.setOldStructure(newStructure);
      newStructure = refactorService.refactor();
      fitnessService.setStructure(newStructure);
      JSONArray response = fitnessService.getStructureWithFitness();
      stopTimestamp();
      return HttpResponse.ok(response.toString());
    }
    stopTimestamp();
    return HttpResponse.badRequest("{\"message\":\"No project has been analyzed!\"}");
  }

  private void startTime() {
    timeStart = System.currentTimeMillis();
  }

  private void stopTimestamp() {
    long ms = System.currentTimeMillis() - timeStart;
    long min = TimeUnit.MILLISECONDS.toMinutes(ms);
    long sec = TimeUnit.MILLISECONDS.toSeconds(ms);
    ms -= sec * 1000;
    sec -= min * 60;

    LOGGER.info("[FINISHED] - {}min {}s {}ms", min, sec, ms);
  }
}