package atc.Model;

import atc.Service.Commands.AtcCommand;
import atc.Service.Commands.LandCommand;
import atc.Service.Commands.TakeoffCommand;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Silas Daniel
 */

public class Airplane extends Aircraft implements Runnable {
    private int altitude;
    private States state = States.Inactive;
    private long cruisingTime = 0;

    public Airplane(Aircraft aircraft) {
        super(aircraft.getId(), aircraft.getName(), aircraft.getModel());
    }

    /**
     * This method should set the current thread to sleep for 10 seconds
     */
    public void taxing() {
        System.out.println(super.getName() + ": taxing: 10 sec");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(super.getName() + ": taxing ended");
    }

    /**
     * This method should set the current thread to sleep for 10 * altitude seconds
     */
    private void ascending() {
        System.out.println("Ascending " + super.getName() + " at " + this.altitude + " m");
        for (int i = 0; i <= altitude; i += 1000) {
            System.out.println(super.getName() + ": " + i + " m ^");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method should set the current thread to sleep for 10 * altitude seconds
     * Descending will be done in 1.0000 meters decrements
     */
    private void descending() {
        System.out.println(super.getName() + ": descending from " + this.altitude + " m");
        int maxAltitudeDescending = 10000;
        while (this.altitude > 0) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.altitude -= 1000;
            maxAltitudeDescending -= 1000;
            if (maxAltitudeDescending <= 0) {
                break;
            }
            if (this.altitude < 0) {
                this.altitude = 0;
            }
            System.out.println(super.getName() + ": " + altitude + " m v");
        }
        if (this.altitude <= 0) {
            System.out.println(super.getName() + ": successfully landed!");
        } else {
            System.out.println(super.getName() + ": is 10 000 m lower now, but is still cruising!");
            this.state = States.Cruising;
        }
    }

    /**
     * This method should execute the instance of received atc command given by @param: msg
     *
     * @param msg The command to execute
     */
    public void receiveAtcMessage(AtcCommand msg) {
        synchronized (super.getName()) {
            if (msg instanceof TakeoffCommand) {
                if (this.state == States.On_Stand) {
                    this.altitude += msg.getAltitude();
                    new TakeoffCommand(this.altitude).initialize();
                    super.getName().notify();
                } else {
                    System.out.println(super.getName() + ": cannot takeoff because it is not ON STAND!");
                }
            } else if (msg instanceof LandCommand) {
                if (this.state == States.Cruising) {
                    new LandCommand().initialize();
                    super.getName().notify();

                } else {
                    System.out.println(super.getName() + ": cannot land because it's not in cruising state!");
                }
            }
        }

    }

    /**
     * This method should change the state of Aircraft depending on current state
     */
    @Override
    public void run() {
        long currentTime1 = 0;
        long currentTime2;
        while (this.state != States.Landed) {
            switch (this.state) {
                case On_Stand: {
                    System.out.println("Aircraft " + super.getName() + " is on stand!");
                    synchronized (super.getName()) {
                        try {
                            super.getName().wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    this.state = States.Taxing;
                    break;
                }
                case Taxing: {
                    this.taxing();
                    this.state = States.Taking_Off;
                    break;
                }
                case Taking_Off: {
                    System.out.println("Taking off " + super.getName() + " aircraft!");

                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.state = States.Ascending;
                    break;
                }
                case Ascending: {
                    this.ascending();
                    this.state = States.Cruising;
                    break;
                }
                case Cruising: {
                    currentTime1 = System.currentTimeMillis();
                    System.out.println(super.getName() + ": cruising at " + this.altitude + " m!");
                    synchronized (super.getName()) {
                        try {
                            super.getName().wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    this.state = States.Descending;
                    break;
                }
                case Descending: {
                    this.descending();
                    currentTime2 = System.currentTimeMillis();
                    this.cruisingTime += (currentTime2 - currentTime1) / 1000;
                    if (this.altitude <= 0) {
                        this.state = States.Landed;
                        System.out.println(super.getName() + ": cruising time: " + this.cruisingTime + " sec");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Airplane aircraft = (Airplane) o;
        return altitude == aircraft.altitude && cruisingTime == aircraft.cruisingTime && state == aircraft.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), altitude, state, cruisingTime);
    }

    public void startEngine() {
        this.state = States.On_Stand;
        Thread thread = new Thread(this);
        thread.start();
    }
}