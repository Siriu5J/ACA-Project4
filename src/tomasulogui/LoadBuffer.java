package tomasulogui;

public class LoadBuffer {
  private static final int BUFFER_SIZE = 10;
  PipelineSimulator simulator;

  LoadEntry[] buff = new LoadEntry[BUFFER_SIZE];
//  boolean[] used = new boolean[BUFFER_SIZE];

  boolean requestWriteback = false;
  boolean canWriteback = false;
  int writebackEntry = -1;
  int writeTag = -1;
  int writeData = -1;
  boolean loadExecuting = false;

  public LoadBuffer(PipelineSimulator sim) {
    simulator = sim;

  }

  public boolean isRequestingWriteback() {
    return requestWriteback;
  }

  public void setCanWriteback() {
    canWriteback = true;
  }

  public int getWriteTag() {
    return writeTag;
  }

  public int getWriteData() {
    return writeData;
  }

  public void squashAll() {
    for (int i = 0; i < BUFFER_SIZE; i++) {
      buff[i] = null;
    }
    loadExecuting = false;
    writebackEntry = -1;
    requestWriteback = false;
  }

  public void execCycle(CDB cdb) {
    // first check if a reservation station was freed by writeback
    if (canWriteback) {
      buff[writebackEntry] = null;
      writebackEntry = -1;
      requestWriteback = false;
      loadExecuting = false;
    }
    // only execute if not stuck
    if (!requestWriteback) {
      if (loadExecuting) {
        // we are finished execution

        requestWriteback = true;
        int address = buff[writebackEntry].getAddress();
        writeData = simulator.getMemory().getIntDataAtAddr(address);
      }
      // aren't executing inst
      // because Branches might fall to here after completing (since don't
      // writeback), we can't make this an else-if
      else {
        for (int i = 0; i < BUFFER_SIZE; i++) {
          LoadEntry entry = buff[i];
          if (entry != null) {
            if (entry.isReady()) {
              writebackEntry = i;
              writeTag = entry.getDestTag();
              loadExecuting = true;

              break;
            }
          }
        }
      }

    }
    // check reservationStations for cdb data
    if (cdb.getDataValid()) {
      for (int i = 0; i < BUFFER_SIZE; i++) {
        if (buff[i] != null) {
          buff[i].snoop(cdb);
        }
      }
    }

    canWriteback = false;
  }

  public boolean isReservationStationAvail() {
    for (int i=0; i < BUFFER_SIZE; i++) {
      if (buff[i] == null) {
        return true;
      }
    }
    return false;
  }

  public void acceptIssue(IssuedInst inst) {
    int slot=0;
    for (slot=0; slot < BUFFER_SIZE; slot++) {
     if (buff[slot] == null) {
       break;
     }
   }
   if (slot == BUFFER_SIZE) {
     throw new MIPSException("Loader accept issue: slot not available");
   }

   LoadEntry entry = new LoadEntry();
   buff[slot] = entry;

   entry.loadInst (inst);
  }
}
