package tomasulogui;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Assembler {
  private static final int MAX_OFFSET = 20000;
  String fileBase;
  HashMap hash;
  int assemblyOffset = 0;
  static int lineNum = 0;

  public Assembler(String base) {
    fileBase = base;
    hash = new HashMap();
  }

  public void assemble() throws FileNotFoundException, IOException {

    String inFileName = fileBase + ".s";
    String outFileName = fileBase + ".mo";
    String assemblyOutFile = fileBase + ".ss";

    // two passes over input - first gathers labels
    firstPass(inFileName, assemblyOutFile);
    secondPass(inFileName, outFileName);
  }

  private void firstPass(String inFileName, String assemblyOutFile) throws MIPSIOException,
      FileNotFoundException, IOException {
    assemblyOffset = 0;
  //  try {
      Scanner myScanner = new Scanner(new File(inFileName));

      FileWriter fw = new FileWriter(assemblyOutFile);
      BufferedWriter bw = new BufferedWriter(fw);
      PrintWriter fout = new PrintWriter(bw);

      while (myScanner.hasNext(Pattern.compile("--.*"))) {
        fout.println("      " + myScanner.nextLine());
      }

      if (!myScanner.hasNext("Begin")) {
        throw new MIPSIOException(
            "assemble: assembly file missing Begin Assembly");
      }
      myScanner.next();
      if (!myScanner.hasNext("Assembly")) {
        throw new MIPSIOException(
            "assemble: assembly file missing Begin Assembly");
      }
      myScanner.nextLine();

      while (myScanner.hasNext()) {
        String nl = myScanner.nextLine();
        String[] opc = nl.split(" ");

        if (opc[0].length() > 1 && opc[0].substring(0,2).equalsIgnoreCase("--")) {
          fout.println("      " + nl);
        }
        else if (opc[0].equalsIgnoreCase("End")) {
          if (opc[1].equalsIgnoreCase("Assembly")) {
            break;
          }
        }
        else if (opc[0].equalsIgnoreCase("Label")) {
          fout.println(nl);
          String target = opc[1];
          hash.put(target, new Integer(assemblyOffset));
        }
        else {
          fout.println(assemblyOffset + ":  " + nl);
          assemblyOffset += 4;
        }
      }
      myScanner.close();
      fout.close();
 //   }
/*
    catch (IOException ioe) {}
*/
  }

  private void secondPass(String inFileName, String outFileName) throws
      MIPSIOException, FileNotFoundException, IOException {
    assemblyOffset = 0;
    lineNum++;
//    try {
      Scanner myScanner = new Scanner(new File(inFileName));

      FileWriter fw = new FileWriter(outFileName);
      BufferedWriter bw = new BufferedWriter(fw);
      PrintWriter fout = new PrintWriter(bw);

      while (myScanner.hasNext(Pattern.compile("--.*"))) {
        myScanner.nextLine();
        lineNum++;
      }

      if (!myScanner.hasNext("Begin")) {
        throw new MIPSIOException(
            "assemble: assembly file missing Begin Assembly");
      }
      myScanner.next();
      if (!myScanner.hasNext("Assembly")) {
        throw new MIPSIOException(
            "assemble: assembly file missing Begin Assembly");
      }
      myScanner.next();
      lineNum++;

      fout.println("Org 0");

      while (myScanner.hasNext()) {
        String opc = myScanner.next();

        if (opc.length() > 1 && opc.substring(0,2).equalsIgnoreCase("--")) {
          myScanner.nextLine();
          lineNum++;
        }
        else if (opc.equalsIgnoreCase("End")) {
          if (!myScanner.hasNext("Assembly")) {
            throw new MIPSIOException(
                "assemble: assembly file missing End Assembly");
          }
          myScanner.next();
          lineNum++;
          // finished first section
          break;
        }
        else if (opc.equalsIgnoreCase("LABEL")) {
            // ignore on 2nd pass
            if (!myScanner.hasNext()) {
              throw new MIPSIOException(
                  "assemble: assembly file bad label");
            }
              // munch the label tgt
            myScanner.next();
            lineNum++;
        }
        else {
          parseOpcode(myScanner, fout, opc);
          assemblyOffset += 4;
          lineNum++;
        }
      }
        // allow comments between sections
      while (myScanner.hasNext(Pattern.compile("--.*"))) {
        myScanner.nextLine();
      }

      if (!myScanner.hasNext("Begin")) {
        throw new MIPSIOException(
            "assemble: assembly file missing Begin Data");
      }
      myScanner.next();
      if (!myScanner.hasNext("Data")) {
        throw new MIPSIOException(
            "assemble: assembly file missing Begin Data2");
      }
      myScanner.next();
      lineNum++;

      if (!myScanner.hasNextInt()) {
        throw new MIPSIOException(
            "assemble: assembly file missing Begin Data org");
      }
      int org = myScanner.nextInt();
      if (org > MAX_OFFSET) {
        throw new MIPSIOException(
            "assemble: assembly file data org too large");
      }

      if (!myScanner.hasNextInt()) {
       throw new MIPSIOException(
           "assemble: assembly file missing Begin Data offset");
     }
     int offset = myScanner.nextInt();
     if (org + offset > MAX_OFFSET) {
       throw new MIPSIOException(
           "assemble: assembly file data offset too large");
     }

      fout.println("Org " + org + " " + offset);

      while (myScanner.hasNext()) {
        lineNum++;
        if (myScanner.hasNextInt()) {
          int myInt = myScanner.nextInt();
          outputInBytes(fout, myInt);
        }
        else if (myScanner.hasNextFloat()) {
          float myFloat = myScanner.nextFloat();
          int myInt = Float.floatToIntBits(myFloat);
          outputInBytes(fout, myInt);
        }
        else {
          String end = myScanner.next();
          if (end.equalsIgnoreCase("End")) {
            if (!myScanner.hasNext("Data")) {
              throw new MIPSIOException(
                  "assemble: assembly file missing End Data");
            }
            myScanner.next();
            // finished first section
            break;
          }
        }
      }
      while (myScanner.hasNext(Pattern.compile("--.*"))) {
        myScanner.nextLine();
      }
      // allow 2nd data section
      if (myScanner.hasNext("Begin")) {

        myScanner.next();
        if (!myScanner.hasNext("Data")) {
          throw new MIPSIOException(
              "assemble: assembly file missing Begin Data3");
        }
        myScanner.next();
        lineNum++;

        if (!myScanner.hasNextInt()) {
          throw new MIPSIOException(
              "assemble: assembly file missing Begin Data org2");
        }
        org = myScanner.nextInt();
        if (org > MAX_OFFSET) {
          throw new MIPSIOException(
              "assemble: assembly file data org2 too large");
        }

        if (!myScanner.hasNextInt()) {
          throw new MIPSIOException(
              "assemble: assembly file missing Begin Data offset2");
        }
        offset = myScanner.nextInt();
        if (org + offset > MAX_OFFSET) {
          throw new MIPSIOException(
              "assemble: assembly file data offset2 too large");
        }

        fout.println("Org " + org + " " + offset);

        while (myScanner.hasNext()) {
          lineNum++;
          if (myScanner.hasNextInt()) {
            int myInt = myScanner.nextInt();
            outputInBytes(fout, myInt);
          }
          else if (myScanner.hasNextFloat()) {
            float myFloat = myScanner.nextFloat();
            int myInt = Float.floatToIntBits(myFloat);
            outputInBytes(fout, myInt);
          }
          else {
            String end = myScanner.next();
            if (end.equalsIgnoreCase("End")) {
              if (!myScanner.hasNext("Data")) {
                throw new MIPSIOException(
                    "assemble: assembly file missing End Data2");
              }
              myScanner.next();
              // finished first section
              break;
            }
          }
        }
      }
      myScanner.close();
      fout.close();
 //   }

 //   catch (IOException ioe) {}
  }

  private void outputInBytes(PrintWriter fout, int myInt) {
    // little endian
    fout.println(myInt & 0xFF);
    fout.println( (myInt >> 8) & 0xFF);
    fout.println( (myInt >> 16) & 0xFF);
    fout.println( (myInt >> 24) & 0xFF);
  }

  private void parseOpcode(Scanner myScanner, PrintWriter fout, String name) {
    Instruction inst = Instruction.getInstructionFromName(name);

    if (inst == null) {
      throw new MIPSIOException(
          "Assembler: Bad assembly operation name");
    }

    if (inst instanceof ITypeInst) {
      parseIType(myScanner, fout, inst.getOpcode());
    }
    else if (inst instanceof RTypeInst) {
      parseRType(myScanner, fout, inst.getOpcode());
    }
    else if (inst instanceof JTypeInst) {
      parseJType(myScanner, fout, inst.getOpcode());
    }
    else {
      throw new MIPSIOException(
          "Assembler: Bad instruction type");
    }
  }

  private int parseIntReg(Scanner myScanner) {
    int retReg = -1;

    if (!myScanner.hasNext()) {
      throw new MIPSIOException("Assembler: int reg not found");
    }

    String r = myScanner.next();
    if (r.charAt(0) != 'R' && r.charAt(0) != 'r') {
      throw new MIPSIOException("Assembler: int reg no 'r'");
    }

    String reg = r.substring(1);
    if (reg.charAt(reg.length() - 1) == ',') {
      throw new MIPSIOException("Assembler: comma found in parseIntReg");
    }

    try {
      retReg = Integer.parseInt(reg);
    }
    catch (NumberFormatException nfe) {
      throw new MIPSIOException("Assembler: bad regnum");
    }

    if (retReg < 0 || retReg > 31) {
      throw new MIPSIOException("Assembler: bad regnum2");
    }

    return retReg;
  }

  private int parseIntRegAndComma(Scanner myScanner) {
    int retReg = -1;
    boolean foundComma = false;
    if (!myScanner.hasNext()) {
      throw new MIPSIOException("Assembler: int reg not found");
    }

    String r = myScanner.next();
    if (r.charAt(0) != 'R' && r.charAt(0) != 'r') {
      throw new MIPSIOException("Assembler: int reg no 'r'");
    }

    String reg = r.substring(1);
    if (reg.charAt(reg.length() - 1) == ',') {
      foundComma = true;
      reg = reg.substring(0, reg.length() - 1);
    }
    try {
      retReg = Integer.parseInt(reg);
    }
    catch (NumberFormatException nfe) {
      throw new MIPSIOException("Assembler: bad regnum");
    }

    if (retReg < 0 || retReg > 31) {
      throw new MIPSIOException("Assembler: bad regnum2");
    }

    if (!foundComma) {
      if (!myScanner.hasNext()) {
        throw new MIPSIOException("Assembler: int reg not found");
      }
      String comma = myScanner.next();
      if (!comma.equalsIgnoreCase(",")) {
        throw new MIPSIOException("Assembler: comma not found");
      }
    }

    return retReg;
  }

  private String parseLabel(Scanner myScanner) {
    if (!myScanner.hasNext()) {
      throw new MIPSIOException("Assembler: label not found");
    }
    String label = myScanner.next();
    return label;
  }

  private int parseInt(Scanner myScanner) {
    int retInt = -1;
    if (!myScanner.hasNext()) {
      throw new MIPSIOException("Assembler: no data, need int");
    }
    String retString = myScanner.next();
    try {
      retInt = Integer.parseInt(retString);
    }
    catch (NumberFormatException nfe) {
      throw new MIPSIOException("Assembler: bad int constant");
    }
    return retInt;
  }

  private int parseLoadStore(Scanner myScanner, int opcode, int reg) {
    int oper = -1;

    if (!myScanner.hasNext()) {
      throw new MIPSIOException("Assembler: load/store need int");
    }
    int offset = -1;

    boolean foundReg = false;
    boolean foundCloseParen = false;
    int regNum = -1;

    String s = myScanner.next();
    String str = null;

    int index = s.indexOf("(");
    if (index == -1) {
      try {
        offset = Integer.parseInt(s);
      }
      catch (NumberFormatException nfe) {
        throw new MIPSIOException("Assembler: load/store need int2");
      }
      if (!myScanner.hasNext()) {
        throw new MIPSIOException("Assembler: load/store bracket");
      }
      str = myScanner.next();

    }
    else {
      String offStr = s.substring(0, index);
      try {
        offset = Integer.parseInt(offStr);
      }
      catch (NumberFormatException nfe) {
        throw new MIPSIOException("Assembler: load/store need int2");
      }
      str = s.substring(index);
    }

    if (! (str.charAt(0) == '(')) {
      throw new MIPSIOException("Assembler: load/store no open paren");
    }

    if (str.length() > 1) {
      String str2 = str.substring(1, str.length());
      if (str2.charAt(0) != 'r' && str2.charAt(0) != 'R') {
        throw new MIPSIOException("Assembler: load/store no open reg R");
      }
      foundReg = true;
      if (str2.charAt(str2.length() - 1) == ')') {
        foundCloseParen = true;
        str2 = str2.substring(0, str2.length() - 1);
      }
      // strip off R
      str2 = str2.substring(1);
      try {
        regNum = Integer.parseInt(str2);

      }
      catch (NumberFormatException nfe) {
        throw new MIPSIOException("Assembler: load/store bad regnum");
      }
    }

    if (!foundReg) {
      if (!myScanner.hasNext()) {
        throw new MIPSIOException("Assembler: load/store no regnum");
      }
      String str2 = myScanner.next();
      if (str2.charAt(0) != 'r' && str2.charAt(0) != 'R') {
        throw new MIPSIOException("Assembler: load/store no open reg R");
      }
      foundReg = true;
      if (str2.charAt(str2.length() - 1) == ')') {
        foundCloseParen = true;
        str2 = str2.substring(0, str2.length() - 2);
      }
      // strip off R
      str2 = str2.substring(1);
      try {
        regNum = Integer.parseInt(str2);

      }
      catch (NumberFormatException nfe) {
        throw new MIPSIOException("Assembler: load/store bad regnum");
      }
    }

    if (!foundCloseParen) {
      if (!myScanner.hasNext()) {
        throw new MIPSIOException("Assembler: load/store no regnum");
      }
      String str2 = myScanner.next();
      if (!str2.equalsIgnoreCase(")")) {
        throw new MIPSIOException("Assembler: load/store no close paren");
      }
    }
    oper = opcode << 26 | regNum << 21 | reg << 16 | (offset & 0x0000FFFF);
    return oper;
  }

  private void parseIType(Scanner myScanner, PrintWriter fout, int opcode) {
    int oper;
    // 4 formats:
    // 1.  Load/store  -   LW  R1, 30(R3)
    // 2. Immediate arith   -  ADDI R2, R3, 4
    // 3. Cond branch  -   BEQ R1, R2, label
    // 4. Cond branch zero, reg jumps  BLEZ R1, label

    // all need 1 reg
    int reg = 0;

    int reg1 = -1;
    int offset = 0;
    String offString;
    String tgt;

    switch (opcode) {
      case Instruction.INST_LW:
      case Instruction.INST_SW:
      case Instruction.INST_LWC1:
      case Instruction.INST_SWC1:
        reg = parseIntRegAndComma(myScanner);
        oper = parseLoadStore(myScanner, opcode, reg);
        break;
      case Instruction.INST_ADDI:
      case Instruction.INST_ANDI:
      case Instruction.INST_ORI:
      case Instruction.INST_XORI:
        reg = parseIntRegAndComma(myScanner);
        reg1 = parseIntRegAndComma(myScanner);
        offset = parseInt(myScanner);
        oper = opcode << 26 | reg1 << 21 | reg << 16 | (offset & 0x0000FFFF);
        break;
      case Instruction.INST_BEQ:
      case Instruction.INST_BNE:
        reg = parseIntRegAndComma(myScanner);
        reg1 = parseIntRegAndComma(myScanner);
        tgt = parseLabel(myScanner);
        if (!hash.containsKey(tgt)) {
          throw new MIPSIOException("Assembler: bad Itype target");
        }
        Integer off = (Integer) hash.get(tgt);
        try {
          offset = off.intValue();
          // offset from PC+4
          int branchOffset = offset - (assemblyOffset + 4);
          oper = opcode << 26 | reg << 21 | reg1 << 16 |
              (branchOffset & 0x0000FFFF);
        }
        catch (NumberFormatException nfe) {
          throw new MIPSIOException("Assembler: bad Itype target3");
        }
        break
            ;
      case Instruction.INST_BLTZ:
      case Instruction.INST_BLEZ:
      case Instruction.INST_BGTZ:
      case Instruction.INST_BGEZ:

        reg = parseIntRegAndComma(myScanner);
        tgt = parseLabel(myScanner);
        if (!hash.containsKey(tgt)) {
          throw new MIPSIOException("Assembler: bad Itype target2");
        }
        off = (Integer) hash.get(tgt);
        try {
          offset = off.intValue();
          // offset from PC+4
          int branchOffset = offset - (assemblyOffset + 4);

          oper = opcode << 26 | reg << 21 | (branchOffset & 0x0000FFFF);
        }
        catch (NumberFormatException nfe) {
          throw new MIPSIOException("Assembler: bad Itype target4");
        }

        break
            ;
      case Instruction.INST_JR:
      case Instruction.INST_JALR:
        reg = parseIntReg(myScanner);
        oper = opcode << 26 | reg << 21;
        break;
      default:
        throw new MIPSIOException("Assembler: unexpected Itype opcode");
    }
    outputInBytes(fout, oper);
  }

  private void parseRType(Scanner myScanner, PrintWriter fout, int opcode) {

    int oper;
    // 4 formats:
    // 1.  Load/store  -   LW  R1, 30(R3)
    // 2. Immediate arith   -  ADDI R2, R3, 4
    // 3. Cond branch  -   BEQ R1, R2, label
    // 4. Cond branch zero, reg jumps  BLEZ R1, label

    // all need 1 reg
    int reg = parseIntRegAndComma(myScanner);
    int reg1 = -1;
    int reg2 = -1;
    int shamt = -1;

    switch (opcode) {

      case Instruction.INST_ADD:
      case Instruction.INST_SUB:
      case Instruction.INST_MUL:
      case Instruction.INST_DIV:
      case Instruction.INST_AND:
      case Instruction.INST_OR:
      case Instruction.INST_XOR:
      case Instruction.INST_ADD_S:
      case Instruction.INST_SUB_S:
      case Instruction.INST_MUL_S:
      case Instruction.INST_DIV_S:

        reg1 = parseIntRegAndComma(myScanner);
        reg2 = parseIntReg(myScanner);
        oper = opcode << 26 | reg1 << 21 | reg2 << 16 | reg << 11;
        break;
      case Instruction.INST_SLL:
      case Instruction.INST_SRL:
      case Instruction.INST_SRA:
        reg1 = parseIntRegAndComma(myScanner);
        shamt = parseInt(myScanner);
        oper = opcode << 26 | reg1 << 21 | reg << 11 | shamt << 6;
        break;
      case Instruction.INST_CVT_W_S:
      case Instruction.INST_CVT_S_W:
        reg1 = parseIntReg(myScanner);
        oper = opcode << 26 | reg1 << 21 | reg << 11;
        break;
      case Instruction.INST_C_LT_S:
      case Instruction.INST_C_LE_S:
      case Instruction.INST_C_GT_S:
      case Instruction.INST_C_GE_S:
      case Instruction.INST_C_EQ_S:
      case Instruction.INST_C_NE_S:
        reg1 = parseIntReg(myScanner);
        oper = opcode << 26 | reg << 21 | reg1 << 16;

        break;
      case Instruction.INST_MTC1:
      case Instruction.INST_MFC1:

      case Instruction.INST_BC1T:
      case Instruction.INST_BC1F:
      default:
        throw new MIPSIOException("Assembler: unexpected Rtype opcode");
    }
    outputInBytes(fout, oper);
  }

  private void parseJType(Scanner myScanner, PrintWriter fout, int opcode) {

    if (opcode == Instruction.INST_HALT ||
        opcode == Instruction.INST_NOP) {
      outputInBytes (fout, (opcode << 26));
      return;
    }

    if (!myScanner.hasNext()) {
      throw new MIPSIOException("Assembler: Jtype offset not found");
    }
    String tgt = myScanner.next();
    if (!hash.containsKey(tgt)) {
      throw new MIPSIOException("Assembler: bad Jtype offset");
    }

    Integer off = (Integer) hash.get(tgt);
    try {
      int offset = off.intValue();
      // offset from PC+4
      int branchOffset = offset - (assemblyOffset + 4);
      int oper = opcode << 26 | (branchOffset & 0x03FFFFFF);
      outputInBytes(fout, oper);
    }
    catch (NumberFormatException nfe) {}
  }

  public static void main(String[] args) {

    boolean done = false;
    while (!done) {
      System.out.println();
      System.out.println();
      System.out.print("Enter filename to assemble (do not add .s suffix)  ");

      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(isr);

      try {
        String inString = br.readLine();
        Assembler assembler = new Assembler(inString);
        assembler.assemble();
        done=true;
        System.out.print("Assembly Completed!  Press ENTER to exit   ");
        inString = br.readLine();
      }

      catch (IOException ioe) {
        System.out.println("Error in opening filename");
      }
      catch (MIPSIOException me) {
        throw new MIPSIOException(me + " at line " + lineNum);
      }

    }

  }
}
