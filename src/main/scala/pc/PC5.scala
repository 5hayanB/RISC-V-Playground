package pc

import chisel3._
import circt.stage.ChiselStage


class PCIO5 extends Bundle {
    val imm: SInt = Input(SInt(32.W))
    val rs1: SInt = Input(SInt(32.W))
    val npc_en: Bool = Input(Bool())

    val pc: UInt = Output(UInt(32.W))
}


class PC5 extends Module {
    val io: PCIO5 = IO(new PCIO5)

    // Program Counter
    val pc: UInt = RegInit(0.U(32.W))


    /********************
     * Interconnections *
     ********************/

    pc    := Mux(io.npc_en, (io.rs1 + io.imm).asUInt, pc + 4.U)
    io.pc := pc
}


object VerilogMain5 extends App {
    ChiselStage.emitSystemVerilogFile(new PC5)
}
