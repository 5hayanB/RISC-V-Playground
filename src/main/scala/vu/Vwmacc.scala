// package vu
// 
// import chisel3._
// 
// 
// // Current configs: LMUL = 1, SEW = 8
// class Vwmacc extends Module {
//   val io = IO(new Bundle {
//     val vs : Vec[UInt] = Input(Vec(2, UInt(128.W)))
//     val rs1: SInt      = Input(SInt(32.W))
//     val mulAddOp: UInt = Input(UInt(3.W))
//     val vsew    : UInt = Input(UInt(3.W))
//     val vlmul   : UInt = Input(UInt(3.W))
//   })
// 
//   val vs_sExtended: Seq[Seq[SInt]] = for (_ <- 0 until 2) yield for (_ <- 0 until 8) yield WireInit(0.S(16.W))
// }
