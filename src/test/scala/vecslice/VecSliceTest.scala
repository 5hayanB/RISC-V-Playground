package vecslice

import chiseltest._
import org.scalatest.freespec.AnyFreeSpec


class VecSliceTest extends AnyFreeSpec with ChiselScalatestTester {
  "vec_slice" in {
    test(new VecSlice()).withAnnotations(Seq(VerilatorBackendAnnotation)) {
      vs =>
        vs.clock.step(10)
    }
  }
}
