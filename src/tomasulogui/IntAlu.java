package tomasulogui;

public class IntAlu extends FunctionalUnit{
  public static final int EXEC_CYCLES = 1;

  public IntAlu(PipelineSimulator sim) {
    super(sim);
  }


  public int calculateResult(int station) {
    int result = 0;
    int data1 = stations[station].getData1();
    int data2 = stations[station].getData2();
    switch (stations[station].getFunction()) {
      case ADD, ADDI -> result = data1 + data2;
      case ANDI, AND -> result = data1 & data2;
      case OR, ORI -> result = data1 | data2;
      case XORI, XOR -> result = data1 ^ data2;
      case SUB -> result = data1 - data2;
      case SLL -> result = data1 << data2;
      case SRL -> result = data1 >>> data2;
      case SRA -> result = data1 >> data2;
    }

    // TODO add branching functionalities

    return result;
  }

  public int getExecCycles() {
    return EXEC_CYCLES;
  }
}
