package nucleusrv.components.vu

import chisel3._
import chisel3.util._

// Current config: LMUL = 1, SEW = 8
class Vwmul extends Module {
  val io = IO(new Bundle {
    val vs   : Vec[UInt] = Input(Vec(2, UInt(128.W)))
    val vsew : UInt      = Input(UInt(3.W))
    val vlmul: UInt      = Input(UInt(3.W))
    val mulOp: UInt      = Input(UInt(3.W))

    val vd: Vec[Bits] = Output(Vec(2, Bits(128.W)))
  })

  val vs_sExtend: Vec[Vec[SInt]] = Vec(2, Vec(8, Wire(SInt(16.W))))
  val vs_uExtend: Vec[Vec[UInt]] = Vec(2, Vec(8, Wire(UInt(16.W))))

  (127 to 0 by -8).toSeq.map(v => (v, v - 7)).zipWithIndex.map(
    v => for (i <- 0 until 2) {
      vs_sExtend(i)(v._2) := io.vs(i)(v._1._1, v._1._2).asSInt
    }
  )

  for (i <- 0 until 2) {
    io.vd(i) := 0.U
  }
  // extend_vs1(0) := io.vs1(7, 0).asSInt
  // extend_vs1(1) := io.vs1(15, 8).asSInt
  // extend_vs1(2) :=
  
  // for (i <- 0 until 8) {
  //   extend_vs1(i) := io.vs1()
  // }

  // io.vd(0) := MuxLookup(io.vlmul, 0.U, Seq(
  //   1.U -> MuxLookup(io.vsew, 0.U, Seq(
  //     8.U -> (63 to 0 by -8).map(
  //       v => MuxLookup(io.mulOp, 0.U(16.W), Seq(
  //         // 1.U -> (io.vs2(v + 7, v).asSInt * io.vs1(v + 7, v).asSInt)(16.W),  // vwmul.vv
  //         // 2.U -> (io.vs2(v + 7, v).asSInt * io.vs1(7, 0).asSInt)(16.W),      // vwmul.vx
  //         3.U -> io.vs2(v + 7, v) * io.vs1(v + 7, v),                // vwmulu.vv
  //         4.U -> io.vs2(v + 7, v) * io.vs1(7, 0),                          // vwmulu.vx
  //         // 5.U -> (io.vs2(v + 7, v).asSInt * io.vs1(v + 7, v))(16.W),         // vwmulsu.vv
  //         // 6.U -> (io.vs2(v + 7, v).asSInt * io.vs1(7, 0))(16.W)              // vwmulsu.vx
  //       ))
  //     ).reduce(Cat(_, _))
  //   ))
  // ))

  // io.vd(1) := MuxLookup(io.vlmul, 0.U, Seq(
  //   1.U -> MuxLookup(io.vsew, 0.U, Seq(
  //     8.U -> (127 to 64 by -8).map(
  //       v => MuxLookup(io.mulOp, 0.U(16.W), Seq(
  //         // 1.U -> (io.vs2(v + 7, v).asSInt * io.vs1(v + 7, v).asSInt)(16.W),  // vwmul.vv
  //         // 2.U -> (io.vs2(v + 7, v).asSInt * io.vs1(7, 0).asSInt)(16.W),      // vwmul.vx
  //         3.U -> io.vs2(v + 7, v) * io.vs1(v + 7, v),                // vwmulu.vv
  //         4.U -> io.vs2(v + 7, v) * io.vs1(7, 0),                          // vwmulu.vx
  //         // 5.U -> (io.vs2(v + 7, v).asSInt * io.vs1(v + 7, v))(16.W),         // vwmulsu.vv
  //         // 6.U -> (io.vs2(v + 7, v).asSInt * io.vs1(7, 0))(16.W)              // vwmulsu.vx
  //       ))
  //     ).reduce(Cat(_, _))
  //   ))
  // ))
}
