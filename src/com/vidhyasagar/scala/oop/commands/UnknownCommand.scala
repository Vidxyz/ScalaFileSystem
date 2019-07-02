package com.vidhyasagar.scala.oop.commands
import com.vidhyasagar.scala.oop.filesystem.State

class UnknownCommand extends Command {

  override def apply(state: State): State = state.setMessage("Command not found!")

}
