package com.vidhyasagar.scala.oop.commands
import com.vidhyasagar.scala.oop.files.{DirEntry, Directory}
import com.vidhyasagar.scala.oop.filesystem.State

class Mkdir(name:String) extends CreateEntry(name) {

  override def createSpecificEntry(state: State): DirEntry = Directory.empty(state.wd.path(), name)
}
