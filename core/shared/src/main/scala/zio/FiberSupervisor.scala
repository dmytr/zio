package zio

import zio.Supervisor.Propagation

// TODO: as an alternative?
/**
 * Supervisor that executes provided callbacks on a corresponding fiber
 * lifecycle event.
 */
abstract class FiberSupervisor extends Supervisor[Unit] {

  override def value: UIO[Unit] = UIO.unit

  def unsafeOnStart(fiberId: Fiber.Id): Unit

  def unsafeOnEnd(fiberId: Fiber.Id): Unit

  def unsafeOnSuspend(fiberId: Fiber.Id): Unit

  def unsafeOnResume(fiberId: Fiber.Id): Unit

  override private[zio] def unsafeOnStart[R, E, A](
    environment: R,
    effect: ZIO[R, E, A],
    parent: Option[Fiber.Runtime[Any, Any]],
    fiber: Fiber.Runtime[E, A]
  ): Propagation = {
    unsafeOnStart(fiber.id)
    Propagation.Continue
  }

  override private[zio] def unsafeOnEnd[R, E, A](value: Exit[E, A], fiber: Fiber.Runtime[E, A]): Propagation = {
    unsafeOnEnd(fiber.id)
    Propagation.Continue
  }

  override private[zio] def unsafeOnSuspend[E, A1](fiber: Fiber.Runtime[E, A1]): Unit = unsafeOnSuspend(fiber.id)

  override private[zio] def unsafeOnResume[E, A1](fiber: Fiber.Runtime[E, A1]): Unit = unsafeOnResume(fiber.id)

}
