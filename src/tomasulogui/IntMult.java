package tomasulogui;

public class IntMult extends FunctionalUnit {

    public static final int EXEC_CYCLES = 4;

    public IntMult(PipelineSimulator sim) {
        super(sim);
    }

    public int calculateResult(int station) {
        return stations[station].getData1() * stations[station].getData2();
    }

    public int getExecCycles() {
        return EXEC_CYCLES;
    }
}
