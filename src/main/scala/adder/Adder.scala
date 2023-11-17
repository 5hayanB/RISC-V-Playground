package adder

import chisel3._


class Adder extends Module {
  val io = IO(new Bundle {
    val in: Vec[UInt] = Input(Vec(2, UInt(32.W)))

    val out: UInt = Output(UInt(32.W))
  })

  io.out := io.in.reduce(_ + _)
}
