package vu

import chisel3._
import chisel3.util._

// Current config: LMUL = 1, SEW = 8
class Vwmul extends Module {
  //noinspection TypeAnnotation
  val io = IO(new Bundle {
    val vs   : Vec[UInt] = Input(Vec(2, UInt(128.W)))
    val vd_in: Vec[UInt] = Input(Vec(2, UInt(128.W)))
    val vsew : UInt      = Input(UInt(3.W))
    val vlmul: UInt      = Input(UInt(3.W))
    val mulOp: UInt      = Input(UInt(3.W))

    val vd_out: Vec[UInt] = Output(Vec(2, UInt(128.W)))
  })

  //noinspection ScalaWeakerAccess
  val vs_sExtend: Seq[Seq[Seq[SInt]]] = (
    for (_ <- 0 until 2) yield (
      for (_ <- 0 until 2) yield (
        for (_ <- 0 until 8) yield dontTouch(WireInit(0.S(16.W)))
      )
    )
  )
  //noinspection ScalaWeakerAccess
  val vs_uExtend: Seq[Seq[Seq[UInt]]] = (
    for (_ <- 0 until 2) yield (
      for (_ <- 0 until 2) yield (
        for (_ <- 0 until 8) yield dontTouch(WireInit(0.U(16.W)))
      )
    )
  )

  for (i <- 0 until 8) {
    vs_sExtend(0)(0)(i) := (io.vs(0)(((i + 1) * 8) - 1, i * 8)).asSInt
    vs_sExtend(0)(1)(i) := (io.vs(0)((((i + 1) * 8) - 1) + 64, (i * 8) + 64)).asSInt
    vs_sExtend(1)(0)(i) := (io.vs(1)(((i + 1) * 8) - 1, i * 8)).asSInt
    vs_sExtend(1)(1)(i) := (io.vs(1)((((i + 1) * 8) - 1) + 64, (i * 8) + 64)).asSInt

    vs_uExtend(0)(0)(i) := io.vs(0)(((i + 1) * 8) - 1, i * 8)
    vs_uExtend(0)(1)(i) := io.vs(0)((((i + 1) * 8) - 1) + 64, (i * 8) + 64)
    vs_uExtend(1)(0)(i) := io.vs(1)(((i + 1) * 8) - 1, i * 8)
    vs_uExtend(1)(1)(i) := io.vs(1)((((i + 1) * 8) - 1) + 64, (i * 8) + 64)
  }

  io.vd_out(0) := MuxLookup(io.vlmul, 0.U)(Seq(
    1.U -> MuxLookup(io.vsew, 0.U)(Seq(
      0.U -> MuxLookup(io.mulOp, 0.U)(Seq(
        1.U -> (
          for (i <- 0 until 8)
            yield (vs_sExtend(1)(0)(i) * vs_sExtend(0)(0)(i))(15, 0)  // vwmul.vv
        ).reverse.reduce((v, w) => Cat(v, w)),
        2.U -> (
          for (i <- 0 until 8)
            yield (vs_sExtend(1)(0)(i) * vs_sExtend(0)(0)(0))(15, 0)  // vwmul.vx
        ).reverse.reduce((v, w) => Cat(v, w)),
        3.U -> (
          for (i <- 0 until 8)
            yield (vs_uExtend(1)(0)(i) * vs_uExtend(0)(0)(i))(15, 0)  // vwmulu.vv
        ).reverse.reduce((v, w) => Cat(v, w)),
        4.U -> (
          for (i <- 0 until 8)
            yield (vs_uExtend(1)(0)(i) * vs_uExtend(0)(0)(0))(15, 0)  // vwmulu.vx
        ).reverse.reduce((v, w) => Cat(v, w)),
        5.U -> (
          for (i <- 0 until 8)
            yield (vs_sExtend(1)(0)(i) * vs_uExtend(0)(0)(i))(15, 0)  // vwmulsu.vv
        ).reverse.reduce((v, w) => Cat(v, w)),
        6.U -> (
          for (i <- 0 until 8)
            yield (vs_sExtend(1)(0)(i) * vs_uExtend(0)(0)(0))(15, 0)  // vwmulsu.vx
        ).reverse.reduce((v, w) => Cat(v, w)),
        7.U -> (
          for (i <- 0 until 8)
            yield ((vs_uExtend(1)(0)(i) * vs_uExtend(0)(0)(i))(15, 0) + io.vd_in(0)(((i + 1) * 8) - 1, i * 8))(15, 0)  // vwmaccu.vv
        ).reverse.reduce((v, w) => Cat(v, w)),
        8.U -> (
          for (i <- 0 until 8)
            yield ((vs_uExtend(1)(0)(i) * vs_uExtend(0)(0)(0))(15, 0) + io.vd_in(0)(((i + 1) * 8) - 1, i * 8))(15, 0)  // vwmaccu.vx
        ).reverse.reduce((v, w) => Cat(v, w)),
        9.U -> (
          for (i <- 0 until 8)
            yield ((vs_sExtend(1)(0)(i) * vs_sExtend(0)(0)(i))(15, 0) + io.vd_in(0)(((i + 1) * 8) - 1, i * 8))(15, 0)  // vwmacc.vv
        ).reverse.reduce((v, w) => Cat(v, w)),
        10.U -> (
          for (i <- 0 until 8)
            yield ((vs_sExtend(1)(0)(i) * vs_sExtend(0)(0)(0))(15, 0) + io.vd_in(0)(((i + 1) * 8) - 1, i * 8))(15, 0)  // vwmacc.vx
        ).reverse.reduce((v, w) => Cat(v, w)),
        11.U -> (
          for (i <- 0 until 8)
            yield ((vs_uExtend(1)(0)(i) * vs_sExtend(0)(0)(i))(15, 0) + io.vd_in(0)(((i + 1) * 8) - 1, i * 8))(15, 0)  // vwmaccsu.vv
        ).reverse.reduce((v, w) => Cat(v, w)),
        12.U -> (
          for (i <- 0 until 8)
            yield ((vs_uExtend(1)(0)(i) * vs_sExtend(0)(0)(0))(15, 0) + io.vd_in(0)(((i + 1) * 8) - 1, i * 8))(15, 0)  // vwmaccu.vx
        ).reverse.reduce((v, w) => Cat(v, w)),
        13.U -> (
          for (i <- 0 until 8)
            yield ((vs_sExtend(1)(0)(i) * vs_uExtend(0)(0)(0))(15, 0) + io.vd_in(0)(((i + 1) * 8) - 1, i * 8))(15, 0)  // vwmaccus.vx
        ).reverse.reduce((v, w) => Cat(v, w)),
      ))
    ))
  ))

  io.vd_out(1) := MuxLookup(io.vlmul, 0.U)(Seq(
    1.U -> MuxLookup(io.vsew, 0.U)(Seq(
      0.U -> MuxLookup(io.mulOp, 0.U)(Seq(
        1.U -> (
          for (i <- 0 until 8)
            yield (vs_sExtend(1)(1)(i) * vs_sExtend(0)(1)(i))(15, 0)  // vwmul.vv
        ).reverse.reduce((v, w) => Cat(v, w)),
        2.U -> (
          for (i <- 0 until 8)
            yield (vs_sExtend(1)(1)(i) * vs_sExtend(0)(0)(0))(15, 0)  // vwmul.vx
        ).reverse.reduce((v, w) => Cat(v, w)),
        3.U -> (
          for (i <- 0 until 8)
            yield (vs_uExtend(1)(1)(i) * vs_uExtend(0)(1)(i))(15, 0)  // vwmulu.vv
        ).reverse.reduce((v, w) => Cat(v, w)),
        4.U -> (
          for (i <- 0 until 8)
            yield (vs_uExtend(1)(1)(i) * vs_uExtend(0)(0)(0))(15, 0)  // vwmulu.vx
        ).reverse.reduce((v, w) => Cat(v, w)),
        5.U -> (
          for (i <- 0 until 8)
            yield (vs_sExtend(1)(1)(i) * vs_uExtend(0)(1)(i))(15, 0)  // vwmulsu.vv
        ).reverse.reduce((v, w) => Cat(v, w)),
        6.U -> (
          for (i <- 0 until 8)
            yield (vs_sExtend(1)(1)(i) * vs_uExtend(0)(0)(0))(15, 0)  // vwmulsu.vx
        ).reverse.reduce((v, w) => Cat(v, w)),
        7.U -> (
          for (i <- 0 until 8)
            yield ((vs_uExtend(1)(1)(i) * vs_uExtend(0)(1)(i))(15, 0) + io.vd_in(1)(((i + 1) * 8) - 1, i * 8))(15, 0)  // vwmaccu.vv
        ).reverse.reduce((v, w) => Cat(v, w)),
        8.U -> (
          for (i <- 0 until 8)
            yield ((vs_uExtend(1)(1)(i) * vs_uExtend(0)(0)(0))(15, 0) + io.vd_in(1)(((i + 1) * 8) - 1, i * 8))(15, 0)  // vwmaccu.vx
        ).reverse.reduce((v, w) => Cat(v, w)),
        9.U -> (
          for (i <- 0 until 8)
            yield ((vs_sExtend(1)(1)(i) * vs_sExtend(0)(1)(i))(15, 0) + io.vd_in(1)(((i + 1) * 8) - 1, i * 8))(15, 0)  // vwmacc.vv
        ).reverse.reduce((v, w) => Cat(v, w)),
        10.U -> (
          for (i <- 0 until 8)
            yield ((vs_sExtend(1)(1)(i) * vs_sExtend(0)(0)(0))(15, 0) + io.vd_in(1)(((i + 1) * 8) - 1, i * 8))(15, 0)  // vwmacc.vx
        ).reverse.reduce((v, w) => Cat(v, w)),
        11.U -> (
          for (i <- 0 until 8)
            yield ((vs_uExtend(1)(1)(i) * vs_sExtend(0)(1)(i))(15, 0) + io.vd_in(1)(((i + 1) * 8) - 1, i * 8))(15, 0)  // vwmaccsu.vv
        ).reverse.reduce((v, w) => Cat(v, w)),
        12.U -> (
          for (i <- 0 until 8)
            yield ((vs_uExtend(1)(1)(i) * vs_sExtend(0)(0)(0))(15, 0) + io.vd_in(1)(((i + 1) * 8) - 1, i * 8))(15, 0)  // vwmaccu.vx
        ).reverse.reduce((v, w) => Cat(v, w)),
        13.U -> (
          for (i <- 0 until 8)
            yield ((vs_sExtend(1)(1)(i) * vs_uExtend(0)(0)(0))(15, 0) + io.vd_in(1)(((i + 1) * 8) - 1, i * 8))(15, 0)  // vwmaccus.vx
        ).reverse.reduce((v, w) => Cat(v, w)),
      ))
    ))
  ))
}
