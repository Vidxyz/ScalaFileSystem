package com.vidhyasagar.scala.oop.commands

import com.vidhyasagar.scala.oop.filesystem.State

// generalization would be similar to CD command. Make abstract class and extend, substitute differing logic
class Cat(filename: String) extends Command {

  override def apply(state: State): State = {
    val wd = state.wd
    val dirEntry = wd.findEntry(filename)

    if(dirEntry == null || !dirEntry.isFile)
      state.setMessage(filename + ": No such file found!")
    else
      state.setMessage(dirEntry.asFile.contents)
  }

}
