package tomasulogui;

public class IntAlu extends FunctionalUnit{
  public static final int EXEC_CYCLES = 1;

  public IntAlu(PipelineSimulator sim) {
    super(sim);
  }


  public int calculateResult(int station) {
     // just placeholder code
    int result=0;
    return result;
  }

  public int getExecCycles() {
    return EXEC_CYCLES;
  }
}
