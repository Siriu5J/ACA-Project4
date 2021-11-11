package tomasulogui;

public class IssuedInst {
  public enum INST_TYPE {
    ADD, ADDI, SUB, MUL, DIV, AND, ANDI, OR, ORI, XOR, XORI, SLL, SRL, SRA,
        LOAD, STORE, HALT,
        NOP, BEQ, BNE, BLTZ, BLEZ, BGEZ, BGTZ, J, JAL, JR, JALR} ;

    INST_TYPE opcode;
    int pc = -1;

    int regDest = -1;
    int regDestTag = -1;
    boolean regDestUsed = false;

    int regSrc1 = -1;
    int regSrc1Value = -1;
    int regSrc1Tag = -1;
    boolean regSrc1Used = false;
    boolean regSrc1Valid = false;

    int regSrc2 = -1;
    int regSrc2Value = -1;
    int regSrc2Tag = -1;
    boolean regSrc2Used = false;
    boolean regSrc2Valid = false;

    int immediate = -1;

    boolean branch = false;
    boolean branchPrediction = false;
    int branchTgt = -1;

    public IssuedInst() {
    }

    public INST_TYPE getOpcode() {
      return opcode;
    }

    public int getRegSrc1() {
      return regSrc1;
    }

    public int getRegSrc2() {
      return regSrc2;
    }

    public int getRegDest() {
      return regDest;
    }

    public int getRegDestTag() {
      return regDestTag;
    }

    public int getPC() {
      return pc;
    }

    public void setPC(int newPC) {
      pc = newPC;
    }

    public boolean isBranch() {
      return branch;
    }

    public void setBranch() {
      branch = true;
    }

    public boolean getBranchPrediction() {
      return branchPrediction;
    }

    public void setBranchPrediction(boolean predict) {
      branchPrediction = predict;
    }

    public int getBranchTgt() {
      return branchTgt;
    }

    public void setBranchTgt(int tgt) {
      branchTgt = tgt;
    }

    public int getImmediate() {
      return immediate;
    }

    public boolean getRegSrc1Valid() {
      return regSrc1Valid;
    }

    public boolean getRegSrc2Valid() {
      return regSrc2Valid;
    }

    public int getRegSrc1Value() {
      return regSrc1Value;
    }

    public int getRegSrc2Value() {
      return regSrc2Value;
    }

    public int getRegSrc1Tag() {
      return regSrc1Tag;
    }

    public int getRegSrc2Tag() {
      return regSrc2Tag;
    }

    public void setRegSrc1Valid() {
      regSrc1Valid = true;
    }

    public void setRegSrc2Valid() {
      regSrc2Valid = true;
    }

    public void setRegSrc1Value(int val) {
      regSrc1Value = val;
    }

    public void setRegSrc2Value(int val) {
      regSrc2Value = val;
    }

    public void setRegSrc1Tag(int tag) {
      regSrc1Tag = tag;
    }

    public void setRegSrc2Tag(int tag) {
      regSrc2Tag = tag;
    }

    public void setRegDestTag(int tag) {
      regDestTag = tag;
    }

    public static IssuedInst createIssuedInst(Instruction inst) {
      IssuedInst issued = new IssuedInst();

      issued.opcode = issued.getOpcode(inst.getOpcode());

      if (inst instanceof ITypeInst) {
        issued.decodeIType( (ITypeInst) inst);
      }
      else if (inst instanceof JTypeInst) {
        issued.decodeJType( (JTypeInst) inst);
      }
      else if (inst instanceof RTypeInst) {
        issued.decodeRType( (RTypeInst) inst);
      }

      return issued;
    }

    public boolean determineIfBranch() {
      if (opcode == INST_TYPE.BEQ ||
          opcode == INST_TYPE.BNE ||
          opcode == INST_TYPE.BLTZ ||
          opcode == INST_TYPE.BLEZ ||
          opcode == INST_TYPE.BGEZ ||
          opcode == INST_TYPE.BGTZ ||
          opcode == INST_TYPE.J ||
          opcode == INST_TYPE.JAL ||
          opcode == INST_TYPE.JR ||
          opcode == INST_TYPE.JALR) {
        return true;
      }
      else {
        return false;
      }

    }

    private INST_TYPE getOpcode(int type) {
      switch (type) {
        case Instruction.INST_NOP:
          return INST_TYPE.NOP;
        case Instruction.INST_HALT:
          return INST_TYPE.HALT;
        case Instruction.INST_ADD:
          return INST_TYPE.ADD;
        case Instruction.INST_ADDI:
          return INST_TYPE.ADDI;
        case Instruction.INST_SUB:
          return INST_TYPE.SUB;
        case Instruction.INST_MUL:
          return INST_TYPE.MUL;
        case Instruction.INST_DIV:
          return INST_TYPE.DIV;
        case Instruction.INST_AND:
          return INST_TYPE.AND;
        case Instruction.INST_ANDI:
          return INST_TYPE.ANDI;
        case Instruction.INST_OR:
          return INST_TYPE.OR;
        case Instruction.INST_ORI:
          return INST_TYPE.ORI;
        case Instruction.INST_XOR:
          return INST_TYPE.XOR;
        case Instruction.INST_XORI:
          return INST_TYPE.XORI;

        case Instruction.INST_SLL:
          return INST_TYPE.SLL;
        case Instruction.INST_SRL:
          return INST_TYPE.SRL;
        case Instruction.INST_SRA:
          return INST_TYPE.SRA;
        case Instruction.INST_LW:
          return INST_TYPE.LOAD;
        case Instruction.INST_SW:
          return INST_TYPE.STORE;
        case Instruction.INST_J:
          return INST_TYPE.J;
        case Instruction.INST_JAL:
          return INST_TYPE.JAL;
        case Instruction.INST_JR:
          return INST_TYPE.JR;
        case Instruction.INST_JALR:
          return INST_TYPE.JALR;
        case Instruction.INST_BEQ:
          return INST_TYPE.BEQ;
        case Instruction.INST_BNE:
          return INST_TYPE.BNE;
        case Instruction.INST_BLTZ:
          return INST_TYPE.BLTZ;
        case Instruction.INST_BLEZ:
          return INST_TYPE.BLEZ;
        case Instruction.INST_BGTZ:
          return INST_TYPE.BGTZ;
        case Instruction.INST_BGEZ:
          return INST_TYPE.BGEZ;
        default:
          throw new MIPSException("IssueInst getOpcode: no matching opcode");
      }
    }

    private void decodeIType(ITypeInst inst) {
      // need to determine if it has a dest/src or 2 src
      if (opcode == INST_TYPE.ADDI ||
          opcode == INST_TYPE.ANDI ||
          opcode == INST_TYPE.ORI ||
          opcode == INST_TYPE.XORI ||
          opcode == INST_TYPE.LOAD) {
        regDestUsed = true;
        regDest = inst.getRT();
        regSrc1Used = true;
        regSrc1 = inst.getRS();
        immediate = (inst.getImmed() << 16) >> 16;
      }
      else {
        regSrc1Used = true;
        regSrc1 = inst.getRS();
        regSrc2Used = true;
        regSrc2 = inst.getRT();
        immediate = (inst.getImmed() << 16) >> 16;
      }

    }

    private void decodeJType(JTypeInst inst) {
      immediate = (inst.getOffset() << 6) >> 6;
    }

    private void decodeRType(RTypeInst inst) {
      regDestUsed = true;
      regDest = inst.getRD();
      regSrc1Used = true;
      regSrc1 = inst.getRS();
	  
	  if(inst.opcode != Instruction.INST_SLL && inst.opcode != Instruction.INST_SRA && inst.opcode != Instruction.INST_SRL)
      {
          regSrc2Used = true;
          regSrc2 = inst.getRT();
      }
      else
      {
        immediate = inst.getShamt();
      }
    }

  }
