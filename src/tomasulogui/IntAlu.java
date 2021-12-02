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
      case ADD:
      case ADDI:
        result = data1 + data2;
        break;
      case ANDI:
      case AND:
        result = data1 & data2;
        break;
      case OR:
      case ORI:
        result = data1 | data2;
        break;
      case XORI:
      case XOR:
        result = data1 ^ data2;
        break;
      case SUB:
        result = data1 - data2;
        break;
      case SLL:
        result = data1 << data2;
        break;
      case SRL:
        result = data1 >>> data2;
        break;
      case SRA:
        result = data1 >> data2;
        break;
    }

    return result;
  }

  public int getExecCycles() {
    return EXEC_CYCLES;
  }
}
