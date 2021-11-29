package tomasulogui;

import java.util.Arrays;

public abstract class FunctionalUnit {
    PipelineSimulator simulator;
    ReservationStation[] stations = new ReservationStation[2];

    // Custom Fields
    int currentCycle = 1;
    int writeData = -1;
    int writeTag = -1;
    boolean requestWriteback = false;
    boolean canWriteback = false;

    public FunctionalUnit(PipelineSimulator sim) {
        simulator = sim;

    }


    public void squashAll() {
        // todo fill in
        Arrays.fill(stations, null);
    }

    public abstract int calculateResult(int station);

    public abstract int getExecCycles();

    private boolean isExecuting() {
        if (currentCycle == getExecCycles()) {
            return false;
        } else {
            return true;
        }
    }

    public void execCycle(CDB cdb) {
        //todo - start executing, ask for CDB, etc.

        // Snoop
        if (cdb.getDataValid()) {
            for (ReservationStation station : stations) {
                if (station != null) {
                    station.snoop(cdb);
                }
            }
        }

        // Clear the reservation station if written back
        if (canWriteback) {
            // Move all items up the queue
            for (int i = 1; i < stations.length; i++) {
                stations[i - 1] = stations[i];
                stations[i] = null;
            }

            requestWriteback = false;
            // Reset count if executed successfully
            currentCycle = 1;
        }

        // Execute if not stuck
        if (!requestWriteback) {
            // If we are still "executing", we just increment counter
            if (stations[0] != null && stations[0].isReady()) {
                if (isExecuting()) {
                    // Update status in ROB
                    simulator.reorder.getEntryByTag(stations[0].getDestTag()).setExecuting(true);

                    currentCycle++;
                }
                // If we are done waiting, we need to ask our child class to calculate result
                else {
                    // Remember, a queue only reads from the top
                    writeData = calculateResult(0);
                    writeTag = stations[0].destTag;
                    requestWriteback = true;
                }
            }

            canWriteback = false;
        }
    }

    public void acceptIssue(IssuedInst inst) {
        // This is what it will look like if we maintain the reservation stations like a queue
        // Find the first empty spot
        int slot = 0;
        for (slot = 0; slot < stations.length; slot++) {
            if (stations[slot] == null) {
                break;
            }
        }

        // Maybe a station is full
        if (slot == stations.length) {
            throw new MIPSException("Loader accept issue: slot not available");
        }

        ReservationStation station = new ReservationStation(simulator);
        stations[slot] = station;

        station.loadInst(inst);
    }

    public boolean isReservationStationAvail() {
        return stations[stations.length - 1] == null;
    }

    public boolean isRequestWriteback() {
        return requestWriteback;
    }

    public int getResult() {
        return writeData;
    }

    public int getDestTag() {
        return writeTag;
    }

    public void setCanWriteback() {
        canWriteback = true;
    }
}
