package com.vidhyasagar.scala.oop.filesystem

import java.util.Scanner

import com.vidhyasagar.scala.oop.commands.Command
import com.vidhyasagar.scala.oop.files.Directory

object FileSystem extends App {

  val root = Directory.ROOT()

  // Stateful applications require VARs
  var state = State(root, root)
  val scanner = new Scanner(System.in)

  while(true) {
    state.show()
    state = Command.from(scanner.nextLine()).apply(state)

  }

  /* ELEGENT FUNCTIONAL WAY TO HANDLE STATEFUL APPLICATIONS

  io.Source.stdin.getLines().foldLeft(State(root, root)) ((currentState, newLine) => {
    currentState.show()
    Command.from(newLine).apply(currentState)
  })

   */


}
