package com.vidhyasagar.scala.oop.commands

import com.vidhyasagar.scala.oop.files.{DirEntry, Directory}
import com.vidhyasagar.scala.oop.filesystem.State

import scala.annotation.tailrec

class Cd(dir: String) extends Command {

  override def apply(state: State): State = {
    /*
    Support the following
    1. Absolute Paths
    2. Relative Paths - relative to current working directory
    3. cd ../././../ ==> PART 2
     */

    // 1. Find the root
    var root = state.root
    val wd = state.wd

    // 2. Find ABSOLUTE path of directory I want to CD to
    val absolutePath = {
      if (dir.startsWith(Directory.SEPARATOR)) dir
      else if(wd.isRoot()) wd.path + dir
      else wd.path() + Directory.SEPARATOR + dir
    }

    // 3. Find the directory to CD to, given the path
    val destinationDirectory = doFindEntry(root, absolutePath)

    // 4. Change the STATE, given the new directory
    // Error out if destination is a file and not a folder
    if (destinationDirectory == null || !destinationDirectory.isDirectory) {
      state.setMessage(dir + ": No such directory")
    }
    else State(root, destinationDirectory.asDirectory)

  }

  def doFindEntry(root: Directory, absPath: String): DirEntry = {

    @tailrec
    def findEntryHelper(currentDirectory: Directory, path: List[String]): DirEntry = {
      if(path.isEmpty || path.head.isEmpty) currentDirectory
      else if(path.tail.isEmpty)  currentDirectory.findEntry(path.head)
      else {
        val nextDir = currentDirectory.findEntry(path.head)
        if (nextDir == null || !nextDir.isDirectory) null
        else findEntryHelper(nextDir.asDirectory, path.tail)
      }
    }

    // 1. Tokens
    val tokens: List[String] = absPath.substring(1).split(Directory.SEPARATOR).toList

    // 2. Navigate to the correct entry
    findEntryHelper(root, tokens)
  }

}
