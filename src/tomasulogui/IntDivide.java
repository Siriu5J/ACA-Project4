package tomasulogui;

public class IntDivide extends FunctionalUnit {

    public static final int EXEC_CYCLES = 7;

    public IntDivide(PipelineSimulator sim) {
        super(sim);
    }

    public int calculateResult(int station) {
        // station should always be zero
        return stations[station].getData1() / stations[station].getData2();
    }

    public int getExecCycles() {
        return EXEC_CYCLES;
    }
}
