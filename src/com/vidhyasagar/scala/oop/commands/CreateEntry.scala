package com.vidhyasagar.scala.oop.commands

import com.vidhyasagar.scala.oop.files.{DirEntry, Directory}
import com.vidhyasagar.scala.oop.filesystem.State

abstract class CreateEntry(name: String) extends Command {


  override def apply(state: State): State = {
    val wd = state.wd
    if(wd.hasEntry(name)) state.setMessage("Entry " + name + " already exists!")
    else if(name.contains(Directory.SEPARATOR)) {
      // Not allowing mkdir something/somethingelse -> linux way is mkdir -p p1/p2
      state.setMessage(name + " must not contain separators")
    }
    else if (checkIllegal(name)) {
      state.setMessage(name + ": Illegal Entry Name")
    }
    else doCreateEntry(state, name)
  }

  // Make sure it is standalone
  def checkIllegal(str: String): Boolean = {
    name.contains(".")
  }

  def doCreateEntry(state: State, name: String): State = {
    val wd = state.wd

    def updateStructure(currentDirectory: Directory, path: List[String], newEntry: DirEntry): Directory = {
      /*
      someDir
        /a
        /b
        (new) /d

   => newSomeDir
        /a - re-use these instances
        /b - re-use
        /d - newly created
       */
      if(path.isEmpty) currentDirectory.addEntry(newEntry)
      else {
        /*
        /a/b
            /c
            /d
          (new entry)

        currentDir = /a
        path = ["b]
         */
//        println(path)
//        println(currentDirectory.findEntry(path.head))
        val oldEntry = currentDirectory.findEntry(path.head).asDirectory
        currentDirectory.replaceEntry(oldEntry.name, updateStructure(oldEntry, path.tail, newEntry))
      }


    }

    // 1. All the directories in the full path
    //    -> All the tokens in the full-path
    val allDirsInPath = wd.getAllFoldersInPath()

    // 2. Update new structure with the new directory entry
    //    -> Create new directory entry in the wd
    // This is only line of difference between mkdir and touch
     val newEntry: DirEntry = createSpecificEntry(state)
    // val newDir = Directory.empty(wd.path(), name)

    // 3. Update whole directory structure, starting from the root
    val newRoot = updateStructure(state.root, allDirsInPath, newEntry)

    // 4. Find new working directory INSTANCE given wd's full path in the NEW directory structure
    val newWd = newRoot.findDescendant(allDirsInPath)

    State(newRoot, newWd)
  }

  def createSpecificEntry(state: State): DirEntry

}
