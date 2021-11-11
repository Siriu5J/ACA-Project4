package tomasulogui;

public class JTypeInst extends Instruction {
  int opcode;
  int offset;

  public JTypeInst () {
    super();
    opcode = -1;
  }

//  public JTypeInst (String name) {
//    super(name);
//    opcode = getOpcode(name);
//  }

  public int getOpcode() {
    return opcode;
  }

  public int getOffset() {
    return offset;
  }

  public void setOffset(int newOff) {
    offset = newOff;
  }

  public static Instruction getJTypeFromOper(int oper, int opcode) {
    JTypeInst newInst = new JTypeInst();
    newInst.opcode = opcode;
    newInst.offset = oper & 0x03ffffff;
	newInst.offset = (newInst.offset << 6) >> 6;
    return (Instruction) newInst;
  }
}
