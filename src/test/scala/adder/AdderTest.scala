package adder

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec


class AdderTest extends AnyFreeSpec with ChiselScalatestTester {
  "adder" in {
    test(new Adder()).withAnnotations(Seq(VerilatorBackendAnnotation)) {
      a =>
        val in: Seq[Int] = Seq(1, 5)

        for (i <- 0 until in.length) {
          a.io.in(i).poke(in(i).U)
        }

        a.clock.step(5)
    }
  }
}
