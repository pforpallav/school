package arch.y86.machine.seq.student;

import machine.AbstractMainMemory;
import machine.Register;
import machine.RegisterSet;
import arch.y86.machine.AbstractY86CPU;

public class CPU extends AbstractY86CPU.Sequential {
    
    public CPU (String name, AbstractMainMemory memory) {
        super (name, memory);
    }
    
    /**
     * Execute one clock cycle with all stages executing in parallel.
     * @throws InvalidInstructionException                if instruction is invalid (including invalid register number).
     * @throws AbstractMainMemory.InvalidAddressException if instruction attemps an invalid memory access (either instruction or data).
     * @throws MachineHaltException                       if instruction halts the CPU.
     * @throws Register.TimingException
     */
    @Override
    protected void cycle () throws InvalidInstructionException, AbstractMainMemory.InvalidAddressException, MachineHaltException, Register.TimingException, ImplementationException {
        cycleSeq ();
    }
    
    /**
     * The FETCH stage of CPU
     * @throws Register.TimingException
     */
    
    @Override protected void fetch () throws Register.TimingException {
        try {
            
            // get iCd and iFn
            f.iCd.set (mem.read (F.pc.get(),1)[0].value() >>> 4);
            f.iFn.set (mem.read (F.pc.get(),1)[0].value() & 0xf);
            
            // stat MUX
            switch (f.iCd.getValueProduced()) {
                case I_HALT:
                case I_NOP:
                case I_IRMOVL:
                case I_RET:
                case I_PUSHL:
                case I_POPL:
                case I_CALL:
                    switch (f.iFn.getValueProduced()) {
                    case 0x0:
                        f.stat.set (S_AOK);
                        break;
                    default:
                        f.stat.set (S_INS);
                        break;
                }
                break;
                //STUDENT CODE*******************************************
                // Move the I_RMMOVL & I_MRMOVL so that they can accept 4 as a valid iFn
                case I_RMMOVL:
                case I_MRMOVL:
                case I_RRMVXX:
                case I_JXX:
                    switch (f.iFn.getValueProduced()) {
                        case C_NC:
                        case C_LE:
                        case C_L:
                        case C_E:
                        case C_NE:
                        case C_GE:
                        case C_G:
                            f.stat.set (S_AOK);
                            break;
                        default:
                            f.stat.set (S_INS);
                    }
                    break;
                case I_OPL:
            	//STUDENT CODE*******************************************
                // Added I_IOPL to be acceptable instruction for Y86
                case I_IOPL:
                    switch (f.iFn.getValueProduced()) {
                        case A_ADDL:
                        case A_SUBL:
                        case A_ANDL:
                        case A_XORL:
                        case A_MULL:
                        case A_DIVL:
                        case A_MODL:
                            f.stat.set (S_AOK);
                            break;
                        default:
                            f.stat.set (S_INS);
                            break;
                    }
                    break;
                default:
                    f.stat.set (S_INS);
                    break;
            }
            
            if (f.stat.getValueProduced()==S_AOK) {
                
                // rA MUX
                switch (f.iCd.getValueProduced()) {
                    case I_HALT:
                        f.rA.set   (R_NONE);
                        f.stat.set (S_HLT);
                        break;
                    case I_RRMVXX:
                    case I_RMMOVL:
                    case I_MRMOVL:
                	//STUDENT CODE*******************************************
                    // Added I_IOPL to be acceptable instruction for Y86
                    case I_IOPL:
                    case I_OPL:
                    case I_PUSHL:
                    case I_POPL:
                        f.rA.set (mem.read (F.pc.get()+1,1)[0].value() >>> 4);
                        break;
                    default:
                        f.rA.set (R_NONE);
                }
                
                // rB MUX
                switch (f.iCd.getValueProduced()) {
                    case I_RRMVXX:
                    case I_IRMOVL:
                    case I_RMMOVL:
                    case I_MRMOVL:
                    case I_OPL:
                	//STUDENT CODE*******************************************
                    // Added I_IOPL to be acceptable instruction for Y86
                    case I_IOPL:
                        f.rB.set (mem.read (F.pc.get()+1,1)[0].value() & 0xf);
                        break;
                    default:
                        f.rB.set (R_NONE);
                }
                
                // valC MUX
                switch (f.iCd.getValueProduced()) {
                    case I_IRMOVL:
                    case I_RMMOVL:
                    case I_MRMOVL:
                	//STUDENT CODE*******************************************
                    // Added I_IOPL to be acceptable instruction for Y86
                    case I_IOPL:
                        f.valC.set (mem.readIntegerUnaligned (F.pc.get()+2));
                        break;
                    case I_JXX:
                    case I_CALL:
                        f.valC.set (mem.readIntegerUnaligned (F.pc.get()+1));
                        break;
                    default:
                        f.valC.set (0);
                }
                
                // valP MUX
                switch (f.iCd.getValueProduced()) {
                    case I_NOP:
                    case I_HALT:
                    case I_RET:
                        f.valP.set (F.pc.get()+1);
                        break;
                    case I_RRMVXX:
                    case I_OPL:
                    case I_PUSHL:
                    case I_POPL:
                        f.valP.set (F.pc.get()+2);
                        break;
                    case I_JXX:
                    case I_CALL:
                        f.valP.set (F.pc.get()+5);
                        break;
                    case I_IRMOVL:
                    case I_RMMOVL:
                    case I_MRMOVL:
                	//STUDENT CODE*******************************************
                    // Added I_IOPL to be acceptable instruction for Y86
                    case I_IOPL:
                        f.valP.set (F.pc.get()+6);
                        break;
                    default:
                        throw new AssertionError();
                }
            }
        } catch (AbstractMainMemory.InvalidAddressException iae) {
            f.stat.set (S_ADR);
        }
    }
    
    /**
     * The DECODE stage of CPU
     * @throws Register.TimingException
     */
    
    @Override protected void decode () throws Register.TimingException {
        
        // pass-through signals
        d.stat.set (D.stat.get());
        d.iCd.set  (D.iCd.get());
        d.iFn.set  (D.iFn.get());
        d.valC.set (D.valC.get());
        d.valP.set (D.valP.get());
        
        if (D.stat.get() == S_AOK) {
            try {
                
                // srcA MUX
                switch (D.iCd.get()) {
                    case I_RRMVXX:
                    case I_RMMOVL:
                    case I_OPL:
                    case I_PUSHL:
                        d.srcA.set (D.rA.get());
                        break;
                    case I_RET:
                    case I_POPL:
                        d.srcA.set (R_ESP);
                        break;
                    default:
                        d.srcA.set (R_NONE);
                }
                
                // srcB MUX
                switch (D.iCd.get()) {
                    case I_RMMOVL:
                    case I_MRMOVL:
                	//STUDENT CODE*******************************************
                    // Added I_IOPL to be acceptable instruction for Y86
                    case I_IOPL:	
                    case I_OPL:
                        d.srcB.set (D.rB.get());
                        break;
                    case I_CALL:
                    case I_RET:
                    case I_PUSHL:
                    case I_POPL:
                        d.srcB.set (R_ESP);
                        break;
                    default:
                        d.srcB.set (R_NONE);
                }
                
                // dstE MUX
                switch (D.iCd.get()) {
                    case I_RRMVXX:
                    case I_IRMOVL:
                    case I_OPL:
                	//STUDENT CODE*******************************************
                    // Added I_IOPL so dstE can write to register
                    case I_IOPL:	
                        d.dstE.set (D.rB.get());
                        break;
                    case I_CALL:
                    case I_RET:
                    case I_PUSHL:
                    case I_POPL:
                        d.dstE.set (R_ESP);
                        break;
                    default:
                        d.dstE.set (R_NONE);
                }
                
                // dstM MUX
                switch (D.iCd.get()) {
                    case I_MRMOVL:
                    case I_POPL:
                        d.dstM.set (D.rA.get());
                        break;
                    default:
                        d.dstM.set (R_NONE);
                }
                
                try {
                    // read valA from register file
                    if (d.srcA.getValueProduced()!=R_NONE)
                        d.valA.set (reg.get (d.srcA.getValueProduced()));
                    else
                        d.valA.set (0);
                    
                    // read valB from register file
                    if (d.srcB.getValueProduced()!=R_NONE)
                        d.valB.set (reg.get (d.srcB.getValueProduced()));
                    else
                        d.valB.set (0);
                    
                } catch (RegisterSet.InvalidRegisterNumberException irne) {
                    throw new InvalidInstructionException (irne);
                }
                
            } catch (InvalidInstructionException iie) {
                d.stat.set (S_INS);
            }
        }
        
        if (d.stat.getValueProduced()!=S_AOK) {
            d.srcA.set (R_NONE);
            d.srcB.set (R_NONE);
            d.dstE.set (R_NONE);
            d.dstM.set (R_NONE);
        }
    }
    
    /**
     * The EXECUTE stage of CPU
     * @throws Register.TimingException
     */
    
    @Override protected void execute () throws Register.TimingException {
        
        // pass-through signals
        e.stat.set (E.stat.get());
        e.iCd.set  (E.iCd.get());
        e.iFn.set  (E.iFn.get());
        e.valC.set (E.valC.get());
        e.valA.set (E.valA.get());
        e.dstE.set (E.dstE.get());
        e.dstM.set (E.dstM.get());
        e.valP.set (E.valP.get());
        
        if (E.stat.get()==S_AOK) {
            
            // aluA MUX
            int aluA;
            switch (E.iCd.get()) {
                case I_RRMVXX:
                case I_OPL:
                    aluA = E.valA.get();
                    break;
                //STUDENT CODE*******************************************
                //Set aluA from valC for I_IOPL instruction
                case I_IOPL:
                case I_IRMOVL:
                case I_MRMOVL:
                case I_RMMOVL:
                    aluA = E.valC.get();
                    break;
                case I_RET:
                case I_POPL:
                    aluA = 4;
                    break;
                case I_CALL:
                case I_PUSHL:
                    aluA = -4;
                    break;
                default:
                    aluA = 0;
            }
            
            // aluB MUX
            int aluB;
            switch (E.iCd.get()) {
                case I_RRMVXX:
                case I_IRMOVL:
                    aluB = 0;
                    break;
                case I_RMMOVL:
                case I_MRMOVL:
                case I_OPL:
                //STUDENT CODE*******************************************
                //Set aluB from valB for I_IOPL instruction
                case I_IOPL:
                case I_CALL:
                case I_RET:
                case I_PUSHL:
                case I_POPL:
                    aluB = E.valB.get();
                    break;
                default:
                    aluB = 0;
            }
            
            // aluFun and setCC muxes MUX
            int     aluFun;
            boolean setCC;
            switch (E.iCd.get()) {
                case I_RRMVXX:
                case I_IRMOVL:
                case I_CALL:
                case I_RET:
                case I_PUSHL:
                case I_POPL:
                    aluFun = A_ADDL;
                    setCC  = false;
                    break;
                case I_OPL:
            	//STUDENT CODE*******************************************
                //This sets the aluFun to be executed
                case I_IOPL:
                    aluFun = E.iFn.get();
                    setCC  = true;
                    break;
                //STUDENT CODE*******************************************
                //This covers the (valB*ifun) for rmmovl and mrmovl
                case I_RMMOVL:
                case I_MRMOVL:
                	if(E.iFn.get()!=0){
                		aluFun = E.iFn.get();
                    	aluB *= aluFun;
                	}
                	aluFun = A_ADDL;
                	setCC  = false;
                	break;
                default:
                    aluFun = 0;
                    setCC  = false;
            }
            
            // the ALU
            boolean overflow;
            switch (aluFun) {
                case A_ADDL:
                    e.valE.set (aluB + aluA);
                    overflow = ((aluB < 0) == (aluA < 0)) && ((e.valE.getValueProduced() < 0) != (aluB < 0));
                    break;
                case A_SUBL:
                    e.valE.set (aluB - aluA);
                    overflow = ((aluB < 0) != (aluA < 0)) && ((e.valE.getValueProduced() < 0) != (aluB < 0));
                    break;
                case A_ANDL:
                    e.valE.set (aluB & aluA);
                    overflow = false;
                    break;
                case A_XORL:
                    e.valE.set (aluB ^ aluA);
                    overflow = false;
                    break;
                case A_MULL:
                    int result = aluB * aluA;
                    e.valE.set (result);
                    overflow = aluB != 0 && result / aluB != aluA;
                    break;
                case A_DIVL:
                    e.valE.set (aluA == 0 ? aluB : aluB / aluA);
                    overflow = aluA == 0;
                    break;
                case A_MODL:
                    e.valE.set (aluA == 0 ? aluB : aluB % aluA);
                    overflow = aluA == 0;
                    break;
                default:
                    overflow = false;
            }
            
            // CC MUX
            if (setCC)
                p.cc.set (((e.valE.getValueProduced() == 0)? 0x100 : 0) | ((e.valE.getValueProduced() < 0)? 0x10 : 0) | (overflow? 0x1 : 0));
            else
                p.cc.set (P.cc.get());
            
            // cnd MUX
            boolean cnd;
            switch (E.iCd.get()) {
                case I_JXX:
                case I_RRMVXX:
                    boolean zf = (P.cc.get() & 0x100) != 0;
                    boolean sf = (P.cc.get() & 0x010) != 0;
                    boolean of = (P.cc.get() & 0x001) != 0;
                    switch (E.iFn.get()) {
                        case C_NC:
                            cnd = true;
                            break;
                        case C_LE:
                            cnd = (sf ^ of) | zf;
                            break;
                        case C_L:
                            cnd = sf ^ of;
                            break;
                        case C_E:
                            cnd = zf;
                            break;
                        case C_NE:
                            cnd = ! zf;
                            break;
                        case C_GE:
                            cnd = ! (sf ^ of);
                            break;
                        case C_G:
                            cnd = ! (sf ^ of) & ! zf;
                            break;
                        default:
                            throw new AssertionError();
                    }
                    break;
                default:
                    cnd = true;
            }
            e.cnd.set (cnd? 1 : 0);
            
        } else
            e.cnd.set (0);
    }
    
    /**
     * The MEMORY stage of CPU
     * @throws Register.TimingException
     */
    
    @Override protected void memory () throws Register.TimingException {
        
        // pass-through signals
        m.iCd.set  (M.iCd.get());
        m.iFn.set  (M.iFn.get());
        m.cnd.set  (M.cnd.get());
        m.valE.set (M.valE.get());
        m.dstE.set (M.dstE.get());
        m.dstM.set (M.dstM.get());
        m.valP.set (M.valP.get());
        
        if (M.stat.get()==S_AOK) {
            try {
                
                // write Main Memory
                switch (M.iCd.get()) {
                    case I_RMMOVL:
                    case I_PUSHL:
                        mem.writeInteger (M.valE.get(), M.valA.get());
                        break;
                    case I_CALL:
                        mem.writeInteger (M.valE.get(), M.valP.get());
                        break;
                    default:
                }
                
                // valM MUX (read main memory)
                switch (M.iCd.get()) {
                    case I_MRMOVL:
                        m.valM.set (mem.readInteger (M.valE.get()));
                        break;
                    case I_RET:
                    case I_POPL:
                        m.valM.set (mem.readInteger (M.valA.get()));
                        break;
                    default:
                }
                m.stat.set (M.stat.get());
                
            } catch (AbstractMainMemory.InvalidAddressException iae) {
                m.stat.set (S_ADR);
            }
            
        } else {
            m.stat.set (M.stat.get());
        }
    }
    
    /**
     * The WRITE BACK stage of CPU
     * @throws MachineHaltException                       if instruction halts the CPU (e.g., halt instruction).
     * @throws InvalidInstructionException
     * @throws AbstractMainMemory.InvalidAddressException
     * @throws Register.TimingException
     */
    
    @Override protected void writeBack () throws MachineHaltException, InvalidInstructionException, AbstractMainMemory.InvalidAddressException, Register.TimingException {
        if (W.stat.get()==S_AOK)
            try {
                try {
                    
                    // write valE to register file
                    if (W.dstE.get()!=R_NONE && W.cnd.get()==1)
                        reg.set (W.dstE.get(), W.valE.get());
                    
                    // write valM to register file
                    if (W.dstM.get()!=R_NONE)
                        reg.set (W.dstM.get(), W.valM.get());
                    
                    w.stat.set (W.stat.get());
                    
                } catch (RegisterSet.InvalidRegisterNumberException irne) {
                    throw new InvalidInstructionException (irne);
                }
                
            } catch (InvalidInstructionException iie) {
                w.stat.set (S_INS);
            }
        else
            w.stat.set (W.stat.get());
        
        if (w.stat.getValueProduced()==S_ADR)
            throw new AbstractMainMemory.InvalidAddressException();
        else if (w.stat.getValueProduced()==S_INS)
            throw new InvalidInstructionException();
        else if (w.stat.getValueProduced()==S_HLT)
            throw new MachineHaltException();
        
        // Compute newPC
        if (W.stat.get () == S_AOK)
            newPC ();
    }
    
    /**
     * Pseudo-stage to compute the new PC value
     */
    
    private void newPC () {
        switch (E.iCd.get()) {
            case I_CALL:
                w.pc.set (E.valC.get());
                break;
            case I_JXX:
                if (M.cnd.get()==1)
                    w.pc.set (E.valC.get());
                else
                    w.pc.set (E.valP.get());
                break;
            case I_RET:
                w.pc.set (W.valM.get());
                break;
            default:
                w.pc.set (E.valP.get());
        }
    }
}