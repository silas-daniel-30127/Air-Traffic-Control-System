package atc.Service.Commands;

/**
 * @author Silas Daniel
 */

public class LandCommand extends AtcCommand {

    @Override
    public void initialize() {
        System.out.println("Aircraft is landing!");
    }

    @Override
    public int getAltitude() {
        return 0;
    }
}
