package tomasulogui;

public class IntDivide extends FunctionalUnit {

    public static final int EXEC_CYCLES = 7;

    public IntDivide(PipelineSimulator sim) {
        super(sim);
    }

    public int calculateResult(int station) {
        int result = 0;
        return result;
    }

    public int getExecCycles() {
        return EXEC_CYCLES;
    }
}
