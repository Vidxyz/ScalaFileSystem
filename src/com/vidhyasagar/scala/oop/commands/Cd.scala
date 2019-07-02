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


  // Need to collapse relative tokens
  // ...
  // for example, absPath = /a/b/c
  def doFindEntry(root: Directory, absPath: String): DirEntry = {

    // Example argument trace
    /*
    Example filesystem
    ---------
    a/
      d/
      e/
        g/
        h/
      b/
        c/
          c1/
          c2/
      f/

     i/
     j/
     k/
     ----------
     */
    // Incoming command = cd /a/b/c
    // cD = "/" , path = ["a", "b", "c"]
    //

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

    @tailrec
    def collapseRelativeTokens(path: List[String], result: List[String]): List[String] = {
      if(path.isEmpty) result
      else if (".".equals(path.head)) collapseRelativeTokens(path.tail, result)
      else if ("..".equals(path.head)) {
        if(result.isEmpty) null
        else collapseRelativeTokens(path.tail, result.init)
      }
      else collapseRelativeTokens(path.tail, result :+ path.head)
    }

    // 1. Tokens
    val tokens: List[String] = absPath.substring(1).split(Directory.SEPARATOR).toList
    // This list will contain all the . and ..'s
    //  1.1 - Eliminate/Collapse relative tokens
    val newTokens = collapseRelativeTokens(tokens, List())

    if (newTokens == null) null
    // 2. Navigate to the correct entry
    else findEntryHelper(root, newTokens)

  }

}
