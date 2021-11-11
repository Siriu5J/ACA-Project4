package tomasulogui;

public class BranchUnit
        extends FunctionalUnit {

    public static final int EXEC_CYCLES = 1;

    public BranchUnit(PipelineSimulator sim) {
        super(sim);
    }

    public int calculateResult(int station) {
        // todo fill in
        return 0;
    }

    public int getExecCycles() {
        return EXEC_CYCLES;
    }
}
