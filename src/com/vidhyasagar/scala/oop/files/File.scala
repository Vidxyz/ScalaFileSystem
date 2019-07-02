package com.vidhyasagar.scala.oop.files


import com.vidhyasagar.scala.oop.filesystem.MyFileSystemException

class File(override val parentPath: String, override val name: String, val contents: String) extends DirEntry(parentPath, name) {


  override def asDirectory: Directory = throw  new MyFileSystemException("File cannot be converted to a Directory")

  override def getType: String = "File"

  override def asFile: File = this

  override def isDirectory: Boolean = false

  override def isFile: Boolean = true
}

object File {

  def empty(parentPath: String, name: String): File = new File(parentPath, name, "")

}
