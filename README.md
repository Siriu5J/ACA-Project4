# ACA-Project4
Advanced Computer Architecture HW4

### Functional Unit
- We will maintain the stations[] like a queue where we treat stations[0] as the top of the queue where we would grab the instruction into each functional unit. When stations[0] is consumed, stations[1] moves to stations[0].
- We need to keep track of the current execution cycle count (i.e., how many cycles has the executing cycle been running).
- acceptIssue() simply writes the IssuedInst into stations[] in a queue fashion.
- We need to create a stationAvailable() method to notify if there are still available reservation stations.

### Branch Unit
- Similar to functional unit, but we handle the branching result differently.

### Load Buffer
- Has been implemented by Dr. G.
- Change method names to match our naming convention.

### ROBEntry
- We need to know what field we need to know.
- Think through the copyInstData() method.

### Reorder Buffer
- Retire instruction.
- Read CDB.
- Update instruction for issue.

### Issue Unit
- Checking if ROB and Reservation Station avail.
- Issuing to reservation station, if no structural hazard.
- Check branch target buffer and CDB (we can snoop); issue when we can.

### CDB
- Has been implemented by Dr. G.

### Pipeline Simulator
- We need to implement the updateCDB() method.
