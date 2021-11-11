package tomasulogui;

import java.io.*;
import java.util.Scanner;

public class PipelineSimulator {

  private enum Command {
    EXIT, LOAD, RESET, RUN, STEP, SETBREAK, RUNBREAK, SHOWMEM, QUIET, UNUSED} ;

    MemoryModel memory;
    boolean isMemoryLoaded;
    boolean isHalted;

    ProgramCounter pc;
    IssueUnit issue;
    BranchPredictor btb;
    ReorderBuffer reorder;
    RegisterFile regs;
    CDB cdb;
    LoadBuffer loader;
    IntAlu alu;
    IntMult multiplier;
    IntDivide divider;
    BranchUnit branchUnit;

	TomasuloGUIView view;
	
    int breakAddress = -1;
    boolean quietMode = false;
    int instExec = 0;

    public PipelineSimulator() {
      this("");
      try {
        jbInit();
        reset();
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
	
	public PipelineSimulator(TomasuloGUIView view) {
      
      this("");
      this.view = view;
      try {
        jbInit();
        reset();
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }        
    }


    public PipelineSimulator(String fileName) {

      if (fileName != "") {
        memory = new MemoryModel(fileName);
        isMemoryLoaded = true;
      }
      else {
        memory = null;
        isMemoryLoaded = false;
      }
      reset();
    }

    public MemoryModel getMemory() {
      return memory;
    }

    public ProgramCounter getPCStage() {
      return pc;
    }

    public ReorderBuffer getROB() {
      return reorder;
    }

    public BranchPredictor getBTB() {
      return btb;
    }

    public int getPC() {
      return pc.getPC();
    }

    public void setPC(int newPC) {
      pc.setPC(newPC);
    }

    public Object getALU() {
      return alu;
    }

    public Object getMult() {
      return multiplier;
    }

    public Object getDivider() {
      return divider;
    }

    public Object getBranchUnit() {
      return branchUnit;
    }

    public Object getLoader() {
      return loader;
    }

    public CDB getCDB() {
      return cdb;
    }

    public boolean getQuiet() {
      return quietMode;
    }

    private void simulate() {

      Command command = Command.UNUSED;

      while (command != command.EXIT) {
        printMenu();
        command = getCommand();

        switch (command) {
          case EXIT:
            break;
          case LOAD:
            loadMemory();
            break;
          case RESET:
            reset();
            System.out.println("Simulation reset");
            break;
          case RUN:
            run();
            break;
          case STEP:
            step();
            break;
          case SETBREAK:
            setBreak();
            break;
          case RUNBREAK:
            runToBreak();
            break;
          case SHOWMEM:
            showMemory();
            break;
          case QUIET:
            quietMode = !quietMode;
          default:
            command = Command.UNUSED;
        }
      }
      System.out.println("Ya'll come back real soon, ya hear!");

    }

    private void printMenu() {
      System.out.println();
      System.out.println();
      System.out.println();
      System.out.println("What would you like to do?");
      System.out.println("   0. Exit");
      System.out.println("   1. Load");
      if (isMemoryLoaded) {
        System.out.println("   2. Reset");
        System.out.println("   3. Run");
        System.out.println("   4. Single Step");
        System.out.println("   5. Set Breakpoint");
        System.out.println("   6. Run To Breakpoint");
        System.out.println("   7. Show Memory");
        if (quietMode) {
          System.out.println("   8. Go to Verbose Mode");
        }
        else {
          System.out.println("   8. Go to Quiet Mode");
        }
        System.out.println();
        System.out.print("Please enter choice (0-8)  ");
      }
      else {
        System.out.println();
        System.out.print("Please enter choice (0-1)  ");
      }
    }

    private Command getCommand() {
      Command comm = Command.UNUSED;
      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(isr);
      int maxInputValue = 8;
      if (!isMemoryLoaded) {
        maxInputValue = 1;
      }

      try {
        String inString = br.readLine();
        int inChar = Integer.parseInt(inString);
        if (inChar >= 0 && inChar <= maxInputValue) {
//          System.out.println("length = "+Command.values().length+ "   " + Command.values()[3]);
          comm = Command.values()[inChar];
        }
      }

      catch (IOException ioe) {}

      return comm;
    }

    private void reset() {
      pc = new ProgramCounter(this);
      issue = new IssueUnit(this);
      btb = new BranchPredictor(this);
      regs = new RegisterFile(this);
      reorder = new ReorderBuffer(this, regs);
      cdb = new CDB(this);
      loader = new LoadBuffer(this);
      // DMG - commented out since I've deleted from skeleton
      // you will likely want to put these back.
      alu = new IntAlu(this);
      multiplier = new IntMult(this);
      divider = new IntDivide(this);
      branchUnit = new BranchUnit(this);

      pc.setPC(0);
    }

    private void loadMemory() {
      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(isr);

      try {
        System.out.print(
            "Please enter filename to load (do not add .mo suffix) ");
        String fileName = br.readLine();
        fileName = fileName.concat(".mo");

        memory = new MemoryModel(fileName);
        isMemoryLoaded = true;
        reset();
      }
      catch (Exception mioe) {
        isMemoryLoaded = false;
      }
    }

    public void loadMemoryGUI(String fileName) {

      try {


        memory = new MemoryModel(fileName);
        isMemoryLoaded = true;
        //reset();
      }
      catch (Exception mioe) {
        System.out.println("Problem opening file");
        isMemoryLoaded = false;
      }
    }

    private void showMemory() {
      this.getMemory().printObjectCode();
    }

    public void dumpStatus() {

    }

    public void run() {
      //reset();
      
      while (!isHalted) {
        step();
        
      }
      if (quietMode) {
        dumpStatus();
      }
      System.out.println("Total clock cycles executed = " + instExec);
      System.out.println("Total inst retired = " + (reorder.getNumRetirees()+1));
    }

    public void step() {
      isHalted = reorder.retireInst();

      if (!isHalted) {
        if (!quietMode) {
          System.out.println("fetching instruction from address " + pc.getPC());
        }

        updateCDB();

        divider.execCycle(cdb);
        multiplier.execCycle(cdb);
        alu.execCycle(cdb);
        branchUnit.execCycle(cdb);
        loader.execCycle(cdb);

        // this updates PC, so no call from here for that
        issue.execCycle();

        instExec++;
      }

      if (!quietMode) {
        dumpStatus();
      }

    }

    private void setBreak() {
      // this routine asks for a breakpoint, which will be an address in the
      // program (a multiple of 4)
      Scanner myScanner;
      int address = -1;

      boolean goodAddress = false;
      while (!goodAddress) {
        myScanner = new Scanner(System.in);
        System.out.print(
            "Please enter an address for breakpoint (multiple of 4)  ");

        if (myScanner.hasNextInt()) {
          address = myScanner.nextInt();
          if (address % 4 == 0) {
            goodAddress = true;
            breakAddress = address;
          }
          else {
            System.out.println(
                "A breakpoint address must be a multiple of 4");
            System.out.print(
                "Please enter an address for breakpoint (multiple of 4)  ");
          }
        }
        else {
          System.out.println(
              "You must enter an integer address");
          System.out.print(
              "Please enter an address for breakpoint (multiple of 4)  ");
        }

      }
    }

    private void runToBreak() {
      // always step once, to allow you to run when sitting on breakpoint
      step();

      while (!reorder.isHalted() && pc.getPC() != breakAddress) {
        step();
      }
      if (reorder.isHalted()) {
        System.out.println("Program halted prior to hitting breakpoint");
      }
      else if (pc.getPC() == breakAddress) {
        System.out.println("Hit breakpoint at address" + breakAddress);
      }
      else {
        throw new MIPSException("I'm so confused in runToBreak");
      }

    }

    public void squashAllInsts() {
      regs.squashAll();

      loader.squashAll();
//      alu.squashAll();
//      multiplier.squashAll();
//      divider.squashAll();
//      branchUnit.squashAll();
      cdb.squashAll();
    }

    public void updateCDB() {
      // here, we need to poll the functional units and see if they want to
      // writeback.  We pick longest running of those who want to use CDB and
      // notify them they can write
      cdb.setDataValid(false);

      // hint: start with divider, and give it first chance of getting CDB

    }

    public static void main(String[] args) {
      PipelineSimulator sim = new PipelineSimulator();
//    sim.getMemory().printObjectCode();
      sim.simulate();
    }

    private void jbInit() throws Exception {
    }

  }
