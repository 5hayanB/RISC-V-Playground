package reverse

import chiseltest._,
       org.scalatest.freespec.AnyFreeSpec


class RevTest extends AnyFreeSpec with ChiselScalatestTester {
  "reverse" in {
    test(new Rev).withAnnotations(Seq(VerilatorBackendAnnotation)) {
      rev =>
        rev.clock.step(5)
    }
  }
}
