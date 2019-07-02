package com.vidhyasagar.scala.oop.commands

import com.vidhyasagar.scala.oop.filesystem.State

class Pwd extends Command {

  override def apply(state: State): State = state.setMessage(state.wd.path())

}
