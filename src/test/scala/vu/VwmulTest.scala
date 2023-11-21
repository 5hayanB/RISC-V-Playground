package vu

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec


class VwmulTest extends AnyFreeSpec with ChiselScalatestTester {
  "Vwmul" in {
    test(new Vwmul()).withAnnotations(Seq(VerilatorBackendAnnotation)) {
      v =>
        v.io.vs(0).poke(s"h${Seq(
          "01", "02", "03", "04", "05", "06", "07", "09", "00", "0A", "0B", "0C", "0D", "0E", "0F", "08"
        ).reduce((w, x) => s"$w$x")}".U)
        v.io.vs(1).poke(s"h${Seq(
          "02", "04", "05", "08", "00", "0A", "0E", "01", "03", "06", "07", "09", "0B", "0C", "0D", "0F"
        ).reduce((w, x) => s"$w$x")}".U)
        v.io.vsew.poke(0.U)
        v.io.vlmul.poke(1.U)
        v.io.mulOp.poke(1.U)

        v.clock.step(10)
    }
  }
}
