package atc.Service.Commands;

/**
 * @author Silas Daniel
 */

public class TakeoffCommand extends AtcCommand {

    private final int altitude;

    public TakeoffCommand(int altitude) {
        this.altitude = altitude;
    }

    public int getAltitude() {
        return altitude;
    }

    @Override
    public void initialize() {
        System.out.println("Taking of at " + this.altitude + " meters!");
    }
}
