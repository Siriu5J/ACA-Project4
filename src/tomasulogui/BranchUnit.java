package tomasulogui;

public class BranchUnit
        extends FunctionalUnit {

    public static final int EXEC_CYCLES = 1;
    public boolean mispredictValid = false;
    public boolean addressValid = false;

    public BranchUnit(PipelineSimulator sim) {
        super(sim);
    }

    public boolean isMispredict(int station) {
        if (stations[station].function == IssuedInst.INST_TYPE.J ||
            stations[station].function == IssuedInst.INST_TYPE.JR ||
            stations[station].function == IssuedInst.INST_TYPE.JAL ||
            stations[station].function == IssuedInst.INST_TYPE.JALR) {
            
            return false;
        }
        if(stations[station].data1Valid &&  stations[station].data2Valid){
            mispredictValid = true;
            if (stations[station].function == IssuedInst.INST_TYPE.BEQ){
                if(stations[station].data1 == stations[station].data2){
                    return (true == stations[station].isPredictedTaken());
                }
                else{
                    return (false == stations[station].isPredictedTaken());
                }
            }
            if (stations[station].function == IssuedInst.INST_TYPE.BNE){
                if(stations[station].data1 != stations[station].data2){
                    return (true == stations[station].isPredictedTaken());
                }
                else{
                    return (false == stations[station].isPredictedTaken());
                }
            }
        }
        if(stations[station].data1Valid){
            if (stations[station].function == IssuedInst.INST_TYPE.BLTZ){
                mispredictValid = true;
                if(stations[station].data1 < stations[station].data2){
                    return (true == stations[station].isPredictedTaken());
                }
                else{
                    return (false == stations[station].isPredictedTaken());
                }
            }
            if (stations[station].function == IssuedInst.INST_TYPE.BLEZ){
                mispredictValid = true;
                if(stations[station].data1 <= stations[station].data2){
                    return (true == stations[station].isPredictedTaken());
                }
                else{
                    return (false == stations[station].isPredictedTaken());
                }
            }
            if (stations[station].function == IssuedInst.INST_TYPE.BGTZ){
                mispredictValid = true;
                if(stations[station].data1 > stations[station].data2){
                    return (true == stations[station].isPredictedTaken());
                }
                else{
                    return (false == stations[station].isPredictedTaken());
                }
            }
            if (stations[station].function == IssuedInst.INST_TYPE.BGEZ){
                mispredictValid = true;
                if(stations[station].data1 >= stations[station].data2){
                    return (true == stations[station].isPredictedTaken());
                }
                else{
                    return (false == stations[station].isPredictedTaken());
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
                return stations[station].data1;
            }
            return 0;
        }
        addressValid = true;
        return stations[station].pc+stations[station].offset;
    }
    
    public int calculateResult(int station) {
        // todo fill in
        boolean mispredict = isMispredict(station);
        int addr = calculateAddress(station);
        if(mispredictValid && addressValid){
            
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
