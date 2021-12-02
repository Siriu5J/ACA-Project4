package tomasulogui;

public class ROBEntry {
    ReorderBuffer rob;

    // TODO - add many more fields into entry
    // I deleted most, and only kept those necessary to compile GUI
    int instPC = -1;
    // Destination register = write Reg
    int writeReg = -1;
    // The value to be written to the write Reg
    int writeValue = -1;
    // This is the tag that the ROB is snooping for on the cdb
    int tag = -1;
    
    // ROBEntry Store fields
    boolean destAddressRegValueValid = false;
    int destAddressRegValue = -1;
    int destAddressRegValueTag = -1;
    int storeOffset = -1;
    boolean storeDataValid = false;
    int storeData = -1;
    int storeDataTag = -1;
    boolean isBranch = false;
    boolean predictTaken = false;
    boolean mispredicted = false;
    boolean branchDestValid = false;
    int branchDest = -1;
    int branchTag = -1;
    int branchOffset = -1;

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

    public int getTag() {
        return tag;
    }

    public void copyInstData(IssuedInst inst, int frontQ) {
        // TODO - This is a long and complicated method, probably the most complex
        // of the project.  It does 2 things:
        // 1. update the instruction, as shown in 2nd line of code above
        

        // Figure out if any of the registers are valid or not
        // Tag the source registers that are not valid
        
        shouldWb = false;
        if(!inst.isBranch() && !(inst.opcode == IssuedInst.INST_TYPE.STORE)){
            shouldWb = true;
        }
        
        // Tag the dest Register
        if(shouldWb){
            inst.setRegDestTag(frontQ);
            rob.setTagForReg(inst.getRegDest(), frontQ);
            tag = frontQ;
        }
        
        // If we need to read the value of regSrc1 from registers
        // If it is not tagged up, I can read it, otherwise use the tag
        // Same deal for regSrc2
        if(inst.regSrc1Used){
            if(rob.getTagForReg(inst.getRegSrc1())==-1){
                inst.setRegSrc1Valid();
                inst.setRegSrc1Value(rob.getDataForReg(inst.getRegSrc1()));
                
                //for store destination
                destAddressRegValue = rob.getDataForReg(inst.getRegSrc1());
                destAddressRegValueValid = true;
            }
            else{
                inst.setRegSrc1Tag(rob.getTagForReg(inst.getRegSrc1()));
                destAddressRegValueTag = rob.getTagForReg(inst.getRegSrc1());
            }
        }
        if(inst.regSrc2Used){
            if(rob.getTagForReg(inst.getRegSrc2())==-1){
                inst.setRegSrc2Valid();
                inst.setRegSrc2Value(rob.getDataForReg(inst.getRegSrc2()));
                
                //for store data
                storeData = rob.getDataForReg(inst.getRegSrc2());
                storeDataValid = true;
            }  
            else{
                inst.setRegSrc2Tag(rob.getTagForReg(inst.getRegSrc2()));
                storeDataTag = rob.getTagForReg(inst.getRegSrc2());
            }
        }
        //POSSIBLE BUG... MAY NEED TO SNOOP CDB FOR STORE FOR THIS CLOCK CYCLE
        // 2. update the fields of the ROBEntry, as shown in the 1st line of code above
        instPC = inst.getPC();
        opcode = inst.getOpcode();
        writeReg = inst.getRegDest();
        instType = inst.getRIJType();
        //store fields
        storeOffset = inst.getImmediate();
        
    }

}
