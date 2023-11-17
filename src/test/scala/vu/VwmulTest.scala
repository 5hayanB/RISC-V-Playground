package nucleusrv.components.vu

import chisel3._
import chiseltest._
import org.scalatest._


class VwmulTest extends FreeSpec with ChiselScalatestTester {
  "Vwmul" in {
    test(new Vwmul()) {
      v =>
        v.io.vs(0).poke("h01 02 03 04 05 06 07 09 00 0A 0B 0C 0D 0E 0F 08".U)
        v.io.vs(1).poke("h02 04 05 08 00 0A 0E 01 03 06 07 09 0B 0C 0D 0F".U)
        v.io.vsew.poke(8.U)
        v.io.vlmul.poke(1.U)
        v.io.mulOp.poke(1.U)

        v.clock.step(5)
    }
  }
}
