# SetLangDSL

# What to import?
You must import three things:
1. import SetLangDSL.SetLang.construct.* this covers the following:
   1. Variable
   2. Value 
   3. Assign 
   4. Scope 
   5. Macro
   6. Check 
   7. Delete 
   8. Add
2. import SetLangDSL.SetLang.setOperation.*
   1. Insert 
   2. Union 
   3. Intersection 
   4. Difference 
   5. SymmetricDifference 
   6. CartesianProduct
3. import SetLangDSL.scope (Implements scopes)
4. import collection.mutable.Set (For the set data structure)

# Usage:

## Value 
part of enum construct
```
Value(value: Any) => construct
```
All values in the DSL are represented by Value(). Any expression that evaluates something will return Value(value)
To get the "actual value" you can use the getValue() method.
##### Usage
```
Value(1).eval() //returns Value(1)
Value("a").getValue() //returns "a"
Value(HashSet("a","b","c")).getValue() //returns HashSet("a","b","c")
```

## Variable
part of enum construct
```
Variable(name: String) => construct
```
Any variable that exists in the binding table can be accessed using Variable().
If you call Variable() on a name that is not bound to a value, it would return VariableNotFound(name: String).

Note: For variables in scopes, check out Scope()

##### Usage
```
Assign(Variable("x"), Value(100)).eval() // creates a binding between name "x" and value 100
Variable("x").eval() //returns Value(100)
Variable("y").eval() //returns VariableNotFound(y) since y was never bound to any value
```
## Assign
```
Assign(lhs: construct, rhs: construct) => construct
```
Assign is used to assign values to variables, change the value of set variables such as Adding/Deleting elements from sets.

##### Some examples of usage
```
// Creating a variable
Assign(Variable("myVar"), Value("a")).eval()

// Creating a set variable
Assign(Variable("mySet1"), Insert("a", "b", "c")).eval()
Assign(Variable("mySet2"), Insert("a", "b", "c")).eval()

// Doing some kind of set operation
//unionSet will store the Union of mySet1 and mySet2
Assign(Variable("unionSet"), Union(Variable("mySet1"), Variable("mySet2"))).eval()
//intersactionSet will soter the interseaction of mySet1 and mySet2
Assign(Variable("interseactionSet"), Interseaction(Variable("mySet1"), Variable("mySet2"))).eval()

// Modifying an existing set
Assign(Variable("mySet1"), Delete(Value("a"))).eval()
Assign(Variable("mySet2"), Add(Value("b"))).eval()

```

### Delete
```
Delete(value: construct) => nothing
```
Delete cannot be used by itself. It must be used inside an Assign() statement.

##### Usage examples:
```
//remove the element "a" from set someSet
Assign(Variable("someSet"), Delete(Value("a"))).eval()
//remove value refreneced by "myVar" from set someOtherSet
Assign(Variable("someOtherSet"), Delete(Variable("myVar"))).eval()
```

### Add

```
Add(value: construct) => nothing
```
Similar to Delete, Add cannot be used by itself. It must be used inside an Assign() statement.

##### Usage examples:
```
//add the value "a" to set someSet
Assign(Variable("someSet"), Add(Value("a"))).eval()
//add the value refreneced by "myVar" to set someOtherSet
Assign(Variable("someOtherSet"), Add(Variable("myVar"))).eval()
```

### Scope
```
Scope(name:String, body: construct) => construct
Scope(name: String) => construct
```
There are two parts to using scopes. 

First you need to create one, this will create a binding of the scope "name" to the scope object. The scope object will store the bindings of the interiors of the scope and a reference to the parent scope.
```
// Creating a scope
Scope("myScope", Assign(Variable("myVar"), Value(1)).eval()
//at this point you have created a binding for the scope
```

Now, the second part is to execute the scope. You can execute the scope we created above as follows:
```
Scope("myScope")
this will return the value of Assign(..) inside the scope body
```
Once the above scope is executed it will bind the variable "myVar" to 1 in the LOCAL scope.
"myVar" cannot be accessed from outside the scope.

Some rules on variable shadowing. Take the following code snippet as an example:
```
Assign(Variable("v"), Value(1)).eval()
Scope("myScope", Variable("v")).eval()
Scope("myScope").eval() => would return value 1

//lets say we have another scope
Scope("myScope2", Assign(Variable("v"), Value("a"))).eval() //declare scope
Scope("myScope").eval() //execute scope
//In the above case, the value of v would be "a"

//Here is a way to print the value of a variable from a certain scope
//first get the scope instance
val scopeInstance = Variable("myScope2").eval().getValue().asInstanceOf[scope]
Variable("v").evalInScope(scopeInstance).getValue() //would return "a"
```
The simple rule is that when a variable is searched for in a scope, the program will first check the local scope, if it is not found it will look up the scope tree till it reaches the global scope.
The nearest scope with an active binding of the variable would be accessed. 