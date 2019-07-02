package com.vidhyasagar.scala.oop.commands

import com.vidhyasagar.scala.oop.files.DirEntry
import com.vidhyasagar.scala.oop.filesystem.State

class Ls extends Command {

  override def apply(state: State): State = {
    val contents = state.wd.contents
    val niceOutput = createNiceOutput(contents)
    state.setMessage(niceOutput)
  }

  def createNiceOutput(contents: List[DirEntry]): String = {
    if(contents.isEmpty) "\n"
    else {
      val entry = contents.head
      entry.name + "[" + entry.getType + "]\t" + createNiceOutput(contents.tail)
    }
  }
}
