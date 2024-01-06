package bitpattern

import chisel3._,
       chiseltest._,
       org.scalatest.freespec.AnyFreeSpec


class BitPatternTest extends AnyFreeSpec with ChiselScalatestTester {
  "bit_pat" in {
    test(new BitPattern).withAnnotations(Seq(VerilatorBackendAnnotation)) {
      bp =>
        bp.io.in.poke("b1110".U)

        bp.clock.step(5)
    }
  }
}
