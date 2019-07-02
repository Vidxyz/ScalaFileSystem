package com.vidhyasagar.scala.oop.commands

import com.vidhyasagar.scala.oop.files.{DirEntry, File}
import com.vidhyasagar.scala.oop.filesystem.State

class Touch(name: String) extends CreateEntry(name) {

  override def createSpecificEntry(state: State): DirEntry = File.empty(state.wd.path(), name)

}
