package vecslice

import chisel3._


class VecSlice extends Module {
  val io = IO(new Bundle {
    val in: Vec[SInt] = Input(Vec(2, SInt(32.W)))

    val out: SInt = Output(SInt(32.W))
  })

  io.out := io.in(0)(7, 0).asSInt
}
