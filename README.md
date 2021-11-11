# ACA-Project4
Advanced Computer Architecture HW4

## Functional Unit
- We will maintain the stations[] like a queue where we treat stations[0] as the top of the queue where we would grab the instruction into each functional unit. When stations[0] is consumed, stations[1] moves to stations[0].
- We need to keep track of the current execution cycle count (i.e., how many cycles has the executing cycle been running).
- acceptIssue() simply writes the IssuedInst into stations[] in a queue fashion.
- We need to create a stationAvailable() method to notify if there are still available reservation stations.

## Load Buffer
- Has been implemented by Dr. G.
- Change method names to match our naming convention.

