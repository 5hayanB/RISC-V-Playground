package pc

import chisel3._


class PC extends Module {
  val io = IO(new Bundle {
    val pc: UInt = Input(UInt(32.W))
  })

  val pc: UInt = RegInit(0.U(32.W))

  pc    := pc + 4.U
  io.pc := pc
}
