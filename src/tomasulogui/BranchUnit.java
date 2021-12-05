package tomasulogui;

public class BranchUnit
        extends FunctionalUnit {

    public static final int EXEC_CYCLES = 1;
    public boolean mispredictValid = false;
    public boolean mispredict = false;
    public boolean addressValid = false;

    public BranchUnit(PipelineSimulator sim) {
        super(sim);
    }

    public boolean isMispredict(int station) {
        if (stations[station].function == IssuedInst.INST_TYPE.J ||
            stations[station].function == IssuedInst.INST_TYPE.JAL) {
            mispredictValid = true;
            return false;
        }
        if(stations[station].data1Valid &&  stations[station].data2Valid){
            if (stations[station].function == IssuedInst.INST_TYPE.BEQ){
                mispredictValid = true;
                if(stations[station].data1 == stations[station].data2){
                    return (!stations[station].isPredictedTaken());
                }
                else{
                    return (stations[station].isPredictedTaken());
                }
            }
            if (stations[station].function == IssuedInst.INST_TYPE.BNE){
                if(stations[station].data1 != stations[station].data2){
                    return (!stations[station].isPredictedTaken());
                }
                else{
                    return (stations[station].isPredictedTaken());
                }
            }
        }
        if(stations[station].data1Valid){
            if (stations[station].function == IssuedInst.INST_TYPE.BLTZ){
                mispredictValid = true;
                if(stations[station].data1 < 0){
                    return (!stations[station].isPredictedTaken());
                }
                else{
                    return (stations[station].isPredictedTaken());
                }
            }
            if (stations[station].function == IssuedInst.INST_TYPE.BLEZ){
                mispredictValid = true;
                if(stations[station].data1 <= 0){
                    return (!stations[station].isPredictedTaken());
                }
                else{
                    return (stations[station].isPredictedTaken());
                }
            }
            if (stations[station].function == IssuedInst.INST_TYPE.BGTZ){
                mispredictValid = true;
                if(stations[station].data1 > 0){
                    return (!stations[station].isPredictedTaken());
                }
                else{
                    return (stations[station].isPredictedTaken());
                }
            }
            if (stations[station].function == IssuedInst.INST_TYPE.BGEZ){
                mispredictValid = true;
                if(stations[station].data1 >= 0){
                    return (!stations[station].isPredictedTaken());
                }
                else{
                    return (stations[station].isPredictedTaken());
                }
            }
        }
        return false;
    }
    
    public int calculateAddress(int station){
        if (stations[station].function == IssuedInst.INST_TYPE.JR ||
            stations[station].function == IssuedInst.INST_TYPE.JALR) {
            
            if(stations[station].data1Valid){
                addressValid = true;
                mispredict = true;
                mispredictValid = true;
                return stations[station].data1;
            }
            return 0;
        }
        if(stations[station].isPredictedTaken()&&mispredict&&mispredictValid){
            addressValid = true;
            return stations[station].pc;
        }
        else if(mispredictValid){
            addressValid = true;
        }
        return stations[station].pc + stations[station].offset + 4;
    }
    
    public int calculateResult(int station) {
        // todo fill in
        mispredict = isMispredict(station);
        int addr = calculateAddress(station);
        if(mispredictValid && addressValid){
            simulator.reorder.buff[stations[station].robSlot].mispredicted = mispredict;
            simulator.reorder.buff[stations[station].robSlot].branchDest = addr;
            simulator.reorder.buff[stations[station].robSlot].branchDestValid = true;
        }
        //figure out if it is a mispredict
        //calculate the destination address
        //send mispredict and correct address
        return 0;
    }

    public int getExecCycles() {
        return EXEC_CYCLES;
    }
}
