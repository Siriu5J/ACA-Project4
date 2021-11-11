package tomasulogui;

public class ITypeInst
    extends Instruction {

  int opcode;
  int rs;
  int rt;
  int immed;

  public ITypeInst () {
    super();
    opcode = -1;
  }

//  public ITypeInst(String name) {
//    super(name);
//    opcode = getOpcode(name);
//  }

  public int getOpcode() {
    return opcode;
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

  public int getImmed() {
    return immed;
  }

  public void setImmed(int newImmed) {
    immed = newImmed;
  }

  public static Instruction getITypeFromOper(int oper, int opcode) {
    ITypeInst newInst = new ITypeInst();
    newInst.opcode = opcode;
    newInst.rs = (oper >> 21) & 0x1f;
    newInst.rt = (oper >> 16) & 0x1f;
    newInst.immed = oper & 0xffff;

    return (Instruction) newInst;
  }
}
