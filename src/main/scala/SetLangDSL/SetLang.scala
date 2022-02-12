package SetLangDSL
import SetLangDSL.SetLang.construct.*
import SetLangDSL.SetLang.setOperation.*
import SetLangDSL.SetLang.{construct, setOperation}
import scala.collection.mutable
import scala.collection.mutable.Set

/*
 *  Copyright (c) 2022. Yash Kurkure. All rights reserved.
 */


/** Notes:
 * https://stackoverflow.com/questions/47177856/are-scopes-stored-in-a-stack
 * Scope: A SetLangDSL.scope is a semantics construct.
 * It defines where you can use a name (for a variable, type, function, etc.). And what that name will refer to.
 * */


object SetLang {

  // global SetLangDSL.scope contains all the bindings for the global variables
  val globalScope = new scope("global", null, null)


  //Definitions for set operations
  enum setOperation:
    case Insert(value: Any)
    case Union(set1: construct, set2: construct)
    case Intersection(set1: construct, set2: construct)
    case Difference(set1: construct, set2: construct)
    case SymmetricDifference(set1: construct, set2: construct)
    case CartesianProduct(set1: construct, set2: construct)
    private case InvalidSyntax

    def eval(): Value = {
      this match {

        // Insert all values of the tuple into a new set
        // return the new set
        case Insert(values: Tuple) =>
          val set: Set[Any] = mutable.Set.empty[Any]
          values.productIterator.foreach(x => set.add(x))
          Value(set)


        // Insert the constructs.value into a new set
        // return the new set
        case Insert(value: Any) =>
          val set: Set[Any] = Set.empty[Any]
          set.add(value)
          Value(set)


        case Union(set1, set2) =>
          val set = set1.eval().getValue().asInstanceOf[Set[Any]] | set2.eval().getValue().asInstanceOf[Set[Any]]
          Value(set)


        case Intersection(set1, set2) =>
          val set = set1.eval().getValue().asInstanceOf[Set[Any]] & set2.eval().getValue().asInstanceOf[Set[Any]]
          Value(set)


        case Difference(set1, set2) =>
          val set = set1.eval().getValue().asInstanceOf[Set[Any]] &~ set2.eval().getValue().asInstanceOf[Set[Any]]
          Value(set)


        case SymmetricDifference(set1, set2) =>
          val set = (set1.eval().getValue().asInstanceOf[Set[Any]] | set2.eval().getValue().asInstanceOf[Set[Any]]) &~ (set1.eval().getValue().asInstanceOf[Set[Any]] & set2.eval().getValue().asInstanceOf[Set[Any]])
          Value(set)


        case CartesianProduct(set1, set2) =>
          val resultSet = Set.empty[Any]
          if set1.eval().getValue().asInstanceOf[Set[Any]].isEmpty || set2.eval().getValue().asInstanceOf[Set[Any]].isEmpty then
            Value(resultSet)
          else
            set1.eval().getValue().asInstanceOf[Set[Any]].foreach(mem1 => (set2.eval().getValue().asInstanceOf[Set[Any]].foreach(mem2=>resultSet.add((mem1, mem2)))))
            Value(resultSet)




        case default => Value(InvalidSyntax)

      }
    }

  //Definitions for language constructs
  enum construct:
    case Variable(name: String) //returns the value stored in the variable
    case Value(value: Any) // returns the value itself
    case Assign(lhs: construct, rhs: Any) // multiple operations, but will return value of the evaluation of the lhs in all cases
    case Scope(value: Any) // returns the value that body.eval() would generate
    case Macro(value: Any) //returns the construct that can be applied in a expression
    case Check(setName: String, value: Any)
    case Delete(value: construct)
    case Add(value: construct)
    private case VariableNotFound(variableName: Any) // When user tries to access a variable that was never bound
    private case ScopeNotFound(scopeName: Any) // When user tries to access a variable that was never bound
    private case MacroNotFound(macroName: Any) // When user tries to access a variable that was never bound
    private case DuplicateInitialization(variableName: Any) // When user tries to create a variable with the same name as existing variable
    private case InvalidSyntax(value: Any) // When the user's syntax doesn't make sense
    private case NameIsNotBoundToSetValue(value: Any) // When the user is checking a set for a value, but the name is not bound to a set

    //evaluates in global SetLangDSL.scope
    def eval(): construct = {

      this match {

        //Case: create the macro
        case Macro(value: (String, SetLang.setOperation)) =>
          val result = globalScope.createBinding(value._1, value._2)
          if result == Value(null) then
            DuplicateInitialization(value._1)
          else
          // returns Value(setOperation)
            result


        //Case: evaluate the macro
        case Macro(name: String) =>
          val result = globalScope.searchBinding(name)
          if result == Value(null) then
            MacroNotFound(name)
          else
            val macroBody = result.getValue()
            macroBody.asInstanceOf[construct]


        //Case: create the SetLangDSL.scope
        case Scope(value: (String, SetLang.construct)) =>
          // Since we are in the global SetLangDSL.scope right now, we create a binding in it
          val result = globalScope.createInternalScope(value._1, value._2)
          if result == Value(null) then
          // Scope could not be created due to duplicate binding
            DuplicateInitialization(value._1)
          else
          //Scope creation was successful
            Value(true)


        //Case: evaluate the SetLangDSL.scope
        // Here we evaluate the body of the SetLangDSL.scope and return the result of the evaluation
        case Scope(name: String) =>
          val result = globalScope.searchBinding(name)
          if result == Value(null) then
            ScopeNotFound(name)
          else
            val childScopeInstance = result.getValue().asInstanceOf[scope]
            childScopeInstance.evaluateScope()


        //Case: Getting the value of a variable from the bindings
        case Variable(name: String) =>
          val result = globalScope.searchBinding(name)
          if result == Value(null) then
            VariableNotFound(name)
          else
          //returns Value(value of variable)
            result


        //Case: Creation of a value
        case Value(value: Any) =>
          Value(value)


        //Case: Looking up if a set contains a certain value
        case Check(setName: String, value: Value) =>

          //search for the set in the SetLangDSL.scope
          val result = globalScope.searchBinding(setName).getValue()
          //check if the set contains the value
          result match {

            // result is a set
            case setValue: Set[Any] =>
              //set contains the value
              if setValue.contains(value.eval().getValue()) then
                Value(true)
              //set does not contain the value
              else
                Value(false)


            // result is not a set (setName is not bound to a set)
            case anythingElse =>
              NameIsNotBoundToSetValue(setName)

          }


        //Case: Looking up if a set contains a value represented by a variable
        case Check(setName: String, variable: Variable) =>
          //get the value of the variable
          val variableValue = variable.eval()

          //check if the variable is bounded to a value
          if variableValue == VariableNotFound then
            VariableNotFound(variable)
          //variable value was found successfully
          else
            Check(setName, variableValue).eval()



        // Case: Creation of set referenced by name
        case Assign(Variable(name), operation: setOperation) =>
          val theSet = operation.eval().getValue()
          val result = globalScope.createBinding(name, theSet)
          if result == Value(null) then
            DuplicateInitialization(name)
          else
            result


        //Case: Creation of a variable with any value referenced by name
        case Assign(Variable(name), Value(value)) =>
          val result = globalScope.createBinding(name, value)
          if result == Value(null) then
            DuplicateInitialization(name)
          else
            result


        case Assign(Variable(setName), Delete(value: construct))=>
          val originalSetReference = Variable(setName).eval().getValue().asInstanceOf[Set[Any]]
          val valueToDelete = value.eval().getValue()
          if originalSetReference.contains(valueToDelete) then
            originalSetReference.remove(valueToDelete)
            Value(true)
          else
            Value(false)


        case Assign(Variable(setName), Add(value: construct))=>
          val originalSetReference = Variable(setName).eval().getValue().asInstanceOf[Set[Any]]
          val valueToDelete = value.eval().getValue()
          if originalSetReference.contains(valueToDelete) then
            Value(false)
          else
            originalSetReference.add(value.getValue())
            Value(true)


        case Assign(Variable(setName), Macro(name))=>
          Assign(Variable(setName), Macro(name).eval()).eval()


        //Case: Anything else is invalid syntax
        case default => InvalidSyntax(this)

      }

    }

    //evaluates a local SetLangDSL.scope
    def evalInScope(scopeInstance: scope): construct = {
      this match {
        //Case: create the macro
        case Macro(value: (String, SetLang.setOperation)) =>
          val result = scopeInstance.createBinding(value._1, value._2)
          if result == Value(null) then
            DuplicateInitialization(value._1)
          else
          // returns Value(setOperation)
            result


        //Case: evaluate the macro
        case Macro(name: String) =>
          val result = scopeInstance.searchBinding(name)
          if result == Value(null) then
            MacroNotFound(name)
          else
            result


        //Case: evaluate the SetLangDSL.scope
        // Here we evaluate the body of the SetLangDSL.scope and return the result of the evaluation
        case Scope(name: String) =>
          val result = scopeInstance.searchBinding(name)
          if result == Value(null) then
            ScopeNotFound(name)
          else
            val childScopeInstance = result.getValue().asInstanceOf[scope]
            childScopeInstance.evaluateScope()


        //Case: create the SetLangDSL.scope
        case Scope(value: (String, SetLang.construct)) =>
          // We are not in the global SetLangDSL.scope anymore
          val result = scopeInstance.createInternalScope(value._1, value._2)
          if result == Value(null) then
          // Scope could be created sue to duplicate binding
            DuplicateInitialization(value._1)
          else
          //Scope creation was successful
            Value(true)


        //Case: Getting the value of a variable from the bindings
        case Variable(name: String) =>
          val result = scopeInstance.searchBinding(name)
          if result == Value(null) then
            VariableNotFound(name)
          else
          //returns Value(value of variable)
            result


        //Case: Creation of a value
        case Value(value: Any) =>
          Value(value)


        //Case: Looking up if a set contains a certain value
        case Check(setName: String, value: Value) =>
          //search for the set in the SetLangDSL.scope
          val result = scopeInstance.searchBinding(setName).getValue()

          //check if the set contains the value
          result match {

            // result is a set
            case setValue: Set[Any] =>
              //set contains the value
              if setValue.contains(value.evalInScope(scopeInstance).getValue()) then
                Value(true)
              //set does not contain the value
              else
                Value(false)


            // result is not a set (setName is not bound to a set)
            case Value(value: Any) =>
              NameIsNotBoundToSetValue(setName)

          }


        //Case: Looking up if a set contains a value represented by a variable
        case Check(setName: String, variable: Variable) =>
          //get the value of the variable
          val variableValue = variable.eval()

          //check if the variable is bounded to a value
          if variableValue == VariableNotFound then
            VariableNotFound(variable)
          //variable value was found successfully
          else
            Check(setName, variableValue).evalInScope(scopeInstance)



        // Case: Creation of set referenced by name
        case Assign(Variable(name), operation: setOperation) =>
          val theSet = operation.eval().getValue()
          val result = scopeInstance.createBinding(name, theSet)
          if result == Value(null) then
            DuplicateInitialization(name)
          else
            result


        //Case: Creation of a variable with any value referenced by name
        case Assign(Variable(name), Value(value)) =>
          val result = scopeInstance.createBinding(name, value)
          if result == Value(null) then
            DuplicateInitialization(name)
          else
            result


        case Assign(Variable(setName), Delete(value))=>
          val originalSetReference = Variable(setName).evalInScope(scopeInstance).getValue().asInstanceOf[Set[Any]]
          val valueToDelete = value.evalInScope(scopeInstance).getValue()
          if originalSetReference.contains(valueToDelete) then
            originalSetReference.remove(valueToDelete)
            Value(true)
          else
            Value(false)


        case Assign(Variable(setName), Add(value))=>
          val originalSetReference = Variable(setName).evalInScope(scopeInstance).getValue().asInstanceOf[Set[Any]]
          val valueToDelete = value.evalInScope(scopeInstance).getValue()
          if originalSetReference.contains(valueToDelete) then
            Value(false)
          else
            originalSetReference.add(value.getValue())
            Value(true)


        case Assign(Variable(setName), Macro(name))=>
          Assign(Variable(setName), Macro(name).evalInScope(scopeInstance)).evalInScope(scopeInstance)


        //Case: Anything else is invalid syntax
        case default => InvalidSyntax(this)

      }


    }


    // get the actual value from Value()
    def getValue(): Any = {
      this match {
        // This is needed in some cases when a value is extracted from a Map
        case Value(Some(value)) =>
          value

        case Value(value) =>
          value

        case default => this
      }
    }


}
