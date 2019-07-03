package com.vidhyasagar.scala.oop.commands

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

  // Adds to or over-writes contents of file
  def doEcho(state: State, contents: String, filename: String, append: Boolean): State = ???

}
