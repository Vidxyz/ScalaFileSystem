package com.vidhyasagar.scala.oop.commands

import com.vidhyasagar.scala.oop.files.{Directory, File}
import com.vidhyasagar.scala.oop.filesystem.State

import scala.annotation.tailrec

class Echo(args: Array[String]) extends Command {

  override def apply(state: State): State = {
    /*
    If no args -> then state
    If ONE arg -> print to console
    else >=2 args {
      check if next to last argument is '>' or '>>'
      if >
        echo to file (may create file if not there
      if >>
        append to file
      else
        just echo everything to a console
    }
     */

    if(args.isEmpty) state
    else if(args.length == 1) state.setMessage(args(0))
    else {
      val operator = args(args.length - 2)
      val fileName = args(args.length - 1)
      val contents = createContent(args, args.length - 2)

      if(">>".equals(operator)) doEcho(state, contents, fileName, append=true)
      else if(">".equals(operator)) doEcho(state, contents, fileName, append=false)

      else state.setMessage(createContent(args, args.length))
    }

  }


  // Top index non inclusive
  def createContent(args: Array[String], topIndex: Int): String = {

    @tailrec
    def createContentHelper(currentIndex: Int, accumulator: String): String = {
      if(currentIndex >= topIndex) accumulator
      else createContentHelper(currentIndex+1, accumulator + " " + args(currentIndex))
    }

    createContentHelper(0, "")

  }

  def getRootAfterEcho(currentDirectory: Directory, path: List[String], contents: String, append: Boolean): Directory = {
    /*
    LOGIC
    -----
    if path is empty - then fail and return current directory
    else if path.tail is empty,
      find the file to create/add content to
      if file not found, create file
      else if entry is a directory, FAIL
      else,
        replace or append content to the file
        repace the entry with the filename with the NEW file
     else
      find nextDirectory to navigate
      call getRootAfterEcho recursively on that

       if recursive call failed, fail
       else replace entry with the NEW directory after recursive call
     */

    if (path.isEmpty) currentDirectory
    else if (path.tail.isEmpty) {
      val dirEntry = currentDirectory.findEntry(path.head)
      if(dirEntry == null)
        currentDirectory.addEntry(new File(currentDirectory.path(), path.head, contents))
      else if (dirEntry.isDirectory)
        currentDirectory
      else
        if (append) currentDirectory.replaceEntry(path.head, dirEntry.asFile.appendContents(contents))
        else currentDirectory.replaceEntry(path.head, dirEntry.asFile.setContents(contents))

    }
    else {

      // Will always be a valid directory, might fail for relative/abs path
      val nextDirectory = currentDirectory.findEntry(path.head).asDirectory
      val nextNewDirectory = getRootAfterEcho(nextDirectory, path.tail, contents, append)

      if (nextNewDirectory == nextDirectory) currentDirectory
      else currentDirectory.replaceEntry(path.head, nextNewDirectory)
    }

  }

  // ASSUMPTION - echo something >/>> someFile : NO ABSOLUTE/RELATIVE PATHS, just current working directory
  // Adds to or over-writes contents of file
  def doEcho(state: State, contents: String, filename: String, append: Boolean): State = {
    if(filename.contains(Directory.SEPARATOR)) state.setMessage("Echo: Filename must not contain separators")
    else {
      val newRoot: Directory = getRootAfterEcho(state.root, state.wd.getAllFoldersInPath() :+ filename, contents, append)
      if (newRoot == state.root)
        state.setMessage(filename + ": no such file")
      else
        State(newRoot, newRoot.findDescendant(state.wd.getAllFoldersInPath()))
    }
  }

}
