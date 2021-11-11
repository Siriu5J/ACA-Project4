package tomasulogui;

public class LoadEntry {
  int address;
  int offset;
  boolean addressValid;
  int addrTag;
  int destTag;

  public LoadEntry() {
  }

  public int getAddress() {
    return address;
  }

  public int getDestTag() {
    return destTag;
  }

  public boolean isReady() {
    return addressValid;
  }

  public void snoop(CDB cdb) {
    if (!addressValid) {
      if (cdb.getDataValid() && cdb.getDataTag() == addrTag) {
        address = cdb.getDataValue() + offset;
        addressValid = true;
      }
    }
  }
  public void loadInst(IssuedInst inst) {
    destTag = inst.getRegDestTag();
    if (inst.getRegSrc1Valid()) {
      address = inst.getRegSrc1Value() + inst.getImmediate();
      addressValid = true;
    }
    else {
      addrTag = inst.getRegSrc1Tag();
      offset = inst.getImmediate();
    }
  }
}
