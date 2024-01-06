package reverse

import chisel3._


class Rev extends Module {
  val op1 = dontTouch(WireInit(1.U(4.W)))
  val op2 = dontTouch(WireInit(0.U(4.W)))
  val res = dontTouch(WireInit(0.U(8.W)))

  val minus = dontTouch(WireInit((0.U(8.W) - 1.U(8.W))))

  res := op1 << (op2 - 1.U)
}
