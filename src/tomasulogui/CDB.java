package tomasulogui;

public class CDB {
  PipelineSimulator simulator;
  int dataTag = -1;
  int dataValue = -1;
  boolean dataValid = false;

  public CDB(PipelineSimulator sim) {
    simulator = sim;
  }

  public boolean getDataValid () {
    return dataValid;
  }
  public void setDataValid(boolean valid) {
    dataValid = valid;
  }

  public int getDataTag () {
    return dataTag;
  }
  public int getDataValue() {
    return dataValue;
  }

  public void setDataTag (int tag) {
    dataTag = tag;
  }
  public void setDataValue (int value) {
    dataValue = value;
  }
  public void squashAll() {
    dataValid = false;
  }

}
