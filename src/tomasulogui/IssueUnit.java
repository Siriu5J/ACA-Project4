package tomasulogui;

public class IssueUnit {
  private enum EXEC_TYPE {
    NONE, LOAD, ALU, MULT, DIV, BRANCH, STORE} ;

    PipelineSimulator simulator;
    IssuedInst issuee;
    Object fu;
    EXEC_TYPE instType;

    public IssueUnit(PipelineSimulator sim) {
      simulator = sim;
    }
    
    public boolean isLoad(IssuedInst.INST_TYPE opcode){
        return (opcode == IssuedInst.INST_TYPE.LOAD);
    }
    
    public boolean isStore(IssuedInst.INST_TYPE opcode){
        return (opcode == IssuedInst.INST_TYPE.STORE);
    }
    
    public boolean isNone(IssuedInst.INST_TYPE opcode){
        return (opcode == IssuedInst.INST_TYPE.HALT || opcode == IssuedInst.INST_TYPE.NOP);
    }
    
    public boolean isMult(IssuedInst.INST_TYPE opcode){
        return (opcode == IssuedInst.INST_TYPE.MUL);
    }
    
    public boolean isDiv(IssuedInst.INST_TYPE opcode){
        return (opcode == IssuedInst.INST_TYPE.DIV);
    }
    
    public boolean isBranch(IssuedInst.INST_TYPE opcode){
        switch(opcode){
            case BEQ:
            case BNE: 
            case BLTZ: 
            case BLEZ: 
            case BGEZ:
            case BGTZ:
            case J:
            case JAL: 
            case JR: 
            case JALR:
                return true;
            default: 
                return false;
        }
    }
    
    public boolean isAlu(IssuedInst.INST_TYPE opcode){
        switch(opcode){
            case ADD:
            case ADDI: 
            case SUB: 
            case AND: 
            case ANDI:
            case OR:
            case ORI:
            case XOR: 
            case XORI: 
            case SLL:
            case SRL:
            case SRA:
                return true;
            default: 
                return false;
        }
    }
    
    public void createIssuedInst() {
        Instruction i = simulator.memory.getInstAtAddr(simulator.getPC());
        issuee = IssuedInst.createIssuedInst(i);
        IssuedInst.INST_TYPE opcode = issuee.getOpcode();
        if(isLoad(opcode)){
            instType = EXEC_TYPE.LOAD;
        }
        else if(isStore(opcode)){
            instType = EXEC_TYPE.STORE;
        }
        else if(isNone(opcode)){
            instType = EXEC_TYPE.NONE;
        }
        else if(isMult(opcode)){
            instType = EXEC_TYPE.MULT;
        }
        else if(isDiv(opcode)){
            instType = EXEC_TYPE.DIV;
        }
        else if(isBranch(opcode)){
            instType = EXEC_TYPE.BRANCH;
        }
        else if(isAlu(opcode)){
            instType = EXEC_TYPE.ALU;
        }
        issuee.setPC(simulator.getPC());
        simulator.pc.incrPC();
        //Need to parse as much as posssible for this issuedInst
    }

    public void execCycle() {
        createIssuedInst();
        switch(instType){
            case STORE:
                if(!simulator.reorder.isFull()){
                    simulator.reorder.updateInstForIssue(issuee);
                    // TODO: snoop cdb for any needed values
                }
                else{
                    simulator.pc.pc-=4;
                }
                break;
            case LOAD:
                if(simulator.loader.isReservationStationAvail()&&!simulator.reorder.isFull()){
                    simulator.reorder.updateInstForIssue(issuee);
                    //if(simulator.cdb.getDataValid())
                    // snoop cdb for any needed values
                    if (!issuee.getRegSrc1Valid()) {
                        if (simulator.cdb.getDataValid() && simulator.cdb.getDataTag() == issuee.regSrc1Tag) {
                            issuee.regSrc1 = simulator.cdb.getDataValue();
                            issuee.regSrc1Valid = true;
                        }
                    }
                    simulator.loader.acceptIssue(issuee);
                }
                else{
                    simulator.pc.pc-=4;
                }
                break;
            case ALU:
                if(simulator.alu.isReservationStationAvail()&&!simulator.reorder.isFull()){
                    simulator.reorder.updateInstForIssue(issuee);
                    // snoop cdb for any needed values
                    if (!issuee.getRegSrc1Valid()) {
                        if (simulator.cdb.getDataValid() && simulator.cdb.getDataTag() == issuee.regSrc1Tag) {
                            issuee.regSrc1 = simulator.cdb.getDataValue();
                            issuee.regSrc1Valid = true;
                        }
                    }

                    if (!issuee.getRegSrc2Valid()) {
                        if (simulator.cdb.getDataValid() && simulator.cdb.getDataTag() == issuee.regSrc2Tag) {
                            issuee.regSrc2 = simulator.cdb.getDataValue();
                            issuee.regSrc2Valid = true;
                        }
                    }
                    simulator.alu.acceptIssue(issuee);
                }
                else{
                    simulator.pc.pc-=4;
                }
                break;
            case MULT:
                if(simulator.multiplier.isReservationStationAvail()&&!simulator.reorder.isFull()){
                    simulator.reorder.updateInstForIssue(issuee);
                    // snoop cdb for any needed values
                    if (!issuee.getRegSrc1Valid()) {
                        if (simulator.cdb.getDataValid() && simulator.cdb.getDataTag() == issuee.regSrc1Tag) {
                            issuee.regSrc1 = simulator.cdb.getDataValue();
                            issuee.regSrc1Valid = true;
                        }
                    }

                    if (!issuee.getRegSrc2Valid()) {
                        if (simulator.cdb.getDataValid() && simulator.cdb.getDataTag() == issuee.regSrc2Tag) {
                            issuee.regSrc2 = simulator.cdb.getDataValue();
                            issuee.regSrc2Valid = true;
                        }
                    }
                    simulator.multiplier.acceptIssue(issuee);
                }
                else{
                    simulator.pc.pc-=4;
                }
                break;
            case DIV:
                break;
            case BRANCH:
                /*
                BRANCH IS FOR NOT 11/19/21
                */
                break;
            case NONE:
                break;
            default:
                break;
        }
      // an execution cycle involves:
      // 1. checking if ROB and Reservation Station avail
      // 2. issuing to reservation station, if no structural hazard

      // to issue, we make an IssuedInst, filling in what we know
      // We check the BTB, and put prediction if branch, updating PC
      //     if pred taken, incr PC otherwise
      // We then send this to the ROB, which fills in the data fields
      // We then check the CDB, and see if it is broadcasting data we need,
      //    so that we can forward during issue

      // We then send this to the FU, who stores in reservation station
    }

  }
