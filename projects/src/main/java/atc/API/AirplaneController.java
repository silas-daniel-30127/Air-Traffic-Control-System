package atc.API;

import atc.Model.Aircraft;
import atc.Service.AirPlaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@CrossOrigin("*")
@RequestMapping("api/v1/atcCRUD")
@RestController
public class AirplaneController {
    private final AirPlaneService airPlaneService;

    @Autowired
    public AirplaneController(AirPlaneService airPlaneService) {
        this.airPlaneService = airPlaneService;
    }

    @PostMapping
    public void addAirPlane(@RequestBody @NotBlank Aircraft airplane) {
        airPlaneService.addAirPlane(airplane);
    }

    @PostMapping("/addMore")
    public void addMultipleAirPlanes(@RequestBody @NotBlank List<Aircraft> airplaneList) {
        airPlaneService.addAirPlaneList(airplaneList);
    }

    @GetMapping
    public List<Aircraft> getAllPlanes() {
        return airPlaneService.selectAllPlanes();
    }

    @GetMapping(path = "{id}")
    public Aircraft selectAirplaneById(@PathVariable("id") int id) {
        return airPlaneService.selectPlaneByID(id);
    }

    @DeleteMapping(path = "{id}")
    public String deleteAirplaneById(@PathVariable("id") int id) {
        return airPlaneService.deleteAirplaneById(id);
    }

    @PutMapping(path = "/update/{id}")
    public Aircraft updateAirplaneById(@PathVariable("id") int id, @RequestBody Aircraft airplane) {
        return airPlaneService.updateAirplaneById(id, airplane);
    }
}
