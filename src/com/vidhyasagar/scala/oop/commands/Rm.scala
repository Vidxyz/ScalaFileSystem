package com.vidhyasagar.scala.oop.commands

import com.vidhyasagar.scala.oop.files.Directory
import com.vidhyasagar.scala.oop.filesystem.State

class Rm(name: String) extends Command {

  override def apply(state: State): State =  {

    // 1. Get working directory
    val wd = state.wd

    // 2. Get absolute path of 'name' parameter that;s being passed in
    val absolutePath = {
      if (name.startsWith(Directory.SEPARATOR)) name
      else if (wd.isRoot()) wd.path() + name
      else wd.path() + Directory.SEPARATOR + name
    }

    // 3. Do some sanity checks
    if (Directory.ROOT_PATH.equals(absolutePath)) state.setMessage("Illegal operation. rm /")
    else doRm(state, absolutePath)

    // 4. Find the entry to remove
    // 5. Update structure like for mkdir
  }

  def doRm(state: State, path: String): State = {
    // 4. Find the entry to remove
    // 5. Update structure like for mkdir
    // Do both these operations in ONE go

    // Returns the new root
    def rmHelper(currentDirectory: Directory, path: List[String]): Directory = {
      if (path.isEmpty) currentDirectory
      else if (path.tail.isEmpty) currentDirectory.removeEntry(path.head)
      else {
        val nextDir = currentDirectory.findEntry(path.head)
        if(!nextDir.isDirectory) currentDirectory
        else {
          val newNextDirectory = rmHelper(nextDir.asDirectory, path.tail)
          if (newNextDirectory == nextDir) currentDirectory
          else currentDirectory.replaceEntry(path.head, newNextDirectory)
        }
      }
    }

    val tokens = path.substring(1).split(Directory.SEPARATOR).toList
    val newRoot: Directory = rmHelper(state.root, tokens)

    if (newRoot == state.root) state.setMessage(path + ": No such file or directory found")
    else State(newRoot, newRoot.findDescendant(state.wd.path().substring(1)))

  }

}

