package tomasulogui;

import java.util.Scanner;
import java.io.*;

public class MemoryModel {
  static final int MAX_PROGRAM_SIZE = 10000;
  String fileName;
  int[] memory;
  int minCodeAddress;
  int maxCodeAddress;
  int minDataAddress;
  int maxDataAddress;
  int minData2Address;
  int maxData2Address;
  int dataOffset;

  public MemoryModel(String name) {
    fileName = name;
    memory = new int[MAX_PROGRAM_SIZE];
    minCodeAddress = 0;
    maxCodeAddress = 0;
    minDataAddress = 0;
    maxDataAddress = 0;
    minData2Address = 0;
    maxData2Address = 0;
    readObjectFile(fileName);
  }

  public void printObjectCode() {
//    System.out.println("Org 0");
    for (int addr = minCodeAddress; addr <= maxCodeAddress; addr++) {
      System.out.println(addr * 4 + ":  " + memory[addr]);
    }
    System.out.println();
    if (minDataAddress > 0) {
      //     System.out.println("Org " + minDataAddress*4);
      for (int addr = minDataAddress; addr <= maxDataAddress; addr++) {
        System.out.println(addr * 4 + ":  " + memory[addr]);
      }
    }
    if (minData2Address > 0) {
      System.out.println();
      //     System.out.println("Org " + minDataAddress*4);
      for (int addr = minData2Address; addr <= maxData2Address; addr++) {
        System.out.println(addr * 4 + ":  " + memory[addr]);
      }
    }

  }

  private void readObjectFile(String fileName) {
    boolean foundData = false;
    boolean foundData2 = false;
    int currentAddr = 0;

    try {
      Scanner myScanner = new Scanner(new File(fileName));
      String org = myScanner.next();
      if (org.compareTo("Org") != 0) {
        throw new MIPSException("readObjectFile: file doesn't start with Org");
      }

      String zero = myScanner.next();
      if (zero.compareTo("0") != 0) {
        throw new MIPSException("readObjectFile: file doesn't start with Org 0");
      }

      while (myScanner.hasNextInt()) {
        memory[currentAddr++] = readFourBytes(myScanner);
      }
      maxCodeAddress = currentAddr - 1;

      if (myScanner.hasNext()) {
        String dataOrg = myScanner.next();
        if (dataOrg.compareTo("Org") != 0) {
          throw new MIPSException("readObjectFile: Data Org expected");
        }
        if (myScanner.hasNextInt()) {
          int dataAddr = myScanner.nextInt();
          if (dataAddr % 4 != 0) {
            throw new MIPSException("readObjectFile: Data Org not aligned");
          }
          if (!myScanner.hasNextInt()) {
            throw new MIPSException("readObjectFile: Data offset not found");
          }
          else {
            dataOffset = myScanner.nextInt();
            if (dataOffset % 4 != 0) {
              throw new MIPSException("readObjectFile: Data offset not aligned");
            }
            minDataAddress = dataAddr / 4;
            currentAddr = minDataAddress;
            maxDataAddress = minDataAddress + (dataOffset / 4) - 1;
          }
        }
      }
      else {
        throw new MIPSException("readObjectFile: Data Org expected");
      }
      while (myScanner.hasNextInt()) {
        memory[currentAddr++] = readFourBytes(myScanner);
      }
      if (currentAddr - 1 > maxDataAddress) {
        maxDataAddress = currentAddr - 1;
      }


      // first data org done; optional 2nd data org
      if (myScanner.hasNext()) {
        String dataOrg = myScanner.next();
        if (dataOrg.compareTo("Org") != 0) {
          throw new MIPSException("readObjectFile: Data Org2 expected");
        }
        if (myScanner.hasNextInt()) {
          int dataAddr = myScanner.nextInt();
          if (dataAddr % 4 != 0) {
            throw new MIPSException("readObjectFile: Data Org2 not aligned");
          }
          if (!myScanner.hasNextInt()) {
            throw new MIPSException("readObjectFile: Data offset2 not found");
          }
          else {
            int dataOffset = myScanner.nextInt();
            if (dataOffset % 4 != 0) {
              throw new MIPSException(
                  "readObjectFile: Data offset2 not aligned");
            }
            minData2Address = dataAddr / 4;
            currentAddr = minData2Address;
            maxData2Address = minData2Address + (dataOffset / 4) - 1;
          }
        }
        while (myScanner.hasNextInt()) {
          memory[currentAddr++] = readFourBytes(myScanner);
        }
        if (currentAddr - 1 > maxData2Address) {
          maxData2Address = currentAddr - 1;
        }
      }

      if (myScanner.hasNext()) {
        throw new MIPSException("readObjectFile: unexpected content at end of file");
      }

    }
    catch (IOException ioe) {
      throw new MIPSIOException("readObjectFile: unable to open file");
    }
  }

  private int readFourBytes(Scanner myScanner) {
    int i1 = myScanner.nextInt();
    int i2 = 0;
    int i3 = 0;
    int i4 = 0;

    if (myScanner.hasNextInt()) {
      i2 = myScanner.nextInt();
    }
    else {
      throw new MIPSException("readFourBytes: 4 consecutive bytes not found");
    }

    if (myScanner.hasNextInt()) {
      i3 = myScanner.nextInt();
    }
    else {
      throw new MIPSException("readFourBytes: 4 consecutive bytes not found");
    }
    if (myScanner.hasNextInt()) {
      i4 = myScanner.nextInt();
    }
    else {
      throw new MIPSException("readFourBytes: 4 consecutive bytes not found");
    }

    int ret = i4 << 24 | i3 << 16 | i2 << 8 | i1;
    return ret;

  }

  public Instruction getInstAtAddr(int addr) {
    if (addr % 4 != 0) {
      throw new MIPSException("getInstAtAddr: non-aligned addr");
    }
    int oper = memory[addr / 4];
    return Instruction.getInstructionFromOper(oper);
  }

  public int getIntDataAtAddr(int addr) {
    if (addr % 4 != 0) {
      throw new MIPSException("getIntDataAtAddr: non-aligned addr");
    }
    int data = memory[addr / 4];
    return data;
  }

  public void setIntDataAtAddr(int addr, int data) {
    if (addr % 4 != 0) {
      throw new MIPSException("setIntDataAtAddr: non-aligned addr");
    }
    memory[addr / 4] = data;

  }

  public float getFloatDataAtAddr(int addr) {
    if (addr % 4 != 0) {
      throw new MIPSException("getFloatDataAtAddr: non-aligned addr");
    }
    float data = Float.intBitsToFloat(memory[addr / 4]);
    return data;
  }

  public void setFloatDataAtAddr(int addr, float data) {
    if (addr % 4 != 0) {
      throw new MIPSException("setFloatDataAtAddr: non-aligned addr");
    }
    memory[addr / 4] = Float.floatToIntBits(data);
  }

  public int getDataSize(){
      return dataOffset;
  }
}
