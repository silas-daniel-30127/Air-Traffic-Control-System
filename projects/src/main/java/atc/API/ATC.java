package atc.API;

import atc.Exceptions.AlreadyExistsAirCraft;
import atc.Exceptions.NotFoundAirCraft;
import atc.Model.Aircraft;
import atc.Model.Airplane;
import atc.Repositories.AircraftRepo;
import atc.Service.Commands.AtcCommand;
import atc.Service.Commands.LandCommand;
import atc.Service.Commands.TakeoffCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@CrossOrigin("*")
@RequestMapping("/api/v1/atc")
@RestController
public class ATC {
    private final List<Airplane> airplanes;
    private final AircraftRepo aircraftRepo;

    @Autowired
    public ATC(AircraftRepo aircraftRepo) {
        this.aircraftRepo = aircraftRepo;
        this.airplanes = addAllPlanes();
    }


    @PostMapping(path = "launch/{id}")
    public void startAircraftById(@PathVariable int id) {
        Airplane airplane = airplanes.stream().filter(air -> air.getId() == id).findFirst().orElse(null);
        assert airplane != null;
        airplane.startEngine();
    }

    @PostMapping(path = "launchAll")
    public void startAllAirCrafts() {
        for (Airplane airplane : airplanes) {
            airplane.startEngine();
        }
    }

    /**
     * This method should add an aircraft to the ATC with the given attributes
     *
     * @param aircraft The aircraft
     * @throws AlreadyExistsAirCraft The exception thrown if the aircraft already exists
     */
    @PostMapping(path = "/private")
    public void addPrivateAircraft(@RequestBody Aircraft aircraft) throws AlreadyExistsAirCraft {

        Airplane airplane = new Airplane(aircraft);
        if (airplanes.contains(airplane)) {
            throw new AlreadyExistsAirCraft("Aircraft " + airplane + " already exists!");
        }
        this.airplanes.add(airplane);
    }

    @PostMapping(path = "takeoff/{id}")
    public void takeOff(@PathVariable int id, @RequestBody String altitude) {
        try {
            int alt = Integer.parseInt(altitude);
            sendCommand(id, new TakeoffCommand(alt));
        } catch (NotFoundAirCraft notFoundAirCraft) {
            System.out.println(notFoundAirCraft.getMessage());
        }
    }

    @PostMapping(path = "/land/{id}")
    public void land(@PathVariable int id) {
        try {
            sendCommand(id, new LandCommand());
        } catch (NotFoundAirCraft notFoundAirCraft) {
            System.out.println(notFoundAirCraft.getMessage());
        }
    }

    /**
     * This method should send to the aircraft given id an atc command thru cmd parameter
     *
     * @param id  The id which refers at aircraft
     * @param cmd The command to execute
     * @throws NotFoundAirCraft The exception thrown if the aircraft does not exists
     */
    private void sendCommand(int id, AtcCommand cmd) throws NotFoundAirCraft {
        Airplane aircraft;
        boolean flag = false;
        for (Airplane value : airplanes) {
            if (value.getId() == id) {
                aircraft = value;
                aircraft.receiveAtcMessage(cmd);
                flag = true;
                break;
            }
        }
        if (!flag) {
            throw new NotFoundAirCraft("Aircraft " + id + " not found!");
        }
    }

    private List<Airplane> addAllPlanes() {
        List<Aircraft> aircraftList = selectAllAirCrafts();
        List<Airplane> airplaneList = new ArrayList<>();
        for (Aircraft aircraft : aircraftList) {
            airplaneList.add(new Airplane(aircraft));
        }
        return airplaneList;
    }

    private List<Aircraft> selectAllAirCrafts() {
        return (List<Aircraft>) this.aircraftRepo.findAll();
    }

    @Override
    public String toString() {
        return "" + airplanes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ATC atc = (ATC) o;
        return Objects.equals(airplanes, atc.airplanes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(airplanes);
    }
}
