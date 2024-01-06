package bitpattern

import chisel3._
import chisel3.util._


class BitPattern extends Module {
  //noinspection TypeAnnotation
  val io = IO(new Bundle {
    val in: UInt = Input(UInt(4.W))

    val out: Bool = Output(Bool())
  })

  val bit_pat: BitPat = BitPat("b?????????????10")

  io.out := io.in === bit_pat
}
