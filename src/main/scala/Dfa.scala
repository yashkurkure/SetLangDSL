class Dfa {

  var states = Seq.empty[State]
  var finalStates = Seq.empty[State]
  var currentState : State = null
  var input:String=""
  val transition = new Transition
  val transitionMap = transition.transitionMap

  def states( block: => Seq[State] ): Dfa = {
    states = block
    this
  }

  def finalStates( block: => Seq[State] ): Dfa = {
    finalStates = block
    this
  }

  def transitions( f: Transition => Unit ): Dfa = {
    f(transition)
    this
  }

  def startFrom(start: State):Dfa = {
    currentState=start
    this
  }

  def withInput(input:String):Dfa = {
    this.input=input
    this
  }

  def run : Boolean = {
    input.foreach { ch =>
      transitionMap.get( (currentState,ch) ) match {
        case Some(state) => currentState=state
        case None => throw new Exception(s"You must provide transition function for input '$ch' when state is ${currentState.toString}")
      }
    }

    finalStates contains currentState
  }

}

class Transition {
  val transitionMap : scala.collection.mutable.HashMap[(State,Char),State] =
    new scala.collection.mutable.HashMap()

  def on(ch : Char) : TransitionFrom = new TransitionFrom(this,ch)
}

class TransitionFrom(val tr: Transition , val ch : Char) {
  def from(s: State) : TransitionTo = new TransitionTo(tr,ch,s)
}

class TransitionTo(val tr:Transition , val ch : Char, val from:State) {
  def to(s : State) : Unit = {
    tr.transitionMap += (from,ch)->s
  }
}

object Dfa {

  def newDfa( f: Dfa => Unit ) : Dfa = {
    val dfa = new Dfa
    f(dfa)
    dfa
  }

}