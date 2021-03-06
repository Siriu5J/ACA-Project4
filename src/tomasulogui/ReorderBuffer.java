package tomasulogui;

import java.util.Arrays;

public class ReorderBuffer {
    public static final int size = 30;
    int frontQ = 0;
    int rearQ = 0;
    ROBEntry[] buff = new ROBEntry[size];
    int numRetirees = 0;

    PipelineSimulator simulator;
    RegisterFile regs;
    boolean halted = false;

    public ReorderBuffer(PipelineSimulator sim, RegisterFile registers) {
        simulator = sim;
        regs = registers;
    }

    public ROBEntry getEntryByTag(int tag) {
        return buff[tag];
    }

    public int getInstPC(int tag) {
        return buff[tag].getInstPC();
    }

    public boolean isHalted() {
        return halted;
    }

    public boolean isFull() {
        return (frontQ == rearQ && buff[frontQ] != null);
    }

    public int getNumRetirees() {
        return numRetirees;
    }

    public boolean retireInst() {
        // 3 cases
        // 1. regular reg dest inst
        // 2. isBranch w/ mispredict
        // 3. isStore
        ROBEntry retiree = buff[frontQ];

        if (retiree == null) {
            return false;
        }

        if (retiree.isHaltOpcode()) {
            halted = true;
            return true;
        }

        boolean shouldAdvance = false;

        // TODO - this is where you look at the type of instruction and
        // figure out how to retire it properly

        // For branch
        if (retiree.isBranch && retiree.branchDestValid) {
            simulator.btb.setBranchAddress(retiree.getInstPC(), retiree.branchDest);
            simulator.btb.setBranchResult(retiree.getInstPC(), (retiree.predictTaken ^ retiree.mispredicted));

            if (retiree.mispredicted) {
                // Always set the offset
                simulator.setPC(retiree.branchDest);
                simulator.squashAllInsts();

                // Squash ROB
                Arrays.fill(buff, null);
                frontQ = 0;
                rearQ = 0;
            } else {
                /*if (retiree.getOpcode() == IssuedInst.INST_TYPE.JAL ||
                retiree.getOpcode() == IssuedInst.INST_TYPE.JALR) {
                    simulator.regs.setReg(31, retiree.getInstPC() + 4);
                }*/

                shouldAdvance = true;
            }
        }
        // For store
        else if (retiree.getOpcode() == IssuedInst.INST_TYPE.STORE &&
        retiree.destAddressRegValueValid &&
        retiree.storeDataValid) {
            simulator.memory.setIntDataAtAddr(retiree.destAddressRegValue + retiree.storeOffset, retiree.storeData);
            shouldAdvance = true;
        }
        // For R-Type or I-Type
        else if ((retiree.getInstType() == IssuedInst.INST_RIJ.RTYPE ||
             retiree.getInstType() == IssuedInst.INST_RIJ.ITYPE) &&
            retiree.isComplete()) {
            if (retiree.shouldWb()) {
                setDataForReg(retiree.getWriteReg(), retiree.getWriteValue());
                if (simulator.regs.getSlotForReg(retiree.writeReg) == retiree.tag)
                    setTagForReg(retiree.getWriteReg(), -1);
                shouldAdvance = true;
            }
        }

        // if mispredict branch, won't do normal advance
        if (shouldAdvance) {
            numRetirees++;
            buff[frontQ] = null;
            frontQ = (frontQ + 1) % size;
        }

        return false;
    }

    public void readCDB(CDB cdb) {
        // check entire ROB for someone waiting on this data
        // could be destination reg
        // could be store address source

        // TODO body of method
        if (cdb.dataValid) {
            for (ROBEntry entry : buff) {
                if (entry != null) {
                    int cdbResult = cdb.getDataValue();
                    if (entry.getTag() == cdb.getDataTag()) {
                        entry.setWriteValue(cdbResult);
                        entry.setDoneExecuting();
                    }

                    // For Store
                    if (entry.getOpcode() == IssuedInst.INST_TYPE.STORE) {
                        if (entry.storeDataTag == cdb.getDataTag() && !entry.storeDataValid) {
                            entry.storeData = cdb.getDataValue();
                            entry.storeDataValid = true;
                        }

                        if (entry.destAddressRegValueTag == cdb.getDataTag() && !entry.destAddressRegValueValid) {
                            entry.destAddressRegValue = cdb.getDataValue();
                            entry.destAddressRegValueValid = true;
                        }
                    }
                }
            }
        }
    }

    public void updateInstForIssue(IssuedInst inst) {
        // the task is to simply annotate the register fields
        // the dest reg will be assigned a tag, which is just our slot#
        // all src regs will either be assigned a tag, read from reg, or forwarded from ROB

        // TODO - possibly nothing if you use my model
        // I use the call to copyInstData below to do 2 things:
        // 1. update the Issued Inst
        // 2. fill in the ROB entry

        // first get a ROB slot
        if (buff[rearQ] != null) {
            throw new MIPSException("updateInstForIssue: no ROB slot avail");
        }
        ROBEntry newEntry = new ROBEntry(this);
        buff[rearQ] = newEntry;
        newEntry.copyInstData(inst, rearQ);
        inst.robSlot = rearQ;

        rearQ = (rearQ + 1) % size;
        System.out.println("Front Q: " + frontQ + "; Rear Q: " + rearQ);
    }

    public boolean checkWAR(int reg, int tag) {
        if (tag != 0) {
            for (ROBEntry entry : buff) {
                if (entry != null && entry.opcode == IssuedInst.INST_TYPE.STORE && (entry.destAddressRegValue + entry.storeOffset == reg)) {
                    // Get the correct slot order so that it doesn't break
                    if (rearQ > frontQ) {
                        return (tag > entry.tag);
                    } else {
                        if (tag < entry.tag && tag <= rearQ)
                            return true;
                        else if (tag > entry.tag && tag >= frontQ && entry.tag >= frontQ)
                            return true;
                        else if (tag > entry.tag && tag <= rearQ)
                            return true;
                        else
                            return false;
                    }
                }
            }
        }

        return false;
    }

    public int getTagForReg(int regNum) {
        return (regs.getSlotForReg(regNum));
    }

    public int getDataForReg(int regNum) {
        return (regs.getReg(regNum));
    }

    public void setDataForReg(int regNum, int regValue) {
        regs.setReg(regNum,regValue);
    }

    public void setTagForReg(int regNum, int tag) {
        regs.setSlotForReg(regNum, tag);
    }

}
