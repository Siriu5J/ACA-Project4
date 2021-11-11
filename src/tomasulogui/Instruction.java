package tomasulogui;

public class Instruction {
  String name = "";
  int opcode = 0;

  public static final int INST_LW = 0;
  public static final int INST_SW = 1;
  public static final int INST_LWC1 = 2;
  public static final int INST_SWC1 = 3;
  public static final int INST_ADD = 4;
  public static final int INST_ADDI = 5;
  public static final int INST_SUB = 6;
  public static final int INST_MUL = 7;
  public static final int INST_DIV = 8;
  public static final int INST_AND = 9;
  public static final int INST_ANDI = 10;
  public static final int INST_OR = 11;
  public static final int INST_ORI = 12;
  public static final int INST_XOR = 13;
  public static final int INST_XORI = 14;
  public static final int INST_SLL = 15;
  public static final int INST_SRL = 16;
  public static final int INST_SRA = 17;
  public static final int INST_ADD_S = 18;
  public static final int INST_SUB_S = 19;
  public static final int INST_MUL_S = 20;
  public static final int INST_DIV_S = 21;
  public static final int INST_CVT_W_S = 22;
  public static final int INST_CVT_S_W = 23;
  public static final int INST_C_LT_S = 24;
  public static final int INST_C_LE_S = 25;
  public static final int INST_C_GT_S = 26;
  public static final int INST_C_GE_S = 27;
  public static final int INST_C_EQ_S = 28;
  public static final int INST_C_NE_S = 29;
//  public static final int INST_BEQ = 30;
//  public static final int INST_BNE = 31;
  public static final int INST_BEQ = 32;
  public static final int INST_BNE = 33;
  public static final int INST_BLTZ = 34;
  public static final int INST_BLEZ = 35;
  public static final int INST_BGTZ = 36;
  public static final int INST_BGEZ = 37;
  public static final int INST_BC1T = 38;
  public static final int INST_BC1F = 39;
  public static final int INST_J = 40;
  public static final int INST_JR = 41;
  public static final int INST_JAL = 42;
  public static final int INST_JALR = 43;
  public static final int INST_MTC1 = 44;
  public static final int INST_MFC1 = 45;

  public static final int INST_NOP = 62;
  public static final int INST_HALT = 63;

  public Instruction() {

  }

    public static String getNameFromOpcode(int opcode)
  {
      switch(opcode)
      {
          case 0:
              return "LW";
          case 1:
              return "SW";
          case 2:
              return "LWC1";
          case 3:
              return "SWC1";
          case 4:
              return "ADD";
          case 5:
              return "ADDI";
          case 6:
              return "SUB";
          case 7:
              return "MUL";
          case 8:
              return "DIV";
          case 9:
              return "AND";
          case 10:
              return "ANDI";
          case 11:
              return "OR";
          case 12:
              return "ORI";
          case 13:
              return "XOR";
          case 14:
              return "XORI";
          case 15:
              return "SLL";
          case 16:
              return "SRL";
          case 17:
              return "SRA";
          case 18:
              return "ADD.S";
          case 19:
              return "SUB.S";
          case 20:
              return "MUL.S";
          case 21:
              return "DIV.S";
          case 22:
              return "CVT.W.S";
          case 23:
              return "CVT.S.W";
          case 24:
              return "C.LT.S";
          case 25:
              return "C.LE.S";
          case 26:
              return "C.GT.S";
          case 27:
              return "C.GE.S";
          case 28:
              return "C.EQ.S";
          case 29:
              return "C.NE.S";
          case 30:
              return "BEQ";
          case 31:
              return "BNE";
          case 32:
              return "BEQ";
          case 33:
              return "BNE";
          case 34:
              return "BLTZ";
          case 35:
              return "BLEZ";
          case 36:
              return "BGTZ";
          case 37:
              return "BGEZ";
          case 38:
              return "BC1T";
          case 39:
              return "BC1F";
          case 40:
              return "J";
          case 41:
              return "JR";
          case 42:
              return "JAL";
          case 43:
              return "JALR";
          case 44:
              return "MTC1";
          case 45:
              return "MFC1";
          case 62:
              return "NOP";
          case 63:
              return "HALT";
          default:
              return "NOP";
      }
  }

  public static int getOpcodeFromName(String name) {

    if (name.equalsIgnoreCase("LW")) {
      return INST_LW;
    }
    else if (name.equalsIgnoreCase("SW")) {
      return INST_SW;
    }
    else if (name.equalsIgnoreCase("LWC1")) {
      return INST_LWC1;
    }
    else if (name.equalsIgnoreCase("SWC1")) {
      return INST_SWC1;
    }
    else if (name.equalsIgnoreCase("ADD")) {
      return INST_ADD;
    }
    else if (name.equalsIgnoreCase("ADDI")) {
      return INST_ADDI;
    }
    else if (name.equalsIgnoreCase("SUB")) {
      return INST_SUB;
    }
    else if (name.equalsIgnoreCase("MUL")) {
      return INST_MUL;
    }
    else if (name.equalsIgnoreCase("DIV")) {
      return INST_DIV;
    }
    else if (name.equalsIgnoreCase("AND")) {
      return INST_AND;
    }
    else if (name.equalsIgnoreCase("ANDI")) {
      return INST_ANDI;
    }
    else if (name.equalsIgnoreCase("OR")) {
      return INST_OR;
    }
    else if (name.equalsIgnoreCase("ORI")) {
      return INST_ORI;
    }
    else if (name.equalsIgnoreCase("XOR")) {
      return INST_XOR;
    }
    else if (name.equalsIgnoreCase("XORI")) {
      return INST_XORI;
    }
    else if (name.equalsIgnoreCase("SLL")) {
      return INST_SLL;
    }
    else if (name.equalsIgnoreCase("SRL")) {
      return INST_SRL;
    }
    else if (name.equalsIgnoreCase("SRA")) {
      return INST_SRA;
    }
    else if (name.equalsIgnoreCase("ADD.S")) {
      return INST_ADD_S;
    }
    else if (name.equalsIgnoreCase("SUB.S")) {
      return INST_SUB_S;
    }
    else if (name.equalsIgnoreCase("MUL.S")) {
      return INST_MUL_S;
    }
    else if (name.equalsIgnoreCase("DIV.S")) {
      return INST_DIV_S;
    }
    else if (name.equalsIgnoreCase("CTV.W.S")) {
      return INST_CVT_W_S;
    }
    else if (name.equalsIgnoreCase("CTV.S.W")) {
      return INST_CVT_S_W;
    }
    else if (name.equalsIgnoreCase("LT.S")) {
      return INST_C_LT_S;
    }
    else if (name.equalsIgnoreCase("LE.S")) {
      return INST_C_LE_S;
    }
    else if (name.equalsIgnoreCase("GT.S")) {
      return INST_C_GT_S;
    }
    else if (name.equalsIgnoreCase("GE.S")) {
      return INST_C_GE_S;
    }
    else if (name.equalsIgnoreCase("EQ.S")) {
      return INST_C_EQ_S;
    }
    else if (name.equalsIgnoreCase("NE.S")) {
      return INST_C_NE_S;
    }
    else if (name.equalsIgnoreCase("BEQ")) {
      return INST_BEQ;
    }
    else if (name.equalsIgnoreCase("BNE")) {
      return INST_BNE;
    }
    else if (name.equalsIgnoreCase("BLTZ")) {
      return INST_BLTZ;
    }
    else if (name.equalsIgnoreCase("BLEZ")) {
      return INST_BLEZ;
    }
    else if (name.equalsIgnoreCase("BGTZ")) {
      return INST_BGTZ;
    }
    else if (name.equalsIgnoreCase("BGEZ")) {
      return INST_BGEZ;
    }
    else if (name.equalsIgnoreCase("BC1T")) {
      return INST_BC1T;
    }
    else if (name.equalsIgnoreCase("BC1F")) {
      return INST_BC1F;
    }
    else if (name.equalsIgnoreCase("J")) {
      return INST_J;
    }
    else if (name.equalsIgnoreCase("JR")) {
      return INST_JR;
    }
    else if (name.equalsIgnoreCase("JAL")) {
      return INST_JAL;
    }
    else if (name.equalsIgnoreCase("JALR")) {
      return INST_JALR;
    }
    else if (name.equalsIgnoreCase("MTC1")) {
      return INST_MTC1;
    }
    else if (name.equalsIgnoreCase("MFC1")) {
      return INST_MFC1;
    }

    else if (name.equalsIgnoreCase("NOP")) {
      return INST_NOP;
    }
    else if (name.equalsIgnoreCase("HALT")) {
      return INST_HALT;
    }
    else {
      throw new MIPSException("unexpected operation name");
    }
  }

  public int getOpcode() {
    return opcode;
  }

  public String getName() {
    return name;
  }

  public void setOpcode(int opc) {
    opcode = opc;
  }

  public static Instruction getInstructionFromName(String name) {
    int opcode = getOpcodeFromName(name);
    return (getInstructionFromOper(opcode << 26));
  }

  public static Instruction getInstructionFromOper(int oper) {
    int opcode = oper >>> 26;
    switch (opcode) {
      case INST_JR:
      case INST_JALR:
      case INST_LW:
      case INST_SW:
      case INST_LWC1:
      case INST_SWC1:
      case INST_ADDI:
      case INST_ANDI:
      case INST_ORI:
      case INST_XORI:
      case INST_BEQ:
      case INST_BNE:
      case INST_BLTZ:
      case INST_BLEZ:
      case INST_BGTZ:
      case INST_BGEZ:
        return ITypeInst.getITypeFromOper(oper, opcode);

      case INST_ADD:
      case INST_SUB:
      case INST_MUL:
      case INST_DIV:
      case INST_AND:
      case INST_OR:
      case INST_XOR:

      case INST_SLL:
      case INST_SRL:
      case INST_SRA:
      case INST_ADD_S:
      case INST_SUB_S:
      case INST_MUL_S:
      case INST_DIV_S:
      case INST_CVT_W_S:
      case INST_CVT_S_W:
      case INST_C_LT_S:
      case INST_C_LE_S:
      case INST_C_GT_S:
      case INST_C_GE_S:
      case INST_C_EQ_S:
      case INST_C_NE_S:

      case INST_MTC1:
      case INST_MFC1:

      case INST_BC1T:
      case INST_BC1F:

        return RTypeInst.getRTypeFromOper(oper, opcode);

      case INST_J:
      case INST_JAL:
      case INST_HALT:
      case INST_NOP:
        return JTypeInst.getJTypeFromOper(oper, opcode);

      default:
        throw new MIPSException("GetInstructionFromOper: unexpected opcode");
    }
  }
}
