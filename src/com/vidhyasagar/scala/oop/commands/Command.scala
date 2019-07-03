package com.vidhyasagar.scala.oop.commands

import com.vidhyasagar.scala.oop.filesystem.State

// Transforms state of world into another state
trait Command {

  def apply(state: State): State

}

object Command {

  val MKDIR = "mkdir"
  val LS = "ls"
  val PWD = "pwd"
  val TOUCH = "touch"
  val CD = "cd"
  val RM = "rm"
  val ECHO = "echo"
  val CAT = "cat"

  def emptyCommand(): Command = new Command {
    override def apply(state: State): State = state
  }

  def incompleteCommand(name: String): Command = new Command {
    override def apply(state: State): State = state.setMessage(name + ": Incomplete Command")
  }

  def from(input: String): Command = {
    val tokens: Array[String] = input.split(" ")


    // CAN REDUCE CLUTTER BY USING CASE MATCHES. FUTURE ENHANCEMENT
    //---
    if (input.isEmpty() ||  tokens.isEmpty) emptyCommand()
    else if (MKDIR.equals(tokens(0))) {
      if(tokens.length < 2) incompleteCommand(MKDIR)
      else new Mkdir(tokens(1)) // Ignoring multiple arguments such as 'mkdir f1 f2'

    }
    else if(LS.equals(tokens(0))) {
      // Dont use apply here, because that is being handled in main while loop
      new Ls
    }
    else if(PWD.equals(tokens(0))) new Pwd
    else if(TOUCH.equals(tokens(0))) new Touch(tokens(1))
    else if(CD.equals(tokens(0))) {
      if (tokens.length < 2) incompleteCommand(CD)
      else new Cd(tokens(1))
    }
    else if(RM.equals(tokens(0))) {
      if(tokens.length < 2) incompleteCommand(RM)
      else new Rm(tokens(1))
    }
    else if (ECHO.equals(tokens(0))) {
      if(tokens.length < 2) incompleteCommand(ECHO)
      else new Echo(tokens.tail)
    }
    else if (CAT.equals(tokens(0))) {
      if(tokens.length < 2) incompleteCommand(CAT)
      else new Cat(tokens(1))
    }

    else new UnknownCommand
  }

}
