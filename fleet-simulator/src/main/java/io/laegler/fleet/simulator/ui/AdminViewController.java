package io.laegler.fleet.simulator.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import io.laegler.fleet.model.Vehicle;
import io.laegler.fleet.repo.VehicleEsRepository;
import io.laegler.fleet.simulator.config.FleetSimulatorProperties;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@Controller
public class AdminViewController {

  @Value("${spring.application.name:localhost}")
  private String appName;

  @Value("${server.port:8080}")
  private int port;

  @Autowired
  private FleetSimulatorProperties props;

  @Autowired
  private VehicleEsRepository vehicleRepo;

  @ApiIgnore
  @GetMapping(value = {"/dashboard", "/", "/index.html", "/index"})
  public String renderDashboard(Model model) {
    log.trace("renderDashboard() called");

    model.addAttribute("apiKey", props.getGoogle().getBrowserKey());
    model.addAttribute("backend", "http://" + appName + ":" + port);

    Vehicle vehicle =
        vehicleRepo.findAll(PageRequest.of(0, 1)).getContent().stream().findFirst().orElse(null);

    String mapCenter = "-36.851243,174.777399";
    if (vehicle != null && vehicle.getPosition() != null) {
      mapCenter = vehicle.getPosition().getLat() + "," + vehicle.getPosition().getLng();
    }
    model.addAttribute("mapCenter", mapCenter);

    return "index";
  }

  @ApiIgnore
  @GetMapping(value = "/websockets")
  public String renderWebsockets(Model model) {
    log.trace("renderWebsockets() called");

    return "websockets";
  }

}
