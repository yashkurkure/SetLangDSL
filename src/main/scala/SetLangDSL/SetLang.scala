package SetLangDSL

import SetLangDSL.SetLang.construct.*
import SetLangDSL.SetLang.setOperation.*
import SetLangDSL.SetLang.{construct, setOperation}

import scala.collection.mutable.Set

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
    case Union(set1: Set[Any], set2: Set[Any])
    case Intersection(set1: Set[Any], set2: Set[Any])
    case Difference(set1: Set[Any], set2: Set[Any])
    case SymmetricDifference(set1: Set[Any], set2: Set[Any])
    case CartesianProduct(set1: Set[Any], set2: Set[Any])

    def eval(): Value = {
      this match {

        // Insert all values of the tuple into a new set
        // return the new set
        case Insert(values: Tuple) => {
          val set: Set[Any] = Set.empty[Any]
          values.productIterator.foreach(x => set.add(x))
          Value(set)
        }

        // Insert the constructs.value into a new set
        // return the new set
        case Insert(value: Any) => {
          val set: Set[Any] = Set.empty[Any]
          set.add(value)
          Value(set)
        }

        case Union(set1: Set[Any], set2: Set[Any]) => {
          val set = set1 | set2
          Value(set)
        }

        case Intersection(set1: Set[Any], set2: Set[Any]) => {
          val set = set1 & set2
          Value(set)
        }

        case Difference(set1: Set[Any], set2: Set[Any]) => {
          val set = set1 &~ set2
          Value(set)
        }

        case SymmetricDifference(set1: Set[Any], set2: Set[Any]) => {
          val set = (set1 | set2) &~ (set1 & set2)
          Value(set)
        }

        case CartesianProduct(set1: Set[Any], set2: Set[Any]) => {
          // TODO: Implement mechanism to calculate the cartesian product of 2 sets
          Value(set1)
        }

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
    case Delete(name: String, value: Any) //TODO
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
        case Macro(value: (String, SetLang.setOperation)) => {
          val result = globalScope.createBinding(value._1, value._2)
          if result == Value(null) then
            DuplicateInitialization(value._1)
          else
          // returns Value(setOperation)
            result
        }

        //Case: evaluate the macro
        case Macro(name: String) => {
          val result = globalScope.searchBinding(name)
          if result == Value(null) then
            MacroNotFound(name)
          else
            val macroBody = result.getValue().asInstanceOf[setOperation]
            macroBody.eval()
        }

        //Case: create the SetLangDSL.scope
        case Scope(value: (String, SetLang.construct)) => {
          // Since we are in the global SetLangDSL.scope right now, we create a binding in it
          val result = globalScope.createInternalScope(value._1, value._2)
          if result == Value(null) then
          // Scope could be created sue to duplicate binding
            DuplicateInitialization(value._1)
          else
          //Scope creation was successful
            Value(true)
        }

        //Case: evaluate the SetLangDSL.scope
        // Here we evaluate the body of the SetLangDSL.scope and return the result of the evaluation
        case Scope(name: String) => {
          val result = globalScope.searchBinding(name)
          if result == Value(null) then
            ScopeNotFound(name)
          else
            val childScopeInstance = result.getValue().asInstanceOf[scope]
            childScopeInstance.evaluateScope()
        }

        //Case: Getting the value of a variable from the bindings
        case Variable(name: String) => {
          val result = globalScope.searchBinding(name)
          if result == Value(null) then
            VariableNotFound(name)
          else
          //returns Value(value of variable)
            result
        }

        //Case: Creation of a value
        case Value(value: Any) => {
          Value(value)
        }

        //Case: Looking up if a set contains a certain value
        case Check(setName: String, value: Value) => {

          //search for the set in the SetLangDSL.scope
          val result = globalScope.searchBinding(setName)

          //check if the set contains the value
          result match {

            // result is a set
            case Value(setValue: Set[Any]) => {
              //set contains the value
              if setValue.contains(value.eval()) then
                Value(true)
              //set does not contain the value
              else
                Value(false)
            }

            // result is not a set (setName is not bound to a set)
            case Value(value: Any) => {
              NameIsNotBoundToSetValue(setName)
            }
          }
        }

        //Case: Looking up if a set contains a value represented by a variable
        case Check(setName: String, variable: Variable) => {
          //get the value of the variable
          val variableValue = variable.eval()

          //check if the variable is bounded to a value
          if variableValue == VariableNotFound then
            VariableNotFound(variable)
          //variable value was found successfully
          else
            Check(setName, variableValue).eval()
        }


        // Case: Creation of set referenced by name
        case Assign(Variable(name), operation: setOperation) => {
          val theSet = operation.eval().getValue()
          val result = globalScope.createBinding(name, theSet)
          if result == Value(null) then
            DuplicateInitialization(name)
          else
            result
        }

        //Case: Creation of a variable with any value referenced by name
        case Assign(Variable(name), Value(value)) => {
          val result = globalScope.createBinding(name, value)
          if result == Value(null) then
            DuplicateInitialization(name)
          else
            result
        }

        //Case: Anything else is invalid syntax
        case default => InvalidSyntax(this)

      }

    }

    //evaluates a local SetLangDSL.scope
    def evalInScope(scopeInstance: scope): construct = {
      this match {
        //Case: create the macro
        case Macro(value: (String, SetLang.setOperation)) => {
          val result = scopeInstance.createBinding(value._1, value._2)
          if result == Value(null) then
            DuplicateInitialization(value._1)
          else
          // returns Value(setOperation)
            result
        }

        //Case: evaluate the macro
        case Macro(name: String) => {
          val result = scopeInstance.searchBinding(name)
          if result == Value(null) then
            MacroNotFound(name)
          else
            val macroBody = result.getValue().asInstanceOf[setOperation]
            macroBody.eval()
        }

        //Case: evaluate the SetLangDSL.scope
        // Here we evaluate the body of the SetLangDSL.scope and return the result of the evaluation
        case Scope(name: String) => {
          val result = scopeInstance.searchBinding(name)
          if result == Value(null) then
            ScopeNotFound(name)
          else
            val childScopeInstance = result.getValue().asInstanceOf[scope]
            childScopeInstance.evaluateScope()
        }

        //Case: create the SetLangDSL.scope
        case Scope(value: (String, SetLang.construct)) => {
          // We are not in the global SetLangDSL.scope anymore
          val result = scopeInstance.createInternalScope(value._1, value._2)
          if result == Value(null) then
          // Scope could be created sue to duplicate binding
            DuplicateInitialization(value._1)
          else
          //Scope creation was successful
            Value(true)
        }

        //Case: Getting the value of a variable from the bindings
        case Variable(name: String) => {
          val result = scopeInstance.searchBinding(name)
          if result == Value(null) then
            VariableNotFound(name)
          else
          //returns Value(value of variable)
            result
        }

        //Case: Creation of a value
        case Value(value: Any) => {
          Value(value)
        }

        //Case: Looking up if a set contains a certain value
        case Check(setName: String, value: Value) => {
          //search for the set in the SetLangDSL.scope
          val result = scopeInstance.searchBinding(setName)

          //check if the set contains the value
          result match {

            // result is a set
            case Value(setValue: Set[Any]) => {
              //set contains the value
              if setValue.contains(value.eval()) then
                Value(true)
              //set does not contain the value
              else
                Value(false)
            }

            // result is not a set (setName is not bound to a set)
            case Value(value: Any) => {
              NameIsNotBoundToSetValue(setName)
            }
          }
        }

        //Case: Looking up if a set contains a value represented by a variable
        case Check(setName: String, variable: Variable) => {

          //get the value of the variable
          val variableValue = variable.eval()

          //check if the variable is bounded to a value
          if variableValue == VariableNotFound then
            VariableNotFound(variable)
          //variable value was found successfully
          else
            Check(setName, variableValue).evalInScope(scopeInstance)
        }


        // Case: Creation of set referenced by name
        case Assign(Variable(name), operation: setOperation) => {
          val theSet = operation.eval().getValue()
          val result = scopeInstance.createBinding(name, theSet)
          if result == Value(null) then
            DuplicateInitialization(name)
          else
            result
        }

        //Case: Creation of a variable with any value referenced by name
        case Assign(Variable(name), Value(value)) => {
          val result = scopeInstance.createBinding(name, value)
          if result == Value(null) then
            DuplicateInitialization(name)
          else
            result
        }

        //Case: Anything else is invalid syntax
        case default => InvalidSyntax(this)

      }


    }


    // get the actual value from Value()
    def getValue(): Any = {
      this match {
        // This is needed in some cases when a value is extracted from a Map
        case Value(Some(value)) => {
          value
        }
        case Value(value) => {
          value
        }
      }
    }


}
