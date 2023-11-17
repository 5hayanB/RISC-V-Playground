package vu

import chisel3._
import chisel3.util._

// Current config: LMUL = 1, SEW = 8
class Vwmul extends Module {
  //noinspection TypeAnnotation
  val io = IO(new Bundle {
    val vs   : Vec[UInt] = Input(Vec(2, UInt(128.W)))
    val vsew : UInt      = Input(UInt(3.W))
    val vlmul: UInt      = Input(UInt(3.W))
    val mulOp: UInt      = Input(UInt(3.W))

    val vd: Vec[UInt] = Output(Vec(2, UInt(128.W)))
  })

  //noinspection ScalaWeakerAccess
  val vs_sExtend: Seq[Seq[Seq[SInt]]] = for (_ <- 0 until 2)
    yield for (_ <- 0 until 2)
      yield for (_ <- 0 until 8)
        yield dontTouch(WireInit(0.S(16.W)))
  //noinspection ScalaWeakerAccess
  val vs_uExtend: Seq[Seq[Seq[UInt]]] = for (_ <- 0 until 2)
    yield for (_ <- 0 until 2)
      yield for (_ <- 0 until 8)
        yield dontTouch(WireInit(0.U(16.W)))

  for (i <- 0 until 2) {
    for (j <- 0 until 2) {
      for (k <- 0 until 8) {
        //vs_sExtend(i)(j)(k) := io.vs(i)(((k * 8) + 7) + (j * 2), (k * 8) + (j * 64)).asSInt
        vs_sExtend(i)(j)(k) := io.vs(i.U).asSInt
        vs_uExtend(i)(j)(k) := io.vs(i)((((k * 8) + 7) + (j * 2)), ((k * 8) + (j * 64)))
      }
    }
  }

  // for (i <- 0 until 2) {
  //   var idx: Int = 0
  //   for (j <- 63 to 0 by -8) {
  //     vs_sExtend(i)(idx) := io.vs(i)(j + (1 * i), (j + (1 * i)) - 7).asSInt
  //     vs_uExtend(i)(idx) := io.vs(i)(j + (1 * i), (j + (1 * i)) - 7)

  //     idx += 1
  //   }
  // }

  // for (i <- 0 until 2) {
  //   io.vd(i) := MuxLookup(io.vlmul, 0.U)(Seq(
  //     1.U -> MuxLookup(io.vsew, 0.U)(Seq(
  //       0.U -> MuxLookup(io.mulOp, 0.U)(Seq(
  //         1.U -> (for (i <- vs_sExtend.indices) yield (vs_sExtend(1)(i) * vs_sExtend.head(i)).asUInt/*((16 * i) + 15, 16 * i)*/).reduce(
  //           (v, w) => Cat(v, w)
  //         ) // vwmul.vv
  //       ))
  //     ))
  //   ))
  // }

  for (i <- 0 until 2) {
    io.vd(i) := 0.U
  }

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
