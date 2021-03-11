package atc.Service;

import atc.Model.Aircraft;
import atc.Repositories.AircraftRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirPlaneService {
    private final AircraftRepo aircraftRepo;

    @Autowired
    public AirPlaneService(AircraftRepo aircraftRepo) {
        this.aircraftRepo = aircraftRepo;
    }

    public void addAirPlane(Aircraft aircraft) {
        aircraftRepo.save(aircraft);
    }

    public void addAirPlaneList(List<Aircraft> aircraftList) {
        aircraftRepo.saveAll(aircraftList);
    }

    public List<Aircraft> selectAllPlanes() {
        return (List<Aircraft>) aircraftRepo.findAll();
    }

    public Aircraft selectPlaneByID(int id) {
        return aircraftRepo.findById(id).orElse(null);
    }

    public String deleteAirplaneById(int id) {
        if (aircraftRepo.findById(id).isPresent()) {
            aircraftRepo.deleteById(id);
            return "Airplane " + id + " removed!!";
        }
        return "Not found airplane: " + id + " !";
    }

    public Aircraft updateAirplaneById(int id, Aircraft aircraft) {
        Aircraft existingAirplane = aircraftRepo.findById(id).orElse(null);
        assert existingAirplane != null;

        if (aircraft.getName() != null) {
            existingAirplane.setName(aircraft.getName());
        }

        if (aircraft.getModel() != null) {
            existingAirplane.setModel(aircraft.getModel());
        }

        return aircraftRepo.save(existingAirplane);

    }
}
