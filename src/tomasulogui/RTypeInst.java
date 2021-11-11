package tomasulogui;

public class RTypeInst
    extends Instruction {

  int opcode;
  int rs;
  int rt;
  int rd;
  int shamt;

  public RTypeInst () {
    super();
    opcode = -1;
  }

//  public RTypeInst(String name) {
//    super(name);
//    opcode = getOpcode(name);
//  }

  public int getOpcode() {
    return opcode;
  }

  public void setOpcode(int opc) {
    opcode = opc;
  }

  public int getRS() {
    return rs;
  }

  public void setRS(int newRS) {
    rs = newRS;
  }

  public int getRT() {
    return rt;
  }

  public void setRT(int newRT) {
    rt = newRT;
  }

  public int getRD() {
    return rd;
  }

  public void setRD(int newRD) {
    rd = newRD;
  }

  public int getShamt() {
    return shamt;
  }

  public void setShamt(int newShamt) {
    shamt = newShamt;
  }

  public static Instruction getRTypeFromOper(int oper, int opcode) {
    RTypeInst newInst = new RTypeInst();
    newInst.opcode = opcode;
    newInst.rs = (oper >> 21) & 0x1f;
    newInst.rt = (oper >> 16) & 0x1f;
    newInst.rd = (oper >> 11) & 0x1f;
    newInst.shamt = (oper >> 6) & 0x1f;

    return (Instruction) newInst;
  }

}
