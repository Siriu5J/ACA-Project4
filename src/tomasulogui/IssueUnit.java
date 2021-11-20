package tomasulogui;

public class IssueUnit {
  private enum EXEC_TYPE {
    NONE, LOAD, ALU, MULT, DIV, BRANCH} ;

    PipelineSimulator simulator;
    IssuedInst issuee;
    Object fu;
    EXEC_TYPE instType;
    int pc;

    public IssueUnit(PipelineSimulator sim) {
      simulator = sim;
    }
    
    public void createIssuedInst() {
        Instruction i = simulator.memory.getInstAtAddr(pc);
        issuee = IssuedInst.createIssuedInst(i);
        pc+=4;
        //Need to parse as much as posssible for this issuedInst
    }

    public void execCycle() {
        createIssuedInst();
        switch(instType){
            case LOAD:
                if(simulator.loader.isReservationStationAvail()&&!simulator.reorder.isFull()){
                    simulator.reorder.updateInstForIssue(issuee);
                    simulator.loader.acceptIssue(issuee);
                }
                else{
                    pc-=4;
                }
                break;
            case ALU:
                /*
                if(simulator.alu.isReservationStationAvail()&&!simulator.reorder.isFull()){
                    simulator.reorder.updateInstForIssue(issuee);
                    simulator.alu.acceptIssue(issuee);
                }
                */
                break;
            case MULT:
                /*
                if(simulator.mult.isReservationStationAvail()&&!simulator.reorder.isFull()){
                    simulator.reorder.updateInstForIssue(issuee);
                    simulator.mult.acceptIssue(issuee);
                }
                */
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
