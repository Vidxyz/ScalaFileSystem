package com.vidhyasagar.scala.oop.files

import com.vidhyasagar.scala.oop.filesystem.MyFileSystemException

import scala.annotation.tailrec

class Directory(override val parentPath: String, override val name: String, val contents: List[DirEntry]) extends DirEntry(parentPath, name) {

  def getAllFoldersInPath(): List[String] = {
    // /a/b/c/d/ => List["a", "b", "c", "d"]
    // Substring 1 is to remove the first leading slash '/'
    path().substring(1).split(Directory.SEPARATOR).toList.filter(e => !e.isEmpty)
  }

  def hasEntry(name: String): Boolean = findEntry(name) != null

  def findDescendant(path: List[String]): Directory = {
    if(path.isEmpty) this
    else findEntry(path.head).asDirectory.findDescendant(path.tail)
  }

  def addEntry(newEntry: DirEntry): Directory = new Directory(parentPath, name, contents :+ newEntry)

  def findEntry(entryName: String): DirEntry = {
    @tailrec
    def findEntryHelper(name: String, contentList: List[DirEntry]): DirEntry = {
      if(contentList.isEmpty) null
      else if (contentList.head.name.equals(name)) contentList.head
      else findEntryHelper(name, contentList.tail)
    }

    findEntryHelper(entryName, contents)
  }

  def replaceEntry(entryName: String, newEntry: DirEntry): Directory =
    new Directory(parentPath, name, contents.filter(e => !e.name.equals(entryName)) :+ newEntry)

  override def asDirectory: Directory = this

  override def getType: String =  "Directory"

  override def asFile: File = throw new MyFileSystemException("Directory cannot be converted to a File!")
}

object Directory {
  val SEPARATOR = "/"
  val ROOT_PATH = "/"

  def ROOT(): Directory = Directory.empty("", "")

  def empty(parentPath: String, name: String): Directory = new Directory(parentPath, name, List())
}