package tomasulogui;

public class ROBEntry {
    ReorderBuffer rob;

    // TODO - add many more fields into entry
    // I deleted most, and only kept those necessary to compile GUI
    boolean predictTaken = false;
    boolean mispredicted = false;
    int instPC = -1;
    int writeReg = -1;
    int writeValue = -1;

    IssuedInst.INST_TYPE opcode;
    IssuedInst.INST_RIJ instType;
    boolean executing = false;
    boolean complete = false;
    boolean shouldWb = false;

    public ROBEntry(ReorderBuffer buffer) {
        rob = buffer;
    }

    public boolean isComplete() {
        return complete;
    }

    public boolean branchMispredicted() {
        return mispredicted;
    }

    public boolean getPredictTaken() {
        return predictTaken;
    }

    public int getInstPC() {
        return instPC;
    }

    public IssuedInst.INST_TYPE getOpcode() {
        return opcode;
    }


    public boolean isHaltOpcode() {
        return (opcode == IssuedInst.INST_TYPE.HALT);
    }

    public void setBranchTaken(boolean result) {
        // TODO - maybe more than simple set
        // For now, we will just predict taken.
        predictTaken = result;
    }

    public int getWriteReg() {
        return writeReg;
    }

    public int getWriteValue() {
        return writeValue;
    }

    public void setWriteValue(int value) {
        writeValue = value;
    }

    public boolean isExecuting() {
        return executing;
    }

    public void setExecuting(boolean executing) {
        this.executing = executing;
    }

    public void setDoneExecuting() {
        this.executing = false;
        this.complete = true;
    }

    public IssuedInst.INST_RIJ getInstType() {
        return instType;
    }

    public boolean shouldWb() {
        return shouldWb;
    }

    public void copyInstData(IssuedInst inst, int frontQ) {
        // TODO - This is a long and complicated method, probably the most complex
        // of the project.  It does 2 things:
        // 1. update the instruction, as shown in 2nd line of code above
        instPC = inst.getPC();
        writeReg = inst.getRegDest();
        instType = inst.getRIJType();
        // 2. update the fields of the ROBEntry, as shown in the 1st line of code above
        inst.setRegDestTag(frontQ);

    }

}
