package tomasulogui;

public class ReservationStation {
    PipelineSimulator simulator;

    int tag1;
    int tag2;
    int data1;
    int data2;
    boolean data1Valid = false;
    boolean data2Valid = false;
    // destTag doubles as branch tag
    int destTag;
    int offset;
    IssuedInst.INST_TYPE function = IssuedInst.INST_TYPE.NOP;
    int pc;

    // following just for branches
    int robSlot = -1;
    int addressTag = -1;
    boolean addressValid = false;
    int address = -1;
    boolean predictedTaken = false;

    public ReservationStation(PipelineSimulator sim) {
        simulator = sim;
    }

    public int getDestTag() {
        return destTag;
    }

    public int getData1() {
        return data1;
    }

    public int getData2() {
        return data2;
    }

    public boolean isPredictedTaken() {
        return predictedTaken;
    }

    public IssuedInst.INST_TYPE getFunction() {
        return function;
    }

    public void snoop(CDB cdb) {
        // TODO - add code to snoop on CDB each cycle
        if (!data1Valid) {
            if (cdb.getDataValid() && cdb.getDataTag() == tag1) {
                data1 = cdb.getDataValue();
                data1Valid = true;
            }
        }

        if (!data2Valid) {
            if (cdb.getDataValid() && cdb.getDataTag() == tag2) {
                data2 = cdb.getDataValue();
                data2Valid = true;
            }
        }
    }

    public boolean isReady() {
        if(function == IssuedInst.INST_TYPE.J || function == IssuedInst.INST_TYPE.JAL){
            return true;
        }
        return data1Valid && data2Valid;
    }

    public void loadInst(IssuedInst inst) {
        // TODO add code to insert inst into reservation station
        predictedTaken = inst.branchPrediction;
        destTag = inst.getRegDestTag();
        function = inst.getOpcode();
        pc = inst.getPC();
        offset = inst.immediate;
        // If operand 1 is valid, then we just add it
        if (inst.getRegSrc1Valid()) {
            tag1 = inst.getRegSrc1Tag();
            data1 = inst.getRegSrc1Value();
            data1Valid = true;
        } else {
            tag1 = inst.getRegSrc1Tag();
        }

        // If field 2 is used
        if(inst.regSrc2Used) {
            if (inst.getRegSrc2Valid()) {
                tag2 = inst.getRegSrc2Tag();
                data2 = inst.getRegSrc2Value();
                data2Valid = true;
            } else {
                tag2 = inst.getRegSrc2Tag();
            }
        }
        // Immediates
        else {
            // To identify an immediate, its tag will be -1
            tag2 = -1;
            data2 = inst.getImmediate();
            data2Valid = true;
        }

        // For branch
        robSlot = inst.robSlot;
    }
}
