package left_shift

import chisel3._,
       chiseltest._,
       org.scalatest.freespec.AnyFreeSpec


class LeftShiftTest extends AnyFreeSpec with ChiselScalatestTester {
  "left_shift" in {
    test(new LeftShift).withAnnotations(Seq(VerilatorBackendAnnotation)) {
      ls =>
        ls.io.in.poke("b1010".U)

        ls.clock.step(4)
    }
  }
}
