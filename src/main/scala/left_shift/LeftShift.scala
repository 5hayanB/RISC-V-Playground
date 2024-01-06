package left_shift

import chisel3._,
       chisel3.util._


class LeftShift extends Module {
  val io = IO(new Bundle {
    val in: UInt = Input(UInt(4.W))

    val wmask: UInt = Output(UInt(4.W))
  })

  io.wmask := MuxLookup(6.U, 0.U)(Seq(
    6 -> "0001",
    7 -> "0011",
    8 -> "1111"
  ).map(
    x => x._1.U -> (("b" + x._2).U(4.W) << io.in(1, 0))
  ))
}
